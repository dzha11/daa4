package org.example.graph.scc;

import org.example.graph.Graph;
import java.util.*;

//   находим strongly connected components (SCC)
public class TarjanSCC {
    private int time = 0;
    private int dfsOps = 0;
    private int edgeOps = 0;

    private int[] disc, low;
    private boolean[] inStack;
    private Deque<Integer> stack = new ArrayDeque<>();
    private List<List<Integer>> components = new ArrayList<>();

    public TarjanSCC() {
        // пустой конструктор — если создаём объект без авто-запуска
    }

    public List<List<Integer>> findSCCs(Graph g) {
        int n = g.V;
        disc = new int[n];
        low = new int[n];
        inStack = new boolean[n];
        stack.clear();
        components.clear();
        time = 0;
        dfsOps = 0;
        edgeOps = 0;

        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);

        // запускаем dfs для каждой вершины
        for (int i = 0; i < n; i++) {
            if (disc[i] == -1) dfs(i, g);
        }

        // добавляем изолированные вершины, если они не в компонентах
        for (int i = 0; i < n; i++) {
            boolean found = false;
            for (List<Integer> comp : components) {
                if (comp.contains(i)) {
                    found = true;
                    break;
                }
            }
            if (!found) components.add(Collections.singletonList(i));
        }

        return components;
    }

    private void dfs(int u, Graph g) {
        disc[u] = low[u] = ++time;
        stack.push(u);
        inStack[u] = true;
        dfsOps++;

        for (Graph.Edge e : g.adj.get(u)) {
            edgeOps++;
            int v = e.to;

            if (disc[v] == -1) {
                dfs(v, g);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }

        // если u — корень компоненты
        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                inStack[v] = false;
                comp.add(v);
            } while (v != u);
            components.add(comp);
        }
    }

    // просто методы для метрик
    public int getSccCount() {
        return components.size();
    }

    public int getDfsOps() {
        return dfsOps;
    }

    public int getEdgeOps() {
        return edgeOps;
    }

    public void printComponents() {
        System.out.println("\nSCC найдено: " + components.size());
        for (int i = 0; i < components.size(); i++) {
            System.out.println("  #" + (i + 1) + ": " + components.get(i));
        }
    }
}
