package map;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class Continent {
	protected int id;
	protected int value;
	protected List<Territory> territories;

	public Continent(int id, int val) {
		this.id = id;
		this.value = val;
		territories = new ArrayList<>();
	}

	public Continent(Continent clone, Agent owner) {
		this.id = clone.id;
		this.value = clone.value;
		this.territories = new ArrayList<>();
		Territory.clone(this.territories, clone.territories, owner);
	}

	public void addTerritory(Territory territory) {
		territories.add(territory);
	}

	public int getId() {
		return id;
	}

	public int getValue() {
		return value;
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	@Override
	public boolean equals(Object obj) { //checking territories for semiContinent
		return id == ((Continent) obj).id && Agent.listEqualsIgnoreOrder(territories, ((Continent) obj).territories);
	}

	@Override
	public String toString() {
		String s = "(id=" + (id + 1) + " , " + "value=" + value + " , territories : ";
		for (Territory territory : territories) {
			s += (territory.getId() + 1) + " ";
		}
		s += ")";
		return s;
	}

}
