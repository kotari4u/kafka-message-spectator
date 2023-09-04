package org.kafka.message.spectator.consumer.impl;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author Hemambara vamsi, kotari
 */
@Service("kafkaMessageCount")
public class KafkaMessageCount extends AbstractKafkaMessageSpectator<String, String, Long> {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageCount.class);
	
	@Override
	public Long spectate(KafkaConsumer<String, String> consumer, SpectatorInput input) {
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
}
