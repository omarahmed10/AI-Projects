package a_star;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Stack;

import main.State;

public class AStar {
	int size = 3;

	public float manhattanScore(State state, State goal) {
		float cost = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int cell = goal.getPuzzle()[i][j];

				for (int x = 0; x < size; x++) {
					for (int y = 0; y < size; y++) {
						if (state.getPuzzle()[x][y] == cell) {
							cost += Math.abs(x - i) + Math.abs(y - j);
							break;
						}
					}
				}
			}
		}
		return cost;
	}

	public float euclideanScore(State state, State goal) {
		float cost = 0;
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				int cell = goal.getPuzzle()[i][j];

				for (int x = 0; x < size; x++) {
					for (int y = 0; y < size; y++) {
						if (state.getPuzzle()[x][y] == cell) {
							cost += Math.sqrt(Math.pow((x - i), 2) + Math.pow((y - j), 2));
							break;
						}
					}
				}
			}
		}
		return cost;
	}

	public void AStarSearchManhattan(State initial, State goal) {
		long startTime = System.currentTimeMillis();

		int depth = 0;

		initial.g = 0;
		initial.parent = null;
		PriorityQueue<State> frontier = new PriorityQueue<>(new Comparator<State>() {
			@Override
			public int compare(State s1, State s2) {
				if (s1.getCost() < s2.getCost())
					return -1;
				if (s1.getCost() > s2.getCost())
					return 1;
				return 0;
			}
		});
		initial.setCost(manhattanScore(initial, goal));
		frontier.add(initial);

		List<State> explored = new ArrayList<>();

		State finalState = null;
		while (!frontier.isEmpty()) {
			State state = frontier.poll();
			explored.add(state);

			if (state.equals(goal)) {
				finalState = state;
				break;
			}

			List<State> neighbors = state.getNeighbors();

			for (State neighbor : neighbors) {
				if (!frontier.contains(neighbor) && !explored.contains(neighbor)) {
					neighbor.g = state.g + 1;
					depth = Math.max(depth, neighbor.g);
					neighbor.parent = state;
					neighbor.setCost(manhattanScore(neighbor, goal));
					frontier.add(neighbor);
				}
			}
		}

		Stack<State> finalPath = new Stack<State>();
		float pathCost = 0;
		while (finalState != null) {
			pathCost += finalState.getCost();
			finalPath.push(finalState);
			finalState = finalState.parent;
		}
		while (!finalPath.isEmpty()) {
			System.out.println(finalPath.pop());
			System.out.println("-----");
		}
		System.out.println("Time taken = " + (System.currentTimeMillis() - startTime));
		System.out.println("Path cost = " + pathCost);
		System.out.println("Path depth = " + depth);

	}

	public void AStarSearchEuclidean(State initial, State goal) {
		long startTime = System.currentTimeMillis();

		int depth = 0;

		initial.g = 0;
		initial.parent = null;
		PriorityQueue<State> frontier = new PriorityQueue<>(new Comparator<State>() {
			@Override
			public int compare(State s1, State s2) {
				if (s1.getCost() < s2.getCost())
					return -1;
				if (s1.getCost() > s2.getCost())
					return 1;
				return 0;
			}
		});
		initial.setCost(manhattanScore(initial, goal));
		frontier.add(initial);

		List<State> explored = new ArrayList<>();

		State finalState = null;
		while (!frontier.isEmpty()) {
			State state = frontier.poll();
			explored.add(state);

			if (state.equals(goal)) {
				finalState = state;
				break;
			}
			List<State> neighbors = state.getNeighbors();

			for (State neighbor : neighbors) {
				if (!frontier.contains(neighbor) && !explored.contains(neighbor)) {
					neighbor.g = state.g + 1;
					depth = Math.max(depth, neighbor.g);
					neighbor.parent = state;
					neighbor.setCost(euclideanScore(neighbor, goal));
					frontier.add(neighbor);
				}
			}
		}

		Stack<State> finalPath = new Stack<State>();
		float pathCost = 0;
		while (finalState != null) {
			pathCost += finalState.getCost();
			finalPath.push(finalState);
			finalState = finalState.parent;
		}
		while (!finalPath.isEmpty()) {
			System.out.println(finalPath.pop());
			System.out.println("-----");
		}
		System.out.println("Time taken = " + (System.currentTimeMillis() - startTime));
		System.out.println("Path cost = " + pathCost);
		System.out.println("Path depth = " + depth);

	}

	public static void main(String[] args) {
		AStar astar = new AStar();
		State initial = new State(new int[] { 1, 4, 5, 3, 2, 0, 7, 6, 8 });
		State goal = new State(new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8 });

		astar.AStarSearchEuclidean(initial, goal);
		astar.AStarSearchManhattan(initial, goal);
	}
}