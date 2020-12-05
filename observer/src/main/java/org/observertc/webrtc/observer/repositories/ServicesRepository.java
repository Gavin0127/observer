/*
 * Copyright  2020 Balazs Kreith
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.observertc.webrtc.observer.repositories;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.inject.Singleton;
import org.observertc.webrtc.observer.ObserverConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Singleton
public class ServicesRepository {
	private static final Logger logger = LoggerFactory.getLogger(ServicesRepository.class);

	private final Map<UUID, String> serviceMap = new HashMap<>();
	private final String defaultServiceName;

	public ServicesRepository(ObserverConfig.OutboundReportsConfig evaluatorsConfig) {
		this.defaultServiceName = evaluatorsConfig.defaultServiceName;
	}

	public String getServiceName(UUID serviceUUID) {
		String result = this.serviceMap.getOrDefault(serviceUUID, this.defaultServiceName);
		return result;
	}

}
