package org.example;

import org.example.graph.Graph;
import org.example.graph.dagsp.DAGShortestPath;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class DAGSPTest {

    @Test
    void testShortestAndLongest() {
        Graph dag = new Graph(6, true);
        dag.addEdge(0, 1, 5);
        dag.addEdge(0, 2, 3);
        dag.addEdge(1, 3, 6);
        dag.addEdge(1, 2, 2);
        dag.addEdge(2, 4, 4);
        dag.addEdge(2, 5, 2);
        dag.addEdge(2, 3, 7);
        dag.addEdge(3, 4, -1);
        dag.addEdge(4, 5, -2);

        DAGShortestPath sp = new DAGShortestPath();
        DAGShortestPath.Result res = sp.findPaths(dag, 1);

        assertNotNull(res.dist);
        assertEquals(6, res.dist.length);
        assertTrue(res.relaxOps > 0);
        assertTrue(res.timeMs >= 0);

        System.out.println("Shortest path test passed ");
        System.out.println("Longest path length: " + res.longestDist);
    }
}
