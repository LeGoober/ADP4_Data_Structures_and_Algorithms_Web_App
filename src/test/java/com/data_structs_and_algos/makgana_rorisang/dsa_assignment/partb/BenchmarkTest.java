package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link Benchmark}. */
class BenchmarkTest {

    private final Benchmark benchmark = new Benchmark(2, 5);

    @Test
    void runReturnsResultForSorter() {
        BenchmarkResult r = benchmark.run(new SelectionSort(), DatasetLoader.random(100, 7));
        assertEquals("Selection Sort", r.algorithm);
        assertEquals(4950, r.comparisons);
        assertTrue(r.bestNanos > 0 && r.averageNanos >= r.bestNanos);
    }

    @Test
    void runAllReturnsOneResultPerSorter() {
        List<Sorter> sorters = List.of(new SelectionSort(), new QuickSort(), new HeapSort());
        assertEquals(3, benchmark.runAll(sorters, DatasetLoader.random(50, 8)).size());
    }

    @Test
    void runDoesNotModifyInputData() {
        int[] data = DatasetLoader.random(100, 9);
        int[] copy = data.clone();
        benchmark.run(new QuickSort(), data);
        assertArrayEquals(copy, data);
    }

    @Test
    void exportCsvWritesHeaderAndRows(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("out.csv");
        Benchmark.exportCsv(List.of(new BenchmarkResult("X", 1, 2.0, 3, 4)), file);
        List<String> lines = Files.readAllLines(file);
        assertEquals("algorithm,best_ns,average_ns,comparisons,swaps", lines.get(0));
        assertEquals("X,1,2.0,3,4", lines.get(1));
    }
}
