package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

/**
 * Base class for the Part A algorithms. Subclasses call {@link #enter(int)} at the top of each
 * recursive call and {@code return exit(n, result);} at the bottom, so the listener sees every call.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public abstract class RecursiveAlgorithm {

    protected CallListener listener;
    protected int callCount;

    /**
     * Attaches (or removes, with null) the listener told about every call.
     *
     * @param l the listener
     */
    public void setListener(CallListener l) {
        this.listener = l;
    }

    /** @return the number of recursive calls made since the last reset */
    public int getCallCount() {
        return callCount;
    }

    /** Resets the call counter to zero. */
    public void resetCount() {
        callCount = 0;
    }

    /**
     * Recursive version of the algorithm.
     *
     * @param n the input
     * @return the result
     */
    public abstract long recursive(int n);

    /**
     * Iterative version of the algorithm (for the time/space comparison).
     *
     * @param n the input
     * @return the result
     */
    public abstract long iterative(int n);

    /** Counts a call and tells the listener a call has started. */
    protected void enter(int n) {
        callCount++;
        if (listener != null) {
            listener.onEnter(n);
        }
    }

    /** Tells the listener a call has finished and passes the result through. */
    protected long exit(int n, long result) {
        if (listener != null) {
            listener.onExit(n, result);
        }
        return result;
    }
}
