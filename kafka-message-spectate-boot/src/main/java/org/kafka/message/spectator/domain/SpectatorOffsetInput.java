package org.kafka.message.spectator.domain;

public class SpectatorOffsetInput extends SpectatorInput{
	private int startPosition;
	
	public int getStartPosition() {
		return startPosition;
	}
	
	public void setStartPosition(int startPosition) {
		this.startPosition = startPosition;
	}
	
	@Override
	public String toString() {
		return "SpectatorOffsetInput{" +
				"topic='" + getTopic() + '\'' +
				", pollTime=" + getPollTime() +
				"startPosition=" + startPosition +
				'}';
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		if (!super.equals(o)) return false;
		
		SpectatorOffsetInput that = (SpectatorOffsetInput) o;
		
		return startPosition == that.startPosition;
	}
	
	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + startPosition;
		return result;
	}
}
