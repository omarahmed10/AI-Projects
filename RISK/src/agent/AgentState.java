package agent;

import java.util.ArrayList;
import java.util.List;

public class AgentState implements Comparable<AgentState>{
	protected Agent agent;
	protected int gx = 0;  // cost to reach state.
	protected int hx = 0;  // cost to reach goal.
	protected AgentState parent = null;
	protected Attack attack; // attack to reach state.
	
	public AgentState() {
	}
	
	public AgentState(AgentState clone) {
		this.agent = new Agent(clone.agent);
		this.gx = clone.gx;
		this.hx = clone.hx;
		this.parent = clone.parent;
		this.attack = clone.attack;
	}
	
	public int fx() { // total cost.
		return gx + hx;
	}

	public List<AgentState> getNeighbors() {
		List<AgentState> neighbours = new ArrayList<>();
		for(Attack attack: agent.possibleAttacks()) {
			AgentState newNeighbour = new AgentState(this);
			newNeighbour.attack = attack;
			newNeighbour.agent.placeArmies();
			newNeighbour.agent.doAttack(attack);
			neighbours.add(newNeighbour);
		}
		return neighbours;
	}
	
	@Override
	public int compareTo(AgentState o) {
		
		// should compare using fx
		return 0;
	}
	
}