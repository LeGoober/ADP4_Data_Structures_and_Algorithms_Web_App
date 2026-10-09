package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.partc;

/** A bank customer (plain data holder). */
public class Customer {

    public int id;
    public int arrivalTick;
    public int serviceTicks;

    public Customer(int id, int arrivalTick, int serviceTicks) {
        this.id = id;
        this.arrivalTick = arrivalTick;
        this.serviceTicks = serviceTicks;
    }
}
