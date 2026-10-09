package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

/** Notified on every recursive call. See UML "Part A: Recursive algorithms". */
public interface CallListener {

    void onEnter(int n);

    void onExit(int n, long result);
}
