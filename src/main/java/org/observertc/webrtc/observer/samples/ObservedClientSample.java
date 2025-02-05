package org.observertc.webrtc.observer.samples;

import java.util.UUID;

public interface ObservedClientSample {

    String getServiceId();

    String getMediaUnitId();

    String getTimeZoneId();

    Long getTimestamp();

    String getRoomId();

    UUID getClientId();

    ClientSample getClientSample();

    String getMarker();

    String getUserId();

    int getSampleSeq();
}
