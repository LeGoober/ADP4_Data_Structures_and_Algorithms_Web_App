package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link BinarySearch}. */
class BinarySearchTest {

    private final BinarySearch search = new BinarySearch();
    private final int[] data = {2, 4, 6, 8, 10, 12, 14, 16, 18, 20};

    @Test
    void iterativeFindsPresentKey() {
        assertEquals(3, search.iterative(data, 8, null));
        assertEquals(0, search.iterative(data, 2, null));
        assertEquals(9, search.iterative(data, 20, null));
    }

    @Test
    void iterativeReturnsMinusOneForMissingKey() {
        assertEquals(-1, search.iterative(data, 7, null));
        assertEquals(-1, search.iterative(data, 100, null));
    }

    @Test
    void recursiveMatchesIterative() {
        for (int key = 0; key <= 22; key++) {
            assertEquals(search.iterative(data, key, null), search.recursive(data, key, null));
        }
    }

    @Test
    void probeCountIsLogarithmic() {
        int[] big = DatasetLoader.sorted(1024);
        List<int[]> probes = new ArrayList<>();
        search.iterative(big, -1, (lo, mid, hi) -> probes.add(new int[] {lo, mid, hi}));
        // floor(log2(1024)) + 1 = 11 probes at most
        assertTrue(probes.size() <= 11);
    }

    @Test
    void handlesEmptyArray() {
        assertEquals(-1, search.iterative(new int[0], 1, null));
        assertEquals(-1, search.recursive(new int[0], 1, null));
    }
}
