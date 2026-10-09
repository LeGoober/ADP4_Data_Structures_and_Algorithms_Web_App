package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

/** One recorded sort event. SORTED steps only use {@code i}. (Plain data holder.) */
public class SortStep {

    public SortStepType type;
    public int i;
    public int j;

    public SortStep(SortStepType type, int i, int j) {
        this.type = type;
        this.i = i;
        this.j = j;
    }
}
