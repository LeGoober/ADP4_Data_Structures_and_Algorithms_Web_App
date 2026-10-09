package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link SelectionSort}. */
class SelectionSortTest {

    private final SelectionSort sorter = new SelectionSort();

    @Test
    void sortsRandomArray() {
        int[] data = DatasetLoader.random(500, 1);
        int[] expected = data.clone();
        Arrays.sort(expected);
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(expected, data);
    }

    @Test
    void sortsEmptyAndSingleElementArrays() {
        int[] empty = {};
        int[] single = {7};
        sorter.sort(empty, SortListener.NO_OP);
        sorter.sort(single, SortListener.NO_OP);
        assertEquals(0, empty.length);
        assertArrayEquals(new int[] {7}, single);
    }

    @Test
    void sortsArrayWithDuplicates() {
        int[] data = {5, 1, 5, 3, 1, 3};
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(new int[] {1, 1, 3, 3, 5, 5}, data);
    }

    @Test
    void leavesSortedArrayUnchanged() {
        int[] data = DatasetLoader.sorted(50);
        OperationCounter counter = new OperationCounter();
        sorter.sort(data, counter);
        assertArrayEquals(DatasetLoader.sorted(50), data);
        assertEquals(0, counter.getSwaps());
    }

    @Test
    void worksWithNoOpListener() {
        int[] data = {3, 2, 1};
        assertDoesNotThrow(() -> sorter.sort(data, SortListener.NO_OP));
        assertArrayEquals(new int[] {1, 2, 3}, data);
    }

    @Test
    void comparisonCountIsQuadratic() {
        OperationCounter counter = new OperationCounter();
        sorter.sort(DatasetLoader.random(100, 2), counter);
        // n(n-1)/2 comparisons for n = 100
        assertEquals(4950, counter.getComparisons());
    }
}
