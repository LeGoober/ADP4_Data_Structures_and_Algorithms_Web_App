package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.ArrayList;
import java.util.List;

/**
 * Undirected graph stored as an adjacency matrix: adj[u][v] = 1 when a road joins u and v.
 * The matrix needs O(V^2) space; an edge lookup is O(1); listing neighbours is O(V).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class Graph {

    private final int[][] adj;
    private final String[] names;

    /** @param cityNames one vertex per name; the array index is the vertex number */
    public Graph(String[] cityNames) {
        this.names = cityNames.clone();
        this.adj = new int[cityNames.length][cityNames.length];
    }

    /** Roads go both ways: sets [u][v] and [v][u]. */
    public void addEdge(int u, int v) {
        adj[u][v] = 1;
        adj[v][u] = 1;
    }

    /** Adds an edge between two named vertices. */
    public void addEdge(String a, String b) {
        addEdge(indexOf(a), indexOf(b));
    }

    public boolean hasEdge(int u, int v) {
        return adj[u][v] == 1;
    }

    /** @return the vertices adjacent to v, in ascending order */
    public List<Integer> neighbours(int v) {
        List<Integer> out = new ArrayList<>();
        for (int u = 0; u < adj.length; u++) {
            if (adj[v][u] == 1) {
                out.add(u);
            }
        }
        return out;
    }

    /**
     * @return the vertex number for a name
     * @throws IllegalArgumentException for an unknown name
     */
    public int indexOf(String name) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].equals(name)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Unknown city: " + name);
    }

    public String nameOf(int v) {
        return names[v];
    }

    public int vertexCount() {
        return names.length;
    }

    /** @return a deep copy of the adjacency matrix */
    public int[][] matrixCopy() {
        int[][] copy = new int[adj.length][];
        for (int i = 0; i < adj.length; i++) {
            copy[i] = adj[i].clone();
        }
        return copy;
    }
}
