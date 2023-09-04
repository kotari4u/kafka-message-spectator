package org.kafka.message.spectator.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kafka.message.spectator.domain.SpectatorInput;

/**
 * @author Hemambara vamsi, Kotari
 */
public interface KafkaMessageSpectator<K, V, Output> {
	
	Output spectate(KafkaConsumer<K, V> consumer, SpectatorInput input);
}
