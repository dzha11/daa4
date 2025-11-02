README.md — Assignment 4: Smart City / Smart Campus Scheduling

🔹 Overview

This project combines two key graph algorithm topics:
	•	Strongly Connected Components (SCC) — using Tarjan’s algorithm
	•	Topological Sorting — using DFS-based variant
	•	Shortest Paths in DAG — using dynamic programming over topological order

The goal is to model smart city task scheduling (maintenance, repairs, analytics, etc.)
and optimize task order + dependency handling by detecting cycles and computing paths.

⸻

🔹 Structure
daa4/
 ├── pom.xml
 ├── src/
 │   ├── main/
 │   │   ├── java/org/example/
 │   │   │    ├── Main.java
 │   │   │    ├── DataRunner.java
 │   │   │    └── graph/
 │   │   │         ├── Graph.java
 │   │   │         ├── scc/
 │   │   │         │    ├── TarjanSCC.java
 │   │   │         │    └── CondensationGraph.java
 │   │   │         ├── topo/
 │   │   │         │    └── TopologicalSort.java
 │   │   │         └── dagsp/
 │   │   │              └── DAGShortestPath.java
 │   │   └── resources/data/
 │   │        ├── small_1.json ... large_3.json
 │   └── test/java/org/example/
 │        ├── SCCTest.java
 │        ├── TopoTest.java
 │        └── DAGSPTest.java
 └── output/batch_results.csv

 Datasets

All datasets are stored in src/main/resources/data/
Generated automatically using DatasetGenerator.java.
Category
Vertices (n)
Description
Variants
Small
6–8
simple graphs, 1–2 cycles
3
Medium
10–15
several SCCs, moderate density
3
Large
20–35
performance and timing tests
3
Each JSON file includes:
{
  "id": "small_1",
  "vertices": 8,
  "edges": [
    {"u":0,"v":1,"w":4},
    {"u":1,"v":2,"w":6}
  ]
}
Algorithms Implemented

1️⃣ TarjanSCC (Strongly Connected Components)
	•	Detects all SCCs using Tarjan’s DFS-based algorithm.
	•	Counts DFS and edge operations.
	•	Output: list of components and their sizes.

2️⃣ CondensationGraph
	•	Builds a new DAG where each node represents an SCC.
	•	Removes duplicate edges between components.

3️⃣ TopologicalSort
	•	Uses DFS-based topological sorting for DAG.
	•	Counts pushes/pops for metrics.

4️⃣ DAGShortestPath
	•	Finds shortest and longest paths in DAG.
	•	Uses dynamic programming over topological order.
	•	Longest path → via sign inversion technique.
	•	Output: shortest dist array, longest path, execution time.

⸻

🔹 Metrics & Instrumentation

Collected metrics per dataset:
Metric
Description
V, E
vertices and edges in original graph
SCC_Count
number of SCCs found
DFS_Ops / Edge_Ops
Tarjan’s operation counters
DAG_V, DAG_E
condensed DAG size
RelaxOps
relaxations performed in DAG-SP
SP_Time_ms
DAG shortest path time
Total_ms
total execution time per dataset

 Analysis & Discussion:
 Aspect
Observation
SCC Detection
Tarjan performs efficiently even on large graphs due to single DFS pass.
Condensation Graph
Reduced graph size drastically improves DAG operations.
Topological Sorting
DFS variant is stable and simple to integrate.
Shortest Paths in DAG
Linear complexity O(V+E) — fast even for 50 nodes.
Performance Bottlenecks
Most time is spent in SCC detection on dense graphs.
When to use which:
Tarjan — for general directed graphs, Topo + DAG-SP — for acyclic scheduling and critical path analysis.

 Conclusions
	•	Tarjan’s SCC is best for detecting cyclic dependencies in any city-service graph.
	•	Condensation + Topological sort gives clean task order.
	•	DAG Shortest/Longest paths help plan optimal and critical schedules.
	•	Works efficiently for both small and large datasets.

✅ Project builds successfully (mvn clean install)
✅ All tests pass
✅ Results are reproducible (deterministic datasets)

How to Run
1️⃣ Generate datasets: mvn compile exec:java -Dexec.mainClass=org.example.data.DatasetGenerator

2️⃣ Run single case:mvn compile exec:java -Dexec.mainClass=org.example.Main

3️⃣ Run batch mode (all datasets):mvn compile exec:java -Dexec.mainClass=org.example.DataRunner

