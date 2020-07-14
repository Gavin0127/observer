package org.observertc.webrtc.common.reports;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeName("JOINED_PEER_CONNECTION")
public class JoinedPeerConnectionReport extends Report {
	public static JoinedPeerConnectionReport of(UUID observerUUID, UUID callUUID, UUID peerConnectionUUID, LocalDateTime joined) {
		JoinedPeerConnectionReport result = new JoinedPeerConnectionReport();
		result.callUUID = callUUID;
		result.observerUUID = observerUUID;
		result.peerConnectionUUID = peerConnectionUUID;
		result.joined = joined;
		return result;
	}

	@JsonCreator
	public JoinedPeerConnectionReport() {
		super(ReportType.JOINED_PEER_CONNECTION);
//		super(ReportType.JOINED_PEER_CONNECTION.name());
	}

	public UUID observerUUID;
	public UUID callUUID;
	public UUID peerConnectionUUID;

	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	public LocalDateTime joined;
}
