package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partd;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link TreeLayout}. */
class TreeLayoutTest {

    private BinarySearchTree tree() {
        BinarySearchTree t = new BinarySearchTree();
        for (int k : new int[] {50, 30, 70, 20, 40}) {
            t.insert(k);
        }
        return t;
    }

    @Test
    void xFollowsInOrderPosition() {
        BinarySearchTree t = tree();
        TreeLayout layout = new TreeLayout();
        layout.compute(t.getRoot(), 10, 20);
        // in-order: 20, 30, 40, 50, 70 -> columns 0..4
        assertEquals(0, layout.positionOf(t.getRoot().left.left).x);
        assertEquals(30, layout.positionOf(t.getRoot()).x);
        assertEquals(40, layout.positionOf(t.getRoot().right).x);
    }

    @Test
    void yFollowsDepth() {
        BinarySearchTree t = tree();
        TreeLayout layout = new TreeLayout();
        layout.compute(t.getRoot(), 10, 20);
        assertEquals(0, layout.positionOf(t.getRoot()).y);
        assertEquals(20, layout.positionOf(t.getRoot().left).y);
        assertEquals(40, layout.positionOf(t.getRoot().left.right).y);
    }

    @Test
    void noTwoNodesShareAPosition() {
        BinarySearchTree t = tree();
        TreeLayout layout = new TreeLayout();
        layout.compute(t.getRoot(), 10, 20);
        Set<Point> seen = new HashSet<>();
        for (int key : t.preOrder()) {
            BSTNode node = t.getRoot();
            while (node.key != key) {
                node = key < node.key ? node.left : node.right;
            }
            assertTrue(seen.add(layout.positionOf(node)));
        }
    }
}
