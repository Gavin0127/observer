package org.observertc.webrtc.observer.sinks.kafka;

/**
 * The key determines the partition the producer writes the report to.
 * When several consumer reads a report, one consumer reads reports from one partition.
 * Choosing the right key strategy based on your needs can help you to
 * distribute the reports accordingly in a distributed system.
 */
public enum KeyAssignmentStrategy {
    /**
     * Every report will have a random key
     */
    RANDOM,
    /**
     * Every report generated by the same observer instance
     * have the same key
     */
    INSTANCE_BASED,
    /**
     * Keys are selected if they are presented in the report
     * in the following order:
     * callId, sfuId
     */
//    CALL_SFU_ORIENTED,
}
