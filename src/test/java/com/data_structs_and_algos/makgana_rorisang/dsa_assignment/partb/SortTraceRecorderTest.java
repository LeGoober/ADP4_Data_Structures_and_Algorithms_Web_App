package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link SortTraceRecorder}. */
class SortTraceRecorderTest {

    @Test
    void recordsStepsInOrder() {
        SortTraceRecorder r = new SortTraceRecorder();
        r.onCompare(0, 1);
        r.onSwap(0, 1);
        r.onSorted(1);
        assertEquals(3, r.getSteps().size());
        assertEquals(SortStepType.COMPARE, r.getSteps().get(0).type);
        assertEquals(SortStepType.SWAP, r.getSteps().get(1).type);
        assertEquals(SortStepType.SORTED, r.getSteps().get(2).type);
        assertEquals(1, r.getSteps().get(2).i);
    }

    @Test
    void replayingSwapsSortsTheArray() {
        int[] original = {4, 2, 5, 1, 3};
        SortTraceRecorder r = new SortTraceRecorder();
        new SelectionSort().sort(original.clone(), r);
        int[] replay = original.clone();
        for (SortStep s : r.getSteps()) {
            if (s.type == SortStepType.SWAP) {
                int t = replay[s.i];
                replay[s.i] = replay[s.j];
                replay[s.j] = t;
            }
        }
        assertArrayEquals(new int[] {1, 2, 3, 4, 5}, replay);
    }
}
