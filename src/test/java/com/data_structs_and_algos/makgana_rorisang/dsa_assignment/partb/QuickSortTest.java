package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link QuickSort}. */
class QuickSortTest {

    private final QuickSort sorter = new QuickSort();

    @Test
    void sortsRandomArray() {
        int[] data = DatasetLoader.random(1000, 3);
        int[] expected = data.clone();
        Arrays.sort(expected);
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(expected, data);
    }

    @Test
    void sortsEmptyAndSingleElementArrays() {
        int[] empty = {};
        int[] single = {1};
        sorter.sort(empty, SortListener.NO_OP);
        sorter.sort(single, SortListener.NO_OP);
        assertEquals(0, empty.length);
        assertArrayEquals(new int[] {1}, single);
    }

    @Test
    void sortsArrayWithDuplicates() {
        int[] data = {4, 4, 4, 1, 1, 9, 9, 4};
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(new int[] {1, 1, 4, 4, 4, 4, 9, 9}, data);
    }

    @Test
    void sortsSortedAndReversedInput() {
        int[] up = DatasetLoader.sorted(2000);
        int[] down = DatasetLoader.reversed(2000);
        sorter.sort(up, SortListener.NO_OP);
        sorter.sort(down, SortListener.NO_OP);
        assertArrayEquals(DatasetLoader.sorted(2000), up);
        assertArrayEquals(DatasetLoader.sorted(2000), down);
    }

    @Test
    void usesFarFewerComparisonsThanQuadratic() {
        OperationCounter counter = new OperationCounter();
        sorter.sort(DatasetLoader.random(1000, 4), counter);
        assertTrue(counter.getComparisons() < 30_000);
    }
}
