package org.kafka.message.spectator.domain;

import java.util.Objects;

import static org.kafka.message.spectator.constant.KafkaMessageSpectatorConstants.DEFAULT_POLLTIME;

/**
 * @author Hemambara vamsi, Kotari
 */
public class SpectatorInput {
	private String topic;
	private int startPosition;
	private int endPosition;
	
	
	private int pollTime = DEFAULT_POLLTIME;
	
	public String getTopic() {
		return topic;
	}
	
	public void setTopic(String topic) {
		this.topic = topic;
	}
	
	public int getStartPosition() {
		return startPosition;
	}
	
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	
	public int getEndPosition() {
		return endPosition;
	}
	
	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}
	
	public int getPollTime() {
		return pollTime;
	}
	
	public void setPollTime(int pollTime) {
		this.pollTime = pollTime;
	}
	
	@Override
	public String toString() {
		return "SpectatorInput{" +
				"topic='" + topic + '\'' +
				", startPosition=" + startPosition +
				", endPosition=" + endPosition +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		SpectatorInput that = (SpectatorInput) o;
		
		if (startPosition != that.startPosition) return false;
		if (endPosition != that.endPosition) return false;
		return Objects.equals(topic, that.topic);
	}
	
	@Override
	public int hashCode() {
		int result = topic != null ? topic.hashCode() : 0;
		result = 31 * result + startPosition;
		result = 31 * result + endPosition;
		return result;
	}
}
