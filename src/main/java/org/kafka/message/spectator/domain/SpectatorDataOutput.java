package org.kafka.message.spectator.domain;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * This is the final output from rest controller
 *
 * @author hemambarakotari
 */
public class SpectatorDataOutput<K, V> {
	
	private long count;
	
	private List<SpectatorOutput<K, V>> messages;
	
	public SpectatorDataOutput(){
	}
	
	public SpectatorDataOutput(Map<K, V> keyValuePairs){
		init(keyValuePairs);
	}
	
	private void init(Map<K,V> keyValuePairs) {
		if (Objects.nonNull(keyValuePairs)) {
			this.count = keyValuePairs.size();
			this.messages =
					keyValuePairs
					.entrySet()
					.stream()
					.map(messageEntry -> new SpectatorOutput<>(messageEntry.getKey(), messageEntry.getValue()))
					.collect(Collectors.toList());
		}
	}
	
	public long getCount() {
		return count;
	}
	
	public List<SpectatorOutput<K, V>> getMessages() {
		return messages;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		SpectatorDataOutput<?, ?> that = (SpectatorDataOutput<?, ?>) o;
		
		if (count != that.count) return false;
		return Objects.equals(messages, that.messages);
	}
	
	@Override
	public int hashCode() {
		int result = (int) (count ^ (count >>> 32));
		result = 31 * result + (messages != null ? messages.hashCode() : 0);
		return result;
	}
}
