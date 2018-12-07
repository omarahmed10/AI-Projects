package agent;

public class Action {

	public ArmyPlacement agentPlacement = null;

	// for the case of AI agent.
	public ArmyPlacement passivePlacement = null;

	public Attack attack = null;

	@Override
	public String toString() {
		return "Action: { " + ((agentPlacement != null) ? agentPlacement.toString() : "") + "\n"
				+ ((passivePlacement != null) ? passivePlacement.toString() : "") + "\n"
				+ ((attack != null) ? attack.toString() : "") + " }";
	}

}
