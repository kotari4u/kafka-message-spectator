package org.kafka.message.spectator.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class SpectatorDateInput extends SpectatorInput{
	
	private LocalDateTime dateTime;
	
	public LocalDateTime getDateTime() {
		return dateTime;
	}
	
	public void setDateTime(LocalDateTime dateTime) {
		this.dateTime = dateTime;
	}
	
	@Override
	public String toString() {
		return "SpectatorDateInput{" +
				"topic='" + getTopic() + '\'' +
				", pollTime=" + getPollTime() +
				", dateTime=" + dateTime +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		
		SpectatorDateInput that = (SpectatorDateInput) o;
		
		return Objects.equals(dateTime, that.dateTime);
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (dateTime != null ? dateTime.hashCode() : 0);
		return result;
	}
}
