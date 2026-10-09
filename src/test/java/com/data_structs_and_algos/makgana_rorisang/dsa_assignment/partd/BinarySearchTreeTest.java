package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partd;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link BinarySearchTree}. */
class BinarySearchTreeTest {

    /** Builds the tree 50 / (30 / 20,40) / (70 / 60,80). */
    private BinarySearchTree sample() {
        BinarySearchTree t = new BinarySearchTree();
        for (int k : new int[] {50, 30, 70, 20, 40, 60, 80}) {
            t.insert(k);
        }
        return t;
    }

    @Test
    void newTreeIsEmpty() {
        BinarySearchTree t = new BinarySearchTree();
        assertEquals(0, t.size());
        assertNull(t.getRoot());
        assertEquals(-1, t.height());
    }

    @Test
    void insertRejectsDuplicates() {
        BinarySearchTree t = sample();
        assertFalse(t.insert(50));
        assertEquals(7, t.size());
    }

    @Test
    void searchFindsInsertedKeys() {
        BinarySearchTree t = sample();
        assertTrue(t.search(60));
        assertFalse(t.search(65));
    }

    @Test
    void searchPathFollowsRootToKey() {
        assertEquals(List.of(50, 70, 60), sample().searchPath(60));
    }

    @Test
    void inOrderIsSorted() {
        assertEquals(List.of(20, 30, 40, 50, 60, 70, 80), sample().inOrder());
    }

    @Test
    void preOrderAndPostOrder() {
        BinarySearchTree t = sample();
        assertEquals(List.of(50, 30, 20, 40, 70, 60, 80), t.preOrder());
        assertEquals(List.of(20, 40, 30, 60, 80, 70, 50), t.postOrder());
    }

    @Test
    void deleteLeaf() {
        BinarySearchTree t = sample();
        assertTrue(t.delete(20));
        assertEquals(List.of(30, 40, 50, 60, 70, 80), t.inOrder());
        assertFalse(t.delete(20));
    }

    @Test
    void deleteNodeWithOneChild() {
        BinarySearchTree t = sample();
        t.delete(20);
        t.delete(30);
        assertEquals(List.of(40, 50, 60, 70, 80), t.inOrder());
        assertEquals(40, t.getRoot().left.key);
    }

    @Test
    void deleteNodeWithTwoChildren() {
        BinarySearchTree t = sample();
        t.delete(50);
        // the in-order successor (60) replaces the root
        assertEquals(60, t.getRoot().key);
        assertEquals(List.of(20, 30, 40, 60, 70, 80), t.inOrder());
        assertEquals(6, t.size());
    }

    @Test
    void heightOfBalancedVersusSortedInsertions() {
        assertEquals(2, sample().height());
        BinarySearchTree chain = new BinarySearchTree();
        for (int i = 1; i <= 100; i++) {
            chain.insert(i);
        }
        // worst case: one node per level, so the height is n - 1
        assertEquals(99, chain.height());
    }

    @Test
    void clearEmptiesTree() {
        BinarySearchTree t = sample();
        t.clear();
        assertEquals(0, t.size());
        assertNull(t.getRoot());
    }
}
