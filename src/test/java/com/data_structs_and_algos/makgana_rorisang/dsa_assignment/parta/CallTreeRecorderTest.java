package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link CallTreeRecorder}. */
class CallTreeRecorderTest {

    private CallTreeRecorder record(RecursiveAlgorithm algorithm, int n) {
        CallTreeRecorder recorder = new CallTreeRecorder();
        algorithm.setListener(recorder);
        algorithm.recursive(n);
        algorithm.setListener(null);
        return recorder;
    }

    @Test
    void rootIsFirstCall() {
        CallTreeRecorder r = record(new Factorial(), 4);
        assertEquals(4, r.getRoot().n);
        assertEquals(24, r.getRoot().result);
        assertTrue(r.getRoot().returned);
    }

    @Test
    void factorialTreeIsAChain() {
        CallNode node = record(new Factorial(), 4).getRoot();
        while (!node.children.isEmpty()) {
            assertEquals(1, node.children.size());
            node = node.children.get(0);
        }
        assertEquals(1, node.n);
    }

    @Test
    void fibonacciTreeBranches() {
        CallNode root = record(new Fibonacci(), 4).getRoot();
        assertEquals(2, root.children.size());
        assertEquals(3, root.children.get(0).n);
        assertEquals(2, root.children.get(1).n);
    }

    @Test
    void maxDepthMatchesDeepestCall() {
        assertEquals(5, record(new Factorial(), 5).getMaxDepth());
        // F(4) -> F(3) -> F(2) -> F(1): four levels
        assertEquals(4, record(new Fibonacci(), 4).getMaxDepth());
    }

    @Test
    void eventsAlternateEnterBeforeExitPerNode() {
        CallTreeRecorder r = record(new Fibonacci(), 5);
        Set<CallNode> entered = new HashSet<>();
        for (CallEvent e : r.getEvents()) {
            if (e.type == EventType.ENTER) {
                assertTrue(entered.add(e.node));
            } else {
                assertTrue(entered.contains(e.node));
            }
        }
    }
}
