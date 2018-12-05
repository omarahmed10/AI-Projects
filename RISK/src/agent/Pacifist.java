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

	public Pacifist(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
	}

	@Override
	public ArmyPlacement placeArmies() {
		if (bonusArmies <= 0)
			return null;

		Territory minTerritory = Collections.min(territories);
		minTerritory.setArmies(bonusArmies + minTerritory.getArmies());

		ArmyPlacement ap = new ArmyPlacement();
		ap.terrID = minTerritory.getId();
		ap.armyCount = minTerritory.getArmies();
		ap.bonusAdded = bonusArmies;

		bonusArmies = 0;

		return ap;
	}

	@Override
	public Attack attack() {
		List<Territory> possAttTerrs = possAttTerrs();

		if (possAttTerrs.isEmpty()) {
			System.out.println("No territory can be attacked!");
			return null;
		}

		Attack attack = null;

		Territory minTerritory = Collections.min(possAttTerrs);

		// attack with a territory with max armies
		Collections.sort(minTerritory.getNeighbors());
		for (int i = minTerritory.getNeighbors().size() - 1; i >= 0; i--) {
			Territory neighbor = minTerritory.getNeighbors().get(i);

			if (territories.contains(neighbor)) {
				// attack with least armies
				attack = new Attack();
				attack.agentTerritory = neighbor;
				attack.enemyTerritory = minTerritory;
				attack.attackArmies = minTerritory.getArmies() + 1;
				doAttack(attack);
				break;
			}
		}
		return null;
	}

}
