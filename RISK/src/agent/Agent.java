package agent;

import java.util.ArrayList;
import java.util.List;

import map.Continent;
import map.SemiContinent;
import map.Territory;

public class Agent {
	protected List<Continent> continents;
	protected List<Territory> allTerritories;
	protected List<Territory> territories;
	protected SemiContinent[] semiContinents;
	protected int bonusArmies;
	protected Agent enemy;
	protected int searchExp;

	public Agent(Agent enemy, List<Continent> continents,
			List<Territory> allTerritories) {
		this.enemy = enemy;
		this.continents = continents;
		this.allTerritories = allTerritories;
		initSemiContinents();
		territories = new ArrayList<>();
	}

	private void initSemiContinents() {
		semiContinents = new SemiContinent[continents.size()];
		for (int i = 0; i < semiContinents.length; i++) {
			semiContinents[i] = new SemiContinent(i, continents.get(i).getValue());
		}
	}

	public static Agent agentFactory(int id, Agent enemy,
			List<Continent> continents, List<Territory> allTerritories) {
		if (id == 0)
			return new Aggressive(enemy, continents, allTerritories);
		else if (id == 1)
			return new AStar(enemy, continents, allTerritories);
		else if (id == 2)
			return new Greedy(enemy, continents, allTerritories);
		else if (id == 3)
			return new Human(enemy, continents, allTerritories);
		else if (id == 4)
			return new Pacifist(enemy, continents, allTerritories);
		else if (id == 5)
			return new RtAStar(enemy, continents, allTerritories);
		else
			return new Passive(enemy, continents, allTerritories);
	}

	public void placeArmies() {

	}

	public void attack() {

	}

	public List<Territory> possAttTerrs() {
		List<Territory> possAttTerrs = new ArrayList<>();

		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor)
						&& (territory.getArmies() - neighbor.getArmies()) > 1
						&& !possAttTerrs.contains(neighbor)) {
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

	public void doAttack(Territory agentTerritory, Territory enemyTerritory,
			int attackArmies) {
		agentTerritory.setArmies(agentTerritory.getArmies() - attackArmies);
		enemyTerritory.setArmies(attackArmies - enemyTerritory.getArmies());

		// May be the territory is neutral
		if (enemyTerritory.getOwner() != null)
			enemyTerritory.getOwner().removeTerritory(enemyTerritory);

		enemyTerritory.assignOwner(this);
		addTerritory(enemyTerritory);
	}

}
