package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc.CircularQueue;

/**
 * BFS plus a parent array gives the path with the fewest edges (every edge counts as 1).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class ShortestPathFinder {

    private final Graph graph;

    public ShortestPathFinder(Graph g) {
        this.graph = g;
    }

    /**
     * @param src first vertex
     * @param dst last vertex
     * @param l   listener (may be null)
     * @return vertices from src to dst, or an empty list when dst is unreachable
     */
    public List<Integer> findPath(int src, int dst, TraversalListener l) {
        TraversalListener listener = l == null ? new TraversalListener() { } : l;
        int[] parent = new int[graph.vertexCount()];
        Arrays.fill(parent, -1);
        boolean[] seen = new boolean[graph.vertexCount()];
        CircularQueue<Integer> queue = new CircularQueue<>(graph.vertexCount());
        seen[src] = true;
        queue.enqueue(src);
        listener.onEnqueue(src);
        while (!queue.isEmpty()) {
            int v = queue.dequeue();
            listener.onDequeue(v);
            listener.onVisit(v);
            if (v == dst) {
                return buildPath(parent, src, dst);
            }
            for (int next : graph.neighbours(v)) {
                if (!seen[next]) {
                    seen[next] = true;
                    // Remember how we got here so the path can be rebuilt backwards
                    parent[next] = v;
                    listener.onEdge(v, next);
                    queue.enqueue(next);
                    listener.onEnqueue(next);
                }
            }
        }
        return new ArrayList<>();
    }

    /**
     * @param from source city name
     * @param to   destination city name
     * @return city names from source to destination, empty when unreachable
     */
    public List<String> findPath(String from, String to) {
        List<String> names = new ArrayList<>();
        for (int v : findPath(graph.indexOf(from), graph.indexOf(to), null)) {
            names.add(graph.nameOf(v));
        }
        return names;
    }

    /** Walks the parent array from dst back to src, then reverses it. */
    private List<Integer> buildPath(int[] parent, int src, int dst) {
        List<Integer> path = new ArrayList<>();
        for (int v = dst; v != -1; v = parent[v]) {
            path.add(v);
        }
        Collections.reverse(path);
        return path;
    }
}
