package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/** Tests for {@link BankSimulation}. */
class BankSimulationTest {

    @Test
    void clockAdvancesEachTick() {
        BankSimulation sim = new BankSimulation(5, 0.5, 1);
        sim.tick();
        sim.tick();
        assertEquals(2, sim.getClock());
    }

    @Test
    void sameSeedGivesSameRun() {
        BankSimulation a = new BankSimulation(5, 0.6, 42);
        BankSimulation b = new BankSimulation(5, 0.6, 42);
        for (int i = 0; i < 100; i++) {
            a.tick();
            b.tick();
        }
        assertEquals(a.getServedCount(), b.getServedCount());
        assertEquals(a.getAverageWait(), b.getAverageWait());
    }

    @Test
    void servedCustomersCountUp() {
        BankSimulation sim = new BankSimulation(5, 0.9, 7);
        for (int i = 0; i < 200; i++) {
            sim.tick();
        }
        assertTrue(sim.getServedCount() > 0);
    }

    @Test
    void averageWaitIsZeroBeforeAnyoneIsServed() {
        assertEquals(0.0, new BankSimulation(5, 0.5, 1).getAverageWait());
    }

    @Test
    void lineNeverExceedsCapacity() {
        BankSimulation sim = new BankSimulation(3, 1.0, 3);
        for (int i = 0; i < 100; i++) {
            sim.tick();
            assertTrue(sim.getLine().size() <= 3);
        }
    }
}
