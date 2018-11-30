package agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import game.AIGame;
import gui.MainScreen;
import map.Territory;

public class AgentState implements Comparable<AgentState> {
    protected Agent aiAgent;
    protected Agent passiveAggent;
    protected int gx = 0; // cost to reach state.
    protected int hx = 0; // cost to reach goal.
    protected AgentState parent = null;
    protected Attack attack; // attack to reach state.
    protected ArmyPlacement agentPlacement;
    protected ArmyPlacement passivePlacement;
    
    public AgentState(Agent agent, Agent passive, AgentState parent) {
        this.aiAgent = agent;
        this.parent = parent;
        this.passiveAggent = passive;
    }

    public AgentState(AgentState clone) {
        this.aiAgent = Agent.agentFactory(clone.aiAgent);
        this.passiveAggent = Agent.agentFactory(clone.passiveAggent);
        this.aiAgent.enemy = this.passiveAggent;
        List<Territory> all = new ArrayList<>(this.passiveAggent.territories);
        all.addAll(this.aiAgent.territories);
        MainScreen.buildNeighbors(all);
        this.gx = clone.gx;
        this.hx = clone.hx;
        this.parent = clone.parent;
        this.attack = clone.attack;
    }

    public int fx() { // total cost.
        return gx + hx;
    }

    public List<AgentState> getNeighbors() {
        passivePlacement = passiveAggent.placeArmies(); /* Making Passive player move. */
        agentPlacement = aiAgent.placeArmies();
        List<AgentState> neighbours = new ArrayList<>();
        for (Attack attack : aiAgent.possibleAttacks()) {
            AgentState newNeighbour = new AgentState(this);
            newNeighbour.attack = getAttack(attack, newNeighbour);
            newNeighbour.aiAgent.doAttack(newNeighbour.attack);
            newNeighbour.aiAgent.addContBonus();
            newNeighbour.aiAgent.enemy.addContBonus();
            neighbours.add(newNeighbour);
        }
        return neighbours;
    }

    protected static Attack getAttack(Attack attack, AgentState newState) {
        Attack newAttack = new Attack();
        for (Territory t : newState.aiAgent.territories) {
            if (t.equals(attack.agentTerritory)) {
                newAttack.agentTerritory = t;
                break;
            }
        }
        for (Territory t : newState.passiveAggent.territories) {
            if (t.equals(attack.enemyTerritory)) {
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
        String s = "gx =" + gx + " hx =" + hx + "\n" + "Agent State : AI" + aiAgent.toString() + "\n" + "Passive "
                + aiAgent.enemy.toString() + a + "\n\n";
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