package agent;

public class AgentState implements Comparable<AgentState>{
	protected Agent agent;
	protected int gx;  // cost to reach state.
	protected int hx;  // cost to reach goal.
	protected AgentState parent;
	
	public AgentState() {
		
	}
	
	public int fx() { // total cost.
		return gx + hx;
	}

	@Override
	public int compareTo(AgentState o) {
		// TODO Auto-generated method stub
		return 0;
	}
}
