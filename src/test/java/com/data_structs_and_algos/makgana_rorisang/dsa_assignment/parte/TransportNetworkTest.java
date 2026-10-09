package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link TransportNetwork}. */
class TransportNetworkTest {

    private final TransportNetwork network = new TransportNetwork();

    @Test
    void hasEightToTwelveCities() {
        int n = network.getGraph().vertexCount();
        assertTrue(n >= 8 && n <= 12);
    }

    @Test
    void everyCityHasAPosition() {
        for (int v = 0; v < network.getGraph().vertexCount(); v++) {
            assertNotNull(network.positionOf(v));
        }
    }

    @Test
    void graphIsConnected() {
        List<Integer> reached = new GraphTraversal(network.getGraph()).bfs(0, null);
        assertEquals(network.getGraph().vertexCount(), reached.size());
    }
}
