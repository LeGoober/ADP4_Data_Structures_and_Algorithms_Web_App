package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

/** One recorded traversal event (plain data holder). See GraphTraceRecorder for the convention. */
public class GraphStep {

    public GraphStepType type;
    public int from;
    public int to;

    public GraphStep(GraphStepType type, int from, int to) {
        this.type = type;
        this.from = from;
        this.to = to;
    }
}
