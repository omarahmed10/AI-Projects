package agent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

import map.Continent;
import map.SemiContinent;
import map.Territory;

public class RtAStar extends Agent {

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
		// add all continents value which the opponent has.
		for (SemiContinent sc : x.passiveAggent.semiContinents.values()) {
			if (!sc.getTerritories().isEmpty()) {
				h += sc.getValue();
			}
		}
		// add all territories armies which the opponent has.
		for (Territory tr : x.passiveAggent.territories) {
			h += tr.getArmies();
		}
		h += x.passiveAggent.bonusArmies;
		return h;
	}

	@Override
	public Action move() {
		if (path == null || path.isEmpty())
			return null;

		AgentState state = path.poll();
		Action action = new Action();
		action.agentPlacement = state.agentPlacement;
		action.passivePlacement = state.passivePlacement;

		if (!path.isEmpty()) {
			Attack a = AgentState.getAttack(path.peek().attack, state);
			System.out.println(a);
			action.attack = a;
		}
		return action;
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

					if ((territory.getArmies() - neighbor.getArmies()) + bonusArmies > 1) {
						value = territory.getArmies() - neighbor.getArmies() + bonusArmies
								+ neighbor.getContinent().getValue();
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

	public Queue<AgentState> path = new LinkedList<>();

	@Override
	public void buildPath(Agent agentPassive) {
		PriorityQueue<AgentState> frontier = new PriorityQueue<>(new Comparator<AgentState>() {
			@Override
			public int compare(AgentState o1, AgentState o2) {
				if (o1.fx() > o2.fx())
					return 1;
				if (o1.fx() < o2.fx())
					return -1;
				return 0;
			}
		});
		AgentState initState = new AgentState(this, agentPassive, null);
		initState.hx = h(initState);
		List<AgentState> explored = new ArrayList<>();
		frontier.add(initState);
		AgentState lastExplored = null;
		while (!frontier.isEmpty()) {
			AgentState currentState = frontier.poll();
			explored.add(currentState);

			if (currentState.aiAgent.isWinner()) {
				Stack<AgentState> visitedStates = new Stack<>();
				while (!currentState.equals(lastExplored)) {
					visitedStates.add(currentState);
					currentState = currentState.parent;
				}
				while (!visitedStates.isEmpty()) {
					path.add(visitedStates.pop());
				}
				/// save that path...
				L = path.size() - 1;
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
				Stack<AgentState> visitedStates = new Stack<>();
				while (actionState != lastExplored) {
					visitedStates.push(currentState);
					actionState = actionState.parent;
				}
				while (!visitedStates.isEmpty()) {
					path.add(visitedStates.pop());
				}
				lastExplored = currentState;
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
					T = Math.max(T, new_gx);
					neighbor.parent = currentState;
					frontier.add(neighbor);
				}
			}
		}
	}

	@Override
	public boolean solutionFound() {
		return path != null && !path.isEmpty();
	}
}