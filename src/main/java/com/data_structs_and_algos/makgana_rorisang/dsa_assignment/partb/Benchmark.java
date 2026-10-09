package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Times sorters on identical data: warm-up runs, timed runs, then a separate counting run.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class Benchmark {

    private int warmupRuns;
    private int timedRuns;
    /** Every timed run of the most recent run call, in nanoseconds. */
    private long[] lastRunNanos = new long[0];

    public Benchmark(int warmupRuns, int timedRuns) {
        this.warmupRuns = warmupRuns;
        this.timedRuns = timedRuns;
    }

    /** @return the individual timings (ns) of the most recent run call */
    public long[] getLastRunNanos() {
        return lastRunNanos.clone();
    }

    /**
     * Benchmarks one sorter on its own copy of the data.
     *
     * @param s    the sorter
     * @param data the dataset (never modified)
     * @return best and average time plus comparison and swap counts
     */
    public BenchmarkResult run(Sorter s, int[] data) {
        // Warm-up lets the JIT compile the sorter before we measure
        for (int i = 0; i < warmupRuns; i++) {
            s.sort(data.clone(), SortListener.NO_OP);
        }
        long best = Long.MAX_VALUE;
        long total = 0;
        lastRunNanos = new long[timedRuns];
        for (int i = 0; i < timedRuns; i++) {
            int[] copy = data.clone();
            long start = System.nanoTime();
            s.sort(copy, SortListener.NO_OP);
            long elapsed = System.nanoTime() - start;
            lastRunNanos[i] = elapsed;
            best = Math.min(best, elapsed);
            total += elapsed;
        }
        // Counting run is separate so the listener overhead never pollutes the timings
        OperationCounter counter = new OperationCounter();
        s.sort(data.clone(), counter);
        double average = timedRuns == 0 ? 0 : (double) total / timedRuns;
        return new BenchmarkResult(s.getName(), timedRuns == 0 ? 0 : best, average,
                counter.getComparisons(), counter.getSwaps());
    }

    /**
     * Benchmarks every sorter on the same data.
     *
     * @param sorters the algorithms to compare
     * @param data    the shared dataset
     * @return one result per sorter, in order
     */
    public List<BenchmarkResult> runAll(List<Sorter> sorters, int[] data) {
        List<BenchmarkResult> results = new ArrayList<>();
        for (Sorter s : sorters) {
            results.add(run(s, data));
        }
        return results;
    }

    /**
     * Writes the results as CSV for Excel / Python.
     *
     * @param results the benchmark results
     * @param file    destination file (parent folders are created)
     */
    public static void exportCsv(List<BenchmarkResult> results, Path file) {
        StringBuilder sb = new StringBuilder("algorithm,best_ns,average_ns,comparisons,swaps\n");
        for (BenchmarkResult r : results) {
            sb.append(String.format(Locale.ROOT, "%s,%d,%.1f,%d,%d%n",
                    r.algorithm, r.bestNanos, r.averageNanos, r.comparisons, r.swaps));
        }
        try {
            if (file.getParent() != null) {
                Files.createDirectories(file.getParent());
            }
            Files.writeString(file, sb.toString());
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
