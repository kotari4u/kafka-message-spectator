package org.kafka.message.spectator.consumer.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Hemambara vamsi, kotari
 */
@Service("kafkaMessagePoll")
public class KafkaMessagePoll extends AbstractKafkaMessageSpectator<String, String, Map<String, String>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessagePoll.class);
	
	@Override
	public Map<String, String> spectate(KafkaConsumer<String, String> consumer, SpectatorInput input) {
		LOGGER.info("Retrieving messages for : {}", input);
		List<TopicPartition> topicPartitions = getTopicPartitions(consumer, input);
		Map<String, String> consumerRecords = new LinkedHashMap<>();
		
		LOGGER.info("Num partitions : {}", topicPartitions.size());
		
		consumer.assign(topicPartitions);
		
		for(TopicPartition topicPartition : topicPartitions) {
			long consumerPosition = consumer.position(topicPartition);
			long startIndex = Math.max(0, consumerPosition - input.getStartPosition());
			
			consumer.seek(topicPartition, startIndex);
			LOGGER.info("Partition : {}, consumer position : {}, start index : {}",
					topicPartition.partition(), consumerPosition, startIndex);
		}
		
		ConsumerRecords<String, String> recordsFromEachPartition =
				consumer.poll(Duration.ofSeconds(input.getPollTime()));
		LOGGER.info("Retrieved message count :{}", recordsFromEachPartition.count());
		
		for (ConsumerRecord<String, String> record : recordsFromEachPartition) {
			String key = record.key();
			key = Objects.nonNull(key) ? key : record.partition() + "_" + record.offset();
			consumerRecords.put(key, record.value());
		}
		
		LOGGER.info("Total Message count : {}", consumerRecords.size());
		
		return consumerRecords;
	}
}
