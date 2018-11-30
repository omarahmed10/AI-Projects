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

public class RtAStar extends Agent {
	private boolean pathCreated = false;

	public RtAStar(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
	}

	public RtAStar(Agent clone) {
		super(clone);
	}

	@Override
	public void setEnemy(Agent enemy) {
		super.setEnemy(enemy);
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

	// Most Damage
	@Override
	public ArmyPlacement placeArmies() {
		
		if (bonusArmies <= 0)
			return null;
		
		// searching for the territories which can do the most damage.
		Map<Territory, Integer> allAttacksMap = new HashMap<>();
		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor)) {
					int value = 0;

					if (neighbor.getContinent().getId() != territory.getContinent().getId()) {
						if ((territory.getArmies() - neighbor.getArmies()) > 1) {
							value = territory.getArmies() - neighbor.getArmies() + bonusArmies
									+ neighbor.getContinent().getValue();
						} else {
							value = territory.getArmies() - neighbor.getArmies() + bonusArmies;
						}
					} else {
						value = territory.getArmies() - neighbor.getArmies() + bonusArmies;
					}
					if (allAttacksMap.containsKey(territory)) {
						if (value > allAttacksMap.get(territory)) {
							allAttacksMap.put(territory, value);
						}
					} else {
						allAttacksMap.put(territory, value);
					}
				}

			}
		}
		// searching for the territories which have the max number of attacks
		// which
		// cannot be done.
		// the most one in danger.
		Map.Entry<Territory, Integer> max = null;
		for (Map.Entry<Territory, Integer> entry : allAttacksMap.entrySet()) {
			if (max == null || max.getValue() < entry.getValue()) {
				max = entry;
			}
		}
		ArmyPlacement ap = null;
		if (max != null) {
			Territory theOne = max.getKey();
			theOne.setArmies(theOne.getArmies() + bonusArmies);
			
			ap = new ArmyPlacement();
			ap.terrID = theOne.getId();
			ap.armyCount = theOne.getArmies();
			ap.bonusAdded = bonusArmies;
		}
		bonusArmies = 0;
		
		return ap;
	}

	public Stack<AgentState> path = new Stack<>();

	@Override
	public void attack() {
		super.attack();
	}

	public void buildPath(Agent agentPassive) {
		PriorityQueue<AgentState> frontier = new PriorityQueue<>();
		AgentState initState = new AgentState(this, agentPassive, null);
		initState.hx = h(initState);
		Set<AgentState> explored = new HashSet<>();
		frontier.add(initState);
		AgentState lastExplored = null;
		while (!frontier.isEmpty()) {
			AgentState currentState = frontier.poll();
			explored.add(currentState);

			if (currentState.aiAgent.isWinner()) {
				while (!currentState.equals(lastExplored)) {
					path.push(currentState);
					currentState = currentState.parent;
				}
				/// save that path...
				return;
			}
			// Pruning
			for (AgentState s : frontier) {
				if (s.gx < currentState.gx && s.hx > currentState.hx) {
					frontier.remove(s);
				}
			}
			if (frontier.isEmpty()) {
				AgentState actionState = new AgentState(currentState);
				while (actionState != lastExplored) {
					path.push(currentState);
					actionState = actionState.parent;
				}
				lastExplored = new AgentState(currentState);
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