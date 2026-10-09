package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link OperationCounter}. */
class OperationCounterTest {

    @Test
    void countsComparisonsAndSwaps() {
        OperationCounter c = new OperationCounter();
        c.onCompare(0, 1);
        c.onCompare(1, 2);
        c.onSwap(0, 1);
        c.onSorted(0);
        assertEquals(2, c.getComparisons());
        assertEquals(1, c.getSwaps());
    }

    @Test
    void resetClearsCounters() {
        OperationCounter c = new OperationCounter();
        c.onCompare(0, 1);
        c.reset();
        assertEquals(0, c.getComparisons());
        assertEquals(0, c.getSwaps());
    }
}
