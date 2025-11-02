package org.example;

import com.google.gson.*;
import org.example.graph.Graph;
import org.example.graph.scc.*;
import org.example.graph.topo.*;
import org.example.graph.dagsp.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Main {
    public static void main(String[] args) throws Exception {
        // путь к JSON
        String path = "src/main/resources/data/small_1.json";
        Gson gson = new Gson();

        JsonObject obj = gson.fromJson(
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8),
                JsonObject.class
        );

        int V = obj.get("vertices").getAsInt();
        Graph g = new Graph(V, true);
        JsonArray edges = obj.getAsJsonArray("edges");

        for (JsonElement e : edges) {
            JsonObject ed = e.getAsJsonObject();
            int u = ed.get("u").getAsInt();
            int v = ed.get("v").getAsInt();
            int w = ed.get("w").getAsInt();
            g.addEdge(u, v, w);
        }

        System.out.println(" загружен граф из JSON: " + V + " вершин, " + edges.size() + " рёбер");

        // SCC (Tarjan)
        TarjanSCC tarjan = new TarjanSCC();
        List<List<Integer>> sccs = tarjan.findSCCs(g);
        System.out.println(" Найдено SCC: " + sccs.size());
        for (int i = 0; i < sccs.size(); i++) {
            System.out.println("компонента " + i + ": " + sccs.get(i));
        }

        // Конденсация SCC → DAG
        CondensationGraph cond = new CondensationGraph(g, sccs);
        Graph dag = cond.build();
        System.out.println("DAG (после конденсации): вершин = " + dag.V);

        // Топологическая сортировка
        TopologicalSort topo = new TopologicalSort();
        List<Integer> order = topo.sort(dag);
        System.out.println(" Топологический порядок: " + order);

        // Кратчайшие и длиннейшие пути (DAG SP)
        DAGShortestPath dagsp = new DAGShortestPath();
        DAGShortestPath.Result res = dagsp.findPaths(dag, 0);

        System.out.println(" РЕЗУЛЬТАТЫ ДЛЯ DAG SP:");
        dagsp.printResult(res);

        System.out.println(" Всё завершено успешно!");
    }
}
