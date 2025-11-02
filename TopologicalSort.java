package org.example.graph.topo;

import org.example.graph.Graph;

import java.util.*;

// простая топологическая сортировка (вариант Kahn)
public class TopologicalSort {
    private int nodeOps = 0;
    private int edgeOps = 0;

    // возвращает список вершин в порядке топосорта
    public List<Integer> sort(int V, List<List<Integer>> adj) {
        int[] indeg = new int[V];
        for (int u = 0; u < V; u++) {
            for (int v : adj.get(u)) {
                indeg[v]++;
                edgeOps++;
            }
        }

        Queue<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < V; i++) {
            if (indeg[i] == 0) q.add(i);
        }

        List<Integer> order = new ArrayList<>();
        while (!q.isEmpty()) {
            int u = q.poll();
            nodeOps++;
            order.add(u);
            for (int v : adj.get(u)) {
                indeg[v]--;
                if (indeg[v] == 0) q.add(v);
            }
        }

        return order;
    }

    public int getNodeOps() {
        return nodeOps;
    }

    public int getEdgeOps() {
        return edgeOps;
    }

    public void printOrder(List<Integer> order) {
        System.out.println("Topological order: " + order);
    }

    public List<Integer> sort(Graph dag) {
        return List.of();
    }
}
