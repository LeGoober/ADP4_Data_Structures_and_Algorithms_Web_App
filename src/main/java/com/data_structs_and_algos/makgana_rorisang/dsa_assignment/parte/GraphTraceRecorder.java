package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.util.ArrayList;
import java.util.List;

/**
 * Records traversal events for GraphPanel to replay.
 * Convention GraphPanel expects: EDGE steps use from/to; VISIT, ENQUEUE and DEQUEUE store the
 * vertex in {@code from} and set {@code to} to -1.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class GraphTraceRecorder implements TraversalListener {

    private final List<GraphStep> steps = new ArrayList<>();

    /** @return the recorded steps in the order they happened */
    public List<GraphStep> getSteps() {
        return steps;
    }

    @Override
    public void onVisit(int v) {
        steps.add(new GraphStep(GraphStepType.VISIT, v, -1));
    }

    @Override
    public void onEdge(int from, int to) {
        steps.add(new GraphStep(GraphStepType.EDGE, from, to));
    }

    @Override
    public void onEnqueue(int v) {
        steps.add(new GraphStep(GraphStepType.ENQUEUE, v, -1));
    }

    @Override
    public void onDequeue(int v) {
        steps.add(new GraphStep(GraphStepType.DEQUEUE, v, -1));
    }
}
