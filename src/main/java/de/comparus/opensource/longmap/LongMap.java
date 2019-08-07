package de.comparus.opensource.longmap;

import java.util.List;

public interface LongMap<V> {
	V put(long key, V value);

	V get(long key);

	V remove(long key);

	boolean isEmpty();

	boolean containsKey(long key);

	boolean containsValue(V value);

	long[] keys();

	/**
	 * I have changed return type from V[] to List<V> to save types covariance of
	 * returned data structure
	 */
	List<V> values();

	/**
	 * I have changed return type from long to int because maximum size was accepted
	 * equals maximum size of HashMap = 1073741824 and int can contains this number
	 * 
	 */
	int size();

	void clear();
}
