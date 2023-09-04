package org.kafka.message.spectator.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.kafka.message.spectator.domain.ConsumerInfo;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * @author Hemambara vamsi, kotari
 */
@Component("kafkaSpectatorFactory")
public class KafkaSpectatorFactory {
	
	private static final Map<String, ConsumerInfo> CONSUMER_STORE = new HashMap<>();
	private static final Object LOCK = new Object();
	
	public ConsumerInfo createConsumer(){
		return createConsumer("localhost:9092", "MySampleConsumer");
	}
	
	public ConsumerInfo createConsumer(String host, String consumer){
		if(Objects.nonNull(CONSUMER_STORE.get(consumer))){
			return CONSUMER_STORE.get(consumer);
		}
		
		ConsumerInfo consumerInfo;
		synchronized (LOCK) {
			final Properties props = new Properties();
			props.put(
					ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
					host
			);
			props.put(
					ConsumerConfig.GROUP_ID_CONFIG,
					consumer
			);
			props.put(
					ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
					StringDeserializer.class.getName()
			);
			props.put(
					ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
					StringDeserializer.class.getName()
			);
			// Create the consumer using props.
			
			KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
			consumerInfo = new ConsumerInfo(kafkaConsumer);
			CONSUMER_STORE.put(consumer, consumerInfo);
		}
		
		return consumerInfo;
	}
}
