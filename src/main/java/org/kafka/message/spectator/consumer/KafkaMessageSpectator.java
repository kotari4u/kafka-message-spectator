package org.kafka.message.spectator.consumer;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.kafka.message.spectator.domain.SpectatorInput;

import java.util.Map;

/**
 * @author Hemambara vamsi, Kotari
 */
public interface KafkaMessageSpectator<K, V> {
	
	long messageCount(KafkaConsumer<K, V> consumer, SpectatorInput input);
	
	Map<String, String> pollMessages(KafkaConsumer<String, String> consumer, SpectatorInput input);
}
