package agent;

import java.util.ArrayList;
import java.util.List;

import map.Continent;
import map.Territory;

public class Agent {
	protected List<Territory> territories;
	protected Continent[] semiContinents;
	protected int bonusArmies;

	public Agent(int contNum) {
		territories = new ArrayList<>();
		initSemiContinents(contNum);
	}

	private void initSemiContinents(int contNum) {
		semiContinents = new Continent[contNum];
		for (int i = 0; i < semiContinents.length; i++) {
			semiContinents[i] = new Continent(i, 0);
		}
	}

	public static Agent agentFactory(int id, int contNum) {
		if (id == 0)
			return new Aggressive(contNum);
		else if (id == 0)
			return new AStar(contNum);
		else if (id == 0)
			return new Greedy(contNum);
		else if (id == 0)
			return new Pacifist(contNum);
		else if (id == 0)
			return new RtAStar(contNum);
		else
			return new Human(contNum);
	}

	public void placeArmies() {

	}

	public void attack(List<Territory> Allterritories) {

	}

	public List<Territory> possAttTerrs() {
		List<Territory> possAttTerrs = new ArrayList<>();

		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor)
						&& (territory.getArmies() - neighbor.getArmies()) > 1) {
					possAttTerrs.add(neighbor);
				}
			}
		}

		return possAttTerrs;
	}

	public void addTerritory(Territory territory) {
		territories.add(territory);
		semiContinents[territory.getContinent().getId()].addTerritory(territory);
	}

	public void removeTerritory(Territory territory) {
		territories.remove(territory);
		semiContinents[territory.getContinent().getId()].removeTerritory(territory);
	}

	public void setBonusArmies(int bonusArmies) {
		this.bonusArmies = bonusArmies;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public Continent[] getSemiContinents() {
		return semiContinents;
	}

}
