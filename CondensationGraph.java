package org.example.graph.scc;

import org.example.graph.Graph;
import java.util.*;

// этот класс строит граф конденсации (DAG из SCC)
public class CondensationGraph {
    private final Graph original;
    private final List<List<Integer>> sccs;
    private final Map<Integer, Integer> compMap = new HashMap<>();

    public CondensationGraph(Graph g, List<List<Integer>> sccs) {
        this.original = g;
        this.sccs = sccs;
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) {
                compMap.put(v, i);
            }
        }
    }

    public Graph build() {
        if (sccs == null || sccs.isEmpty()) {
            System.out.println("⚠ нет SCC, создаётся пустой DAG");
            return new Graph(0, true);
        }

        Graph dag = new Graph(sccs.size(), true);
        Set<String> added = new HashSet<>();

        for (Graph.Edge e : original.edges) {
            Integer cu = compMap.get(e.from);
            Integer cv = compMap.get(e.to);

            //  защищаемся от null
            if (cu == null || cv == null) continue;

            if (!cu.equals(cv)) {
                String key = cu + "-" + cv;
                if (!added.contains(key)) {
                    dag.addEdge(cu, cv, e.weight);
                    added.add(key);
                }
            }
        }

        if (dag.edges.isEmpty()) {
            System.out.println("️ DAG пуст — нет межкомпонентных связей, но создаём 0-рёберный граф");
        }

        return dag;
    }
}
