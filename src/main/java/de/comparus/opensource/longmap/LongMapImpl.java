package de.comparus.opensource.longmap;

public class LongMapImpl<V> implements LongMap<V> {

	// Constants:
	/**
	 * Initial default capacity which is used if user doesn't specify his own
	 * capacity in constructor
	 */
	public static final int DEFAULT_CAPACITY = 1 << 4;

	/**
	 * Maximum capacity which was chosen equal to HashMap capacity
	 */
	public static final int MAX_CAPACITY = 1 << 30;
	
	public static final int MIN_LENGTH_TO_DECREASING = 100;

	// Variables:
	private int _capacity;
	private int _size = 0;
	private float _maxLoad = 0.75f;
	private float _minLoad = 0.35f;
	private float _currentLoad;
	private Entry<V>[] _entries;
	private int _bucketsNumber = 0;
	private boolean _doRehash = true;
	private boolean _doDecreasing = false;

	// ------------------------------------------------------------------------------
	// Constructors:

	/**
	 * Constructs an empty <tt>LongMapImpl</tt> with the default initial capacity
	 * (16) and the default load factor (0.75).
	 */
	@SuppressWarnings("unchecked")
	public LongMapImpl() {
		this._capacity = DEFAULT_CAPACITY;
		this._entries = new Entry[DEFAULT_CAPACITY];
	}

	/**
	 * Constructs an empty <tt>LongMapImpl</tt> with the specified initial capacity
	 * and default load factor (0.75).
	 *
	 * @param initialCapacity
	 *            the initial capacity
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative
	 */
	@SuppressWarnings("unchecked")
	public LongMapImpl(int initialCapacity) {
		checkInitCapcity(initialCapacity);
		this._capacity = initialCapacity;
		this._entries = new Entry[initialCapacity];
	}
	
	/**
	 * 
	 * @param initialCapacity the initial capacity
	 * @param doDecreasing if true - do decreasing if current loading of 
	 * LonMapImpl is less than _minload, if false - no decreasing  
	 */
	@SuppressWarnings("unchecked")
	public LongMapImpl(int initialCapacity, boolean doDecreasing) {
		checkInitCapcity(initialCapacity);
		this._capacity = initialCapacity;
		this._entries = new Entry[initialCapacity];
	}

	// ------------------------------------------------------------------------------
	// Methods to implement:

	public V put(long key, V value) {
		Entry<V> prevEntry = null;
		V oldValue = null;
		_currentLoad = calcCurrentLoad();
		if (_currentLoad > _maxLoad) {
			increaseCapacity(_capacity);
			if (_doRehash) {
				rehash(_capacity);
			} else {
				System.out.println("Your HashMap is " + _currentLoad * 100 + "% full. Be careful!");
			}
		}

		int idx = (int) (getHash(key) % _capacity);
		if (_entries[idx] == null) {
			_entries[idx] = new Entry<V>(key, value);
			_size++;
			_bucketsNumber++;
		} else {
			Entry<V> currentEntry = _entries[idx];
			while (currentEntry != null && currentEntry.getKey() != key) {
				prevEntry = currentEntry;
				currentEntry = currentEntry.getNext();
			}
			// If there isn't an entry with such key let's add a new entry with the key
			if (currentEntry == null) {
				prevEntry.setNext(new Entry<V>(key, value));
				_size++;
			} else {
				// If there is an entry with such key lets redefine old value by new value
				oldValue = currentEntry.getValue();
				currentEntry.setValue(value);
			}
		}

		return oldValue;
	}

	/**
	 * 
	 */
	public V get(long key) {
		int idx = (int) (getHash(key) % _capacity);
		Entry<V> currentEntry = _entries[idx];
		if (currentEntry == null)
			return null;
		else {
			while (currentEntry != null && currentEntry.getKey() != key) {
				currentEntry = currentEntry.getNext();
			}
			if (currentEntry == null) {
				return null;
			}
			return currentEntry.getValue();
		}
	}

	public V remove(long key) {
		Entry<V> prevEntry = null;
		Entry<V> nextEntry = null;
		V value;
		int idx = (int) (getHash(key) % _capacity);
		Entry<V> currentEntry = _entries[idx];
		if (currentEntry == null) {
			return null;
		} else {
			if (currentEntry.getKey() == key) {
				_entries[idx] = currentEntry.getNext();
				_size--;
				if (_entries[idx] == null) {
					_bucketsNumber--;
					if (_doDecreasing && calcCurrentLoad() < _minLoad && _entries.length > MIN_LENGTH_TO_DECREASING) {
						decreaseCapacity(_capacity);
					}
				}
				return currentEntry.getValue();
			} else {
				while (currentEntry != null && currentEntry.getKey() != key) {
					prevEntry = currentEntry;
					currentEntry = currentEntry.getNext();
					nextEntry = currentEntry.getNext();
				}
				if (currentEntry == null) {
					return null;
				}
				_size--;
				value = currentEntry.getValue();
				prevEntry.setNext(nextEntry);
				return value;
			}
		}

	}

	public boolean isEmpty() {
		return _size == 0;
	}

	public boolean containsKey(long key) {
		int idx = (int) (getHash(key) % _capacity);
		Entry<V> currentEntry = _entries[idx];
		if (currentEntry == null) {
			return false;
		} else {
			while (currentEntry != null && currentEntry.getKey() != key) {
				currentEntry = currentEntry.getNext();
			}
			if (currentEntry == null) {
				return false;
			}
			return true;
		}
	}

	public boolean containsValue(V value) {
		for (Entry<V> entry : _entries) {
			if (entry == null) {
				continue;
			}
			while (entry != null && entry.getValue() != value) {
				entry = entry.getNext();
			}
			if (entry != null) {
				return true;
			}
		}
		return false;
	}

	public long[] keys() {
		int n = 0;
		long[] keys = new long[_size];
		for (Entry<V> entry : _entries) {
			if (entry == null) {
				continue;
			}
			while (entry != null) {
				keys[n++] = entry.getKey();
				entry = entry.getNext();
			}
		}
		return keys;
	}

	public V[] values() {
		int n = 0;
		@SuppressWarnings("unchecked")
		V[] values = (V[]) new Object[_size];
		for (Entry<V> entry : _entries) {
			if (entry == null) {
				continue;
			}
			while (entry != null) {
				values[n++] = entry.getValue();
				entry = entry.getNext();
			}
		}
		return values;
	}

	public long size() {
		return _size;
	}

	@SuppressWarnings("unchecked")
	public void clear() {
		_size = 0;
		_bucketsNumber = 0;
		this._entries = new Entry[DEFAULT_CAPACITY];
	}
	// -------------------------------------------------------------------------------

	// Auxiliary methods and classes:

	/**
	 * 
	 * @param initialCapacity
	 *            the initial capacity
	 * @throws IllegalArgumentException
	 *             if the initial capacity is negative or change it to MAX_CAPACITY
	 *             if it is more than the last one
	 */
	private void checkInitCapcity(int initialCapacity) {
		if (initialCapacity < 0) {
			throw new IllegalArgumentException("Illegal initial capacity: " + initialCapacity);
		}
		if (initialCapacity > MAX_CAPACITY) {
			System.err.println("Specified capacity is exeeding posible one and will be change to MAX_CAPACITY");
			initialCapacity = MAX_CAPACITY;
		}
	}

	/**
	 * Computes hash code of a key using static method from class Long
	 * 
	 * @param key
	 *            the key for which a hash code is computed
	 * @return hash code of the key
	 */
	private int getHash(long key) {
		return Long.hashCode(key);
	}

	/**
	 * Entity which represent pare key-value and has mechanism to create linked list
	 * if collisions happen
	 * 
	 * @author Mykola Muntian
	 *
	 * @param <E>
	 *            type of a value which is stored in an entry
	 */
	private class Entry<E> {
		private long key;
		private V value;
		private Entry<V> next;

		Entry(long key, V value) {
			this.key = key;
			this.value = value;
		}

		public long getKey() {
			return key;
		}

		public V getValue() {
			return value;
		}

		public void setValue(V value) {
			this.value = value;
		}

		public Entry<V> getNext() {
			return next;
		}

		public void setNext(Entry<V> next) {
			this.next = next;
		}
	}

	/**
	 * Computes current loading of LongHashMap
	 */
	private float calcCurrentLoad() {
		return _bucketsNumber / _capacity;
	}

	/**
	 * Redistributes elements in connection entries capacity changing
	 */
	@SuppressWarnings("unchecked")
	private void rehash(int newCapacity) {
		_size = 0;
		_bucketsNumber = 0;
		Entry<V>[] oldEntries = _entries;
		_entries = new Entry[newCapacity];
		for (Entry<V> entry : oldEntries) {
			if (entry != null) {
				while (entry != null) {
					put(entry.getKey(), entry.getValue());
					entry = entry.getNext();
				}
			}
		}
	}

	private void increaseCapacity(int currentCapacity) {
		currentCapacity = currentCapacity * 2;
		if (currentCapacity > MAX_CAPACITY) {
			_doRehash = false;
			currentCapacity = MAX_CAPACITY;
		}
	}

	private void decreaseCapacity(int currentCapacity) {
		_doRehash = true;
		currentCapacity = currentCapacity / 2;
	}
}
