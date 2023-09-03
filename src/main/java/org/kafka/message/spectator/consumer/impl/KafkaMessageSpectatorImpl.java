package org.kafka.message.spectator.consumer.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.kafka.message.spectator.consumer.KafkaMessageSpectator;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hemambara vamsi, kotari
 */
@Service("kafkaMessageSpectator")
public class KafkaMessageSpectatorImpl implements KafkaMessageSpectator<String, String> {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageSpectatorImpl.class);
	
	@Override
	public long messageCount(KafkaConsumer<String, String> consumer, SpectatorInput input) {
		LOGGER.info("Retrieving message count for : {}", input);
		List<TopicPartition> partitions = getTopicPartitions(consumer, input);
		
		LOGGER.info("Num partitions : {}", partitions.size());
		
		consumer.assign(partitions);
		consumer.seekToEnd(Collections.emptySet());
		Map<TopicPartition, Long> endPartitions =
				partitions.stream()
						.collect(Collectors.toMap(Function.identity(), consumer:: position));
		
		long messageCount = partitions.stream()
								.mapToLong(endPartitions::get)
								.sum();
		
		LOGGER.info("Message count : {}", messageCount);
		
		return messageCount;
	}
	
	@Override
	public Map<String, String> pollMessages(KafkaConsumer<String, String> consumer, SpectatorInput input) {
		LOGGER.info("Retrieving messages for : {}", input);
		List<TopicPartition> topicPartitions = getTopicPartitions(consumer, input);
		
		LOGGER.info("Num partitions : {}", topicPartitions.size());
		
		consumer.assign(topicPartitions);
		consumer.seekToEnd(topicPartitions);
		
		Map<String, String> consumerRecords = new LinkedHashMap<>();
		for(TopicPartition topicPartition : topicPartitions){
			long consumerPosition = consumer.position(topicPartition);
			long startIndex =  consumerPosition - input.getStartPosition();
			if(startIndex < 0){
				startIndex =0;
			}
			consumer.seek(topicPartition, startIndex);
			ConsumerRecords<String, String> recordsFromEachPartition = consumer.poll(Duration.ofSeconds(input.getPollTime()));
			LOGGER.info("Partition : {}, consumer position : {}, start index : {}, message count :{}",
					topicPartition.partition(), consumerPosition, startIndex, recordsFromEachPartition.count());
			
			int index=0;
			for(ConsumerRecord<String, String> record: recordsFromEachPartition){
				String key = record.key();
				key = Objects.nonNull(key) ? key : topicPartition.partition() + "_" + index;
				consumerRecords.put(key, record.value());
				index++;
			}
		}
		
		LOGGER.info("Total Message count : {}", consumerRecords.size());
		
		return consumerRecords;
	}
	
	private List<TopicPartition> getTopicPartitions(KafkaConsumer<String, String> consumer, SpectatorInput input) {
		return consumer.partitionsFor(input.getTopic())
				.stream()
				.map(partitionInfo -> new TopicPartition(input.getTopic(), partitionInfo.partition()))
				.collect(Collectors.toList());
	}
}
