package dfs;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import main.Path;
import main.State;

public class DFS {
    public static Path search(State initState, State goalState) {
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
            for (State neighbor : currentState.getNeighbors()) {
                if (!path.contains(neighbor) && !explored.contains(neighbor)) {
                    // System.out.println(neighbor+"\n==================");
                    Path newPath = new Path(path);
                    newPath.addState(neighbor);
                    frontier.push(newPath);
                }
            }
        }
        return null;
    }
}
