package org.kafka.message.spectator.consumer;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.kafka.message.spectator.config.KafkaMessageSpectateConfig;
import org.kafka.message.spectator.domain.ConsumerInfo;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
	
	@Autowired
	@Qualifier("config")
	private KafkaMessageSpectateConfig config;
	
	@Value("${kafka.message.spectate.maxPoll}")
	private int maxPollRecords;
	
	public ConsumerInfo createConsumer(SpectatorInput spectatorInput){
		return createConsumer(spectatorInput.getHost(), spectatorInput.getConsumerGroup());
	}
	
	/**
	 * Consumer should be only one for consumer group.
	 * There is no need to create multiple consumers for the same consumer group
	 * Giving parallel access will cause issues while seeking the offset#
	 *
	 * @param host
	 * @param consumer
	 * @return
	 */
	 ConsumerInfo createConsumer(String host, String consumer){
		String uniqueKey = host + consumer;
		if(Objects.nonNull(CONSUMER_STORE.get(uniqueKey))){
			return CONSUMER_STORE.get(uniqueKey);
		}
		
		ConsumerInfo consumerInfo;
		synchronized (LOCK) {
			final Properties consumerProperties = new Properties();
			consumerProperties.put(
					ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
					host
			);
			consumerProperties.put(
					ConsumerConfig.GROUP_ID_CONFIG,
					consumer
			);
			consumerProperties.put(
					ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
					StringDeserializer.class.getName()
			);
			consumerProperties.put(
					ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
					StringDeserializer.class.getName()
			);
			consumerProperties.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, this.maxPollRecords);
			consumerProperties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
			consumerProperties.put(ConsumerConfig.CLIENT_ID_CONFIG, consumer);
			consumerProperties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
			// Create the consumer using props.
			
			KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(consumerProperties);
			consumerInfo = new ConsumerInfo(kafkaConsumer);
			CONSUMER_STORE.put(uniqueKey, consumerInfo);
		}
		
		return consumerInfo;
	}
}
