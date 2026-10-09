package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.HashSet;
import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link GraphTraversal}. */
class GraphTraversalTest {

    /**
     * 0 - 1 - 3
     * |   |
     * 2 - 4      and an isolated vertex 5.
     */
    private Graph graph() {
        Graph g = new Graph(new String[] {"0", "1", "2", "3", "4", "5"});
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 3);
        g.addEdge(1, 4);
        g.addEdge(2, 4);
        return g;
    }

    @Test
    void dfsVisitsEveryReachableVertexOnce() {
        List<Integer> order = new GraphTraversal(graph()).dfs(0, null);
        assertEquals(5, order.size());
        assertEquals(5, new HashSet<>(order).size());
        assertFalse(order.contains(5));
    }

    @Test
    void bfsVisitsInLevelOrder() {
        assertEquals(List.of(0, 1, 2, 3, 4), new GraphTraversal(graph()).bfs(0, null));
    }

    @Test
    void dfsGoesDeepBeforeBacktracking() {
        assertEquals(List.of(0, 1, 3, 4, 2), new GraphTraversal(graph()).dfs(0, null));
    }

    @Test
    void bfsReportsEnqueueAndDequeue() {
        GraphTraceRecorder r = new GraphTraceRecorder();
        new GraphTraversal(graph()).bfs(0, r);
        long enq = r.getSteps().stream().filter(s -> s.type == GraphStepType.ENQUEUE).count();
        long deq = r.getSteps().stream().filter(s -> s.type == GraphStepType.DEQUEUE).count();
        assertEquals(5, enq);
        assertEquals(5, deq);
    }

    @Test
    void dfsAndBfsStartAtStartVertex() {
        GraphTraversal t = new GraphTraversal(graph());
        assertEquals(3, t.dfs(3, null).get(0));
        assertEquals(3, t.bfs(3, null).get(0));
    }
}
