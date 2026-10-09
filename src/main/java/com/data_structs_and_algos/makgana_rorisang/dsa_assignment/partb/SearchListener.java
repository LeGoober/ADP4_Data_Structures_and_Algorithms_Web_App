package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/** Notified on every probe of a binary search. One method, so a lambda works. */
@FunctionalInterface
public interface SearchListener {

    void onProbe(int lo, int mid, int hi);
}
