package pl.lodz.p.iad.structure;

import java.util.HashMap;

public class KsiazkaKodowa<K, E, V> {

	private K key;
	private E element;
    private V value;
    private HashMap<K, EntryBlad> lista;

    // Generic constructor
    public KsiazkaKodowa(K key, E element, V value) {
        this.key = key;
        this.element = element;
        this.value = value;
        this.lista = new HashMap<K, EntryBlad>(10000);
    }

    public KsiazkaKodowa(int size) {
    	this.lista = new HashMap<K, EntryBlad>(size);
	}

	// Generic methods
    public void setKey(K key) { this.key = key; }
    public void setElement(E element) { this.element = element; }
    public void setValue(V value) { this.value = value; }
    public K getKey()   { return key; }
    public E getElement() { return element; }
    public V getValue() { return value; }
    
    class EntryBlad {
    	private Point entry;
    	private Double blad;
    }
}
