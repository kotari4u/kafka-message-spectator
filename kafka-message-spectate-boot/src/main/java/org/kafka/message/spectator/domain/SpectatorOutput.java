package org.kafka.message.spectator.domain;

import java.util.Objects;

/**
 * Key
 * Value
 *
 * @author hemambarakotari
 */
public class SpectatorOutput<K, V> {
	
	private final K key;
	private final V value;
	
	public SpectatorOutput(K key, V value){
		this.key = key;
		this.value = value;
	}
	
	public K getKey() {
		return key;
	}
	
	public V getValue() {
		return value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		SpectatorOutput that = (SpectatorOutput) o;
		
		if (!Objects.equals(key, that.key)) return false;
		return Objects.equals(value, that.value);
	}
	
	@Override
	public int hashCode() {
		int result = (key != null ? key.hashCode() : 0);
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}
}
