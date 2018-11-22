package map;

public class SemiContinent extends Continent
		implements Comparable<SemiContinent> {
	// how much territory it needs to be a continent
	private int diff;

	public SemiContinent(int id, int val) {
		super(id, val);
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

}