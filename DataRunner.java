package org.example;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.example.graph.Graph;
import org.example.graph.scc.TarjanSCC;
import org.example.graph.scc.CondensationGraph;
import org.example.graph.topo.TopologicalSort;
import org.example.graph.dagsp.DAGShortestPath;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.*;


public class DataRunner {
    public static void main(String[] args) throws Exception {
        File dataDir = new File("src/main/resources/data");
        if (!dataDir.exists() || !dataDir.isDirectory()) {
            System.err.println("data dir missing: src/main/resources/data");
            return;
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File out = new File("output");
        out.mkdirs();
        File csv = new File(out, "batch_results.csv");

        // header
        try (Writer w = new OutputStreamWriter(new FileOutputStream(csv), StandardCharsets.UTF_8)) {
            w.write("id,V,E,SCC_Count,SCC_Time_ms,DFS_Ops,Edge_Ops,DAG_V,DAG_E,RelaxOps,SP_Time_ms,LongestDist,Total_ms\n");
        }

        File[] files = dataDir.listFiles((d, name) -> name.endsWith(".json"));
        if (files == null) files = new File[0];
        Arrays.sort(files);

        for (File f : files) {
            System.out.println("processing " + f.getName());
            JsonObject jo = gson.fromJson(new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8), JsonObject.class);
            String id = jo.get("id").getAsString();
            int V = jo.get("vertices").getAsInt();

            Graph g = new Graph(V, true);
            JsonArray arr = jo.getAsJsonArray("edges");
            for (JsonElement ee : arr) {
                JsonObject eo = ee.getAsJsonObject();
                int u = eo.get("u").getAsInt();
                int v = eo.get("v").getAsInt();
                int wgt = eo.get("w").getAsInt();
                if (u >= 0 && u < V && v >= 0 && v < V) g.addEdge(u, v, wgt);
            }

            long t0 = System.nanoTime();
            TarjanSCC tar = new TarjanSCC();
            long s0 = System.nanoTime();
            List<List<Integer>> sccs = tar.findSCCs(g);
            long s1 = System.nanoTime();

            CondensationGraph cond = new CondensationGraph(g, sccs);
            Graph dag = cond.build();

            TopologicalSort topo = new TopologicalSort();
            long s2 = System.nanoTime();
            List<Integer> order = topo.sort(dag);
            long s3 = System.nanoTime();

            DAGShortestPath sp = new DAGShortestPath();
            int source = 0;
            long s4 = System.nanoTime();
            DAGShortestPath.Result res = sp.findPaths(dag, source);
            long s5 = System.nanoTime();

            long totalMs = (System.nanoTime() - t0) / 1_000_000;
            long sccMs = (s1 - s0) / 1_000_000;
            long topoMs = (s3 - s2) / 1_000_000;
            long spMs = (s5 - s4) / 1_000_000;

            try (Writer w = new OutputStreamWriter(new FileOutputStream(csv, true), StandardCharsets.UTF_8)) {
                w.write(String.format(Locale.ROOT,
                        "%s,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d,%d\n",
                        id, V, g.edges.size(),
                        tar.getSccCount(), sccMs, tar.getDfsOps(), tar.getEdgeOps(),
                        dag.V, dag.edges.size(),
                        res.relaxOps, res.timeMs, res.longestDist, totalMs
                ));
            }

            System.out.printf(" -> done: %s | V=%d E=%d SCC=%d DAG_V=%d total=%dms\n",
                    id, V, g.edges.size(), tar.getSccCount(), dag.V, totalMs);
        }

        System.out.println("batch finished, results -> output/batch_results.csv");
    }
}
