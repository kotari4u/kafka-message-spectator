package org.kafka.message.spectator.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.stereotype.Component;

import java.util.Properties;

/**
 * @author Hemambara vamsi, kotari
 */
@Component("kafkaSpectatorFactory")
public class KafkaSpectatorFactory {
	
	public KafkaConsumer<String, String> createConsumer(){
		final Properties props = new Properties();
		props.put(
				ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
				"localhost:9092");
		props.put(ConsumerConfig.GROUP_ID_CONFIG,
				"MySampleConsumer");
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class.getName());
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
				StringDeserializer.class.getName());
		// Create the consumer using props.
		
		return new KafkaConsumer<>(props);
	}
}
