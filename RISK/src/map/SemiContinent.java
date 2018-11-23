package map;

import java.util.ArrayList;
import java.util.List;

import agent.Agent;

public class SemiContinent extends Continent implements Comparable<SemiContinent> {
	// how much territory it needs to be a continent
	private int diff;

	public SemiContinent(int id, int val) {
		super(id, val);
	}

	public SemiContinent(SemiContinent clone, Agent owner) {
		super(clone, owner);
		this.diff = clone.diff;
	}

	public void setDiff(int diff) {
		this.diff = diff;
	}

	public int getDiff() {
		return diff;
	}

	public void removeTerritory(Territory territory) {
		territories.remove(territory);
	}

	@Override
	public int compareTo(SemiContinent semiContinent) {
		if (semiContinent.diff < diff)
			return -1;
		if (semiContinent.diff > diff)
			return 1;
		if (semiContinent.value < value)
			return -1;
		if (semiContinent.value > value)
			return 1;

		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		return super.equals(obj) && diff == ((SemiContinent)obj).diff;
	}
	
	public static void clone(List<SemiContinent> dest, List<SemiContinent> source, Agent owner) {
		for (SemiContinent sc : source) {
			SemiContinent scNew = new SemiContinent(sc, owner);
			dest.add(scNew);
		}
	}

}
