package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parta;

/** A recorded ENTER or EXIT of one call (plain data holder). */
public class CallEvent {

    public EventType type;
    public CallNode node;

    public CallEvent(EventType type, CallNode node) {
        this.type = type;
        this.node = node;
    }
}
