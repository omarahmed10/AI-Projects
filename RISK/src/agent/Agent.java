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
	public int id;

	public Agent(Agent clone) {
		this.id = clone.id;
		this.territories = new ArrayList<>();
		Territory.clone(this.territories, clone.territories, this);
		this.continents = clone.continents;
		this.semiContinents = new ArrayList<>();
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
		semiContinents = new ArrayList<>();
		for (int i = 0; i < continents.size(); i++) {
			semiContinents.add(new SemiContinent(i, continents.get(i).getValue()));
			semiContinents.get(i).setDiff(continents.get(i).getTerritories().size());
		}
	}

	public static Agent agentFactory(Agent clone) {
		if (clone.id == 1)
			return new AStar(clone);
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

	public void placeArmies() {
		System.out.println("There are " + bonusArmies + " armies to place");
		if (bonusArmies <= 0)
			return;
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

	public List<SemiContinent> getSemiContinents() {
		return semiContinents;
	}

	public void doAttack(Attack attack) {
		// May be the territory is neutral
		if (attack.enemyTerritory.getOwner() != null)
			attack.enemyTerritory.getOwner().removeTerritory(attack.enemyTerritory);
		
		attack.agentTerritory.setArmies(attack.agentTerritory.getArmies() - attack.attackArmies);
		attack.enemyTerritory.setArmies(attack.attackArmies - attack.enemyTerritory.getArmies());
		attack.enemyTerritory.assignOwner(this);
		addTerritory(attack.enemyTerritory);
		
		bonusArmies = 2;
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
	}

	public void addContBonus() {
		for (SemiContinent semiContinent : semiContinents) {
			if (semiContinent.getDiff() == 0)
				bonusArmies += semiContinent.getValue();
		}
	}

	public boolean isWinner() {
		for (SemiContinent semiContinent : semiContinents) {
			if (semiContinent.getDiff() != 0) {
				return false;
			}
		}
		System.out.println("We have a winner " + this.toString());
		return true;
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

	/*
	 * not right ...................
	 */
	@Override
	public boolean equals(Object obj) {
		System.out.println("ASDJFASDFKNASDFNKASDFIKNASDFINASDF");
		Agent aObj = (Agent) obj;
		return listEqualsIgnoreOrder(territories, aObj.territories)
				&& listEqualsIgnoreOrder(semiContinents, aObj.semiContinents);
	}

	public static <T> boolean listEqualsIgnoreOrder(List<T> list1, List<T> list2) {
		return new HashSet<>(list1).equals(new HashSet<>(list2));
	}

	@Override
	public String toString() {
		String s = "Agent :" + "Territories : " + territories + " ,, ";
		s += "Semicontinents : " + semiContinents + " ,, Bonus :"+ bonusArmies;
		return s;
	}

}
