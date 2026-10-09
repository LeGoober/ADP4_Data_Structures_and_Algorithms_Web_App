package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partd;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

/**
 * Screen positions for BST nodes: x from the node's position in an in-order walk, y from its depth.
 * (java.awt.Point is just an x/y pair here; this class does no drawing.)
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class TreeLayout {

    private final Map<BSTNode, Point> positions = new HashMap<>();
    private int nextColumn;
    private int hGap;
    private int vGap;

    /**
     * Computes a position for every node under root.
     *
     * @param root the tree root (may be null)
     * @param hGap pixels between neighbouring columns
     * @param vGap pixels between levels
     */
    public void compute(BSTNode root, int hGap, int vGap) {
        positions.clear();
        nextColumn = 0;
        this.hGap = hGap;
        this.vGap = vGap;
        assign(root, 0);
    }

    private void assign(BSTNode node, int depth) {
        if (node == null) {
            return;
        }
        // In-order: left subtree, this node, then right subtree
        assign(node.left, depth + 1);
        positions.put(node, new Point(nextColumn++ * hGap, depth * vGap));
        assign(node.right, depth + 1);
    }

    /** @return the pixel position of a node, or null if it was not part of the last layout */
    public Point positionOf(BSTNode node) {
        return positions.get(node);
    }
}
