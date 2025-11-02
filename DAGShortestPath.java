package org.example.graph.dagsp;

import org.example.graph.Graph;
import java.util.*;

// shortest & longest path for DAG (через топосорт)
public class DAGShortestPath {
    private long relaxOps = 0;
    private long timeNs = 0;

    public static class Result {
        public int[] dist;
        public int[] parent;
        public int source;
        public int target;
        public List<Integer> path = new ArrayList<>();
        public long relaxOps;
        public long timeMs;
        public int longestDist;
        public List<Integer> longestPath = new ArrayList<>();
    }

    public Result findPaths(Graph g, int source) {
        long start = System.nanoTime();
        int V = g.V;
        Result res = new Result();
        res.source = source;

        // топосорт
        List<Integer> order = topoSort(g);

        int[] dist = new int[V];
        int[] parent = new int[V];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;

        // shortest path relaxations
        for (int u : order) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Graph.Edge e : g.adj.get(u)) {
                    relaxOps++;
                    if (dist[e.to] > dist[u] + e.weight) {
                        dist[e.to] = dist[u] + e.weight;
                        parent[e.to] = u;
                    }
                }
            }
        }

        res.dist = dist;
        res.parent = parent;
        res.relaxOps = relaxOps;
        res.timeMs = (System.nanoTime() - start) / 1_000_000;

        // longest path
        res.longestDist = findLongest(g, order, source, res.longestPath);

        return res;
    }

    // longest path через динамику
    private int findLongest(Graph g, List<Integer> order, int source, List<Integer> longestPath) {
        int[] dist = new int[g.V];
        Arrays.fill(dist, Integer.MIN_VALUE);
        dist[source] = 0;
        int[] parent = new int[g.V];
        Arrays.fill(parent, -1);

        for (int u : order) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Graph.Edge e : g.adj.get(u)) {
                    if (dist[e.to] < dist[u] + e.weight) {
                        dist[e.to] = dist[u] + e.weight;
                        parent[e.to] = u;
                    }
                }
            }
        }

        int maxDist = Integer.MIN_VALUE;
        int end = -1;
        for (int i = 0; i < g.V; i++) {
            if (dist[i] > maxDist) {
                maxDist = dist[i];
                end = i;
            }
        }

        if (end != -1) {
            List<Integer> path = new ArrayList<>();
            for (int v = end; v != -1; v = parent[v]) path.add(v);
            Collections.reverse(path);
            longestPath.addAll(path);
        }

        return maxDist;
    }

    private List<Integer> topoSort(Graph g) {
        boolean[] visited = new boolean[g.V];
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < g.V; i++) {
            if (!visited[i]) dfs(i, g, visited, order);
        }
        Collections.reverse(order);
        return order;
    }

    private void dfs(int u, Graph g, boolean[] visited, List<Integer> order) {
        visited[u] = true;
        for (Graph.Edge e : g.adj.get(u)) {
            if (!visited[e.to]) dfs(e.to, g, visited, order);
        }
        order.add(u);
    }

    public void printResult(Result r) {
        System.out.println("Shortest path dist from " + r.source + ": " + Arrays.toString(r.dist));
        System.out.println("Relaxations: " + r.relaxOps + " | Time: " + r.timeMs + " ms");
        System.out.println("Longest path: " + r.longestPath + " | Len=" + r.longestDist);
    }
}
