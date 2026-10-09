package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link Fibonacci}. */
class FibonacciTest {

    private final Fibonacci fibonacci = new Fibonacci();

    @Test
    void baseCasesZeroAndOne() {
        assertEquals(0, fibonacci.recursive(0));
        assertEquals(1, fibonacci.recursive(1));
        assertEquals(0, fibonacci.iterative(0));
        assertEquals(1, fibonacci.iterative(1));
    }

    @Test
    void recursiveOfTenIs55() {
        assertEquals(55, fibonacci.recursive(10));
    }

    @Test
    void iterativeMatchesRecursiveUpTo30() {
        for (int n = 0; n <= 30; n++) {
            assertEquals(fibonacci.recursive(n), fibonacci.iterative(n), "n = " + n);
        }
    }

    @Test
    void callCountForSevenIs41() {
        fibonacci.resetCount();
        fibonacci.recursive(7);
        // 2 * F(8) - 1 = 41 calls
        assertEquals(41, fibonacci.getCallCount());
    }

    @Test
    void resetCountClearsCounter() {
        fibonacci.recursive(5);
        fibonacci.resetCount();
        assertEquals(0, fibonacci.getCallCount());
    }
}
