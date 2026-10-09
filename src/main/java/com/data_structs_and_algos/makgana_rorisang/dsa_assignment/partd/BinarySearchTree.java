package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partd;

import java.util.ArrayList;
import java.util.List;

/**
 * Binary search tree of int keys. Each public method wraps a private recursive version that takes a node.
 * Insert, search and delete are O(h): O(log n) on average for a balanced tree and O(n) in the
 * worst case (keys inserted in sorted order give a chain). Duplicates are rejected.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class BinarySearchTree {

    private BSTNode root;
    private int size;

    /**
     * Inserts a key.
     *
     * @return false when the key is already present (nothing changes)
     */
    public boolean insert(int key) {
        if (search(key)) {
            return false;
        }
        root = insert(root, key);
        size++;
        return true;
    }

    private BSTNode insert(BSTNode node, int key) {
        // Base case: found the empty spot
        if (node == null) {
            return new BSTNode(key);
        }
        if (key < node.key) {
            node.left = insert(node.left, key);
        } else if (key > node.key) {
            node.right = insert(node.right, key);
        }
        return node;
    }

    /** @return true when the key is in the tree */
    public boolean search(int key) {
        return search(root, key) != null;
    }

    private BSTNode search(BSTNode node, int key) {
        if (node == null || node.key == key) {
            return node;
        }
        return key < node.key ? search(node.left, key) : search(node.right, key);
    }

    /** @return the keys visited from the root down while looking for the key (BSTPanel animates this) */
    public List<Integer> searchPath(int key) {
        List<Integer> path = new ArrayList<>();
        BSTNode current = root;
        while (current != null) {
            path.add(current.key);
            if (key == current.key) {
                break;
            }
            current = key < current.key ? current.left : current.right;
        }
        return path;
    }

    /** @return true when the key was found and removed */
    public boolean delete(int key) {
        if (!search(key)) {
            return false;
        }
        root = delete(root, key);
        size--;
        return true;
    }

    private BSTNode delete(BSTNode node, int key) {
        if (node == null) {
            return null;
        }
        if (key < node.key) {
            node.left = delete(node.left, key);
        } else if (key > node.key) {
            node.right = delete(node.right, key);
        } else {
            // Case 1 and 2: a leaf or a node with one child is replaced by that child (or null)
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Case 3: two children. Copy in the in-order successor, then delete it from the right side
            BSTNode successor = findMin(node.right);
            node.key = successor.key;
            node.right = delete(node.right, successor.key);
        }
        return node;
    }

    private BSTNode findMin(BSTNode node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    /** @return keys in ascending order (left, node, right) */
    public List<Integer> inOrder() {
        List<Integer> out = new ArrayList<>();
        inOrder(root, out);
        return out;
    }

    /** @return keys with each node before its children (node, left, right) */
    public List<Integer> preOrder() {
        List<Integer> out = new ArrayList<>();
        preOrder(root, out);
        return out;
    }

    /** @return keys with each node after its children (left, right, node) */
    public List<Integer> postOrder() {
        List<Integer> out = new ArrayList<>();
        postOrder(root, out);
        return out;
    }

    private void inOrder(BSTNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        inOrder(node.left, out);
        out.add(node.key);
        inOrder(node.right, out);
    }

    private void preOrder(BSTNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        out.add(node.key);
        preOrder(node.left, out);
        preOrder(node.right, out);
    }

    private void postOrder(BSTNode node, List<Integer> out) {
        if (node == null) {
            return;
        }
        postOrder(node.left, out);
        postOrder(node.right, out);
        out.add(node.key);
    }

    /** @return the height in edges: -1 for an empty tree, 0 for a single node */
    public int height() {
        return height(root);
    }

    private int height(BSTNode node) {
        if (node == null) {
            return -1;
        }
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public int size() {
        return size;
    }

    /** Removes every key. */
    public void clear() {
        root = null;
        size = 0;
    }

    public BSTNode getRoot() {
        return root;
    }
}
