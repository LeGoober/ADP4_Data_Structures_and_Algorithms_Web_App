package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link Factorial}. */
class FactorialTest {

    private final Factorial factorial = new Factorial();

    @Test
    void recursiveOfZeroIsOne() {
        assertEquals(1, factorial.recursive(0));
    }

    @Test
    void recursiveOfFiveIs120() {
        assertEquals(120, factorial.recursive(5));
    }

    @Test
    void iterativeMatchesRecursiveUpTo20() {
        for (int n = 0; n <= 20; n++) {
            assertEquals(factorial.recursive(n), factorial.iterative(n));
        }
        assertEquals(2432902008176640000L, factorial.iterative(20));
    }

    @Test
    void callCountIsN() {
        factorial.resetCount();
        factorial.recursive(6);
        // one call each for 6, 5, 4, 3, 2 and the base case 1
        assertEquals(6, factorial.getCallCount());
    }

    @Test
    void listenerSeesMatchingEnterAndExit() {
        List<String> log = new ArrayList<>();
        factorial.setListener(new CallListener() {
            @Override public void onEnter(int n) { log.add("in" + n); }
            @Override public void onExit(int n, long result) { log.add("out" + n + "=" + result); }
        });
        factorial.recursive(3);
        factorial.setListener(null);
        assertEquals(List.of("in3", "in2", "in1", "out1=1", "out2=2", "out3=6"), log);
    }

    @Test
    void negativeInputIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> factorial.recursive(-1));
    }
}
