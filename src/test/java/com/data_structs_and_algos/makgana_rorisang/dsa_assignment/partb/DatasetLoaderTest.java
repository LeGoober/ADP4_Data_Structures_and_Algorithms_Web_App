package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link DatasetLoader}. */
class DatasetLoaderTest {

    @Test
    void randomIsReproducibleForSameSeed() {
        assertArrayEquals(DatasetLoader.random(100, 1), DatasetLoader.random(100, 1));
        assertEquals(100, DatasetLoader.random(100, 1).length);
    }

    @Test
    void sortedAndReversedAreOrdered() {
        assertArrayEquals(new int[] {0, 1, 2}, DatasetLoader.sorted(3));
        assertArrayEquals(new int[] {2, 1, 0}, DatasetLoader.reversed(3));
    }

    @Test
    void loadCsvReadsIntegers(@TempDir Path dir) throws IOException {
        Path file = dir.resolve("data.csv");
        Files.writeString(file, "5, 3,8\n1\n");
        assertArrayEquals(new int[] {5, 3, 8, 1}, DatasetLoader.loadCsv(file));
    }
}
