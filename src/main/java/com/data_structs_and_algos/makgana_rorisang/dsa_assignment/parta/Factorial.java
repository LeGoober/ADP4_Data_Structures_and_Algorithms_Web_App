package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Factorial, recursively and iteratively.
 * Recursive: time O(n), space O(n) (one stack frame per call).
 * Iterative: time O(n), space O(1). A long overflows beyond 20!.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
@Order(1)
public class Factorial extends RecursiveAlgorithm {

    /**
     * Recursive factorial: n! = n * (n - 1)!, with 0! = 1.
     *
     * @param n a non-negative integer (at most 20 so the result fits in a long)
     * @return n!
     */
    @Override
    public long recursive(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        enter(n);
        // Base case: 0! = 1! = 1 stops the recursion
        if (n <= 1) {
            return exit(n, 1);
        }
        // Recursive case: combine this n with the result of the smaller problem
        return exit(n, n * recursive(n - 1));
    }

    /**
     * Iterative factorial using a single accumulator.
     *
     * @param n a non-negative integer (at most 20)
     * @return n!
     */
    @Override
    public long iterative(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be >= 0");
        }
        long result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }
}
