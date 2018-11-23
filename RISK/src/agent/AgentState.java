package agent;

import java.util.ArrayList;
import java.util.List;

import map.Territory;

public class AgentState implements Comparable<AgentState> {
	protected Agent aiAgent;
	protected Agent passiveAggent;
	protected int gx = 0; // cost to reach state.
	protected int hx = 0; // cost to reach goal.
	protected AgentState parent = null;
	protected Attack attack; // attack to reach state.

	public AgentState(Agent agent, Agent passive, AgentState parent) {
		this.aiAgent = agent;
		this.parent = parent;
		this.passiveAggent = passive;
	}

	public AgentState(AgentState clone) {
		this.aiAgent = new Agent(clone.aiAgent);
		this.passiveAggent = new Agent(clone.passiveAggent);
		this.aiAgent.enemy = this.passiveAggent;
		this.gx = clone.gx;
		this.hx = clone.hx;
		this.parent = clone.parent;
		this.attack = clone.attack;
	}

	public int fx() { // total cost.
		return gx + hx;
	}

	public List<AgentState> getNeighbors() {
		passiveAggent.placeArmies(); /* Making Passive player move. */
		aiAgent.placeArmies();
		List<AgentState> neighbours = new ArrayList<>();
		for (Attack attack : aiAgent.possibleAttacks()) {
			AgentState newNeighbour = new AgentState(this);
			newNeighbour.attack = getAttack(attack, newNeighbour);
			newNeighbour.aiAgent.doAttack(attack);
			newNeighbour.aiAgent.addContBonus();
			newNeighbour.aiAgent.enemy.addContBonus();
			neighbours.add(newNeighbour);
		}
		System.out.println("Done with getNeighbours");
		return neighbours;
	}

	private Attack getAttack(Attack attack, AgentState newState) {
		Attack newAttack = new Attack();
		System.out.println("AGENT: "+attack.agentTerritory);
		for (Territory t : newState.aiAgent.territories) {
			System.out.println(t);
			if (t.equals(attack.agentTerritory)) {
				System.out.println("FOUND agent");
				newAttack.agentTerritory = t;
				break;
			}
		}
		System.out.println("ENEMY: "+attack.enemyTerritory);
		for (Territory t : newState.passiveAggent.territories) {
			System.out.println(t);
			if (t.equals(attack.enemyTerritory)) {
				System.out.println("FOUND enemy");
				newAttack.enemyTerritory = t;
				break;
			}
		}
		newAttack.attackArmies = attack.attackArmies;

		return newAttack;
	}

	@Override
	public boolean equals(Object obj) {
		return this.aiAgent.equals(((AgentState) obj).aiAgent);
	}

	@Override
	public String toString() {
		String a = "";
		if (attack != null) {
			a = attack.toString();
		}
		String s = "Agent State :" + aiAgent.toString() + "\n" + aiAgent.enemy.toString() + "\ngx =" + gx + " hx =" + hx
				+ " " + a;
		return s;
	}

	@Override
	public int compareTo(AgentState o) {
		if (fx() > o.fx())
			return 1;
		if (fx() < o.fx())
			return -1;
		return 0;
	}

}