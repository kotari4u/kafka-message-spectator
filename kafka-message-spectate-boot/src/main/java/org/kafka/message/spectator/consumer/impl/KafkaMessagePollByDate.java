package org.kafka.message.spectator.consumer.impl;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.TopicPartition;
import org.kafka.message.spectator.domain.ConsumerInfo;
import org.kafka.message.spectator.domain.SpectatorDateInput;
import org.kafka.message.spectator.domain.SpectatorInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Hemambara vamsi, kotari
 */
@Service("kafkaMessagePollByDate")
public class KafkaMessagePollByDate extends AbstractKafkaMessageSpectator<String, String, Map<String, String>> {
	private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessagePollByDate.class);
	
	@Override
	public Map<String, String> spectate(ConsumerInfo consumerInfo, SpectatorInput input) {
		KafkaConsumer<String, String> consumer = consumerInfo.getConsumer();
		Map<String, String> consumerRecords = new LinkedHashMap<>();
		ConsumerRecords<String, String> recordsFromEachPartition = null;
		ZonedDateTime zdt =
				ZonedDateTime.of(((SpectatorDateInput) input).getDateTime(), ZoneId.of("America/New_York"));
		long dateOffset = zdt.toInstant().toEpochMilli();
		
		synchronized (consumerInfo.getLock()) {
			LOGGER.info("Retrieving messages for : {}", input);
			List<TopicPartition> topicPartitions = getTopicPartitions(consumer, input);
			
			LOGGER.info("Num partitions : {}", topicPartitions.size());
			
			consumer.assign(topicPartitions);
			
			Map<TopicPartition, Long> offsetPartitions = new HashMap<>();
			for (TopicPartition topicPartition : topicPartitions) {
				offsetPartitions.put(topicPartition, dateOffset);
			}
			
			Map<TopicPartition, OffsetAndTimestamp> offsetAndTimestampMap = consumer.offsetsForTimes(offsetPartitions);
			
			for (Map.Entry<TopicPartition, OffsetAndTimestamp> entry : offsetAndTimestampMap.entrySet()) {
				TopicPartition partition = entry.getKey();
				OffsetAndTimestamp offsetAndTimestamp = entry.getValue();
				if (Objects.nonNull(partition)
						&& Objects.nonNull(offsetAndTimestamp)) {
					consumer.seek(partition, offsetAndTimestamp.offset());
					LOGGER.info("Partition : {}, start index : {}",
							partition, offsetAndTimestamp.offset()
					);
				}
			}
			
			recordsFromEachPartition =
					consumer.poll(Duration.ofSeconds(input.getPollTime()));
			LOGGER.info("Retrieved message count :{}", recordsFromEachPartition.count());
		} // End of synchronization
		
		if(Objects.nonNull(recordsFromEachPartition)) {
			for (ConsumerRecord<String, String> record : recordsFromEachPartition) {
				String key = record.key();
				key = Objects.nonNull(key) ? key : record.partition() + "_" + record.offset();
				consumerRecords.put(key, record.value());
			}
		}
		
		LOGGER.info("Total Message count : {}", consumerRecords.size());
		
		return consumerRecords;
	}
}
