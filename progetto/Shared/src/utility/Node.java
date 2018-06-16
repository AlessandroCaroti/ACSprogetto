package utility;

import java.util.Map;

class Node<K,V> implements Map.Entry<K,V> {
    final int hash;
    final K key;
    volatile V val;

    Node(int hash, K key, V val) {
        this.hash = hash;
        this.key = key;
        this.val = val;
    }

    public final K getKey()       { return key; }
    public final V getValue()     { return val; }
    public final int hashCode()   { return key.hashCode() ^ val.hashCode(); }
    public final String toString(){ return key + "=" + val; }
    public final V setValue(V value) { throw new UnsupportedOperationException(); }

    public final boolean equals(Object o) {
        Object v, u;
        return ((o instanceof Map.Entry) &&
                (v = ((Map.Entry<?,?>)o).getValue()) != null &&
                (v == (u = val) || v.equals(u)));
    }
}

//ConcurrentLinkedQueue<Node<UserName, Stub>> -> lista degli utenti con il loro stub che si sono iscritti ad uno specifico topic