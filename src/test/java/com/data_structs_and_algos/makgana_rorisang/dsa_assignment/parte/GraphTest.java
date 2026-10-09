package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link Graph}. */
class GraphTest {

    private Graph graph() {
        return new Graph(new String[] {"A", "B", "C", "D"});
    }

    @Test
    void newGraphHasNoEdges() {
        Graph g = graph();
        for (int u = 0; u < 4; u++) {
            for (int v = 0; v < 4; v++) {
                assertFalse(g.hasEdge(u, v));
            }
        }
    }

    @Test
    void addEdgeIsUndirected() {
        Graph g = graph();
        g.addEdge(0, 1);
        assertTrue(g.hasEdge(0, 1));
        assertTrue(g.hasEdge(1, 0));
    }

    @Test
    void addEdgeByName() {
        Graph g = graph();
        g.addEdge("A", "C");
        assertTrue(g.hasEdge(0, 2));
    }

    @Test
    void neighboursListsAdjacentVertices() {
        Graph g = graph();
        g.addEdge(0, 1);
        g.addEdge(0, 3);
        assertEquals(List.of(1, 3), g.neighbours(0));
    }

    @Test
    void indexOfAndNameOfRoundTrip() {
        Graph g = graph();
        assertEquals(2, g.indexOf("C"));
        assertEquals("C", g.nameOf(2));
        assertEquals(4, g.vertexCount());
        assertThrows(IllegalArgumentException.class, () -> g.indexOf("Z"));
    }

    @Test
    void matrixCopyIsIndependent() {
        Graph g = graph();
        g.addEdge(0, 1);
        int[][] copy = g.matrixCopy();
        copy[0][1] = 0;
        assertTrue(g.hasEdge(0, 1));
    }
}
