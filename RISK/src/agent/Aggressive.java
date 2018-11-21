package agent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import map.Continent;
import map.Territory;

/*
 * An aggressive agent, that always places all its bonus armies on the 
 * vertex with the most armies, and greedily attempts to attack so as 
 * to cause the most damage - i.e. to prevent its opponent getting 
 * a continent bonus (the largest possible).
 * */

public class Aggressive extends Agent {
	
	public Aggressive(int contNum) {
		super(contNum);
	}

	@Override
	public void placeArmies() {
		Territory maxTerritory = Collections.max(territories);
		maxTerritory.setArmies(bonusArmies + maxTerritory.getArmies());
	}

	@Override
	public void attack(List<Territory> Allterritories, List<Continent> continents,
			Agent enemy) {
		Continent[] enemySemiConts = enemy.getSemiContinents();
		int[] contDiffs = new int[enemySemiConts.length];

		for (int i = 0; i < enemySemiConts.length; i++) {
			contDiffs[i] = continents.get(i).getTerritories().size()
					- enemySemiConts[i].getTerritories().size();
		}

		int[] contDiffsSort = contDiffs;
		Arrays.sort(contDiffsSort);

		List<Territory> possAttTerrs = possAttTerrs();

		// choose the territory
		for (int i = 0, lastInd = -1; i < contDiffsSort.length; i++) {
			for (int j = 0; j < contDiffs.length; j++) {
				if (contDiffsSort[i] == contDiffs[j] && j != lastInd) {

				}
			}
		}
	}

}
