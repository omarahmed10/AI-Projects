package agent;

import map.Territory;

public class Attack {
	protected Territory agentTerritory;
	protected Territory enemyTerritory;
	protected int attackArmies;

	public Attack(Territory agentTerritory, Territory enemyTerritory, int attackArmies) {
		this.agentTerritory = agentTerritory;
		this.enemyTerritory = enemyTerritory;
		this.attackArmies = attackArmies;
	}

	@Override
	public boolean equals(Object obj) {
		Attack aObj = (Attack) obj;
		return agentTerritory.equals(aObj.agentTerritory) && enemyTerritory.equals(aObj.enemyTerritory);
	}

}
