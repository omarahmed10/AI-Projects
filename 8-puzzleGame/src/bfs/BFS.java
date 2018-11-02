package bfs;

import main.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import main.State;

public class BFS {
	public static int MaxDepth = 0;

	public static Path search(State initState, State goalState) {
		MaxDepth = 0;
		Queue<Path> frontier = new LinkedList<>();
		Set<State> explored = new HashSet<State>();
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
				if (!frontier.contains(new Path(neighbor)) && !explored.contains(neighbor)) {
//					System.out.println(neighbor+"\n==================");
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
