package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link StringReverser}. */
class StringReverserTest {

    private final StringReverser reverser = new StringReverser();

    @Test
    void reversesWord() {
        assertEquals("tpircsavaj", reverser.reverse("javascript"));
    }

    @Test
    void reversesEmptyString() {
        assertEquals("", reverser.reverse(""));
    }

    @Test
    void reversesPalindromeToItself() {
        assertEquals("racecar", reverser.reverse("racecar"));
    }
}
