package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Heap Sort: builds a max-heap inside the array, then repeatedly swaps the maximum to the
 * end and restores the heap. Time O(n log n) in every case, extra space O(1).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
@Order(3)
public class HeapSort extends AbstractSorter {

    @Override
    public String getName() {
        return "Heap Sort";
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
        buildMaxHeap(a, l);
        for (int end = n - 1; end > 0; end--) {
            // The root is the largest remaining element: move it to its final slot
            swap(a, 0, end, l);
            markSorted(end, l);
            siftDown(a, 0, end, l);
        }
        if (n > 0) {
            markSorted(0, l);
        }
    }

    /** Turns the array into a max-heap by sifting down every parent, last parent first. */
    private void buildMaxHeap(int[] a, SortListener l) {
        for (int i = a.length / 2 - 1; i >= 0; i--) {
            siftDown(a, i, a.length, l);
        }
    }

    /**
     * Restores the max-heap property below index i.
     *
     * @param a        the array holding the heap
     * @param i        root of the sub-heap to repair
     * @param heapSize number of elements still inside the heap
     * @param l        listener
     */
    private void siftDown(int[] a, int i, int heapSize, SortListener l) {
        while (true) {
            int largest = i;
            int left = 2 * i + 1;
            int right = 2 * i + 2;
            if (left < heapSize && less(a, largest, left, l)) {
                largest = left;
            }
            if (right < heapSize && less(a, largest, right, l)) {
                largest = right;
            }
            if (largest == i) {
                return;
            }
            swap(a, i, largest, l);
            i = largest;
        }
    }
}
