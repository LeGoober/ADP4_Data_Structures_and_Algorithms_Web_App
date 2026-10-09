package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/** Timing and operation counts for one sorter (plain data holder). */
public class BenchmarkResult {

    public String algorithm;
    public long bestNanos;
    public double averageNanos;
    public long comparisons;
    public long swaps;

    public BenchmarkResult(String algorithm, long bestNanos, double averageNanos, long comparisons, long swaps) {
        this.algorithm = algorithm;
        this.bestNanos = bestNanos;
        this.averageNanos = averageNanos;
        this.comparisons = comparisons;
        this.swaps = swaps;
    }
}
