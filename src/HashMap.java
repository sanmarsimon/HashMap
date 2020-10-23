package tp2;

import java.util.Iterator;
import java.util.Arrays;
import java.util.LinkedList;

public class HashMap<KeyType, DataType> implements Iterable<KeyType> {

    private static final int DEFAULT_CAPACITY = 20;
    private static final float DEFAULT_LOAD_FACTOR = 0.5f;
    private static final int CAPACITY_INCREASE_FACTOR = 2;

    private Node<KeyType, DataType>[] map;
    private int size = 0;
    private int capacity;
    private final float loadFactor; // Compression factor

    public HashMap() { this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR); }

    public HashMap(int initialCapacity) {
        this(initialCapacity > 0 ? initialCapacity : DEFAULT_CAPACITY,
                DEFAULT_LOAD_FACTOR);
    }

    public HashMap(int initialCapacity, float loadFactor) {
        capacity = initialCapacity;
        this.loadFactor = 1 / loadFactor;
        map = new Node[capacity];
    }

    /**
     * Finds the index attached to a particular key
     * This is the hashing function ("Fonction de dispersement")
     * @param key Value used to access to a particular instance of a DataType within map
     * @return Index value where this key should be placed in attribute map
     */
    private int hash(KeyType key){
        int keyHash = key.hashCode() % capacity;
        return Math.abs(keyHash);
    }

    /**
     * @return if map should be rehashed
     */
    private boolean needRehash() {
        return size * loadFactor > capacity;
    }

    /**
     * @return Number of elements currently in the map
     */
    public int size() {
        return size;
    }

    /**
     * @return Current reserved space for the map
     */
    public int capacity(){
        return capacity;
    }

    /**
     * @return if map does not contain any element
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /** TODO Average Case : O(1)
     * Find the next prime after increasing the capacity by CAPACITY_INCREASE_FACTOR (multiplication)
     */
    /*
    * Méthode isPrime et NextPrime inspirées des notes de cours (Cours 3)
    * */
    private static boolean isPrime(int n ) {
        if (n == 2 || n == 3)
            return true;
        if (n == 1 || n % 2 == 0)
            return false;
        for (int i = 3; i * i <= n; i += 2)
            if (n % i == 0)
                return false;
        return true;
    }

    private static int nextPrime(int n ){
        if( n % 2 == 0 )
            n++;
        for( ; !isPrime( n ); n += 2 )
            ;
        return n;
    }

    private void increaseCapacity() {
        // ...
        capacity = nextPrime(capacity*CAPACITY_INCREASE_FACTOR);
    }

    /** TODO Average Case : O(n)
     * Increases capacity by CAPACITY_INCREASE_FACTOR (multiplication) and
     * reassigns all contained values within the new map
     */
    private void rehash() {
        // ...
        Node<KeyType,DataType>[] oldMap = map;
        Node<KeyType,DataType> headNode; // To iterate through the list of nodes at a given position int the map
        Node<KeyType,DataType> currentNode;
        increaseCapacity();

        map = new Node[capacity];
        // On remplis la nouvelle map. Sachant que la capacité a agumenté la position de chaque Node
        // doit etre calculé à nouveau car il se pourrait qu'elle soit différente.
        for (int i = 0; i< oldMap.length; i++){
            headNode = oldMap[i];
            currentNode = headNode;
            if (headNode != null){
                currentNode = map[hash(headNode.key)];  // Nouvelle position ou placé le headNode
                if (currentNode == null){               // On vérifie que la position est vide.
                    map[hash(headNode.key)]= headNode;
                }
                else {
                    while (currentNode != null){
                        currentNode = currentNode.next;     // On parcours la liste jusqu'à trouver une position vide.
                    }
                    currentNode = headNode;                 // On écris notre Node à cette position vide
                }

                headNode = oldMap[i].next;                  // Le next de la headNode sur odlMap[i]

                while (headNode != null){
                    currentNode = map[hash(headNode.key)];
                    if (currentNode == null){
                        map[hash(headNode.key)] = headNode;
                    }
                    else{
                        while (currentNode != null){
                            currentNode = currentNode.next;
                        }
                    currentNode = headNode;                // On écris notre Node à cette position vide dans map
                    }

                    headNode = headNode.next;
                }
            }
        }

    }

    /** TODO Average Case : O(1)
     * Finds if map contains a key
     * @param key Key which we want to know if exists within map
     * @return if key is already used in map
     */
    public boolean containsKey(KeyType key) {

        Node<KeyType,DataType> currentNode = map[hash(key)];
        while (currentNode != null){
            if(currentNode.key.equals(key)){
                return true;
            }
            currentNode = currentNode.next;
        }
        return false;
    }

    /** TODO Average Case : O(1)
     * Finds the value attached to a key
     * @param key Key which we want to have its value
     * @return DataType instance attached to key (null if not found)
     */
    public DataType get(KeyType key) {
        Node<KeyType,DataType> currentNode = map[hash(key)];
        if(!containsKey(key) || isEmpty()){
            return null;
        }

        while (currentNode != null){
            if(currentNode.key.equals(key)){
                return currentNode.data;
            }
            currentNode = currentNode.next;
        }

        return null;
    }

    /** TODO Average Case : O(1) , Worst case : O(n)
     * Assigns a value to a key
     * @param key Key which will have its value assigned or reassigned
     * @return Old DataType instance at key (null if none existed)
     */
    public DataType put(KeyType key, DataType value) {

        if(!containsKey(key)){
            Node<KeyType,DataType> currentNode = map[hash(key)];
            if (currentNode == null){
                map[hash(key)] = new Node<KeyType,DataType>(key,value);
            }
            else {
                while (currentNode != null){

                    Node<KeyType,DataType> nextNode = currentNode.next;
                    if(nextNode == null) {
                        currentNode.next = new Node<KeyType, DataType>(key, value);
                        break;
                    }
                    currentNode = currentNode.next;
                }
            }
            size++;
            if(needRehash()){       // On vérifie si il y'a besoin de rehash à chaque fois que size augmente
                rehash();
            }
            return null;
        }
        else{
            Node<KeyType,DataType> currentNode = map[hash(key)];
            while (!currentNode.key.equals(key)){
                currentNode = currentNode.next;
            }
            DataType oldVal = currentNode.data;
            currentNode.data = value;
            return oldVal;
        }

    }

    /** TODO Average Case : O(1)
     * Removes the node attached to a key
     * @param key Key which is contained in the node to remove
     * @return Old DataType instance at key (null if none existed)
     */
    public DataType remove(KeyType key) {
        if(!containsKey(key)) {
            return null;
        }
        DataType oldValue;

        Node<KeyType,DataType> nodeToRemove = map[hash(key)];
        Node<KeyType,DataType> currentNode = map[hash(key)].next;
        if (nodeToRemove.key.equals(key)){
            oldValue = nodeToRemove.data;
            nodeToRemove = null;
            size--;
            return oldValue;
        }
        while (currentNode != null){
            if(currentNode.key.equals(key)){
                oldValue = currentNode.data;
                nodeToRemove.next = currentNode.next;
                currentNode = null;
                size--;
                return oldValue;
            }
            nodeToRemove = nodeToRemove.next;
            currentNode = currentNode.next;
        }
        return null;
    }

    /** TODO Worst Case : O(1)
     * Removes all nodes contained within the map
     */
    public void clear() {
        // ...
        map = new Node[capacity];
        size = 0;
    }

    static class Node<KeyType, DataType> {
        final KeyType key;
        DataType data;
        Node<KeyType, DataType> next; // Pointer to the next node within a Linked List

        Node(KeyType key, DataType data)
        {
            this.key = key;
            this.data = data;
            next = null;
        }
    }

    @Override
    public Iterator<KeyType> iterator() {
        return new HashMapIterator();
    }

    // Iterators are used to iterate over collections like so:
    // for (Key key : map) { doSomethingWith(key); }
    private class HashMapIterator implements Iterator<KeyType> {
        // TODO: Add any relevant data structures to remember where we are in the list.
        private int currentPos = 0;

        /** TODO Worst Case : O(n)
         * Determine if there is a new element remaining in the hashmap.
         */
        public boolean hasNext() {

            if(map[currentPos].next != null){
                return true;
            }
            currentPos++;
            while (currentPos < map.length){
                if (map[currentPos] != null){
                    return true;
                }
                currentPos++;
            }
            return false;
        }

        /** TODO Worst Case : O(n)
         * Return the next new key in the hashmap.
         */
        public KeyType next() {

            if( !hasNext( ) )
                return null;

            if(map[currentPos].next != null){
                return map[currentPos].next.key;
            }
            currentPos++;
            while( currentPos < map.length ) {
                if(map[currentPos] != null) {
                    return map[currentPos].key;
                }
                currentPos++;
            }
            return null;
        }
    }
}
