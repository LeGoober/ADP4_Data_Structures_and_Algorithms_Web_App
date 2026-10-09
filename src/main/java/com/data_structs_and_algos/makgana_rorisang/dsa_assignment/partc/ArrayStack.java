package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

/**
 * Fixed-capacity stack on an array (no java.util collections inside). push, pop, peek are O(1).
 *
 * @param <T> the element type
 * @author Rorisang Makgana
 * @version 1.0
 */
public class ArrayStack<T> {

    private final Object[] items;
    /** Index of the next free slot, which is also the number of elements. */
    private int top;

    /** @param capacity the maximum number of elements */
    public ArrayStack(int capacity) {
        items = new Object[capacity];
        top = 0;
    }

    /**
     * Pushes onto the top.
     *
     * @throws IllegalStateException when the stack is full
     */
    public void push(T item) {
        if (isFull()) {
            throw new IllegalStateException("Stack is full");
        }
        items[top++] = item;
    }

    /**
     * Removes and returns the top element.
     *
     * @throws IllegalStateException when the stack is empty
     */
    @SuppressWarnings("unchecked")
    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        T item = (T) items[--top];
        // Drop the reference so it can be garbage collected
        items[top] = null;
        return item;
    }

    /**
     * Returns the top element without removing it.
     *
     * @throws IllegalStateException when the stack is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return (T) items[top - 1];
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public boolean isFull() {
        return top == items.length;
    }

    public int size() {
        return top;
    }

    /** @return a copy of the contents, bottom of the stack first */
    public Object[] toArray() {
        Object[] out = new Object[top];
        System.arraycopy(items, 0, out, 0, top);
        return out;
    }
}
