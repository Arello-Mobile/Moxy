package com.arellomobile.mvp;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

// inspired by http://www.java2s.com/Code/Java/Collections-Data-Structure/WeakValueHashMap.htm
// own implementation

/**
 * The desired behaviour of an in-memory cache is to keep a weak reference to the cached object,
 * this will allow the garbage collector to remove an object from memory once it isn't needed
 * anymore.
 * <p>
 * A {@link HashMap} doesn't help here since it will keep hard references for key and
 * value objects. A {@link WeakHashMap} doesn't either, because it keeps weak references to the
 * key objects, but we want to track the value objects.
 * <p>
 * This implementation of a Map uses a {@link WeakReference} to the value objects. Once the
 * garbage collector decides it wants to finalize a value object, it will be removed from the
 * map automatically.
 *
 * @param <K> - the type of the key object
 * @param <V> - the type of the value object
 */
public class WeakValueHashMap<K, V> extends AbstractMap<K, V> {

	// the internal hash map to the weak references of the actual value objects
	private HashMap<K, WeakValue<V>> references;
	// the garbage collector's removal queue
	private ReferenceQueue<V> gcQueue;


	/**
	 * Creates a WeakValueHashMap with a desired initial capacity
	 *
	 * @param capacity - the initial capacity
	 */
	public WeakValueHashMap(int capacity) {
		references = new HashMap<>(capacity);
		gcQueue = new ReferenceQueue<>();
	}

	/**
	 * Creates a WeakValueHashMap with an initial capacity of 1
	 */
	public WeakValueHashMap() {
		this(1);
	}

	/**
	 * Creates a WeakValueHashMap and copies the content from an existing map
	 *
	 * @param map - the map to copy from
	 */
	public WeakValueHashMap(Map<? extends K, ? extends V> map) {
		this(map.size());
		for (Map.Entry<? extends K, ? extends V> entry : map.entrySet()) {
			put(entry.getKey(), entry.getValue());
		}
	}

	@Override
	public V put(K key, V value) {
		processQueue();
		WeakValue<V> valueRef = new WeakValue<V>(key, value, gcQueue);
		return getReferenceValue(references.put(key, valueRef));
	}

	;

	@Override
	public V get(Object key) {
		processQueue();
		return getReferenceValue(references.get(key));
	}

	@Override
	public V remove(Object key) {
		return getReferenceValue(references.remove(key));
	}

	@Override
	public void clear() {
		references.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		processQueue();
		return references.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		processQueue();
		for (Map.Entry<K, WeakValue<V>> entry : references.entrySet()) {
			if (value == getReferenceValue(entry.getValue())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Set<K> keySet() {
		processQueue();
		return references.keySet();
	}

	@Override
	public int size() {
		processQueue();
		return references.size();
	}

	@Override
	public Set<Map.Entry<K, V>> entrySet() {
		processQueue();

		Set<Map.Entry<K, V>> entries = new LinkedHashSet<>();
		for (Map.Entry<K, WeakValue<V>> entry : references.entrySet()) {
			entries.add(new AbstractMap.SimpleEntry<>(entry.getKey(), getReferenceValue(entry.getValue())));
		}
		return entries;
	}

	public Collection<V> values() {
		processQueue();

		Collection<V> values = new ArrayList<V>();
		for (WeakValue<V> valueRef : references.values()) {
			values.add(getReferenceValue(valueRef));
		}
		return values;
	}

	private V getReferenceValue(WeakValue<V> valueRef) {
		return valueRef == null ? null : valueRef.get();
	}

	// remove entries once their value is scheduled for removal by the garbage collector
	@SuppressWarnings("unchecked")
	private void processQueue() {
		WeakValue<V> valueRef;
		while ((valueRef = (WeakValue<V>) gcQueue.poll()) != null) {
			references.remove(valueRef.getKey());
		}
	}

	// for faster removal in {@link #processQueue()} we need to keep track of the key for a value
	private class WeakValue<T> extends WeakReference<T> {
		private final K key;

		private WeakValue(K key, T value, ReferenceQueue<T> queue) {
			super(value, queue);
			this.key = key;
		}

		private K getKey() {
			return key;
		}
	}

}