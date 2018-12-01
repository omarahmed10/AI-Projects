package main;

import a_star.AStar;
import bfs.BFS;
import dfs.DFS;

public class Main {
	public static void main(String[] args) {
		State parent = new State(new int[]{ 1, 2, 5, 3, 4, 0, 6, 7, 8 });
//		State parent = new State(new int[] { 1, 4, 2, 6, 5, 8, 7, 3, 0 });
		State goal = new State(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });


		long startTime = System.currentTimeMillis();
		System.out.println("\n>>>>>>>>>>>>> BFS Search");
		Path bfsSearchPath = BFS.search(parent, goal);
		if (bfsSearchPath == null)
			System.out.println("could't reach the goal");
		else {
			for (State s : bfsSearchPath.getStates()) {
				System.out.println(s);
				System.out.println("-----");
			}
			System.out.println("cost of path = " + bfsSearchPath.getCost());
			System.out.println("search depth = " + BFS.MaxDepth);
		}
		long bfsEndTime = System.currentTimeMillis();
		System.out.println("Time taken = " + (bfsEndTime - startTime) + " ms");

		
///////////////////////////////////////////////////////////////////////////////////////////////////////////

		System.out.println("\n>>>>>>>>>>>>> DFS Search");
		Path dfsSearchPath = DFS.search(parent, goal);
		if (dfsSearchPath == null)
			System.out.println("could't reach the goal");
		else {
			for (State s : dfsSearchPath.getStates()) {
				System.out.println(s);
				System.out.println("-----");
			}
			System.out.println("cost of path = " + dfsSearchPath.getCost());
			System.out.println("search depth = " + DFS.MaxDepth);
		}
		long dfsEndTime = System.currentTimeMillis();
		System.out.println("Time taken = " + (dfsEndTime - bfsEndTime) + " ms");
		

///////////////////////////////////////////////////////////////////////////////////////////////////////////

		AStar astar = new AStar();
		astar.AStarSearchEuclidean(parent, goal);
		astar.AStarSearchManhattan(parent, goal);
	}
}
