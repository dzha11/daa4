package org.example.graph;

import java.util.*;

public class Graph {
    public int V;
    public List<List<Edge>> adj;
    public List<Edge> edges = new ArrayList<>();
    public boolean directed;

    public Graph(int V, boolean directed) {
        this.V = V;
        this.directed = directed;
        adj = new ArrayList<>(V);
        for (int i = 0; i < V; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int w) {
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj.get(u).add(e);
        if (!directed) {
            adj.get(v).add(new Edge(v, u, w));
        }
    }

    public static class Edge {
        public int from, to, weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        @Override
        public String toString() {
            return "(" + from + "->" + to + ", w=" + weight + ")";
        }
    }
}
