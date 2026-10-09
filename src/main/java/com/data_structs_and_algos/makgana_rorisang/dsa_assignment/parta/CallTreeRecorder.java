package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Builds the call tree while the algorithm runs: on enter, attach a new node to whatever is on
 * top of {@code openCalls} and push it; on exit, pop it and fill in the result.
 * Every enter/exit is also recorded as a {@link CallEvent} for the panel to replay.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class CallTreeRecorder implements CallListener {

    private CallNode root;
    private final Deque<CallNode> openCalls = new ArrayDeque<>();
    private final List<CallEvent> events = new ArrayList<>();
    private int maxDepth;

    @Override
    public void onEnter(int n) {
        CallNode node = new CallNode("f(" + n + ")", n);
        if (openCalls.isEmpty()) {
            root = node;
        } else {
            openCalls.peek().children.add(node);
        }
        openCalls.push(node);
        events.add(new CallEvent(EventType.ENTER, node));
        // The stack size is the current recursion depth
        maxDepth = Math.max(maxDepth, openCalls.size());
    }

    @Override
    public void onExit(int n, long result) {
        CallNode node = openCalls.pop();
        node.result = result;
        node.returned = true;
        events.add(new CallEvent(EventType.EXIT, node));
    }

    /** @return the first call made (the root of the call tree) */
    public CallNode getRoot() {
        return root;
    }

    /** @return every enter/exit in the order it happened */
    public List<CallEvent> getEvents() {
        return events;
    }

    /** @return the deepest recursion level reached (the first call is depth 1) */
    public int getMaxDepth() {
        return maxDepth;
    }
}
