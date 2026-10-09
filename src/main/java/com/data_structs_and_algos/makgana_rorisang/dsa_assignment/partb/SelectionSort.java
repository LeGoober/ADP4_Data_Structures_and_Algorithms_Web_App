package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Selection Sort (iterative): repeatedly selects the smallest element of the
 * unsorted part and swaps it to the front. Time O(n^2) in every case, extra space O(1).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
@Order(1)
public class SelectionSort extends AbstractSorter {

    @Override
    public String getName() {
        return "Selection Sort";
    }

    /**
     * Sorts the array in ascending order, in place.
     *
     * @param a the array to sort
     * @param l listener notified of comparisons, swaps and finalised positions
     */
    @Override
    public void sort(int[] a, SortListener l) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            // Assume the first unsorted position holds the minimum
            int minimumIndex = i;
            for (int j = i + 1; j < n; j++) {
                // A smaller value means we have a new candidate minimum
                if (less(a, j, minimumIndex, l)) {
                    minimumIndex = j;
                }
            }
            // Only swap when the minimum is not already in place
            if (minimumIndex != i) {
                swap(a, i, minimumIndex, l);
            }
            // Everything up to and including i is now in its final position
            markSorted(i, l);
        }
    }
}
