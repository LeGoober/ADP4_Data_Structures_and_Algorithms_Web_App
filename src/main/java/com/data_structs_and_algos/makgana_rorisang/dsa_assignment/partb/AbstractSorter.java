package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/**
 * Base class for every sorting algorithm. All listener calls live here so each
 * algorithm reads like textbook code.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public abstract class AbstractSorter implements Sorter {

    /**
     * Compares two positions of the array and notifies the listener.
     *
     * @param a the array being sorted
     * @param i first index
     * @param j second index
     * @param l listener told about the comparison
     * @return true when a[i] is strictly smaller than a[j]
     */
    protected boolean less(int[] a, int i, int j, SortListener l) {
        l.onCompare(i, j);
        return a[i] < a[j];
    }

    /**
     * Swaps two elements and notifies the listener.
     *
     * @param a the array being sorted
     * @param i first index
     * @param j second index
     * @param l listener told about the swap
     */
    protected void swap(int[] a, int i, int j, SortListener l) {
        int temporary = a[i];
        a[i] = a[j];
        a[j] = temporary;
        l.onSwap(i, j);
    }

    /**
     * Reports that position i now holds its final value.
     *
     * @param i the finalised index
     * @param l listener told about the finalised position
     */
    protected void markSorted(int i, SortListener l) {
        l.onSorted(i);
    }
}
