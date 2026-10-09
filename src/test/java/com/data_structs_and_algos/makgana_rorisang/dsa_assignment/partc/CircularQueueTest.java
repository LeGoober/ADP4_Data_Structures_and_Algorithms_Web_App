package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link CircularQueue}. */
class CircularQueueTest {

    @Test
    void newQueueIsEmpty() {
        CircularQueue<Integer> q = new CircularQueue<>(3);
        assertTrue(q.isEmpty());
        assertFalse(q.isFull());
        assertEquals(3, q.capacity());
    }

    @Test
    void enqueueThenDequeueIsFifo() {
        CircularQueue<Integer> q = new CircularQueue<>(3);
        q.enqueue(1);
        q.enqueue(2);
        assertEquals(1, q.peek());
        assertEquals(1, q.dequeue());
        assertEquals(2, q.dequeue());
    }

    @Test
    void wrapsAroundAfterDequeues() {
        CircularQueue<Integer> q = new CircularQueue<>(3);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.dequeue();
        q.dequeue();
        q.enqueue(4);
        q.enqueue(5);
        // rear has wrapped round to slot 1; front sits on slot 2
        assertTrue(q.isFull());
        assertEquals(2, q.getFrontIndex());
        assertEquals(2, q.getRearIndex());
        assertEquals(3, q.dequeue());
        assertEquals(4, q.dequeue());
        assertEquals(5, q.dequeue());
    }

    @Test
    void dequeueOnEmptyThrows() {
        assertThrows(IllegalStateException.class, () -> new CircularQueue<Integer>(2).dequeue());
    }

    @Test
    void enqueueOnFullThrows() {
        CircularQueue<Integer> q = new CircularQueue<>(1);
        q.enqueue(1);
        assertThrows(IllegalStateException.class, () -> q.enqueue(2));
    }

    @Test
    void slotsSnapshotReturnsCopy() {
        CircularQueue<Integer> q = new CircularQueue<>(2);
        q.enqueue(7);
        Object[] snapshot = q.slotsSnapshot();
        snapshot[0] = 99;
        assertEquals(7, q.peek());
    }
}
