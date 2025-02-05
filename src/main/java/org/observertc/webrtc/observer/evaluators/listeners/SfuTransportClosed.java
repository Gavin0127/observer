package org.observertc.webrtc.observer.evaluators.listeners;

import io.micronaut.context.annotation.Prototype;
import org.observertc.webrtc.observer.common.SfuEventType;
import org.observertc.webrtc.observer.common.UUIDAdapter;
import org.observertc.webrtc.observer.dto.SfuTransportDTO;
import org.observertc.webrtc.observer.repositories.RepositoryEvents;
import org.observertc.webrtc.observer.repositories.RepositoryExpiredEvent;
import org.observertc.webrtc.observer.repositories.tasks.RemoveSfuTransportsTask;
import org.observertc.webrtc.schemas.reports.SfuEventReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import java.time.Instant;
import java.util.*;

@Prototype
class SfuTransportClosed extends EventReporterAbstract.SfuEventReporterAbstract<SfuTransportDTO> {

    private static final Logger logger = LoggerFactory.getLogger(SfuTransportClosed.class);

    @Inject
    RepositoryEvents repositoryEvents;

    @Inject
    Provider<RemoveSfuTransportsTask> removeSfuTransportTask;

    @PostConstruct
    void setup() {
        this.repositoryEvents
                .removedSfuTransports()
                .subscribe(this::receiveRemovedSfuTransport);

        this.repositoryEvents
                .expiredSfuTransports()
                .subscribe(this::receiveExpiredSfuTransport);
    }

    private void receiveRemovedSfuTransport(List<SfuTransportDTO> sfuTransportDTOs) {
        if (Objects.isNull(sfuTransportDTOs) || sfuTransportDTOs.size() < 1) {
            return;
        }
        sfuTransportDTOs.stream()
                .map(this::makeReport)
                .filter(Objects::nonNull)
                .forEach(this::forward);
    }

    private void receiveExpiredSfuTransport(List<RepositoryExpiredEvent<SfuTransportDTO>> expiredSfuTransports) {
        if (Objects.isNull(expiredSfuTransports) || expiredSfuTransports.size() < 1) {
            return;
        }
        var task = this.removeSfuTransportTask.get();
        Map<UUID, Long> estimatedRemovals = new HashMap<>();
        expiredSfuTransports.stream().forEach(expiredSfuTransport -> {
            var sfuTransportDTO = expiredSfuTransport.getValue();
            var estimatedRemoval = expiredSfuTransport.estimatedLastTouch();
            estimatedRemovals.put(sfuTransportDTO.transportId, estimatedRemoval);
            task.addRemovedSfuTransportDTO(sfuTransportDTO);
        });

        if (!task.execute().succeeded()) {
            logger.warn("Removing expired SfuRtpPad was unsuccessful");
            return;
        }
        task.getResult().stream().map(removedSfuTransport -> {
            Long estimatedRemoval = estimatedRemovals.getOrDefault(removedSfuTransport.transportId, Instant.now().toEpochMilli());
            var report = this.makeReport(removedSfuTransport, estimatedRemoval);
            return report;
        }).filter(Objects::nonNull).forEach(this::forward);
    }

    private SfuEventReport makeReport(SfuTransportDTO sfuTransportDTO) {
        var now = Instant.now().toEpochMilli();
        return this.makeReport(sfuTransportDTO, now);
    }

    @Override
    protected SfuEventReport makeReport(SfuTransportDTO sfuTransportDTO, Long timestamp) {
        try {
            String sfuId = UUIDAdapter.toStringOrNull(sfuTransportDTO.sfuId);
            String callId = UUIDAdapter.toStringOrNull(sfuTransportDTO.callId);
            String transportId = UUIDAdapter.toStringOrNull(sfuTransportDTO.transportId);
            var builder = SfuEventReport.newBuilder()
                    .setName(SfuEventType.SFU_TRANSPORT_CLOSED.name())
                    .setSfuId(sfuId)
                    .setCallId(callId)
                    .setTransportId(transportId)
                    .setMessage("Sfu Transport is closed")
                    .setServiceId(sfuTransportDTO.serviceId)
                    .setMediaUnitId(sfuTransportDTO.mediaUnitId)
                    .setTimestamp(timestamp);
            logger.info("SFU Transport (id: {}, internal: {}) is CLOSED (mediaUnitId: {}, serviceId {})",
                    transportId, sfuTransportDTO.internal, sfuTransportDTO.mediaUnitId, sfuTransportDTO.serviceId
            );
            return builder.build();
        } catch (Exception ex) {
            logger.error("Cannot make report for Sfu Transport DTO", ex);
            return null;
        }
    }
}
