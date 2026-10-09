package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link GraphTraceRecorder}. */
class GraphTraceRecorderTest {

    @Test
    void recordsStepsInOrder() {
        GraphTraceRecorder r = new GraphTraceRecorder();
        r.onEnqueue(1);
        r.onDequeue(1);
        r.onVisit(1);
        r.onEdge(1, 2);
        assertEquals(4, r.getSteps().size());
        assertEquals(GraphStepType.ENQUEUE, r.getSteps().get(0).type);
        assertEquals(GraphStepType.EDGE, r.getSteps().get(3).type);
        assertEquals(2, r.getSteps().get(3).to);
    }

    @Test
    void singleVertexStepsUseFromField() {
        GraphTraceRecorder r = new GraphTraceRecorder();
        r.onVisit(4);
        assertEquals(4, r.getSteps().get(0).from);
        assertEquals(-1, r.getSteps().get(0).to);
    }
}
