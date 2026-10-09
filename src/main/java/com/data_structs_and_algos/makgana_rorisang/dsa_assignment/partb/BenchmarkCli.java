package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Locale;

/**
 * Headless benchmark runner (no Spring, no Swing). Produces the CSV files that
 * {@code benchmarks/plot_sorts.py} turns into pyplot graphs.
 *
 * <p>Usage: {@code BenchmarkCli [outputDirectory]} (default: ./benchmarks)</p>
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class BenchmarkCli {

    /** Dataset size required by the assignment. */
    private static final int N = 1000;
    /** Number of timed samples per algorithm on the 1,000-integer dataset. */
    private static final int SAMPLES = 1000;
    private static final int WARMUPS = 100;
    private static final long SEED = 470;

    public static void main(String[] args) throws IOException {
        Path out = Path.of(args.length > 0 ? args[0] : "benchmarks");
        Files.createDirectories(out);
        List<Sorter> sorters = List.of(new SelectionSort(), new QuickSort(), new HeapSort());

        // 1. The assignment experiment: 1,000 random integers, 1,000 timed samples each
        int[] data = DatasetLoader.random(N, SEED);
        StringBuilder runs = new StringBuilder("algorithm,sample,nanos\n");
        StringBuilder summary = new StringBuilder("algorithm,mean_ns,stddev_ns,best_ns,comparisons,swaps\n");
        Benchmark benchmark = new Benchmark(WARMUPS, SAMPLES);
        for (Sorter s : sorters) {
            BenchmarkResult r = benchmark.run(s, data);
            long[] times = benchmark.getLastRunNanos();
            double variance = 0;
            for (int i = 0; i < times.length; i++) {
                runs.append(String.format(Locale.ROOT, "%s,%d,%d%n", s.getName(), i + 1, times[i]));
                variance += Math.pow(times[i] - r.averageNanos, 2);
            }
            double stddev = Math.sqrt(variance / times.length);
            summary.append(String.format(Locale.ROOT, "%s,%.1f,%.1f,%d,%d,%d%n",
                    s.getName(), r.averageNanos, stddev, r.bestNanos, r.comparisons, r.swaps));
        }
        Files.writeString(out.resolve("sort_runs_n1000.csv"), runs.toString());
        Files.writeString(out.resolve("sort_results_n1000.csv"), summary.toString());

        // 2. Scaling sweep for the Big-O growth plot
        StringBuilder scaling = new StringBuilder("n,algorithm,mean_ns,comparisons,swaps\n");
        for (int n : new int[] {100, 250, 500, 1000, 2000, 4000, 8000}) {
            int[] d = DatasetLoader.random(n, SEED);
            for (BenchmarkResult r : new Benchmark(20, 100).runAll(sorters, d)) {
                scaling.append(String.format(Locale.ROOT, "%d,%s,%.1f,%d,%d%n",
                        n, r.algorithm, r.averageNanos, r.comparisons, r.swaps));
            }
        }
        Files.writeString(out.resolve("sort_scaling.csv"), scaling.toString());

        // 3. Input-shape comparison at n = 1000
        StringBuilder shapes = new StringBuilder("input,algorithm,mean_ns,comparisons,swaps\n");
        String[] names = {"random", "sorted", "reversed"};
        int[][] inputs = {data, DatasetLoader.sorted(N), DatasetLoader.reversed(N)};
        for (int k = 0; k < inputs.length; k++) {
            for (BenchmarkResult r : new Benchmark(20, 100).runAll(sorters, inputs[k])) {
                shapes.append(String.format(Locale.ROOT, "%s,%s,%.1f,%d,%d%n",
                        names[k], r.algorithm, r.averageNanos, r.comparisons, r.swaps));
            }
        }
        Files.writeString(out.resolve("sort_shapes_n1000.csv"), shapes.toString());

        System.out.println("Benchmark CSVs written to " + out.toAbsolutePath());
    }
}
