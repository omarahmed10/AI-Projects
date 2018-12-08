package agent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import map.Continent;
import map.SemiContinent;
import map.Territory;

public class Agent {
	protected List<Continent> continents;
	protected List<Territory> allTerritories;
	protected List<Territory> territories;
	protected Map<Integer, SemiContinent> semiContinents;
	protected int bonusArmies;
	protected Agent enemy;
	protected int searchExp;
	protected int L = 0, T = 0;
	public int id;

	public Agent(Agent clone) {
		this.id = clone.id;
		this.territories = new ArrayList<>();
		Territory.clone(this.territories, clone.territories, this);
		this.continents = clone.continents;
		this.semiContinents = new HashMap<>();
		SemiContinent.clone(this.semiContinents, clone.semiContinents, this);
		this.bonusArmies = clone.bonusArmies;
		this.searchExp = clone.searchExp;
	}

	public Agent(int id, List<Continent> continents, List<Territory> allTerritories) {
		this.continents = continents;
		this.allTerritories = allTerritories;
		initSemiContinents();
		territories = new ArrayList<>();
		this.id = id;
	}

	public Agent(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		this.enemy = enemy;
		this.continents = continents;
		this.allTerritories = allTerritories;
		initSemiContinents();
		territories = new ArrayList<>();
		this.id = id;
	}

	private void initSemiContinents() {
		semiContinents = new HashMap<>();
		for (int i = 0; i < continents.size(); i++) {
			semiContinents.put(i, new SemiContinent(i, continents.get(i).getValue()));
			semiContinents.get(i).setDiff(continents.get(i).getTerritories().size());
		}
	}

	public static Agent agentFactory(Agent clone) {
		if (clone.id == 1)
			return new AStar(clone);
		else if (clone.id == 2)
			return new Greedy(clone);
		else if (clone.id == 5)
			return new RtAStar(clone);
		else
			return new Passive(clone);
	}

	public static Agent agentFactory(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		if (id == 0)
			return new Aggressive(0, enemy, continents, allTerritories);
		else if (id == 1)
			return new AStar(1, enemy, continents, allTerritories);
		else if (id == 2)
			return new Greedy(2, enemy, continents, allTerritories);
		else if (id == 3)
			return new Human(3, enemy, continents, allTerritories);
		else if (id == 4)
			return new Pacifist(4, enemy, continents, allTerritories);
		else if (id == 5)
			return new RtAStar(5, enemy, continents, allTerritories);
		else
			return new Passive(6, enemy, continents, allTerritories);
	}

	public ArmyPlacement placeArmies() {
		return null;
	}

	public Attack attack() {
		return null;
	}

	public Action move() {
		return null;
	}

	public void buildPath(Agent agent) {
		// TODO Auto-generated method stub

	}

	public boolean solutionFound() {
		return false;
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

	public List<Attack> possibleAttacks() {
		List<Attack> possAttaks = new ArrayList<>();
		for (Territory territory : territories) {
			for (Territory neighbor : territory.getNeighbors()) {
				if (!territories.contains(neighbor) && (territory.getArmies() - neighbor.getArmies()) > 1) {
					Attack a = new Attack(territory, neighbor, territory.getArmies() - 1);
					if (!possAttaks.contains(a))
						possAttaks.add(a);
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
		int oldDiff = semiContinent.getDiff();
		semiContinent.removeTerritory(territory);
		semiContinent.setDiff(semiContinent.getDiff() + 1);
		if (oldDiff == 0 && bonusArmies > 0) {
			bonusArmies -= semiContinent.getValue();
		}
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

	public Map<Integer, SemiContinent> getSemiContinents() {
		return semiContinents;
	}

	public void doAttack(Attack attack) {
		// May be the territory is neutral
		attack.enemyTerritory.getOwner().removeTerritory(attack.enemyTerritory);

		attack.agentTerritory.setArmies(attack.agentTerritory.getArmies() - attack.attackArmies);
		attack.enemyTerritory.setArmies(attack.attackArmies - attack.enemyTerritory.getArmies());
		attack.enemyTerritory.assignOwner(this);
		addTerritory(attack.enemyTerritory);

		bonusArmies = 2;
	}

	public void addContBonus() {
		for (SemiContinent semiContinent : semiContinents.values()) {
			if (semiContinent.getDiff() == 0)
				bonusArmies += semiContinent.getValue();
		}
	}

	public boolean isWinner() {
		for (SemiContinent semiContinent : semiContinents.values()) {
			if (semiContinent.getDiff() != 0) {
				return false;
			}
		}
		return true;
	}

	public boolean gameOver() {
		boolean gameOver = true;
		for (SemiContinent semiContinent : semiContinents.values()) {
			if (semiContinent.getDiff() != 0) {
				gameOver = false;
				break;
			}
		}
		return gameOver;
	}

	public int getPerformance(int f) {
		return f * L + T;
	}

	/*
	 * not right ...................
	 */
	@Override
	public boolean equals(Object obj) {

		Agent aObj = (Agent) obj;
		return listEqualsIgnoreOrder(territories, aObj.territories)
				&& listEqualsIgnoreOrder(semiContinents.values(), aObj.semiContinents.values());
	}

	private boolean listEqualsIgnoreOrder(Collection<SemiContinent> values, Collection<SemiContinent> values2) {
		// TODO Auto-generated method stub
		return new HashSet<>(values).equals(new HashSet<>(values2));
	}

	public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
		return new HashSet<>(list1).equals(new HashSet<>(list2));
	}

	@Override
	public String toString() {
		String s = "Agent : " + "Territories : " + territories + " ,, ";
		s += "\nAgent: Semicontinents : " + semiContinents.values() + ",,Bonus :" + bonusArmies + "\n";
		return s;
	}

}
