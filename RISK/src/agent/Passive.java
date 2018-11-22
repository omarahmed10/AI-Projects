package agent;

import java.util.Collections;
import java.util.List;

import map.Continent;
import map.Territory;

/*
 *  A passive agent never attacks, and always places all its additional
 *  armies on the vertex that has the fewest armies, breaking ties by 
 *  favoring the lowest-numbered vertex.
 * */

public class Passive extends Agent {


	public Passive(Agent enemy, List<Continent> continents,
			List<Territory> allTerritories) {
		super(enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void placeArmies() {
		Territory minTerritory = Collections.min(territories);
		minTerritory.setArmies(bonusArmies + minTerritory.getArmies());
	}
}
