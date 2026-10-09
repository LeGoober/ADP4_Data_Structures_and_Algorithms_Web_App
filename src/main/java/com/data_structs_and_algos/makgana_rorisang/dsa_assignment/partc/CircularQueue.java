package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

/**
 * Fixed-capacity circular queue; {@code count} distinguishes full from empty.
 * enqueue and dequeue are O(1) because indexes wrap with the modulo operator.
 *
 * @param <T> the element type
 * @author Rorisang Makgana
 * @version 1.0
 */
public class CircularQueue<T> {

    private final Object[] items;
    /** Index of the oldest element. */
    private int front;
    /** Index of the next free slot. */
    private int rear;
    private int count;

    /** @param capacity the maximum number of elements */
    public CircularQueue(int capacity) {
        items = new Object[capacity];
        front = 0;
        rear = 0;
        count = 0;
    }

    /**
     * Adds at the rear.
     *
     * @throws IllegalStateException when the queue is full
     */
    public void enqueue(T item) {
        if (isFull()) {
            throw new IllegalStateException("Queue is full");
        }
        items[rear] = item;
        rear = (rear + 1) % items.length;
        count++;
    }

    /**
     * Removes and returns the front element.
     *
     * @throws IllegalStateException when the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T dequeue() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        T item = (T) items[front];
        items[front] = null;
        front = (front + 1) % items.length;
        count--;
        return item;
    }

    /**
     * Returns the front element without removing it.
     *
     * @throws IllegalStateException when the queue is empty
     */
    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }
        return (T) items[front];
    }

    public boolean isEmpty() {
        return count == 0;
    }

    public boolean isFull() {
        return count == items.length;
    }

    public int size() {
        return count;
    }

    public int capacity() {
        return items.length;
    }

    /** @return the physical slot of the oldest element */
    public int getFrontIndex() {
        return front;
    }

    /** @return the physical slot the next enqueue will use */
    public int getRearIndex() {
        return rear;
    }

    /** @return a copy of the raw slots array (index = physical slot) */
    public Object[] slotsSnapshot() {
        return items.clone();
    }
}
