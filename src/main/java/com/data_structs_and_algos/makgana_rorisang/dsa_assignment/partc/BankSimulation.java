package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import java.util.Random;

/**
 * One teller, one line (a CircularQueue), customers arriving at random.
 *
 * @author Rorisang Makgana
 * @version 1.0
 */
public class BankSimulation {

    private final CircularQueue<Customer> line;
    private Customer atTeller;
    private int serviceRemaining;
    private int clock;
    private int nextId = 1;
    private final double arrivalChance;
    private final Random rng;
    private long totalWaitTicks;
    private int servedCount;
    /** Customers who have reached the teller (the denominator of the average wait). */
    private int startedCount;

    /**
     * @param lineCapacity  how many customers fit in the line
     * @param arrivalChance chance (0..1) that a customer arrives on a tick
     * @param seed          random seed, so a run can be repeated
     */
    public BankSimulation(int lineCapacity, double arrivalChance, long seed) {
        this.line = new CircularQueue<>(lineCapacity);
        this.arrivalChance = arrivalChance;
        this.rng = new Random(seed);
    }

    /** Advances the simulation by one tick. */
    public void tick() {
        clock++;
        // 1. Maybe a customer arrives (turned away when the line is full)
        if (rng.nextDouble() < arrivalChance && !line.isFull()) {
            line.enqueue(new Customer(nextId++, clock, 2 + rng.nextInt(4)));
        }
        // 2. The teller works on the current customer
        if (atTeller != null) {
            serviceRemaining--;
            if (serviceRemaining <= 0) {
                servedCount++;
                atTeller = null;
            }
        }
        // 3. A free teller takes the customer at the front of the line
        if (atTeller == null && !line.isEmpty()) {
            atTeller = line.dequeue();
            serviceRemaining = atTeller.serviceTicks;
            totalWaitTicks += clock - atTeller.arrivalTick;
            startedCount++;
        }
    }

    public CircularQueue<Customer> getLine() {
        return line;
    }

    public Customer getAtTeller() {
        return atTeller;
    }

    public int getClock() {
        return clock;
    }

    public int getServedCount() {
        return servedCount;
    }

    /** @return mean ticks spent waiting in line, 0 before anyone reaches the teller */
    public double getAverageWait() {
        return startedCount == 0 ? 0.0 : (double) totalWaitTicks / startedCount;
    }
}
