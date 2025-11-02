package org.example;

import org.example.graph.Graph;
import org.example.graph.topo.TopologicalSort;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class TopoTest {

    @Test
    void testTopoSortOrder() {
        Graph g = new Graph(6, true);
        g.addEdge(5, 2, 1);
        g.addEdge(5, 0, 1);
        g.addEdge(4, 0, 1);
        g.addEdge(4, 1, 1);
        g.addEdge(2, 3, 1);
        g.addEdge(3, 1, 1);

        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(g);

        // проверяем, что порядок валидный
        int pos[] = new int[g.V];
        for (int i = 0; i < order.size(); i++) pos[order.get(i)] = i;

        for (Graph.Edge e : g.edges) {
            assertTrue(pos[e.from] < pos[e.to], "ребро нарушает порядок: " + e);
        }
    }
}
