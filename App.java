package main;

import java.util.Scanner;

public class App {
	

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		
		int nodes;
		System.out.print("Введите колличество вершин графа:\n ");
		nodes = sc.nextInt();
		
		Graph graph = new Graph();
		graph.setNodes(nodes);

		graph.setPList();
		graph.getPList();
		
		graph.getMaxFlow();	
		sc.close();
	}
	
	

	// МИ -1 1 0 0 0 0 1 -1 0 0 0 -1 0 1 0 1 0 0 -1 0 -1 0 0 0 1 0 0 0 1 -1 0 0 2 0 0
	// МС 0 1 0 0 1 0 0 0 1 0 0 1 1 0 0 1 0 0 0 0 0 0 0 1 0
	//5 7 2 5 0 4 0 2 3 0 1 0 4 0 1
	
	/*
	 1 2 = 30
	 1 3 = 18
	 1 4 = 24
	 2 5 = 31
	 2 7 = 9
	 3 5 = 23
	 3 7 = 4
	 4 6 = 26
	 4 7 = 2
	 5 8 = 42
	 6 5 = 8
	 6 8 = 14
	 7 8 = 25
	 */
	
	/*
	 1 2 = 29
	 1 3 = 21
	 1 4 = 31
	 2 5 = 31
	 2 7 = 20
	 3 5 = 15
	 3 7 = 7
	 4 6 = 28
	 4 7 = 4
	 5 8 = 30
	 6 5 = 7
	 6 8 = 18
	 7 8 = 26
	 */

}
