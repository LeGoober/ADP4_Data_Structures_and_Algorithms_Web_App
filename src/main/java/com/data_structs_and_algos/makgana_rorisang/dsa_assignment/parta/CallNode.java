package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

import java.util.ArrayList;
import java.util.List;

/** One call in the recursion tree (plain data holder). */
public class CallNode {

    public String label;
    public int n;
    public long result;
    public boolean returned;
    public List<CallNode> children = new ArrayList<>();

    public CallNode() {
    }

    public CallNode(String label, int n) {
        this.label = label;
        this.n = n;
    }
}
