package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Random;

/**
 * Produces the benchmark datasets.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class DatasetLoader {

    /**
     * Reads integers from a CSV/text file (any mix of commas, semicolons and whitespace).
     *
     * @param file the file to read
     * @return the integers in file order
     */
    public static int[] loadCsv(Path file) {
        try {
            String content = Files.readString(file);
            return Arrays.stream(content.split("[,;\\s]+"))
                    .filter(s -> !s.isBlank())
                    .mapToInt(Integer::parseInt)
                    .toArray();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Random integers in [0, 10000), reproducible through the seed.
     *
     * @param n    number of values
     * @param seed random seed
     * @return the generated array
     */
    public static int[] random(int n, long seed) {
        Random r = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = r.nextInt(10_000);
        }
        return a;
    }

    /** @return 0, 1, 2 ... n-1 */
    public static int[] sorted(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = i;
        }
        return a;
    }

    /** @return n-1, n-2 ... 0 */
    public static int[] reversed(int n) {
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = n - 1 - i;
        }
        return a;
    }
}
