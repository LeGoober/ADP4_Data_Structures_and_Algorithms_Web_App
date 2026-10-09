package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.util.ArrayList;
import java.util.List;

/**
 * Records every sort event as a SortStep for SortingPanel to replay.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class SortTraceRecorder implements SortListener {

    private final List<SortStep> steps = new ArrayList<>();

    /** @return the recorded steps in the order they happened */
    public List<SortStep> getSteps() {
        return steps;
    }

    @Override
    public void onCompare(int i, int j) {
        steps.add(new SortStep(SortStepType.COMPARE, i, j));
    }

    @Override
    public void onSwap(int i, int j) {
        steps.add(new SortStep(SortStepType.SWAP, i, j));
    }

    @Override
    public void onSorted(int i) {
        steps.add(new SortStep(SortStepType.SORTED, i, -1));
    }
}
