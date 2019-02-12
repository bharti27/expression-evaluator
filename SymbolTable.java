public class SymbolTable<T> {

	/**
	 * @author bhartisharma
	 *
	 * @param <K>
	 * @param <V>
	 */
	class TableEntry<K, V> {
		private K key;
		private V value;

		/**
		 * @param key
		 * @param value
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}

		/**
		 * @return
		 */
		public K getKey() {
			return key;
		}

		/**
		 * @return
		 */
		public V getValue() {
			return value;
		}

		public String toString() {
			return key.toString() + "=" + value.toString();
		}
	}

	private TableEntry<String, T>[] hashtable;
	private int size;
	private int capacity;

	/**
	 * creates a hash table where the initial storage is 2 and
	 * string keys can be mapped to T values
	 */
	public SymbolTable() {
		hashtable = (TableEntry<String, T>[]) new TableEntry[2];
		capacity = 2;
		size = 0;
	}

	/**
	 * returns the size of hash table
	 * @return capacity
	 */
	public int getCapacity() {
		return capacity;
	}

	/**
	 * returns the number of elements in the hash table
	 * @return size
	 */
	public int size() {
		return size;
	}

	/**
	 * places value v at the location of key k.
	 * This method uses linear probing if a location is in use.
	 * If the key already exists in the table, replace its value with v. 
	 * If the key isn't in the table and the table is >= 80% full, 
	 * expand the table to twice the size and rehash.
	 * @param k
	 * @param v
	 */
	public void put(String k, T v) {
		if ((size() + 1) / getCapacity() >= 0.8) {
			rehash(size());
		}
		int hash = k.hashCode();
		int index = (hash & 0x7FFFFFFF) % getCapacity();
		if (hashtable[index] == null || hashtable[index].getKey().equals(k)) {
			hashtable[index] = new TableEntry(k, v);
			size++;
			return;
		}
		index++;
		boolean isFound = false;
		while ( ! isFound ) {
			if (hashtable[index] == null || hashtable[index].getKey().equals(k)) {
				hashtable[index] = new TableEntry(k, v);
				size++;
				return;
			}

			index = index + 1;
			if (index >= getCapacity()) {
				index = 0;
			}
		}
	}

	/**
	 * Removes the given key (and associated value) 
	 * from the table and returns the value removed or null if value is not found.
	 * @param k
	 * @return T
	 */
	public T remove(String k) {
		int hash = k.hashCode();
		int index = (hash & 0x7FFFFFFF) % getCapacity();
		
		if ( hashtable[index] == null ) {
			return null;
		}
		
		if ( hashtable[index].getKey().equals(k) ) {
			T value = hashtable[index].getValue();
			hashtable[index] = null;
			size--;
			return value;
		}
		index++;
		boolean isFound = false;
		while ( ! isFound ) {
			if (hashtable[index] != null && hashtable[index].getKey().equals(k)) {
				T value = hashtable[index].getValue();
				hashtable[index] = null;
				size--;
				return value;
			}

			index = index + 1;
			if (index >= getCapacity()) {
				index = 0;
			}
		}
		return null;
		
	}

	/**
	 * Given a key, returns the value from the table, returns null if not found
	 * @param k
	 * @return T
	 */
	public T get(String k) {
		int hash = k.hashCode();
		int index = (hash & 0x7FFFFFFF) % getCapacity();
		
		if ( hashtable[index] == null ) {
			return null;
		}
		
		if ( hashtable[index].getKey().equals(k) ) {
			return hashtable[index].getValue();
		}
		
		index++;
		boolean isFound = false;
		while ( ! isFound ) {
			if (hashtable[index] != null && hashtable[index].getKey().equals(k)) {
				return hashtable[index].getValue();
			}

			index = index + 1;
			if (index >= getCapacity()) {
				index = 0;
			}
		}
		
		return null;
	}

	/**
	 *  Clear the table and resize it to its default size
	 */
	public void clear() {
		hashtable = (TableEntry<String, T>[]) new TableEntry[2];
		capacity = 2;
		size = 0;
	}

	/**
	 * Increases or decreases the size of the hash table, rehashing all values
	 * @param size
	 * @return boolean
	 */
	public boolean rehash(int size) {
		int oldCapacity = getCapacity();
		int newCapacity = oldCapacity * 2;
		capacity = newCapacity;
		TableEntry<String, T>[] oldHashTable = hashtable;
		TableEntry<String, T>[] newHashTable = (TableEntry<String, T>[]) new TableEntry[newCapacity];

		for (int i = 0; i < oldCapacity; i++) {
			if (oldHashTable[i] != null) {
				int index = (oldHashTable[i].getKey().hashCode() & 0x7FFFFFFF) % newCapacity;
				if (index < oldCapacity) {
					newHashTable[index] = oldHashTable[i];
				} else {
					return false;
				}
			}
		}
		hashtable = newHashTable;
		return true;
	}

	@Override
	public String toString() {
		String displayValue = "";
		for (TableEntry<String, T> entry : hashtable) {
			if (entry != null) {
				if (displayValue.isEmpty()) {
					displayValue = displayValue + entry.toString();
				} else {
					displayValue = displayValue + ", " + entry.toString();
				}
			}
		}
		return displayValue;
	}
}
