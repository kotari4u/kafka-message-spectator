package org.kafka.message.spectator.domain;

import org.apache.kafka.clients.consumer.KafkaConsumer;

public class ConsumerInfo {
	private KafkaConsumer<String, String> consumer;
	private Object lock;
	
	public ConsumerInfo(KafkaConsumer<String, String> consumer){
		this(consumer, new Object());
	}
	
	public ConsumerInfo(KafkaConsumer<String, String> consumer, Object lock){
		this.consumer = consumer;
		this.lock = lock;
	}
	
	public KafkaConsumer<String, String> getConsumer() {
		return consumer;
	}
	
	public Object getLock() {
		return lock;
	}
}
