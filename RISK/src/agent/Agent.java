package agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import map.Continent;
import map.SemiContinent;
import map.Territory;

public class Agent {
	protected List<Continent> continents;
	protected List<Territory> allTerritories;
	protected List<Territory> territories;
	protected List<SemiContinent> semiContinents;
	protected int bonusArmies;
	protected Agent enemy;
	protected int searchExp;

	public Agent(Agent clone) {
		Territory.clone(this.territories, clone.territories);
		Territory.clone(this.allTerritories, clone.allTerritories);
		this.continents = clone.continents;
		SemiContinent.clone(this.semiContinents, clone.semiContinents);
		this.bonusArmies = clone.bonusArmies;
		this.searchExp = clone.searchExp;
	}

	public Agent(List<Continent> continents, List<Territory> allTerritories) {
		this.continents = continents;
		this.allTerritories = allTerritories;
		initSemiContinents();
		territories = new ArrayList<>();
	}

	public Agent(Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		this.enemy = enemy;
		this.continents = continents;
		this.allTerritories = allTerritories;
		initSemiContinents();
		territories = new ArrayList<>();
	}

	private void initSemiContinents() {
		semiContinents = new ArrayList<>();
		for (int i = 0; i < continents.size(); i++) {
			semiContinents.add(new SemiContinent(i, continents.get(i).getValue()));
			semiContinents.get(i).setDiff(continents.get(i).getTerritories().size());
		}
	}

	public static Agent agentFactory(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
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
		System.out.println("There are " + bonusArmies + " armies to place");
	}

	public void attack() {

	}

	public List<Territory> possAttTerrs() {
		List<Territory> possAttTerrs = new ArrayList<>();

		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor) && (territory.getArmies() - neighbor.getArmies()) > 1
						&& !possAttTerrs.contains(neighbor)) {
					possAttTerrs.add(neighbor);
				}
			}
		}

		return possAttTerrs;
	}

	public Set<Attack> possibleAttacks() {
		Set<Attack> possAttaks = new HashSet<>();

		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor) && (territory.getArmies() - neighbor.getArmies()) > 1) {
					possAttaks.add(new Attack(territory, neighbor, neighbor.getArmies() + 1));
				}
			}
		}
		return possAttaks;
	}

	public void addTerritory(Territory territory) {
		territories.add(territory);

		SemiContinent semiContinent = semiContinents.get(territory.getContinent().getId());
		semiContinent.addTerritory(territory);
		semiContinent.setDiff(semiContinent.getDiff() - 1);
	}

	public void removeTerritory(Territory territory) {
		territories.remove(territory);

		SemiContinent semiContinent = semiContinents.get(territory.getContinent().getId());
		semiContinent.removeTerritory(territory);
		semiContinent.setDiff(semiContinent.getDiff() + 1);
	}

	public void setBonusArmies(int bonusArmies) {
		this.bonusArmies = bonusArmies;
	}

	public void setEnemy(Agent enemy) {
		this.enemy = enemy;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public List<SemiContinent> getSemiContinents() {
		return semiContinents;
	}

	public void doAttack(Attack attack) {
		attack.agentTerritory.setArmies(attack.agentTerritory.getArmies() - attack.attackArmies);
		attack.enemyTerritory.setArmies(attack.attackArmies - attack.enemyTerritory.getArmies());

		// May be the territory is neutral
		if (attack.enemyTerritory.getOwner() != null)
			attack.enemyTerritory.getOwner().removeTerritory(attack.enemyTerritory);

		attack.enemyTerritory.assignOwner(this);
		addTerritory(attack.enemyTerritory);

		bonusArmies += 2;
	}

	public void doAttack(Territory agentTerritory, Territory enemyTerritory, int attackArmies) {
		agentTerritory.setArmies(agentTerritory.getArmies() - attackArmies);
		enemyTerritory.setArmies(attackArmies - enemyTerritory.getArmies());

		// May be the territory is neutral
		if (enemyTerritory.getOwner() != null)
			enemyTerritory.getOwner().removeTerritory(enemyTerritory);

		enemyTerritory.assignOwner(this);
		addTerritory(enemyTerritory);

		bonusArmies = 2;
		addContBonus();
	}

	private void addContBonus() {
		for (SemiContinent semiContinent : semiContinents) {
			if (semiContinent.getDiff() == 0)
				bonusArmies += semiContinent.getValue();
		}
	}

	public boolean gameOver() {
		boolean gameOver = true;
		for (SemiContinent semiContinent : semiContinents) {
			if (semiContinent.getDiff() != 0) {
				gameOver = false;
				break;
			}
		}
		return gameOver;
	}

	@Override
	public String toString() {
		String s = "Territories : " + territories + " ,, ";
		s += "Semicontinents : " + semiContinents;
		return s;
	}

}
