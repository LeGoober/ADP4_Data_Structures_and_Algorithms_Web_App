package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link ShortestPathFinder}. */
class ShortestPathFinderTest {

    private final TransportNetwork network = new TransportNetwork();
    private final ShortestPathFinder finder = new ShortestPathFinder(network.getGraph());

    @Test
    void pathStartsAtSourceAndEndsAtDestination() {
        List<String> path = finder.findPath("Cape Town", "Pretoria");
        assertEquals("Cape Town", path.get(0));
        assertEquals("Pretoria", path.get(path.size() - 1));
    }

    @Test
    void pathHasFewestEdges() {
        // Cape Town - Kimberley - Johannesburg - Pretoria is 3 edges; nothing shorter exists
        assertEquals(4, finder.findPath("Cape Town", "Pretoria").size());
        assertEquals(List.of("Durban", "Mbombela"), finder.findPath("Durban", "Mbombela"));
    }

    @Test
    void pathToSelfIsSingleVertex() {
        assertEquals(List.of("George"), finder.findPath("George", "George"));
    }

    @Test
    void findPathByName() {
        List<String> path = finder.findPath("George", "Durban");
        assertEquals(List.of("George", "Gqeberha", "East London", "Durban"), path);
    }

    @Test
    void unreachableGivesEmptyPath() {
        Graph g = new Graph(new String[] {"A", "B"});
        assertTrue(new ShortestPathFinder(g).findPath(0, 1, null).isEmpty());
    }

    @Test
    void everyConsecutivePairIsARoad() {
        List<Integer> path = finder.findPath(0, 9, null);
        for (int i = 0; i + 1 < path.size(); i++) {
            assertTrue(network.getGraph().hasEdge(path.get(i), path.get(i + 1)));
        }
    }
}
