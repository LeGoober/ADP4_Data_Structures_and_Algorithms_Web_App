package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/** Notified by AbstractSorter on every comparison, swap and finalised position. */
public interface SortListener {

    /** Ignores everything. Pass it when timing: benchmark runs must be silent. */
    SortListener NO_OP = new SortListener() {
        @Override public void onCompare(int i, int j) { }
        @Override public void onSwap(int i, int j) { }
        @Override public void onSorted(int i) { }
    };

    void onCompare(int i, int j);

    void onSwap(int i, int j);

    void onSorted(int i);
}
