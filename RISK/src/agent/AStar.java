package agent;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;
import map.Continent;
import map.SemiContinent;
import map.Territory;

public class AStar extends Agent {

	public AStar(Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	private int h(AgentState x) {
		int h = 0;
		for (SemiContinent sc : x.agent.semiContinents) {
			if (sc.getDiff() == 0) {
				h++;
			}
		}
		return h;
	}

	@Override
	public void placeArmies() {
		super.placeArmies();

		if (bonusArmies == 0) {
			return;
		}

		Map<Territory, Integer> allAttacksMap = new HashMap<>();
		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				// searching for the territories which can't attack any neighbors.
				if (!territories.contains(neighbor) && (territory.getArmies() - neighbor.getArmies()) < 1) {
					int value = 0;
					if (allAttacksMap.containsKey(territory)) {
						value = allAttacksMap.get(territory);
					}
					allAttacksMap.put(territory, value + 1);
				}
			}
		}
		// let's sort this map by values first
		// Map<Territory, Integer> sorted = allAttacksMap
		// .entrySet()
		// .stream()
		// .sorted(comparingByValue())
		// .collect(
		// toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
		// LinkedHashMap::new));
		Map.Entry<Territory, Integer> min = null;
		for (Map.Entry<Territory, Integer> entry : allAttacksMap.entrySet()) {
			if (min == null || min.getValue() > entry.getValue()) {
				min = entry;
			}
		}
		if (min != null) {
			Territory theOne = min.getKey();
			theOne.setArmies(theOne.getArmies() + bonusArmies);
		}
	}

	@Override
	public void attack() {
		super.attack();

		PriorityQueue<AgentState> frontier = new PriorityQueue<>();
		AgentState goalState = new AgentState();
		AgentState initState = new AgentState(); // with null parent.
		Set<AgentState> explored = new HashSet<>();
		frontier.add(initState);
		while (!frontier.isEmpty()) {
			AgentState currentState = frontier.poll();
			explored.add(currentState);
			if (currentState.equals(goalState)) {
				Stack<AgentState> path = new Stack<>();
				while (currentState.parent != null) {
					path.push(currentState);
					currentState = currentState.parent;
				}
				/// save that path...
			}
			for (AgentState neighbor : currentState.getNeighbors()) {
				int new_hx = h(neighbor), new_gx = currentState.gx + 1;
				if (frontier.contains(neighbor)) {
					if (neighbor.fx() > new_hx + new_gx) {
						frontier.remove(neighbor);
					}
				}
				if (!frontier.contains(neighbor) && !explored.contains(neighbor)) {
					neighbor.hx = new_hx;
					neighbor.gx = new_gx;
					neighbor.parent = currentState;
					frontier.add(neighbor);
				}
			}
		}
	}
}