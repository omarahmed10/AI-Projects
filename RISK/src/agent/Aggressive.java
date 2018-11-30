package agent;

import java.util.Collections;
import java.util.List;

import map.Continent;
import map.SemiContinent;
import map.Territory;

/*
 * An aggressive agent, that always places all its bonus armies on the 
 * vertex with the most armies, and greedily attempts to attack so as 
 * to cause the most damage - i.e. to prevent its opponent getting 
 * a continent bonus (the largest possible).
 * */

public class Aggressive extends Agent {

	public Aggressive(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ArmyPlacement placeArmies() {

		if(bonusArmies <= 0)
			return null;
		
		Territory maxTerritory = Collections.max(territories);
		maxTerritory.setArmies(bonusArmies + maxTerritory.getArmies());

		ArmyPlacement ap = new ArmyPlacement();
		ap.terrID = maxTerritory.getId();
		ap.armyCount = maxTerritory.getArmies();
		ap.bonusAdded = bonusArmies;
		
		bonusArmies = 0;
		return ap;
	}

	@Override
	public void attack() {
		List<Territory> possAttTerrs = possAttTerrs();

		if (possAttTerrs.isEmpty()) {
			System.out.println("No territory can be attacked!");
			return;
		}

		List<SemiContinent> enemySemiConts = enemy.getSemiContinents();
		Collections.sort(enemySemiConts);

		Collections.sort(possAttTerrs);

		// choose the territory

		// if there is no enemy territory to attack select
		// the neutral territory with largest armies
		Territory attTerr = possAttTerrs.get(possAttTerrs.size() - 1);

		boolean attEnemy = false;
		for (int i = enemySemiConts.size() - 1; i >= 0; i--) {
			for (int j = possAttTerrs.size() - 1; j >= 0; j--) {
				if (possAttTerrs.get(j).getContinent().getId() == enemySemiConts.get(i).getId()) {
					attTerr = possAttTerrs.get(j);
					attEnemy = true;
					break;
				}
			}
			if (attEnemy)
				break;
		}

		// choose the territory with max armies to attack with
		Collections.sort(attTerr.getNeighbors());
		for (int i = attTerr.getNeighbors().size() - 1; i >= 0; i--) {
			Territory neighbor = attTerr.getNeighbors().get(i);

			if (territories.contains(neighbor)) {
				// attack with all armies except for one
				doAttack(neighbor, attTerr, neighbor.getArmies() - 1);
				break;
			}
		}

	}

}
