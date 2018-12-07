package agent;

public class ArmyPlacement {

	public int terrID = -1;
	public int armyCount = -1;
	public int bonusAdded = -1;

	@Override
	public String toString() {
		return "Placing : " + bonusAdded + " to " + (terrID + 1);
	}
}
