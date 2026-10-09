package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

/**
 * Singly linked list built from scratch (no java.util collections inside).
 * insertFirst O(1); insertLast, insertAt, delete, search and get O(n).
 *
 * @param <T> the element type
 * @author Rorisang Makgana
 * @version 1.0
 */
public class SinglyLinkedList<T> {

    private static class Node<T> {
        T data;
        Node<T> next;

        Node(T data) {
            this.data = data;
        }
    }

    private Node<T> head;
    private int size;

    /** Inserts at the front of the list in O(1). */
    public void insertFirst(T value) {
        Node<T> node = new Node<>(value);
        node.next = head;
        head = node;
        size++;
    }

    /** Inserts at the end of the list in O(n). */
    public void insertLast(T value) {
        insertAt(size, value);
    }

    /**
     * Inserts so that the new value ends up at the given index.
     *
     * @param index 0..size (size appends)
     * @param value the value to insert
     * @throws IndexOutOfBoundsException when the index is outside 0..size
     */
    public void insertAt(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
        if (index == 0) {
            insertFirst(value);
            return;
        }
        // Walk to the node just before the insertion point
        Node<T> previous = head;
        for (int i = 0; i < index - 1; i++) {
            previous = previous.next;
        }
        Node<T> node = new Node<>(value);
        node.next = previous.next;
        previous.next = node;
        size++;
    }

    /**
     * Removes the first node holding the value.
     *
     * @return true if a node was found and removed
     */
    public boolean delete(T value) {
        int index = search(value);
        if (index < 0) {
            return false;
        }
        deleteAt(index);
        return true;
    }

    /**
     * Removes and returns the element at an index.
     *
     * @throws IndexOutOfBoundsException when the index is outside 0..size-1
     */
    public T deleteAt(int index) {
        checkIndex(index);
        if (index == 0) {
            T removed = head.data;
            head = head.next;
            size--;
            return removed;
        }
        Node<T> previous = head;
        for (int i = 0; i < index - 1; i++) {
            previous = previous.next;
        }
        T removed = previous.next.data;
        // Bypass the removed node
        previous.next = previous.next.next;
        size--;
        return removed;
    }

    /** @return the index of the first match, or -1 */
    public int search(T value) {
        Node<T> current = head;
        int index = 0;
        while (current != null) {
            if (current.data == null ? value == null : current.data.equals(value)) {
                return index;
            }
            current = current.next;
            index++;
        }
        return -1;
    }

    /** @return the element at an index */
    public T get(int index) {
        checkIndex(index);
        Node<T> current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    /** @return the number of elements */
    public int size() {
        return size;
    }

    /** @return true when the list holds nothing */
    public boolean isEmpty() {
        return size == 0;
    }

    /** @return a copy of the contents, head first */
    public Object[] toArray() {
        Object[] out = new Object[size];
        Node<T> current = head;
        for (int i = 0; i < size; i++) {
            out[i] = current.data;
            current = current.next;
        }
        return out;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", size: " + size);
        }
    }
}
