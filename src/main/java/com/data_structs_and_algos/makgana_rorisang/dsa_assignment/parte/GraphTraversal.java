package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.ArrayList;
import java.util.List;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc.CircularQueue;

/**
 * DFS (recursive) and BFS (using the Part C CircularQueue). Both are O(V^2) on an adjacency
 * matrix because every visited vertex scans a full matrix row.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class GraphTraversal {

    private final Graph graph;

    public GraphTraversal(Graph g) {
        this.graph = g;
    }

    /**
     * Depth-first search from a start vertex.
     *
     * @param start the first vertex
     * @param l     listener notified of visits and edges followed (may be null)
     * @return vertices in the order they were visited
     */
    public List<Integer> dfs(int start, TraversalListener l) {
        TraversalListener listener = l == null ? new TraversalListener() { } : l;
        boolean[] visited = new boolean[graph.vertexCount()];
        List<Integer> order = new ArrayList<>();
        dfs(start, visited, order, listener);
        return order;
    }

    private void dfs(int v, boolean[] visited, List<Integer> order, TraversalListener l) {
        visited[v] = true;
        order.add(v);
        l.onVisit(v);
        for (int next : graph.neighbours(v)) {
            if (!visited[next]) {
                l.onEdge(v, next);
                // Recursive case: go as deep as possible before backtracking
                dfs(next, visited, order, l);
            }
        }
    }

    /**
     * Breadth-first search from a start vertex, level by level.
     *
     * @param start the first vertex
     * @param l     listener notified of queue operations, visits and edges (may be null)
     * @return vertices in the order they were visited
     */
    public List<Integer> bfs(int start, TraversalListener l) {
        TraversalListener listener = l == null ? new TraversalListener() { } : l;
        boolean[] seen = new boolean[graph.vertexCount()];
        List<Integer> order = new ArrayList<>();
        CircularQueue<Integer> queue = new CircularQueue<>(graph.vertexCount());
        seen[start] = true;
        queue.enqueue(start);
        listener.onEnqueue(start);
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            listener.onDequeue(v);
            order.add(v);
            listener.onVisit(v);
            for (int next : graph.neighbours(v)) {
                if (!seen[next]) {
                    // Mark when queued, so a vertex is never queued twice
                    seen[next] = true;
                    listener.onEdge(v, next);
                    queue.enqueue(next);
                    listener.onEnqueue(next);
                }
            }
        }
        return order;
    }
}
