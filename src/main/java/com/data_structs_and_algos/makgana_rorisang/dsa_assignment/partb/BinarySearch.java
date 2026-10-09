package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partb;

import org.springframework.stereotype.Component;

/**
 * Binary search over a sorted array, written iteratively and recursively.
 * Time O(log n); space O(1) iteratively and O(log n) recursively (call stack).
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
@Component
public class BinarySearch {

    /**
     * Iterative binary search.
     *
     * @param a   sorted array
     * @param key value to find
     * @param l   probe listener (may be null)
     * @return index of key, or -1 when absent
     */
    public int iterative(int[] a, int key, SearchListener l) {
        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            // Written this way to avoid int overflow of (lo + hi)
            int mid = lo + (hi - lo) / 2;
            if (l != null) {
                l.onProbe(lo, mid, hi);
            }
            if (a[mid] == key) {
                return mid;
            } else if (a[mid] < key) {
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }
        return -1;
    }

    /**
     * Recursive binary search over the whole array.
     *
     * @param a   sorted array
     * @param key value to find
     * @param l   probe listener (may be null)
     * @return index of key, or -1 when absent
     */
    public int recursive(int[] a, int key, SearchListener l) {
        return recursive(a, key, 0, a.length - 1, l);
    }

    private int recursive(int[] a, int key, int lo, int hi, SearchListener l) {
        // Base case: an empty range means the key is not present
        if (lo > hi) {
            return -1;
        }
        int mid = lo + (hi - lo) / 2;
        if (l != null) {
            l.onProbe(lo, mid, hi);
        }
        if (a[mid] == key) {
            return mid;
        }
        // Recursive case: continue in the half that can contain the key
        return a[mid] < key
                ? recursive(a, key, mid + 1, hi, l)
                : recursive(a, key, lo, mid - 1, l);
    }
}
