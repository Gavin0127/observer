package org.observertc.webrtc.observer;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.EachProperty;
import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties("evaluators")
public class EvaluatorsConfig {

	public ActiveStreamsConfig activeStreams;

	public SampleTransformerConfig sampleTransformer;

	public CallCleanerConfig callCleaner;

	public ReportDraftsConfig reportDrafts;
	
	@ConfigurationProperties("activeStreams")
	public static class ActiveStreamsConfig {
		public int missingBrowserIDMapSize;
		public int maxTimeBrowserIdCanMiss;
	}

	@ConfigurationProperties("reportDrafts")
	public static class ReportDraftsConfig {
		public int TTL = 120;
	}

	@ConfigurationProperties("callCleaner")
	public static class CallCleanerConfig {
		public int streamMaxIdleTimeInS;
		public int streamMaxAllowedGapInS;
		public int pcRetentionTimeInDays;

	}

	@ConfigurationProperties("sampleTransformer")
	public static class SampleTransformerConfig {
		public boolean reportOutboundRTPs = true;
		public boolean reportInboundRTPs = true;
		public boolean reportRemoteInboundRTPs = true;
		public boolean reportTracks = true;
		public boolean reportMediaSources = true;
		public boolean reportCandidatePairs = true;
		public boolean reportLocalCandidates = true;
		public boolean reportRemoteCandidates = true;
		public int sentReportsCacheSize = 100000;
		public List<IPFlagConfig> ipFlags = new ArrayList<>();

		//		@ConfigurationProperties("ipFlags")
		@EachProperty("ipFlags")
		public static class IPFlagConfig {
			public String name;
			public List<String> networks;
		}
	}


}

