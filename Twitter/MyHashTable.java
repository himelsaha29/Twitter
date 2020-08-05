package FinalProject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 *
 * @author HimelSaha
 */
public class MyHashTable<K, V> implements Iterable<HashPair<K, V>> {

    // num of entries to the table
    private int numEntries;
    // num of buckets 
    private int numBuckets;
    // load factor needed to check for rehashing 
    private static final double MAX_LOAD_FACTOR = 0.75;
    // ArrayList of buckets. Each bucket is a LinkedList of HashPair
    private ArrayList<LinkedList<HashPair<K, V>>> buckets;

    // constructor
    public MyHashTable(int initialCapacity) {

        this.numBuckets = initialCapacity;
        this.numEntries = 0;
        this.buckets = new ArrayList<>(initialCapacity);
        for (int i = 0; i < this.numBuckets; i++) {
            this.buckets.add(null);
        }

    }

    public int size() {
        return this.numEntries;
    }

    public boolean isEmpty() {
        return this.numEntries == 0;
    }

    public int numBuckets() {
        return this.numBuckets;
    }

    /**
     * Returns the buckets variable. Useful for testing purposes.
     */
    public ArrayList<LinkedList< HashPair<K, V>>> getBuckets() {
        return this.buckets;
    }

    /**
     * Given a key, return the bucket position for the key.
     */
    public int hashFunction(K key) {
        int hashValue = Math.abs(key.hashCode()) % this.numBuckets;
        return hashValue;
    }

    /**
     * Takes a key and a value as input and adds the corresponding HashPair to
     * this HashTable. Expected average run time O(1)
     */
    public V put(K key, V value) {

        if (value == null || key == null) {
            return null;
        }

        if (((double) (this.numEntries + 1) / (double) this.numBuckets) > MAX_LOAD_FACTOR) {
            this.rehash();
        }

        if (buckets.get(this.hashFunction(key)) == null) {                      // if everything is empty at first

            HashPair<K, V> hp = new HashPair<>(key, value);
            LinkedList<HashPair<K, V>> list = new LinkedList<>();
            list.add(hp);
            this.buckets.set(this.hashFunction(key), list);
            this.numEntries++;
        } else {
            for (int i = 0; i < buckets.get(this.hashFunction(key)).size(); i++) {
                if (key.equals(buckets.get(this.hashFunction(key)).get(i).getKey())) {
                    V val = buckets.get(this.hashFunction(key)).get(i).getValue();
                    buckets.get(this.hashFunction(key)).get(i).setValue(value);
                    return val;
                }
            }
            HashPair<K, V> hp = new HashPair<>(key, value);
            this.buckets.get(this.hashFunction(key)).add(hp);
            this.numEntries++;
        }
        return null;
    }

    /**
     * Get the value corresponding to key. Expected average runtime O(1)
     */
    public V get(K key) {
        if (key == null) {
            return null;
        }

        if (this.buckets.get(this.hashFunction(key)) != null) {

            for (int i = 0; i < buckets.get(this.hashFunction(key)).size(); i++) {

                if (key.equals(buckets.get(this.hashFunction(key)).get(i).getKey())) {
                    return buckets.get(this.hashFunction(key)).get(i).getValue();
                }
            }
        }

        return null;
    }

    /**
     * Remove the HashPair corresponding to key . Expected average runtime O(1)
     */
    public V remove(K key) {
        if (key == null) {
            return null;
        }

        if (buckets.get(this.hashFunction(key)) != null) {

            for (int i = 0; i < buckets.get(this.hashFunction(key)).size(); i++) {
                if (key.equals(buckets.get(this.hashFunction(key)).get(i).getKey())) {
                    V val = buckets.get(this.hashFunction(key)).get(i).getValue();
                    buckets.get(this.hashFunction(key)).remove(i);
                    this.numEntries--;
                    return val;
                }
            }
        }

        return null;
    }

    /**
     * Method to double the size of the hashtable if load factor increases
     * beyond MAX_LOAD_FACTOR. Made public for ease of testing. Expected average
     * runtime is O(m), where m is the number of buckets
     */
    public void rehash() {

        MyHashTable<K, V> newTable = new MyHashTable<>(2 * this.numBuckets);

        for (HashPair<K, V> iter : this) {
            newTable.put(iter.getKey(), iter.getValue());
        }
        this.numBuckets = 2 * this.numBuckets;
        this.buckets = newTable.buckets;
    }

    /**
     * Return a list of all the keys present in this hashtable. Expected average
     * runtime is O(m), where m is the number of buckets
     */
    public ArrayList<K> keys() {
        if (this.isEmpty()) {
            return null;
        } else {
            ArrayList<K> keysList = new ArrayList<>();
            for (int i = 0; i < this.buckets.size(); i++) {
                if (this.buckets.get(i) != null) {
                    for (int j = 0; j < buckets.get(i).size(); j++) {
                        keysList.add(buckets.get(i).get(j).getKey());
                    }
                }
            }
            return keysList;
        }
    }

    /**
     * Returns an ArrayList of unique values present in this hashtable. Expected
     * average runtime is O(m) where m is the number of buckets
     */
    public ArrayList<V> values() {

        if (this.isEmpty()) {
            return null;
        } else {
            MyHashTable<V, K> m = new MyHashTable<>(numBuckets);
            for (int i = 0; i < this.buckets.size(); i++) {
                if (this.buckets.get(i) != null) {
                    for (int j = 0; j < this.buckets.get(i).size(); j++) {
                        m.put(this.buckets.get(i).get(j).getValue(), this.buckets.get(i).get(j).getKey());
                    }
                }
            }
            return m.keys();
        }
    }

    /**
     * This method takes as input an object of type MyHashTable with values that
     * are Comparable. It returns an ArrayList containing all the keys from the
     * map, ordered in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n^2), where n is the number of
     * pairs in the map.
     */
    public static <K, V extends Comparable<V>> ArrayList<K> slowSort(MyHashTable<K, V> results) {
        ArrayList<K> sortedResults = new ArrayList<>();
        for (HashPair<K, V> entry : results) {
            V element = entry.getValue();
            K toAdd = entry.getKey();
            int i = sortedResults.size() - 1;
            V toCompare = null;
            while (i >= 0) {
                toCompare = results.get(sortedResults.get(i));
                if (element.compareTo(toCompare) <= 0) {
                    break;
                }
                i--;
            }
            sortedResults.add(i + 1, toAdd);
        }
        return sortedResults;
    }

    /**
     * This method takes as input an object of type MyHashTable with values that
     * are Comparable. It returns an ArrayList containing all the keys from the
     * map, ordered in descending order based on the values they mapped to.
     *
     * The time complexity for this method is O(n*log(n)), where n is the number
     * of pairs in the map.
     */
    public static <K, V extends Comparable<V>> ArrayList<K> fastSort(MyHashTable<K, V> results) {
        ArrayList<HashPair<K, V>> unsorted = new ArrayList<>();

        for (HashPair<K, V> entry : results) {
            if (entry != null) {
                unsorted.add(entry);
            }
        }

        ArrayList<HashPair<K, V>> x = mergeSort(unsorted);
        ArrayList<K> res = new ArrayList<>();
        for (int i = 0; i < x.size(); i++) {
            res.add(x.get(i).getKey());
        }
        return res;
    }

    private static <K, V extends Comparable<V>> ArrayList<HashPair<K, V>> mergeSort(ArrayList<HashPair<K, V>> list) {
        ArrayList<HashPair<K, V>> list1;
        ArrayList<HashPair<K, V>> list2;
        if (list.size() == 1) {
            return list;
        } else {
            list1 = new ArrayList<>();
            list2 = new ArrayList<>();
            int mid = (list.size() - 1) / 2;
            for (int i = 0; i <= mid; i++) {
                list1.add(list.get(i));
            }
            for (int k = mid + 1; k < list.size(); k++) {
                list2.add(list.get(k));
            }

            list1 = mergeSort(list1);
            list2 = mergeSort(list2);

        }

        return merge(list1, list2);
    }

    private static <K, V extends Comparable<V>> ArrayList<HashPair<K, V>> merge(ArrayList<HashPair<K, V>> l1, ArrayList<HashPair<K, V>> l2) {

        ArrayList<HashPair<K, V>> sorted = new ArrayList<>();

        while (!l1.isEmpty() && !l2.isEmpty()) {

            if ((l1.get(0).getValue()).compareTo(l2.get(0).getValue()) >= 0) {

                sorted.add(l1.remove(0));
            } else {
                sorted.add(l2.remove(0));
            }
        }
        while (!l1.isEmpty()) {
            sorted.add(l1.remove(0));
        }
        while (!l2.isEmpty()) {
            sorted.add(l2.remove(0));
        }

        return sorted;
    }

    @Override
    public MyHashIterator iterator() {
        return new MyHashIterator();
    }

    private class MyHashIterator implements Iterator<HashPair<K, V>> {

        int index = 1;
        ArrayList<HashPair<K, V>> list = new ArrayList<>();
        HashPair<K, V> cur;

        /**
         * Expected average runtime is O(m) where m is the number of buckets
         */
        private MyHashIterator() {
            for (int i = 0; i < buckets.size(); i++) {
                if (buckets.get(i) != null) {
                    for (int j = 0; j < buckets.get(i).size(); j++) {
                        list.add(buckets.get(i).get(j));
                    }
                }
            }
            if (list.size() != 0) {
                cur = list.get(0);
            }
        }

        @Override
        /**
         * Expected average runtime is O(1)
         */
        public boolean hasNext() {
            return cur != null;
        }

        @Override
        /**
         * Expected average runtime is O(1)
         */
        public HashPair<K, V> next() {
            if (hasNext() == false) {
                throw new NoSuchElementException();
            }

            HashPair<K, V> nextIter = cur;
            try {
                if (index <= list.size()) {
                    cur = list.get(index);
                }
            } catch (Exception e) {
                cur = null;
            }
            index++;
            return nextIter;
        }
    }
}
