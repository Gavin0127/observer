package org.observertc.webrtc.observer.repositories.tasks;

import io.micronaut.context.annotation.Prototype;
import org.observertc.webrtc.observer.common.ChainedTask;
import org.observertc.webrtc.observer.dto.SfuDTO;
import org.observertc.webrtc.observer.repositories.HazelcastMaps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

@Prototype
public class AddSFUsTask extends ChainedTask<Void> {

    private static final Logger logger = LoggerFactory.getLogger(AddSFUsTask.class);

    @Inject
    HazelcastMaps hazelcastMaps;

    private Map<UUID, SfuDTO> sfuDTOs = new HashMap<>();

    @PostConstruct
    void setup() {
        new Builder<Void>(this)
                .<Map<UUID, SfuDTO>>addConsumerEntry("Merge all inputs",
                        () -> {},
                        receivedSfuDTOs -> {
                            if (Objects.nonNull(receivedSfuDTOs)) {
                                this.sfuDTOs.putAll(receivedSfuDTOs);
                            }
                        }
                )
                .<Map<UUID, SfuDTO>> addBreakCondition((resultHolder) -> {
                    if (this.sfuDTOs.size() < 1) {
                        resultHolder.set(null);
                        return true;
                    }
                    return false;
                })
                .addActionStage("Add Sfu DTOs",
                // action
                () -> {
                    hazelcastMaps.getSFUs().putAll(this.sfuDTOs);
                },
                // rollback
                (inputHolder, thrownException) -> {
                    for (UUID sfuId : this.sfuDTOs.keySet()) {
                        this.hazelcastMaps.getSFUs().remove(sfuId);
                    }
                })
                .addTerminalPassingStage("Completed")
                .build();
    }

    public AddSFUsTask withSfuDTO(SfuDTO sfuDTO) {
        if (Objects.isNull(sfuDTO)) {
            this.getLogger().info("sfu uuid was not given to be added");
            return this;
        }
        this.sfuDTOs.put(sfuDTO.sfuId, sfuDTO);
        return this;
    }

    public AddSFUsTask withSfuDTOs(SfuDTO... sfuDTOs) {
        if (Objects.isNull(sfuDTOs) || sfuDTOs.length < 1) {
            this.getLogger().info("sfu uuid was not given to be added");
            return this;
        }
        Arrays.stream(sfuDTOs).forEach(sfuDTO -> {
            this.sfuDTOs.put(sfuDTO.sfuId, sfuDTO);
        });
        return this;
    }

    public AddSFUsTask withSfuDTOs(Map<UUID, SfuDTO> sfuDTOs) {
        if (Objects.isNull(sfuDTOs) || sfuDTOs.size() < 1) {
            this.getLogger().info("sfu uuid was not given to be added");
            return this;
        }
        this.sfuDTOs.putAll(sfuDTOs);
        return this;
    }
}
