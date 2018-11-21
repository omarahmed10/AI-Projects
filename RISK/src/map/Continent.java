package map;

import java.util.ArrayList;
import java.util.List;

public class Continent {
	private int id;
	private int value;
	private List<Territory> territories;

	public Continent(int id, int val) {
		this.id = id;
		this.value = val;
		territories = new ArrayList<>();
	}

	public void addTerritory(Territory territory) {
		territories.add(territory);
	}

	public void removeTerritory(Territory territory) {
		territories.remove(territory);
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
