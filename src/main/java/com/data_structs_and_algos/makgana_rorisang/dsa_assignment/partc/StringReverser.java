package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import org.springframework.stereotype.Component;

/**
 * Reverses a string with an ArrayStack: last in, first out turns the order around. O(n) time and space.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
public class StringReverser {

    /**
     * @param text the text to reverse
     * @return the characters of text in reverse order
     */
    public String reverse(String text) {
        ArrayStack<Character> stack = new ArrayStack<>(text.length());
        for (int i = 0; i < text.length(); i++) {
            stack.push(text.charAt(i));
        }
        StringBuilder reversed = new StringBuilder();
        while (!stack.isEmpty()) {
            reversed.append(stack.pop());
        }
        return reversed.toString();
    }
}
