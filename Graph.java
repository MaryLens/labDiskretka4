package main;

import java.util.ArrayDeque;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

public class Graph {

	Scanner sc = new Scanner(System.in);

	private int nodes, arcs;
	private ArrayList<ArrayList<Integer>> incMatrix = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> adjMatrix = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> adjList = new ArrayList<>();
	private boolean visitedDFS[];
	private boolean visitedBFS[];
	private boolean seen[];
	private Deque<Integer> queue = new ArrayDeque<>();

	private ArrayList<HashMap<Integer, Integer>> pList = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> minBellman = new ArrayList<>();
	private ArrayList<ArrayList<Integer>> maxBellman = new ArrayList<>();
	ArrayList<Integer> minH = new ArrayList<>();
	ArrayList<Integer> maxH = new ArrayList<>();

	public Graph(int nodes, int arcs) {
		this.nodes = nodes;
		this.arcs = arcs;
		visitedDFS = new boolean[nodes + 1];
		visitedBFS = new boolean[nodes + 1];
		seen = new boolean[nodes + 1];
	}

	public Graph() {
	}

	public void setNodes(int nodes) {
		this.nodes = nodes;
		visitedDFS = new boolean[nodes + 1];
		visitedBFS = new boolean[nodes + 1];
		seen = new boolean[nodes + 1];
	}

	public void setArcs(int arcs) {
		this.arcs = arcs;
	}

	boolean bfs(int rGraph[][], int s, int t, int parent[]) {
		boolean visited[] = new boolean[nodes];
		for (int i = 0; i < nodes; ++i)
			visited[i] = false;

		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(s);
		visited[s] = true;
		parent[s] = -1;
		while (queue.size() != 0) {
			int u = queue.poll();

			for (int v = 0; v < nodes; v++) {
				if (visited[v] == false && rGraph[u][v] > 0) {
					if (v == t) {
						parent[v] = u;
						return true;
					}
					queue.add(v);
					parent[v] = u;
					visited[v] = true;
				}
			}
		}
		return false;
	}

	int fordFulkerson(int graph[][], int s, int t) {
		int u, v;
		int rGraph[][] = new int[nodes][nodes];

		for (u = 0; u < nodes; u++) {
			for (v = 0; v < nodes; v++) {
				rGraph[u][v] = graph[u][v];
			}
		}
		int parent[] = new int[nodes];
		int max_flow = 0;
		while (bfs(rGraph, s, t, parent)) {
			ArrayList<Integer> path = new ArrayList();
			int e = Integer.MAX_VALUE;
			for (v = t; v != s; v = parent[v]) {
				u = parent[v];
				e = Math.min(e, rGraph[u][v]);
				path.add(v + 1);
			}
			for (v = t; v != s; v = parent[v]) {
				u = parent[v];
				rGraph[u][v] -= e;
				rGraph[v][u] += e;
			}
			max_flow += e;
			path.add(1);
			for (int i = path.size() - 1; i >= 0; i--) {
				if (i == 0) {
					System.out.print(path.get(i));
				} else {
					System.out.print(path.get(i) + "->");
				}
			}
			System.out.println("   e = " + e);
		}
		return max_flow;
	}

	public void getMaxFlow() {
		int graph[][] = new int[nodes][nodes];

		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < nodes; j++) {
				if (pList.get(i).containsKey(j + 1)) {
					graph[i][j] = (pList.get(i).get(j + 1));
				} else {
					graph[i][j] = 0;
				}
			}
		}

		int res = fordFulkerson(graph, 0, nodes - 1);
		System.out.print("максимальный поток: " + res);
	}

	public void setPList() {
		for (int i = 0; i < nodes; i++) {
			pList.add(new HashMap<>());
		}
		System.out.print("Вводите список смежности:\n");
		System.out.print("P = {");
		pList.add(new HashMap<>());
		int i = 0;
		System.out.print("P");
		int start = sc.nextInt();
		int dest = sc.nextInt();
		char eq = sc.next().charAt(0);
		int p = sc.nextInt();
		if (start == i + 1) {
			pList.get(i).put(dest, p);
		} else if (start == 0) {
			return;
		} else {
			for (int j = 0; (j < (start - i - 1)) && j < nodes; j++) {
				i++;
			}
			pList.get(i).put(dest, p);
		}
		while (start != 0 && i < nodes) {
			System.out.print("P");
			start = sc.nextInt();
			dest = sc.nextInt();
			eq = sc.next().charAt(0);
			p = sc.nextInt();
			if (start == i + 1) {
				pList.get(i).put(dest, p);
			} else if (start == 0) {
				break;
			} else {
				for (int j = 0; (j < (start - i - 1)) && j < nodes; j++) {
					i++;
				}
				pList.get(i).put(dest, p);
			}
		}
		System.out.println("}");

	}

	public void getPList() {
		System.out.print("список смежности:\n");
		System.out.print("P = {");
		for (int i = 0; i < pList.size(); i++) {
			final int j = i;
			if (!pList.get(i).isEmpty()) {
				pList.get(i).forEach((k, v) -> System.out.print("P" + (int) (j + 1) + "," + k + " = " + v + "; "));
				System.out.println();
			}
		}
		System.out.println("}");
	}

	public void minFord() {
		minH.add(0);
		for (int i = 1; i < nodes; i++) {
			minH.add(100000);
		}
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < nodes; j++) {
				if (pList.get(i).containsKey(j + 1)) {
					if (minH.get(j) - minH.get(i) > pList.get(i).get(j + 1)) {
						minH.set(j, minH.get(i) + pList.get(i).get(j + 1));
					}
				}
			}
		}
		int k = 0;
		while (true) {
			k--;
			for (int i = 0; i < nodes; i++) {
				for (int j = 0; j < nodes; j++) {
					if (pList.get(i).containsKey(j + 1)) {
						if (minH.get(j) - minH.get(i) > pList.get(i).get(j + 1)) {
							minH.set(j, minH.get(i) + pList.get(i).get(j + 1));
							k++;
						}
					}
				}
			}
			if (k <= 0)
				break;
		}

	}

	public int printMinFord() {
		ArrayList<Integer> print = new ArrayList<>();
		int k = 0;
		print.add(nodes);
		int i = nodes - 1;
		while (i > 0) {
			for (int j = i - 1; j >= 0; j--) {
				if (pList.get(j).containsKey(i + 1)) {
					if (minH.get(i) - minH.get(j) == pList.get(j).get(i + 1)) {
						print.add(j + 1);
						i = j;
						break;
					}
				}
			}

		}
		for (i = print.size() - 1; i > 0; i--) {
			System.out.print(print.get(i) + "->");
		}
		System.out.print(print.get(0));
		return k;
	}

	public void maxFord() {
		maxH.add(0);
		for (int i = 1; i < nodes; i++) {
			maxH.add(-100000);
		}
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < nodes; j++) {
				if (pList.get(i).containsKey(j + 1)) {
					if (maxH.get(j) - maxH.get(i) < pList.get(i).get(j + 1)) {
						maxH.set(j, maxH.get(i) + pList.get(i).get(j + 1));
					}
				}
			}
		}
		int k = 0;
		while (true) {
			k--;
			for (int i = 0; i < nodes; i++) {
				for (int j = 0; j < nodes; j++) {
					if (pList.get(i).containsKey(j + 1)) {
						if (maxH.get(j) - maxH.get(i) < pList.get(i).get(j + 1)) {
							maxH.set(j, maxH.get(i) + pList.get(i).get(j + 1));
							k++;
						}
					}
				}
			}
			if (k <= 0)
				break;
		}

	}

	public int printMaxFord() {
		ArrayList<Integer> print = new ArrayList<>();
		int k = 0;
		print.add(nodes);
		int i = nodes - 1;
		while (i > 0) {
			for (int j = i - 1; j >= 0; j--) {
				if (pList.get(j).containsKey(i + 1)) {
					if (maxH.get(i) - maxH.get(j) == pList.get(j).get(i + 1)) {
						print.add(j + 1);
						i = j;
						break;
					}
				}
			}

		}
		for (i = print.size() - 1; i > 0; i--) {
			System.out.print(print.get(i) + "->");
		}
		System.out.print(print.get(0));
		return k;
	}

	public void minBellmanCalab() {
		for (int i = 0; i < nodes; i++) {
			minBellman.add(new ArrayList<Integer>());
			for (int j = 0; j < nodes; j++) {
				if (i == j) {
					minBellman.get(i).add(0);
				} else if (pList.get(i).containsKey(j + 1)) {
					minBellman.get(i).add(pList.get(i).get(j + 1));
				} else {
					minBellman.get(i).add(100000);
				}
			}
		}
		minBellman.add(new ArrayList<Integer>());
		for (int i = 0; i < nodes; i++) {
			minBellman.get(nodes).add(minBellman.get(i).get(nodes - 1));
		}
		minBellman.add(new ArrayList<Integer>());
		for (int i = 0; i < nodes; i++) {
			Integer[] sums = minBellman.get(nodes).toArray(new Integer[0]);
			for (int j = 0; j < minBellman.get(i).size(); j++) {
				sums[j] += minBellman.get(i).get(j);
			}
			minBellman.get(nodes + 1)
					.add(Arrays.stream(Arrays.stream(sums).mapToInt(Integer::intValue).toArray()).min().getAsInt());
		}
		while (!minBellman.get(minBellman.size() - 1).equals(minBellman.get(minBellman.size() - 2))) {
			minBellman.add(new ArrayList<Integer>());
			for (int i = 0; i < nodes; i++) {
				Integer[] sums = minBellman.get(minBellman.size() - 2).toArray(new Integer[0]);
				for (int j = 0; j < minBellman.get(i).size(); j++) {
					sums[j] += minBellman.get(i).get(j);
				}
				minBellman.get(minBellman.size() - 1)
						.add(Arrays.stream(Arrays.stream(sums).mapToInt(Integer::intValue).toArray()).min().getAsInt());
			}
		}

		int k = printMinBellman() - 1;
		while (k > 0) {
			printMinBellman();
			k--;
		}

	}

	public int printMinBellman() {
		System.out.print("1");
		int i = 0;
		int k = 0;
		while (i != nodes - 1) {
			for (int j = i + 1; j < nodes; j++) {
				if (minBellman.get(minBellman.size() - 1)
						.get(i) == (minBellman.get(i).get(j) + minBellman.get(minBellman.size() - 1).get(j))) {
					System.out.print("->" + (int) (j + 1));
					for (int j2 = j + 1; j2 < nodes; j2++) {
						if (minBellman.get(minBellman.size() - 1).get(
								i) == (minBellman.get(i).get(j2) + minBellman.get(minBellman.size() - 1).get(j2))) {
							minBellman.get(i).set(j, minBellman.get(i).get(j) + 1);
							k++;
						}
					}
					i = j;
				}
			}
		}
		System.out.println();

		return k;
	}

	public void maxBellmanCalab() {
		for (int i = 0; i < nodes; i++) {
			maxBellman.add(new ArrayList<Integer>());
			for (int j = 0; j < nodes; j++) {
				if (i == j) {
					maxBellman.get(i).add(0);
				} else if (pList.get(i).containsKey(j + 1)) {
					maxBellman.get(i).add(pList.get(i).get(j + 1));
				} else {
					maxBellman.get(i).add(-100000);
				}
			}
		}
		maxBellman.add(new ArrayList<Integer>());
		for (int i = 0; i < nodes; i++) {
			maxBellman.get(nodes).add(maxBellman.get(i).get(nodes - 1));
		}
		maxBellman.add(new ArrayList<Integer>());
		for (int i = 0; i < nodes; i++) {
			Integer[] sums = maxBellman.get(nodes).toArray(new Integer[0]);
			for (int j = 0; j < maxBellman.get(i).size(); j++) {
				sums[j] += maxBellman.get(i).get(j);
			}
			maxBellman.get(nodes + 1)
					.add(Arrays.stream(Arrays.stream(sums).mapToInt(Integer::intValue).toArray()).max().getAsInt());
		}
		while (!maxBellman.get(maxBellman.size() - 1).equals(maxBellman.get(maxBellman.size() - 2))) {
			maxBellman.add(new ArrayList<Integer>());
			for (int i = 0; i < nodes; i++) {
				Integer[] sums = maxBellman.get(maxBellman.size() - 2).toArray(new Integer[0]);
				for (int j = 0; j < maxBellman.get(i).size(); j++) {
					sums[j] += maxBellman.get(i).get(j);
				}
				maxBellman.get(maxBellman.size() - 1)
						.add(Arrays.stream(Arrays.stream(sums).mapToInt(Integer::intValue).toArray()).max().getAsInt());
			}
		}

		int k = printMaxBellman() - 1;
		while (k > 0) {
			printMaxBellman();
			k--;
		}

	}

	public int printMaxBellman() {
		System.out.print("1");
		int i = 0;
		int k = 0;
		while (i != nodes - 1) {
			for (int j = i + 1; j < nodes; j++) {
				if (maxBellman.get(maxBellman.size() - 1)
						.get(i) == (maxBellman.get(i).get(j) + maxBellman.get(maxBellman.size() - 1).get(j))) {
					System.out.print("->" + (int) (j + 1));
					for (int j2 = j + 1; j2 < nodes; j2++) {
						if (maxBellman.get(maxBellman.size() - 1).get(
								i) == (maxBellman.get(i).get(j2) + maxBellman.get(maxBellman.size() - 1).get(j2))) {
							maxBellman.get(i).set(j, maxBellman.get(i).get(j) + 1);
							k++;
						}
					}
					i = j;
				}
			}
		}
		System.out.println();

		return k;
	}

	public void getIncMatrix() {
		System.out.print("Матрица инцедентности данного графа:\n    ");
		for (int i = 0; i < nodes; i++) {
			System.out.print(" x" + (i + 1));
		}
		System.out.println("\n");
		for (int i = 0; i < arcs; i++) {
			System.out.print("l" + (i + 1) + " ");
			for (int j = 0; j < nodes; j++) {
				System.out.printf("%3d", incMatrix.get(i).get(j));
			}
			System.out.println();
		}
	}

	public void setIncMatrix() {
		System.out.print("Вводите матрицу инцидентности:\n ");
		for (int i = 0; i < arcs; i++) {
			incMatrix.add(new ArrayList<>());
			for (int j = 0; j < nodes; j++) {
				incMatrix.get(i).add(sc.nextInt());
			}
		}
		this.fromIncMatrixToAdjList();
		this.fromAdjListToAdjMatrix();
	}

	public void getAdjMatrix() {
		System.out.print("Матрица смежности данного графа:\n    ");
		for (int i = 0; i < nodes; i++) {
			System.out.print(" x" + (i + 1));
		}
		System.out.println("\n");
		for (int i = 0; i < nodes; i++) {
			System.out.print("x" + (i + 1) + " ");
			for (int j = 0; j < nodes; j++) {
				System.out.printf("%3d", adjMatrix.get(i).get(j));
			}
			System.out.println();
		}
	}

	public void setAdjMatrix() {
		System.out.print("Вводите матрицу смежности:\n ");
		for (int i = 0; i < nodes; i++) {
			adjMatrix.add(new ArrayList<>());
			for (int j = 0; j < nodes; j++) {
				adjMatrix.get(i).add(sc.nextInt());
			}
		}
		this.fromAdjMatrixToAdjList();
		this.fromAdjListToIncMatrix();
	}

	public void getAdjList() {
		System.out.println("Список смежности данного графа:");
		for (int i = 0; i < nodes; i++) {
			System.out.print((i + 1) + ": ");
			for (int j = 0; j < adjList.get(i).size(); j++) {
				if (j != (adjList.get(i).size() - 1)) {
					System.out.print(adjList.get(i).get(j) + ", ");
				} else {
					System.out.print(adjList.get(i).get(j) + "\n");
				}
			}
		}
	}

	public void setAdjList() {
		System.out.print("Вводите список смежности:\n");
		for (int i = 0; i < nodes; i++) {
			adjList.add(new ArrayList<>());
			System.out.print(" " + (i + 1) + ": ");
			int value = sc.nextInt();
			while (value != 0) {
				adjList.get(i).add(value);
				value = sc.nextInt();
			}
		}
		this.fromAdjListToAdjMatrix();
		this.fromAdjListToIncMatrix();
	}

	public void fromAdjMatrixToAdjList() {

		for (int i = 0; i < nodes; i++) {
			adjList.add(new ArrayList<>());
			for (int j = 0; j < nodes; j++) {
				if (adjMatrix.get(i).get(j) == 1) {
					adjList.get(i).add(j + 1);
				}
			}
		}

	}

	public void fromAdjListToAdjMatrix() {
		for (int i = 0; i < nodes; i++) {
			adjMatrix.add(new ArrayList<>());
			for (int j = 0; j < nodes; j++) {
				adjMatrix.get(i).add(0);
			}
			for (int j = 0; j < adjList.get(i).size(); j++) {
				if (adjList.get(i).get(j) == (i + 1)) {
					adjMatrix.get(i).set(adjList.get(i).get(j) - 1, 1);
				} else {
					adjMatrix.get(i).set(adjList.get(i).get(j) - 1, 1);
				}
			}
		}

	}

	public void fromIncMatrixToAdjList() {

		for (int i = 0; i < nodes; i++) {
			adjList.add(new ArrayList<>());
		}
		for (int i = 0; i < arcs; i++) {
			int k = -1;
			for (int j = 0; j < nodes; j++) {
				if (incMatrix.get(i).get(j) == -1) {
					k = j;
				}
				if (incMatrix.get(i).get(j) == 2) {
					adjList.get(j).add(j + 1);
				}
			}
			if (k >= 0) {
				for (int j = 0; j < nodes; j++) {
					if (incMatrix.get(i).get(j) == 1) {
						adjList.get(k).add(j + 1);
					}
				}
				k = -1;
			}

		}

	}

	public void fromAdjListToIncMatrix() {
		int k = 0;
		for (int i = 0; i < arcs; i++) {
			incMatrix.add(new ArrayList<>());
			for (int j = 0; j < nodes; j++) {
				incMatrix.get(i).add(0);
			}
		}
		for (int i = 0; i < nodes; i++) {
			for (int j = 0; j < adjList.get(i).size(); j++) {
				if ((i + 1) == adjList.get(i).get(j)) {
					incMatrix.get(k).set(i, 2);
				} else {
					incMatrix.get(k).set(i, -1);
					incMatrix.get(k).set(adjList.get(i).get(j) - 1, 1);
				}
				k++;
			}
		}
	}

	// обход графа в глубину
	public void depthFirstSearch(int start) {
		visitedDFS[start] = true;
		System.out.print(start + " ");

		Iterator<Integer> iterator = adjList.get(start - 1).listIterator();
		while (iterator.hasNext()) {
			int node = iterator.next();
			if (!visitedDFS[node]) {
				depthFirstSearch(node);
			}
		}

	}

	// обход графа в ширину
	public void breadthFirstSearch(int start) {
		seen[start] = true;
		queue.add(start);
		while (true) {
			for (int i = 0; i < adjList.get(start - 1).size(); i++) {
				if (seen[adjList.get(start - 1).get(i)] != true) {
					queue.add(adjList.get(start - 1).get(i));
					seen[adjList.get(start - 1).get(i)] = true;
				}
				visitedBFS[start] = true;
			}
			System.out.print(queue.pollFirst() + " ");
			if (!queue.isEmpty()) {
				start = queue.peekFirst();
			} else {
				break;
			}
		}
		System.out.println();
	}

}
