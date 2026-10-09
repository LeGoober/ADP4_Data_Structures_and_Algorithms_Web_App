package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link SinglyLinkedList}. */
class SinglyLinkedListTest {

    private SinglyLinkedList<Integer> listOf(int... values) {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        for (int v : values) {
            list.insertLast(v);
        }
        return list;
    }

    @Test
    void newListIsEmpty() {
        SinglyLinkedList<Integer> list = new SinglyLinkedList<>();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void insertFirstAndLastKeepOrder() {
        SinglyLinkedList<Integer> list = listOf(2, 3);
        list.insertFirst(1);
        list.insertLast(4);
        assertArrayEquals(new Object[] {1, 2, 3, 4}, list.toArray());
    }

    @Test
    void insertAtMiddle() {
        SinglyLinkedList<Integer> list = listOf(1, 3);
        list.insertAt(1, 2);
        assertArrayEquals(new Object[] {1, 2, 3}, list.toArray());
    }

    @Test
    void insertAtBadIndexThrows() {
        SinglyLinkedList<Integer> list = listOf(1);
        assertThrows(IndexOutOfBoundsException.class, () -> list.insertAt(5, 9));
        assertThrows(IndexOutOfBoundsException.class, () -> list.insertAt(-1, 9));
    }

    @Test
    void deleteRemovesFirstMatch() {
        SinglyLinkedList<Integer> list = listOf(1, 2, 1, 3);
        assertTrue(list.delete(1));
        assertArrayEquals(new Object[] {2, 1, 3}, list.toArray());
        assertFalse(list.delete(99));
    }

    @Test
    void deleteAtReturnsRemovedValue() {
        SinglyLinkedList<Integer> list = listOf(5, 6, 7);
        assertEquals(6, list.deleteAt(1));
        assertEquals(5, list.deleteAt(0));
        assertEquals(1, list.size());
        assertThrows(IndexOutOfBoundsException.class, () -> list.deleteAt(3));
    }

    @Test
    void searchReturnsIndexOrMinusOne() {
        SinglyLinkedList<Integer> list = listOf(10, 20, 30);
        assertEquals(2, list.search(30));
        assertEquals(-1, list.search(40));
        assertEquals(20, list.get(1));
    }

    @Test
    void toArrayReturnsCopy() {
        SinglyLinkedList<Integer> list = listOf(1, 2);
        Object[] copy = list.toArray();
        copy[0] = 99;
        assertEquals(1, list.get(0));
    }
}
