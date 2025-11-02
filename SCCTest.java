package org.example;

import org.example.graph.Graph;
import org.example.graph.scc.TarjanSCC;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class SCCTest {

    @Test
    void testSimpleSCC() {
        Graph g = new Graph(5, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(1, 3, 1);
        g.addEdge(3, 4, 1);

        TarjanSCC tarjan = new TarjanSCC();
        List<List<Integer>> sccs = tarjan.findSCCs(g);

        assertEquals(3, sccs.size());
        assertTrue(sccs.stream().anyMatch(c -> c.containsAll(Arrays.asList(0,1,2))));
    }
}
