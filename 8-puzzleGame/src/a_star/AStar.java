package a_star;

import main.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import main.State;

public class AStar {
	private static int size = 3;

	public static float manhattanScore(State state, State goal) {
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

	public static float euclideanScore(State state, State goal) {
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

	public static int MaxDepth = 0;
	
	public static Path searchWithEclidean(State initState, State goalState) {
		MaxDepth = 0;
		PriorityQueue<Path> frontier = new PriorityQueue<>(new Comparator<Path>() {
			@Override
			public int compare(Path p1, Path p2) {
				if (p1.lastState().getCost() < p2.lastState().getCost())
					return -1;
				if (p1.lastState().getCost() > p2.lastState().getCost())
					return 1;
				return 0;
			}
		});
		initState.setCost(euclideanScore(initState, goalState));
		Set<State> explored = new HashSet<>();
		Path initPath = new Path(initState);
		frontier.add(initPath);
		while (!frontier.isEmpty()) {
			Path path = frontier.poll();
			State currentState = path.lastState();
			explored.add(currentState);
			if (currentState.equals(goalState)) {
				return path;
			}
			for (State neighbor : currentState.getNeighbors()) {
				if (!frontier.contains(path) && !explored.contains(neighbor)) {
					neighbor.setCost(euclideanScore(neighbor, goalState));
					Path newPath = new Path(path);
					newPath.addState(neighbor);
					MaxDepth = Math.max(MaxDepth, newPath.getCost());
					frontier.add(newPath);
				}
			}
		}
		return null;
	}
	
	public static Path searchWithManhattan(State initState, State goalState) {
		MaxDepth = 0;
		PriorityQueue<Path> frontier = new PriorityQueue<>(new Comparator<Path>() {
			@Override
			public int compare(Path p1, Path p2) {
				if (p1.lastState().getCost() < p2.lastState().getCost())
					return -1;
				if (p1.lastState().getCost() > p2.lastState().getCost())
					return 1;
				return 0;
			}
		});
		initState.setCost(manhattanScore(initState, goalState));
		Set<State> explored = new HashSet<>();
		Path initPath = new Path(initState);
		frontier.add(initPath);
		while (!frontier.isEmpty()) {
			Path path = frontier.poll();
			State currentState = path.lastState();
			explored.add(currentState);
			if (currentState.equals(goalState)) {
				return path;
			}
			for (State neighbor : currentState.getNeighbors()) {
				if (!frontier.contains(path) && !explored.contains(neighbor)) {
					neighbor.setCost(manhattanScore(neighbor, goalState));
					Path newPath = new Path(path);
					newPath.addState(neighbor);
					MaxDepth = Math.max(MaxDepth, newPath.getCost());
					frontier.add(newPath);
				}
			}
		}
		return null;
	}
	
}
