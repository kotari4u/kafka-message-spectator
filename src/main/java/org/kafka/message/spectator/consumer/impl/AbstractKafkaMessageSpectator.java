package org.kafka.message.spectator.consumer.impl;

import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.kafka.message.spectator.consumer.KafkaMessageSpectator;
import org.kafka.message.spectator.domain.SpectatorInput;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractKafkaMessageSpectator<K, V, Output>
		implements KafkaMessageSpectator<K, V, Output> {
	
	List<TopicPartition> getTopicPartitions(KafkaConsumer<String, String> consumer, SpectatorInput input) {
		return consumer.partitionsFor(input.getTopic())
				.stream()
				.map(partitionInfo -> new TopicPartition(partitionInfo.topic(), partitionInfo.partition()))
				.collect(Collectors.toList());
	}
}
