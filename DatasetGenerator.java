package org.example.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class DatasetGenerator {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final Random rnd = new Random(42); // фиксированный сид — воспроизводимо

    public static void main(String[] args) throws Exception {
        File outDir = new File("src/main/resources/data");
        outDir.mkdirs();

        // small: 6..8
        for (int i = 1; i <= 3; i++) {
            int n = 6 + rnd.nextInt(3); // 6-8
            List<Map<String, Integer>> edges = generateGraph(n, 0.15 + 0.15 * i, true);
            writeJson(outDir, "small_" + i + ".json", "small_" + i, n, edges);
        }

        // medium: 10..15
        for (int i = 1; i <= 3; i++) {
            int n = 10 + rnd.nextInt(6); // 10-15
            List<Map<String, Integer>> edges = generateGraph(n, 0.12 + 0.12 * i, true);
            writeJson(outDir, "medium_" + i + ".json", "medium_" + i, n, edges);
        }

        // large: 22..35
        for (int i = 1; i <= 3; i++) {
            int n = 22 + rnd.nextInt(14); // 22-35
            List<Map<String, Integer>> edges = generateGraph(n, 0.06 + 0.06 * i, true);
            writeJson(outDir, "large_" + i + ".json", "large_" + i, n, edges);
        }

        System.out.println("datasets generated in src/main/resources/data/");
    }

    // generate directed graph with some cycles (we force some cycles)
    private static List<Map<String,Integer>> generateGraph(int n, double density, boolean forceCycles) {
        List<Map<String,Integer>> edges = new ArrayList<>();

        // 1) add a few base cycles to ensure SCCs exist
        if (forceCycles && n >= 3) {
            int blocks = Math.max(1, n / 6);
            for (int b = 0; b < blocks; b++) {
                int a = (b * 3) % n;
                int b1 = (a + 1) % n;
                int c = (a + 2) % n;
                addEdge(edges, a, b1);
                addEdge(edges, b1, c);
                addEdge(edges, c, a);
            }
        }

        // 2) ensure connectivity-ish by adding a chain
        for (int i = 0; i < n - 1; i++) addEdge(edges, i, i + 1);

        // 3) random extra edges according to density
        int possible = n * n;
        int targetExtra = (int) (possible * density);
        for (int k = 0; k < targetExtra; k++) {
            int u = rnd.nextInt(n);
            int v = rnd.nextInt(n);
            if (u == v) continue;
            addEdge(edges, u, v);
        }

        // remove duplicates while preserving weight randomness
        Map<String, Map<String,Integer>> unique = new LinkedHashMap<>();
        for (Map<String,Integer> e : edges) {
            String key = e.get("u") + "-" + e.get("v");
            if (!unique.containsKey(key)) unique.put(key, e);
        }
        return new ArrayList<>(unique.values());
    }

    private static void addEdge(List<Map<String,Integer>> edges, int u, int v) {
        Map<String,Integer> e = new HashMap<>();
        e.put("u", u);
        e.put("v", v);
        // weight 1..10
        e.put("w", 1 + rnd.nextInt(10));
        edges.add(e);
    }

    private static void writeJson(File dir, String filename, String id, int n, List<Map<String,Integer>> edges) throws IOException {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("id", id);
        out.put("vertices", n);
        out.put("edges", edges);

        File f = new File(dir, filename);
        try (Writer w = new OutputStreamWriter(new FileOutputStream(f), StandardCharsets.UTF_8)) {
            gson.toJson(out, w);
        }
    }
}
