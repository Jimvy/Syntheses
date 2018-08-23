package util;

import java.util.NoSuchElementException;

/*
 * TODO
 * - IndexMinPQ : implémente les méthodes suivantes
 *   - insert(int k, Item item) -> void;
 *   - change(int k, Item item) -> void;
 *   - contains(int k) -> boolean;
 *   - delete(int k) -> void;
 *   - min() -> Item;
 *   - minIndex() -> int;
 *   - delMin() -> int;
 *   - size() -> int et isEmpty() -> boolean;
 *   Les éléments y sont référés par les indices, et sont triés selon les item. A tout moment, chaque indice ne réfère qu'à un seul item.
 * - Rendre la file de priorité stable. Deux manières :
 *   - Rajouter une clé qui indique l'ordre dans lequel les éléments ont été insérés.
 *   - TODO
 */

/**
 * Une queue de priorité, qui permet d'accéder en temps constant au plus petit élément de l'ensemble,
 * d'insérer de nouveaux éléments en temps logarithmique, et de supprimer le plus petit élément en temps logarithmique.
 * Ce heap n'est pas stable : l'ordre de sortie des éléments égaux n'est pas défini.
 * @param <K> le type de données stockées dans la queue de priorité.
 */
public class MinHeap<K extends Comparable<? super K>> extends AbstractHeap<K> implements MinPriorityQueue<K> {

    // Inherited from AbstractHeap : n, minCapacity, ROOT, tab

    public MinHeap() {
        this(minCapacity);
    }

    @SuppressWarnings("unchecked")
    public MinHeap(int initialCapacity) {
        super(initialCapacity);
    }

    /**
     * Rétablit la propriété du tas, en remontant depuis l'élément à l'indice i.
     * Aussi nommé bottom-up reheapify, trickle-up, bubble-up, sift-up
     *
     * @param i l'indice de l'élément à partir duquel il faut rétablir l'ordre.
     */
    private void swim(int i) {
        assert (1 <= i && i <= n);
        K temp = tab[i];
        // min-heap : temp < tab[i/2]
        while (i != 1 && temp.compareTo(tab[i >> 1]) < 0) {
                tab[i] = tab[i >> 1];
                i >>= 1;
        }
        tab[i] = temp;
    }

    /**
     * Rétablit la propriété du tas, en descendant depuis l'élément à l'indice i.
     * Aussi nommé top-down reheapify, trickle-down, sift-down.
     *
     * @param i l'indice de l'élément à partir duquel il faut rétablir l'ordre.
     */
    private void sink(int i) {
        assert (1 <= i && i <= n);
        K temp = tab[i];
        int j = i << 1;
        while (j <= n) {
            // tab[j] is left child, tab[j+1] is (possibly absent if j==n) right child.
            if (j < n && tab[j].compareTo(tab[j+1]) > 0) {
                // right child should be used only if it is strictly less than left child (min heap)
                j++;
            }
            if (temp.compareTo(tab[j]) > 0) {
                tab[j >> 1] = tab[j];
                j <<= 1;
            } else {
                break;
            }
        }
        tab[j >> 1] = temp;
    }

    @Override
    public void insert(K key) {
        // typically, if n=7, then there is no more place in the array of size 8, so we have to double its size
        if (n + 1 >= tab.length)
            resize(2 * tab.length);
        // if n=7, then the new element should be placed at position n=8
        this.tab[++this.n] = key;
        // from now on, n=8, the new element is at index 8, and we have to reheapify.
        swim(n);
    }

    @Override
    public K min() {
        if (n <= 0 || tab.length < 2)
            throw new NoSuchElementException();
        return tab[ROOT];
    }

    @SuppressWarnings("Duplicates")
    @Override
    public K deleteMin() {
        if (n <= 0 || tab.length < 2)
            throw new NoSuchElementException();
        K temp = tab[ROOT];
        // if n=7, then we have to remove the element at index 7 and decrement n.
        tab[ROOT] = tab[n];
        tab[n--] = null; // loitering
        if (n < tab.length/4)
            resize(tab.length/2);
        if (n > 0)
            sink(ROOT);
        return temp;
    }
}
