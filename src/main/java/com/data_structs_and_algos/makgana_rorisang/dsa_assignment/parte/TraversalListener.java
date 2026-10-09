package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

/** Default no-ops, so DFS only needs onVisit / onEdge and BFS can also report its queue. */
public interface TraversalListener {

    default void onVisit(int v) {
    }

    default void onEdge(int from, int to) {
    }

    default void onEnqueue(int v) {
    }

    default void onDequeue(int v) {
    }
}
