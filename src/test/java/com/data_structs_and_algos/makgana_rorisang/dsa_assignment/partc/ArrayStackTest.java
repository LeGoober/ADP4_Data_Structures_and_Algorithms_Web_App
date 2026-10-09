package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link ArrayStack}. */
class ArrayStackTest {

    @Test
    void newStackIsEmpty() {
        ArrayStack<Integer> stack = new ArrayStack<>(3);
        assertTrue(stack.isEmpty());
        assertFalse(stack.isFull());
        assertEquals(0, stack.size());
    }

    @Test
    void pushThenPopIsLifo() {
        ArrayStack<Integer> stack = new ArrayStack<>(3);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    void peekDoesNotRemove() {
        ArrayStack<String> stack = new ArrayStack<>(2);
        stack.push("a");
        assertEquals("a", stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    void popOnEmptyThrows() {
        assertThrows(IllegalStateException.class, () -> new ArrayStack<Integer>(1).pop());
        assertThrows(IllegalStateException.class, () -> new ArrayStack<Integer>(1).peek());
    }

    @Test
    void pushOnFullThrows() {
        ArrayStack<Integer> stack = new ArrayStack<>(1);
        stack.push(1);
        assertTrue(stack.isFull());
        assertThrows(IllegalStateException.class, () -> stack.push(2));
    }

    @Test
    void toArrayIsBottomFirst() {
        ArrayStack<Integer> stack = new ArrayStack<>(3);
        stack.push(1);
        stack.push(2);
        assertArrayEquals(new Object[] {1, 2}, stack.toArray());
    }
}
