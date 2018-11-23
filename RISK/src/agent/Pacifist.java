package agent;

import java.util.Collections;
import java.util.List;

import map.Continent;
import map.Territory;

/*
 * A nearly pacifist agent, that places its armies like the
 * completely passive agent, then conquers only one vertex 
 * (if it can), such that it loses as few armies as possible.
 * */

public class Pacifist extends Agent {

	public Pacifist(int id, Agent enemy, List<Continent> continents,
			List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
	}

	@Override
	public void placeArmies() {
		super.placeArmies();

		Territory minTerritory = Collections.min(territories);
		minTerritory.setArmies(bonusArmies + minTerritory.getArmies());

		bonusArmies = 0;
	}

	@Override
	public void attack() {
		List<Territory> possAttTerrs = possAttTerrs();

		if (possAttTerrs.isEmpty()) {
			System.out.println("No territory can be attacked!");
			return;
		}

		Territory minTerritory = Collections.min(possAttTerrs);

		// attack with a territory with max armies
		Collections.sort(minTerritory.getNeighbors());
		for (int i = minTerritory.getNeighbors().size() - 1; i >= 0; i--) {
			Territory neighbor = minTerritory.getNeighbors().get(i);

			if (territories.contains(neighbor)) {
				// attack with least armies
				doAttack(neighbor, minTerritory, minTerritory.getArmies() + 1);
				break;
			}
		}
	}

}
