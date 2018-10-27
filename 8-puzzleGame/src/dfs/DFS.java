package dfs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import main.Path;
import main.State;

public class DFS {
	public static int MaxDepth = 0;

	public static Path search(State initState, State goalState) {
		MaxDepth = 0;
		Stack<Path> frontier = new Stack<>();
		Set<State> explored = new HashSet<>();
		Path initPath = new Path(initState);
		frontier.push(initPath);
		while (!frontier.isEmpty()) {
			Path path = frontier.pop();
			State currentState = path.lastState();
			explored.add(currentState);
			if (currentState.equals(goalState)) {
				return path;
			}
			List<State> neighbour = new ArrayList<>(currentState.getNeighbors());
			Collections.reverse(neighbour);
			for (State neighbor : neighbour) {
				if (!path.contains(neighbor) && !explored.contains(neighbor)) {
					// System.out.println(neighbor+"\n==================");
					Path newPath = new Path(path);
					newPath.addState(neighbor);
					MaxDepth = Math.max(MaxDepth, newPath.getCost());
					frontier.push(newPath);
				}
			}
		}
		return null;
	}
}
