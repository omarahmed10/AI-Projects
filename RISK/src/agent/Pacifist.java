package agent;

import java.util.Collections;
import java.util.List;

import map.Continent;
import map.Territory;

public class Pacifist extends Agent {

	public Pacifist(Agent enemy, List<Continent> continents,
			List<Territory> allTerritories) {
		super(enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void placeArmies() {
		Territory minTerritory = Collections.min(territories);
		minTerritory.setArmies(bonusArmies + minTerritory.getArmies());
	}

	@Override
	public void attack() {
		Territory minTerritory = Collections.min(possAttTerrs());

		Collections.sort(minTerritory.getNeighbors());
		for (Territory neighbor : minTerritory.getNeighbors()) {
			if (territories.contains(neighbor)) {
				// attack with least armies
				doAttack(neighbor, minTerritory, minTerritory.getArmies() + 1);
				break;
			}
		}
	}

}
