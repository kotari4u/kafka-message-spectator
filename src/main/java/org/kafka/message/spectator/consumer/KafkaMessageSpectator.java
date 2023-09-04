package org.kafka.message.spectator.consumer;

import org.kafka.message.spectator.domain.ConsumerInfo;
import org.kafka.message.spectator.domain.SpectatorInput;

/**
 * @author Hemambara vamsi, Kotari
 */
public interface KafkaMessageSpectator<K, V, Output> {
	
	Output spectate(ConsumerInfo consumerInfo, SpectatorInput input);
}
