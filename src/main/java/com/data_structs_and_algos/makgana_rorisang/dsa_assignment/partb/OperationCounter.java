package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/**
 * Counts comparisons and swaps (Benchmark uses it in a separate, untimed run).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class OperationCounter implements SortListener {

    private long comparisons;
    private long swaps;

    /** @return number of comparisons seen since the last reset */
    public long getComparisons() {
        return comparisons;
    }

    /** @return number of swaps seen since the last reset */
    public long getSwaps() {
        return swaps;
    }

    /** Sets both counters back to zero. */
    public void reset() {
        comparisons = 0;
        swaps = 0;
    }

    @Override
    public void onCompare(int i, int j) {
        comparisons++;
    }

    @Override
    public void onSwap(int i, int j) {
        swaps++;
    }

    @Override
    public void onSorted(int i) {
        // Finalised positions are not an operation worth counting
    }
}
