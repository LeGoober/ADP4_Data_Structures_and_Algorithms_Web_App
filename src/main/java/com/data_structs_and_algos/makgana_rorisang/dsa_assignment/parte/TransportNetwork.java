package com.data_structs_and_algos.makgana_rorisang.dsa_assignment.parte;

import java.awt.Point;

/**
 * Hard-coded South African cities, their map positions, and the routes between them.
 * Positions are rough longitude/latitude scaled to pixels; GraphPanel scales them to fit.
 */
public class TransportNetwork {

    private static final String[] CITIES = {
            "Cape Town", "George", "Gqeberha", "East London", "Durban", "Bloemfontein",
            "Kimberley", "Johannesburg", "Pretoria", "Polokwane", "Mbombela"
    };

    private static final int[][] COORDS = {
            {24, 624}, {270, 630}, {456, 628}, {594, 570}, {780, 384}, {492, 336},
            {408, 312}, {600, 170}, {618, 118}, {687, 24}, {778, 118}
    };

    /** Routes to add with graph.addEdge(a, b) once Graph is implemented. */
    private static final String[][] ROUTES = {
            {"Cape Town", "George"}, {"Cape Town", "Kimberley"}, {"George", "Gqeberha"},
            {"Gqeberha", "East London"}, {"East London", "Durban"}, {"Gqeberha", "Bloemfontein"},
            {"East London", "Bloemfontein"}, {"Kimberley", "Bloemfontein"}, {"Bloemfontein", "Johannesburg"},
            {"Kimberley", "Johannesburg"}, {"Durban", "Johannesburg"}, {"Durban", "Mbombela"},
            {"Johannesburg", "Pretoria"}, {"Pretoria", "Polokwane"}, {"Pretoria", "Mbombela"},
            {"Polokwane", "Mbombela"}
    };

    private Graph graph;
    private Point[] positions;

    public TransportNetwork() {
        graph = new Graph(CITIES);
        positions = new Point[COORDS.length];
        for (int i = 0; i < COORDS.length; i++) {
            positions[i] = new Point(COORDS[i][0], COORDS[i][1]);
        }
        // Add every route to the graph (roads are two-way)
        for (String[] route : ROUTES) {
            graph.addEdge(route[0], route[1]);
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public Point positionOf(int v) {
        return positions[v];
    }
}
