package dfs;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import main.Path;
import main.State;

public class DFS {
    private int runningTime;
    private int searchDep;

    public static Path search(State initState, State goalState) {
        int nodesExp, cost;
        Stack<Path> frontier = new Stack<>();
        Set<State> explored = new HashSet<>();
        Path initPath = new Path(initState);
        frontier.push(initPath);
        nodesExp = 1;
        while (!frontier.isEmpty()) {
            Path path = frontier.pop();
            State currentState = path.lastState();
            explored.add(currentState);
            if (currentState.equals(goalState)) {
                cost = path.getStates().size() - 1;
                System.out.println("Cost = " + cost);
                System.out.println("Nodes expanded = " + nodesExp);
                return path;
            }
            for (State neighbor : currentState.getNeighbors()) {
                if (!path.contains(neighbor) && !explored.contains(neighbor)) {
                    // System.out.println(neighbor+"\n==================");
                    Path newPath = new Path(path);
                    newPath.addState(neighbor);
                    frontier.push(newPath);
                    nodesExp += 1;
                }
            }
        }
        System.out.println("hi");
        return null;
    }

}
