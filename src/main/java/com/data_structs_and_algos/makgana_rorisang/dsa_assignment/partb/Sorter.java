package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/** A sorting algorithm. Implementations are Spring components, so they appear in the UI automatically. */
public interface Sorter {

    String getName();

    void sort(int[] a, SortListener l);
}
