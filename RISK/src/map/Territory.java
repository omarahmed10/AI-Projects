package map;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class Territory implements Comparable<Territory> {
	private int id;
	private int armies;
	private Continent continent;
	private Agent owner;
	List<Territory> neighbors;

	public Territory(int id) {
		this.id = id;
		armies = 0;
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
	public String toString() {
		return "id=" + id + " , " + "armies=" + armies;
	}

	@Override
	public int compareTo(Territory t) {
		if (t.getArmies() < armies)
			return -1;
		if (t.getArmies() > armies)
			return 1;

		// break ties by neighbors numbers
		if (t.getNeighbors().size() < neighbors.size())
			return -1;
		if (t.getNeighbors().size() > neighbors.size())
			return 1;

		return 0;
	}
}
