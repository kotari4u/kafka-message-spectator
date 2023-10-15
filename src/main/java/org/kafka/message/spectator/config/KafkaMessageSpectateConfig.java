package org.kafka.message.spectator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Read configuration from application.yml file
 * This is to segregate a way to provide configuration.
 * If there is change in a way to read configuration,
 * we don't have to change the whole code.
 *
 * @author hemambarakotari
 */
@Configuration("config")
@EnableConfigurationProperties
@ConfigurationProperties("kafka.message.spectate")
public class KafkaMessageSpectateConfig {
	private int maxPoll;
	private String offsetReset;
	private String isolationLevel;
	
	private List<ConsumerConfig> consumers;
	
	public int getMaxPoll() {
		return maxPoll;
	}
	
	public String getOffsetReset() {
		return offsetReset;
	}
	
	public String getIsolationLevel() {
		return isolationLevel;
	}
	
	public List<ConsumerConfig> getConsumers() {
		return consumers;
	}
	
	public void setMaxPoll(int maxPoll) {
		this.maxPoll = maxPoll;
	}
	
	public void setOffsetReset(String offsetReset) {
		this.offsetReset = offsetReset;
	}
	
	public void setIsolationLevel(String isolationLevel) {
		this.isolationLevel = isolationLevel;
	}
	
	public void setConsumers(List<ConsumerConfig> consumers) {
		this.consumers = consumers;
	}
	
	public static class ConsumerConfig {
		
		private String host;
		private List<String> consumerGroups;
		private List<String> topics;
		
		public String getHost() {
			return host;
		}
		
		public void setHost(String host) {
			this.host = host;
		}
		
		public List<String> getConsumerGroups() {
			return consumerGroups;
		}
		
		public void setConsumerGroups(List<String> consumerGroups) {
			this.consumerGroups = consumerGroups;
		}
		
		public List<String> getTopics() {
			return topics;
		}
		
		public void setTopics(List<String> topics) {
			this.topics = topics;
		}
	}
}