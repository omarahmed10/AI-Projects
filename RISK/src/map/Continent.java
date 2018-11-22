package map;

import java.util.ArrayList;
import java.util.List;

public class Continent {
	protected int id;
	protected int value;
	protected List<Territory> territories;

	public Continent(int id, int val) {
		this.id = id;
		this.value = val;
		territories = new ArrayList<>();
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
	public String toString() {
		String s = "id=" + id + " , " + "territories : ";
		s += territories.toString();
		return s;
	}
}
