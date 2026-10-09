package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * The nth Fibonacci number, recursively and iteratively.
 * Recursive: time O(2^n) (tightly O(phi^n)), space O(n) call stack.
 * Iterative: time O(n), space O(1).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
@Order(2)
public class Fibonacci extends RecursiveAlgorithm {

    /**
     * Recursive Fibonacci: F(n) = F(n-1) + F(n-2), with F(0) = 0 and F(1) = 1.
     *
     * @param value a non-negative index (keep it small: the call count grows exponentially)
     * @return the Fibonacci number at that index
     */
    @Override
    public long recursive(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be >= 0");
        }
        enter(value);
        // Base cases: F(0) = 0 and F(1) = 1
        if (value <= 1) {
            return exit(value, value);
        }
        // Recursive case: two smaller sub-problems
        return exit(value, recursive(value - 1) + recursive(value - 2));
    }

    /**
     * Iterative Fibonacci that only keeps the previous two values.
     *
     * @param value a non-negative index
     * @return the Fibonacci number at that index
     */
    @Override
    public long iterative(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("value must be >= 0");
        }
        long previous = 0;
        long current = 1;
        for (int i = 0; i < value; i++) {
            long next = previous + current;
            previous = current;
            current = next;
        }
        return previous;
    }
}
