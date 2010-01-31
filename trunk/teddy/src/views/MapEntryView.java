package views;

import java.io.Serializable;
import java.util.Map.Entry;

public class MapEntryView<K,V>
	implements Comparable<MapEntryView<K, V>>, Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6290012202782244506L;
	
	private K key;
	private V value;
	
	public MapEntryView() {
	}
	
	public MapEntryView (Entry<K, V> param){
		this.key = param.getKey();
		this.value = param.getValue();
	}
	
	public MapEntryView (K key, V value){
		this.key = key;
		this.value = value;		
	}
	
	public K getKey() {
		return this.key;
	}

	public void setKey (K key){
		this.key = key;
	}
	
	public V getValue() {
		return this.value;
	}

	public void setValue(V value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "{ " + this.getKey() + ", " + this.getValue() + "}" ;
	}

	@Override
	public int compareTo(MapEntryView<K, V> arg0) {
		return this.getKey().toString().compareTo(arg0.toString());
	}
}
