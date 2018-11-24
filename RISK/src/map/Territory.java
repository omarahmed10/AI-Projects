package map;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class Territory implements Comparable<Territory> {
	private int id;
	private int armies;
	private Continent continent;
	public Agent owner;
	List<Territory> neighbors;

	public Territory(int id) {
		this.id = id;
		armies = 0;
		this.neighbors = new ArrayList<>();
	}

	public Territory(Territory clone, Agent owner) {
		this.id = clone.id;
		this.armies = clone.armies;
		this.continent = clone.continent;
		this.owner = owner;
		this.neighbors = new ArrayList<>();
	}

	public void addNeighbor(Territory country) {
		neighbors.add(country);
	}

	public void assignOwner(Agent owner) {
		this.owner = owner;
	}

	public void setArmies(int armies) {
		this.armies = armies;
	}

	public void setContinent(Continent continent) {
		this.continent = continent;
	}

	public Continent getContinent() {
		return continent;
	}

	public int getId() {
		return id;
	}

	public int getArmies() {
		return armies;
	}

	public Agent getOwner() {
		return owner;
	}

	public List<Territory> getNeighbors() {
		return neighbors;
	}

	@Override
	public boolean equals(Object obj) {
		Territory tObj = (Territory) obj;
		return id == tObj.id && armies == tObj.armies && owner.id == tObj.owner.id;
	}

	@Override
	public String toString() {
		return "(id=" + (id + 1) + " , " + "armies=" + armies + ")";
	}

	@Override
	public int compareTo(Territory t) {
		if (t.getArmies() < armies)
			return 1;
		if (t.getArmies() > armies)
			return -1;

		// break ties by neighbors numbers
		if (t.getNeighbors().size() < neighbors.size())
			return 1;
		if (t.getNeighbors().size() > neighbors.size())
			return -1;

		// break ties by ids
		if (t.getId() < id)
			return 1;
		if (t.getId() > id)
			return -1;

		return 0;
	}

	public static void clone(List<Territory> dest, List<Territory> source, Agent owner) {
		for (Territory t : source) {
			Territory newT = new Territory(t, owner);
			dest.add(newT);
		}
	}
}
