package agent;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

import map.Continent;
import map.Territory;

public class AStar extends Agent {

	public AStar(Agent enemy, List<Continent> continents,
			List<Territory> allTerritories) {
		super(enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}
	
	private int h(AgentState x) {
		return 0;
	}
	
	@Override
	public void attack() {
		PriorityQueue<AgentState> frontier = new PriorityQueue<>(new Comparator<AgentState>() {
			@Override
			public int compare(AgentState state1, AgentState state2) {
				return 0;
			}
		});
		AgentState initState = new AgentState(); // with null parent.
		Set<AgentState> explored = new HashSet<>();
		frontier.add(initState);
		while (!frontier.isEmpty()) {
			AgentState currentState = frontier.poll();
			explored.add(currentState);
			if (currentState.equals(goalState)) {
				Stack<AgentState> path = new Stack<>();
				while(currentState.parent != null) {
					path.push(currentState);
					currentState = currentState.parent;
				}
				/// save that path...
			}
			for (AgentState neighbor : currentState.getNeighbors()) {
				if (!frontier.contains(new Path(neighbor)) && !explored.contains(neighbor)) {
					neighbor.setCost(h(neighbor));
					frontier.add(neighbor);
				}
			}
		}
	}
}
