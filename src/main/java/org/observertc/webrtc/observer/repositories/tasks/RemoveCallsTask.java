package org.observertc.webrtc.observer.repositories.tasks;

import io.micronaut.context.annotation.Prototype;
import org.observertc.webrtc.observer.common.ChainedTask;
import org.observertc.webrtc.observer.dto.CallDTO;
import org.observertc.webrtc.observer.entities.CallEntity;
import org.observertc.webrtc.observer.repositories.HazelcastMaps;
import org.observertc.webrtc.observer.samples.ServiceRoomId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Provider;
import java.util.*;

@Prototype
public class RemoveCallsTask extends ChainedTask<Map<UUID, CallDTO>> {

    private static final Logger logger = LoggerFactory.getLogger(RemoveCallsTask.class);

    private static final String LOCK_NAME = "observertc-call-remover-lock";

    private Set<UUID> callIds = new HashSet<>();
    private Map<UUID, CallDTO> removedCallDTOs = new HashMap<>();
    private Map<UUID, Collection<UUID>> removedCallClientIds = new HashMap<>();
    private boolean unmodifiableResult = false;

    @Inject
    HazelcastMaps hazelcastMaps;

    @Inject
    Provider<RemoveClientsTask> removeClientsTaskProvider;

    @Inject
    WeakLockProvider weakLockProvider;

    @PostConstruct
    void setup() {
        new ChainedTask.Builder<Map<UUID, CallDTO>>(this)
                .withLockProvider(() -> weakLockProvider.autoLock(LOCK_NAME))
                .<Set<UUID>, Set<UUID>>addSupplierEntry("Fetch CallIds",
                        () -> this.callIds,
                        receivedCallIds -> {
                            this.callIds.addAll(receivedCallIds);
                            return this.callIds;
                        }
                )
                .<Set<UUID>>addBreakCondition((callIds, resultHolder) -> {
                    if (Objects.isNull(callIds)) {
                        this.getLogger().warn("No Entities have been passed");
                        resultHolder.set(Collections.EMPTY_MAP);
                        return true;
                    }
                    if (callIds.size() < 1) {
                        resultHolder.set(Collections.EMPTY_MAP);
                        return true;
                    }
                    return false;
                })
                .<Set<UUID>, Map<UUID, CallDTO>> addFunctionalStage("Remove Call DTOs",
                        // action
                        callIds -> {
                            callIds.forEach(callId -> {
                                if (this.removedCallDTOs.containsKey(callId)) {
                                    return;
                                }
                                CallDTO callDTO = this.hazelcastMaps.getCalls().remove(callId);
                                this.removedCallDTOs.put(callId, callDTO);
                            });
                            return this.removedCallDTOs;
                        },
                        // rollback
                        (callEntitiesHolder, thrownException) -> {
                            if (Objects.isNull(callEntitiesHolder) || Objects.isNull(callEntitiesHolder.get())) {
                                this.getLogger().warn("Unexpected condition at rollback.");
                                return;
                            }
                            this.hazelcastMaps.getCalls().putAll(this.removedCallDTOs);
                        })
                .<Map<UUID, CallDTO>> addConsumerStage("Remove Room relation",
                        removedCallDTOs -> {
                            removedCallDTOs.forEach((callId, callDTO) -> {
                                var serviceRoomId = ServiceRoomId.make(callDTO.serviceId, callDTO.roomId);
                                var serviceRoomKey = ServiceRoomId.createKey(serviceRoomId);
                                this.hazelcastMaps.getServiceRoomToCallIds().remove(serviceRoomKey);
                                var callEntityBuilder = CallEntity.builder().withCallDTO(callDTO);
                            });
                        },
                        // rollback
                        (callDTOsHolder, thrownException) -> {
                            if (Objects.isNull(callDTOsHolder) || Objects.isNull(callDTOsHolder.get())) {
                                this.getLogger().warn("Unexpected condition at rollback. callEntities are null");
                                return;
                            }
                            this.removedCallDTOs.forEach((callId, callDTO) -> {
                                var serviceRoomId = ServiceRoomId.make(callDTO.serviceId, callDTO.roomId);
                                var serviceRoomKey = ServiceRoomId.createKey(serviceRoomId);
                                this.hazelcastMaps.getServiceRoomToCallIds().put(serviceRoomKey, callId);
                            });
                        })
                .addActionStage("Remove Call Client Relations",
                        // action
                        () -> {
                            Set<UUID> clientIds = new HashSet<>();
                            this.removedCallDTOs.keySet().forEach(callId -> {
                                Collection<UUID> callsClientIds = this.hazelcastMaps.getCallToClientIds().remove(callId);
                                this.removedCallClientIds.put(callId, callsClientIds);
                                callsClientIds.forEach(clientIds::add);
                            });
                            if (clientIds.size() < 1) {
                                return;
                            }
                            var task = removeClientsTaskProvider.get()
                                    .whereClientIds(clientIds);

                            if (!task.execute().succeeded()) {
                                logger.warn("Clients removal failed");
                            }
                        },
                        // rollback
                        (callDTOsHolder, thrownException) -> {
                            if (Objects.isNull(callDTOsHolder) || Objects.isNull(callDTOsHolder.get())) {
                                this.getLogger().warn("Unexpected condition at rollback. callEntities are null");
                                return;
                            }
                            this.removedCallClientIds.forEach((callId, clientIds) -> {
                                clientIds.forEach(clientId -> {
                                    this.hazelcastMaps.getCallToClientIds().put(callId, clientId);
                                });
                            });
                        })
                .<Map<UUID, CallDTO>> addTerminalSupplier("Completed", () -> {
                    if (this.unmodifiableResult) {
                        return Collections.unmodifiableMap(this.removedCallDTOs);
                    } else {
                        return this.removedCallDTOs;
                    }
                })
                .build();
    }

    public RemoveCallsTask whereCallIds(Set<UUID> callIds) {
        if (Objects.isNull(callIds) || callIds.size() < 1) {
            return this;
        }
        this.callIds.addAll(callIds);
        return this;
    }

    public RemoveCallsTask addRemovedCallDTO(CallDTO callDTO) {
        if (Objects.isNull(callDTO)) {
            return this;
        }
        this.callIds.add(callDTO.callId);
        this.removedCallDTOs.put(callDTO.callId, callDTO);
        return this;
    }

    public RemoveCallsTask withUnmodifiableResult(boolean value) {
        this.unmodifiableResult = value;
        return this;
    }
}
