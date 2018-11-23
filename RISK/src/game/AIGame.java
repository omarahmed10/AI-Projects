package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import agent.AStar;
import agent.Agent;
import map.Continent;
import map.Territory;

public class AIGame {
	public static void main(String[] args) {
		InputReader ir = new InputReader("input.txt");
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
		
		AStar agentAI = (AStar) Agent.agentFactory(1, null, continents,
				allTerritories);
		Agent agentPassive = Agent.agentFactory(6, agentAI, continents,
				allTerritories);
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
	public static void intialPlace(String filePath,
			List<Territory> allTerritories, Agent[] agents) {
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
}