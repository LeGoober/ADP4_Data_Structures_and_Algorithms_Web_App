package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link HeapSort}. */
class HeapSortTest {

    private final HeapSort sorter = new HeapSort();

    @Test
    void sortsRandomArray() {
        int[] data = DatasetLoader.random(1000, 5);
        int[] expected = data.clone();
        Arrays.sort(expected);
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(expected, data);
    }

    @Test
    void sortsEmptyAndSingleElementArrays() {
        int[] empty = {};
        int[] single = {2};
        sorter.sort(empty, SortListener.NO_OP);
        sorter.sort(single, SortListener.NO_OP);
        assertEquals(0, empty.length);
        assertArrayEquals(new int[] {2}, single);
    }

    @Test
    void sortsArrayWithDuplicates() {
        int[] data = {3, 3, 1, 2, 2, 1};
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(new int[] {1, 1, 2, 2, 3, 3}, data);
    }

    @Test
    void sortsReversedInput() {
        int[] data = DatasetLoader.reversed(300);
        sorter.sort(data, SortListener.NO_OP);
        assertArrayEquals(DatasetLoader.sorted(300), data);
    }

    @Test
    void marksEveryPositionSortedOnce() {
        SortTraceRecorder recorder = new SortTraceRecorder();
        sorter.sort(DatasetLoader.random(64, 6), recorder);
        long sorted = recorder.getSteps().stream().filter(s -> s.type == SortStepType.SORTED).count();
        assertEquals(64, sorted);
    }
}
