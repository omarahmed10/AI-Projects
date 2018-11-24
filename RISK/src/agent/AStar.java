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

	public AStar(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	public AStar(Agent clone) {
		super(clone);
	}

	private int h(AgentState x) {
		int h = 0;
		for (SemiContinent sc : x.aiAgent.semiContinents) {
			if (sc.getDiff() != 0) {
				h++;
			}
		}
		return h;
	}

	// To be changed...................................
	@Override
	public void placeArmies() {
		super.placeArmies();
		// searching for the territories which can't attack any neighbors.
		Map<Territory, Integer> allAttacksMap = new HashMap<>();
		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor) && (territory.getArmies() - neighbor.getArmies()) < 1) {
					int value = 0;
					if (allAttacksMap.containsKey(territory)) {
						value = allAttacksMap.get(territory);
					}
					allAttacksMap.put(territory, value + 1);
				}
			}
		}
		// searching for the territories which have the max number of attacks which
		// cannot be done.
		// the most one in danger.
		Map.Entry<Territory, Integer> max = null;
		for (Map.Entry<Territory, Integer> entry : allAttacksMap.entrySet()) {
			if (max == null || max.getValue() < entry.getValue()) {
				max = entry;
			}
		}
		if (max != null) {
			Territory theOne = max.getKey();
			theOne.setArmies(theOne.getArmies() + bonusArmies);
		}
		bonusArmies = 0;
	}

	public Stack<AgentState> path = new Stack<>();

	@Override
	public void attack() {
		super.attack();
	}

	public void buildPath(Agent agentPassive) {
		PriorityQueue<AgentState> frontier = new PriorityQueue<>();
		// this here is our A* agent whose territories are predefined from the initial
		// position file
		// and his enemy is preinitialized too.
		AgentState initState = new AgentState(this, agentPassive, null);
		initState.hx = h(initState);
		Set<AgentState> explored = new HashSet<>();
		frontier.add(initState);

		while (!frontier.isEmpty()) {
			AgentState currentState = frontier.poll();
			explored.add(currentState);

			if (currentState.aiAgent.isWinner()) {
				while (currentState != null) {
					path.push(currentState);
					currentState = currentState.parent;
				}
				/// save that path...
				return;
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