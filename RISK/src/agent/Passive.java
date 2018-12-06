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

	public Passive(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	public Passive(Agent clone) {
		super(clone);
	}

	@Override
	public Action move() {
		Action action = new Action();
		action.agentPlacement = placeArmies();
		action.attack = attack();
		addContBonus();
		return action.agentPlacement == null && action.attack == null ? null : action;
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
}
