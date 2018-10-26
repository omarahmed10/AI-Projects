package main;

import bfs.BFS;

public class Main {
	public static void main(String[] args) {
		int[] parentSeq = { 1, 2, 5, 3, 4, 0, 6, 7, 8 };
		State parent = new State(parentSeq);
		int[] goalSeq = { 0, 1, 2, 3, 4, 5, 6, 7, 8 };
		State goal = new State(goalSeq);
		Path searchPath = BFS.search(parent, goal);
		for(State s : searchPath.getStates()) {
			System.out.print(s);
			System.out.println("  |");
			System.out.println("  |");
			System.out.println("  |");
			System.out.println("  V");
		}
	}
}
