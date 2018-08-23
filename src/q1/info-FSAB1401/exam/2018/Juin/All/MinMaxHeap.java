package util;

import java.util.NoSuchElementException;

public class MinMaxHeap<T extends Comparable<T>> extends AbstractHeap<T> implements MinPriorityQueue<T>, MaxPriorityQueue<T> {
    private static final int ROOT = 1;
    private static final int minCapacity = 32;
    /*
     * Represents this heap in a concise form
     * min levels : every line whose MSB indicates a power of 4 : 1, 4, 5, 6, 7, 16, 17, ..., 30, 31
     * max levels : every other lines : 2, 3, 8, 9, ..., 14, 15
     */
    // private T[] tab;
    // private int n;

    public MinMaxHeap() {
        this(minCapacity);
    }
    @SuppressWarnings("unchecked")
    public MinMaxHeap(int initialCapacity) {
        super(initialCapacity);
    }

    @Override
    public void insert(T key) {
        if (n +1 >= tab.length)
            resize(2* tab.length);
        tab[++n] = key;
        swim(n);
    }

    @Override
    public T min() {
        if (n <= 0) throw new NoSuchElementException();
        return tab[ROOT];
    }

    @Override
    public T max() {
        if (n <= 0) throw new NoSuchElementException();
        // If there is only one element, we have to return it, as it is both the min and the max.
        if (n == 1)
            return tab[ROOT];
        else {
            if (n >= 3 && tab[3].compareTo(tab[2]) > 0)
                return tab[3];
            else
                return tab[2];
        }
    }

    @Override
    public T deleteMin() {
        if (n <= 0 || tab.length < 2) throw new NoSuchElementException();
        T temp = tab[ROOT];
        tab[ROOT] = tab[n];
        tab[n--] = null; // loitering
        if (n < tab.length/4 && tab.length > minCapacity)
            resize(tab.length/2);
        if (n > 0)
            sink(ROOT);
        return temp;
    }

    @Override
    public T deleteMax() {
        if (n <= 0 || tab.length < 2) throw new NoSuchElementException();
        if (n == 1) return deleteMin(); // after all, this is the same element.
        // otherwise, we have to choose from two elements.
        int j = 2; // left max
        if (3 <= n && tab[2].compareTo(tab[3]) < 0) j = 3;
        T temp = tab[j];
        tab[j] = tab[n];
        tab[n--] = null;
        if (n < tab.length/4 && tab.length > minCapacity)
            resize(tab.length/2);
        if (n == 2)
            sink(2); // probably useless
        else // n >= 3
            sink(j);
        return temp;
    }

    /**
     * Should only be called on an item that may be in an incorrect position.
     * The rest of the heap should respect its property, as this method will not restore all the heap,
     * but only the parts affected by the element at index i.
     * @param i index down which we have to restore the heap property.
     */
    private void sink(int i) {
        if (isMaxLevel(i)) // i is on a max-level
            sinkMax(i);
        else
            sinkMin(i);
    }

    /**
     * Should only be called on an item that may be in an incorrect position.
     * The rest of the heap should respect its property, as this method will not restore all the heap,
     * but only the parts affected by the element at index i.
     * @param i index down which we have to restore the heap property.
     */
    private void sinkMin(int i) {
        /* Pseudo-code
         * if A[i] has children then
         *   m = index of the smallest of the children and grandchildren (if any) of i.
         *   if A[m] is a grandchild of A[i] then
         *     if A[m] < A[i] then
         *       swap(A, m, i)
         *       if A[m] > A[parent(m)] then
         *         swap(A, m, parent(m))
         *       endif
         *       sinkMin(m)
         *     endif
         *   else // A[m] is a child of A[i]
         *     if A[m] < A[i] then
         *       swap(A, i, m)
         *     endif
         *   endif
         * endif
         */
        /* Recall that i is on a min-level ; therefore, its grandchildren are from the standard heap and should be greater,
         * and its children should be greater, but come from the max-heap part. Also, we assume that the elements
         * at the max-level are greater than those on the low min-level: failure to do so results in undefined behavior,
         * as this method only restores the heap property for the specified element. */
        int j;
        while ((j = (i << 1)) <= n) {
            int m = j; // assume left child is the smallest ; may change
            if (j+1 <= n && tab[m].compareTo(tab[j+1]) > 0) // right child smallest ?
                m = j+1;
            if (2*j <= n && tab[m].compareTo(tab[2*j]) > 0) // left-left grandchild ?
                m = 2*j;
            if (2*j+1 <= n && tab[m].compareTo(tab[2*j+1]) > 0) // left-right grandchild ?
                m = 2*j+1;
            if (2*(j+1) <= n && tab[m].compareTo(tab[2*(j+1)]) > 0) // right-left grandchild ?
                m = 2*(j+1);
            if (2*j+3 <= n && tab[m].compareTo(tab[2*j+3]) > 0) // right-right grandchild ?
                m = 2*j+3;
            if (tab[m].compareTo(tab[i]) < 0) {
                // alas, this is not the case, and so we should exchange i with the real smaller element
                if (m >= 2*j) {
                    // m is a grandchild of i
                    T temp = tab[i];
                    tab[i] = tab[m];
                    tab[m] = temp;
                    if (tab[m].compareTo(tab[m >> 1]) > 0) {
                        /* Before the swap, m referred to a grandchild of i. We have therefore restored the heap property
                         * for the root and its other children, but there may still be problems in respect to
                         * the max-level child: so we have to swap them in this case.
                         */
                        temp = tab[m];
                        tab[m] = tab[m >> 1];
                        tab[m >> 1] = temp;
                    }
                    i = m; // move on to child
                } else {
                    // m is a child of i ; in a properly constructed heap, this should happen only if there is no grandchildren.
                    T temp = tab[i];
                    tab[i] = tab[m];
                    tab[m] = temp;
                    break; // we're done: there are no more grandchildren.
                }
            } else {
                break;
            }
        }
    }

    /**
     * Should only be called on an item that may be in an incorrect position.
     * The rest of the heap should respect its property, as this method will not restore all the heap,
     * but only the parts affected by the element at index i.
     * @param i index down which we have to restore the heap property.
     */
    private void sinkMax(int i) {
        /* Pseudo-code
         * if A[i] has children then
         *   m = index of the greatest of the children and grandchildren (if any) of i.
         *   if A[m] is a grandchild of A[i] then
         *     if A[m] > A[i] then
         *       swap(A, m, i)
         *       if A[m] < A[parent(m)] then
         *         swap(A, m, parent(m))
         *       endif
         *       sinkMin(m)
         *     endif
         *   else // A[m] is a child of A[i]
         *     if A[m] > A[i] then
         *       swap(A, i, m)
         *     endif
         *   endif
         * endif
         */
        /* Recall that i is on a min-level ; therefore, its grandchildren are from the standard heap and should be greater,
         * and its children should be greater, but come from the max-heap part. Also, we assume that the elements
         * at the max-level are greater than those on the low min-level: failure to do so results in undefined behavior,
         * as this method only restores the heap property for the specified element. */
        int j;
        while ((j = (i << 1)) <= n) {
            int m = j; // assume left child is the greatest ; may change
            if (j+1 <= n && tab[m].compareTo(tab[j+1]) < 0) // right child smallest ?
                m = j+1;
            if (2*j <= n && tab[m].compareTo(tab[2*j]) < 0) // left-left grandchild ?
                m = 2*j;
            if (2*j+1 <= n && tab[m].compareTo(tab[2*j+1]) < 0) // left-right grandchild ?
                m = 2*j+1;
            if (2*(j+1) <= n && tab[m].compareTo(tab[2*(j+1)]) < 0) // right-left grandchild ?
                m = 2*(j+1);
            if (2*j+3 <= n && tab[m].compareTo(tab[2*j+3]) < 0) // right-right grandchild ?
                m = 2*j+3;
            if (tab[m].compareTo(tab[i]) > 0) {
                // alas, this is not the case, and so we should exchange i with the real smaller element
                if (m >= 2*j) {
                    // m is a grandchild of i
                    T temp = tab[i];
                    tab[i] = tab[m];
                    tab[m] = temp;
                    if (tab[m].compareTo(tab[m >> 1]) < 0) {
                        /* Before the swap, m referred to a grandchild of i. We have therefore restored the heap property
                         * for the root and its other children, but there may still be problems in respect to
                         * the max-level child: so we have to swap them in this case.
                         */
                        temp = tab[m];
                        tab[m] = tab[m >> 1];
                        tab[m >> 1] = temp;
                    }
                    i = m; // move on to child
                } else {
                    // m is a child of i ; in a properly constructed heap, this should happen only if there is no grandchildren.
                    T temp = tab[i];
                    tab[i] = tab[m];
                    tab[m] = temp;
                    break; // we're done: there are no more grandchildren.
                }
            } else {
                break;
            }
        }
    }

    /**
     * This method should only be called to restore the heap property up the index i. It assumes that the rest of the heap
     * is correctly ordered: failure to do so results in undefined behavior.
     * @param i index up which we have to restore the heap property.
     */
    private void swim(int i) {
        /* Pseudo-code
         * if i is on a min-level then
         *   if i has a parent and A[i] > A[parent(i)] then
         *     swap(A, i, parent(i))
         *     swimMax(parent(i))
         *   else
         *     swimMin(parent(i))
         *   endif
         * else
         *   if i has a parent and A[i] < A[parent(i)] then
         *     swap(A, i, parent(i))
         *     swimMin(parent(i))
         *   else
         *     swimMax(parent(i))
         *   endif
         * endif
         */
        int j = i >> 1;
        if (isMaxLevel(i)) {
            if (ROOT <= j && tab[i].compareTo(tab[j]) < 0) {
                // The parent, which is on a min-level, is incorrectly greater than i: replace i with it, and restore.
                T temp = tab[i];
                tab[i] = tab[j];
                tab[j] = temp;
                /* After the swap, the order of the two levels is correct, and so is the order of the max levels;
                 * but the order of the min levels may have changed. */
                swimMin(j);
            } else {
                /* The parent is correctly less than or equal to i: we only have to make sure that i is not greater than
                 * its grandparents. */
                swimMax(i);
            }
        } else {
            if (ROOT <= j && tab[i].compareTo(tab[j]) > 0) {
                // The parent, which is on a max-level, is incorrectly lesser than i; replace it with i, and restore.
                T temp = tab[i];
                tab[i] = tab[j];
                tab[j] = temp;
                /* After the swap, the order of the two levels is correct, and so is the order of the min levels;
                 * but the order of the max levels may have changed. */
                swimMax(j);
            } else {
                /* The parent is correctly greater than or equal to i: we only have to make sure that i is not less than
                 * its grandparents. Also, we may be at the root. */
                swimMin(i);
            }
        }
    }

    /**
     * This method should only be called to restore the heap property up the index i. It assumes that the rest of the heap
     * is correctly ordered: failure to do so results in undefined behavior.
     * @param i index up which we have to restore the heap property.
     */
    private void swimMin(int i) {
        /* Pseudo-code
         * if A[i] has a grandparent then
         *   if A[i] < A[grandparent(i)] then
         *     swap(A, i, grandparent(i))
         *     swimMin(grandparent(i))
         *   endif
         * endif
         * TODO iterative fashion
         */
        int j;
        if ((j = (i >> 2)) >= ROOT) {
            if (tab[i].compareTo(tab[j]) < 0) {
                T temp = tab[i];
                tab[i] = tab[j];
                tab[j] = temp;
                swimMin(j);
            }
        }
    }

    /**
     * This method should only be called to restore the heap property up the index i. It assumes that the rest of the heap
     * is correctly ordered: failure to do so results in undefined behavior.
     * @param i index up which we have to restore the heap property.
     */
    private void swimMax(int i) {
        /* Pseudo-code
         * if A[i] has a grandparent then
         *   if A[i] > A[grandparent(i)] then
         *     swap(A, i, grandparent(i))
         *     swimMin(grandparent(i))
         *   endif
         * endif
         * TODO iterative fashion
         */
        int j;
        if ((j = (i >> 2)) >= ROOT) {
            if (tab[i].compareTo(tab[j]) > 0) {
                T temp = tab[i];
                tab[i] = tab[j];
                tab[j] = temp;
                swimMax(j);
            }
        }
    }

    /**
     * @param i position to consider
     * @return true if on maxLevel, false otherwise
     */
    private boolean isMaxLevel(int i) {
        assert (1 <= i);
        int k = 0;
        while (i != 0) {
            k++;
            i >>= 1;
        }
        return (k % 2) == 0;
    }
    /*@Test
    public void test() {
        assertFalse(isMaxLevel(1));
        assertTrue(isMaxLevel(2));
        assertTrue(isMaxLevel(3));
        assertFalse(isMaxLevel(4));
        assertFalse(isMaxLevel(5));
        assertFalse(isMaxLevel(6));
        assertFalse(isMaxLevel(7));
        assertTrue(isMaxLevel(8));
        assertTrue(isMaxLevel(9));
        assertTrue(isMaxLevel(10));
        assertTrue(isMaxLevel(11));
        assertTrue(isMaxLevel(12));
        assertTrue(isMaxLevel(13));
        assertTrue(isMaxLevel(14));
        assertTrue(isMaxLevel(15));
        assertFalse(isMaxLevel(16));
        assertFalse(isMaxLevel(17));
    }*/
}
