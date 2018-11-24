package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import agent.Agent;
import agent.Greedy;
import javafx.util.Pair;
import map.Continent;
import map.Territory;

public class AIGame {
	static InputReader ir;

	public static void main(String[] args) {
		ir = new InputReader("input.txt");
		List<Territory> allTerritories = ir.getTerritories();
		List<Continent> continents = ir.getContinents();
		for (Continent continent : continents) {
			for (Territory territory : continent.getTerritories()) {
				territory.setContinent(continent);
			}
		}

		System.out.print("terr : ");
		System.out.println(allTerritories);
		System.out.print("cont : ");
		System.out.println(continents);

        Greedy agentAI = (Greedy) Agent.agentFactory(2, null, continents, allTerritories);
		Agent agentPassive = Agent.agentFactory(6, agentAI, continents, allTerritories);
		agentAI.setEnemy(agentPassive);
		Agent[] agents = new Agent[] { agentAI, agentPassive };

		intialPlace("initialPlacement.txt", allTerritories, agents);

		for (int i = 0; i < agents.length; i++) {
			System.out.println("Agent " + (i + 1) + " info :");
			System.out.println(agents[i]);
		}
		System.out.println();

		agentAI.buildPath(agentPassive);
		System.out.println("AI is done");
		System.out.println(agentAI.path);

	}

	public static void intialPlace(String filePath, List<Territory> allTerritories, Agent[] agents) {
		try {
			Scanner scanner = new Scanner(new File(filePath));
			int terrNum = scanner.nextInt();

			for (int i = 0; i < terrNum; i++) {
				int terrId = scanner.nextInt();
				int armies = scanner.nextInt();
				int agentId = scanner.nextInt();

				Territory territory = allTerritories.get(terrId - 1);
				Agent agent = agents[agentId - 1];

				territory.setArmies(armies);
				agent.addTerritory(territory);
				territory.assignOwner(agent);
				agent.addContBonus();
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void buildNeighbors(List<Territory> all) {
		for (Pair<Integer, Integer> pair : ir.edges) {
			Territory V1 = all.get(pair.getKey());
			Territory V2 = all.get(pair.getValue());
			V1.addNeighbor(V2);
			V2.addNeighbor(V1);
		}
	}
}
