package org.kafka.message.spectator.domain;

import java.util.Objects;

import static org.kafka.message.spectator.constant.KafkaMessageSpectatorConstants.DEFAULT_POLLTIME;

/**
 * @author Hemambara vamsi, Kotari
 */
public class SpectatorInput {
	private String host;
	private String consumerGroup;
	private String topic;
	
	private int pollTime = DEFAULT_POLLTIME;
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public int getPollTime() {
		return pollTime;
	}
	
	public void setPollTime(int pollTime) {
		this.pollTime = pollTime;
	}
	
	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public String getConsumerGroup() {
		return consumerGroup;
	}
	
	public void setConsumerGroup(String consumerGroup) {
		this.consumerGroup = consumerGroup;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		SpectatorInput that = (SpectatorInput) o;
		
		if (pollTime != that.pollTime) return false;
		if (!Objects.equals(host, that.host)) return false;
		if (!Objects.equals(consumerGroup, that.consumerGroup))
			return false;
		return Objects.equals(topic, that.topic);
	}
	
	@Override
	public int hashCode() {
		int result = host != null ? host.hashCode() : 0;
		result = 31 * result + (consumerGroup != null ? consumerGroup.hashCode() : 0);
		result = 31 * result + (topic != null ? topic.hashCode() : 0);
		result = 31 * result + pollTime;
		return result;
	}
	
	@Override
	public String toString() {
		return "SpectatorInput{" +
				"host='" + host + '\'' +
				", consumerGroup='" + consumerGroup + '\'' +
				", topic='" + topic + '\'' +
				", pollTime=" + pollTime +
				'}';
	}
}
