package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.util.Random;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Quick Sort (recursive, divide and conquer) using the Lomuto partition scheme with a
 * random pivot. Average time O(n log n), worst case O(n^2); stack space O(log n)
 * on average and O(n) in the worst case.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
@Order(2)
public class QuickSort extends AbstractSorter {

    /** Random pivots make the O(n^2) worst case very unlikely, even on sorted input. */
    private final Random random = new Random(470);

    @Override
    public String getName() {
        return "Quick Sort";
    }

    /**
     * Sorts the whole array in ascending order, in place.
     *
     * @param a the array to sort
     * @param l listener notified of comparisons, swaps and finalised positions
     */
    @Override
    public void sort(int[] a, SortListener l) {
        quickSort(a, 0, a.length - 1, l);
    }

    /**
     * Recursively sorts a[lo..hi].
     *
     * @param a  the array being sorted
     * @param lo lowest index of the sub-array
     * @param hi highest index of the sub-array
     * @param l  listener
     */
    private void quickSort(int[] a, int lo, int hi, SortListener l) {
        // Base case: an empty range needs no work
        if (lo > hi) {
            return;
        }
        // Base case: a single element is already in its final position
        if (lo == hi) {
            markSorted(lo, l);
            return;
        }
        // The pivot ends up at its final position, then each side is sorted recursively
        int pivotIndex = partition(a, lo, hi, l);
        markSorted(pivotIndex, l);
        quickSort(a, lo, pivotIndex - 1, l);
        quickSort(a, pivotIndex + 1, hi, l);
    }

    /**
     * Partitions a[lo..hi] around a random pivot.
     *
     * @return the final index of the pivot
     */
    private int partition(int[] a, int lo, int hi, SortListener l) {
        // Move a randomly chosen pivot to the end of the range
        int randomIndex = lo + random.nextInt(hi - lo + 1);
        if (randomIndex != hi) {
            swap(a, randomIndex, hi, l);
        }
        // Boundary of the "smaller than pivot" region
        int boundary = lo - 1;
        for (int j = lo; j < hi; j++) {
            if (less(a, j, hi, l)) {
                boundary++;
                if (boundary != j) {
                    swap(a, boundary, j, l);
                }
            }
        }
        // Place the pivot between the two regions
        if (boundary + 1 != hi) {
            swap(a, boundary + 1, hi, l);
        }
        return boundary + 1;
    }
}
