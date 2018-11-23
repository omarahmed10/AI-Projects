package game;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import agent.Agent;
import map.Continent;
import map.Territory;

public class Game {

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

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

		System.out.println("Choose the first player from :");
		System.out.println("Aggressive=0 , AStar=1 , Greedy=2 "
				+ " Human=3 , Pacifist=4 , RtAStar=5 , otherwise Passive");

		System.out.print("=> ");
		int agentId1 = scanner.nextInt();

		System.out.println("Choose the second player :");
		System.out.print("=> ");
		int agentId2 = scanner.nextInt();

		Agent agent1 = Agent.agentFactory(agentId1, null, continents,
				allTerritories);
		Agent agent2 = Agent.agentFactory(agentId2, agent1, continents,
				allTerritories);
		agent1.setEnemy(agent2);

		Agent[] agents = new Agent[] { agent1, agent2 };

		intialPlace("initialPlacement.txt", allTerritories, agents);

		boolean gameOver = false;
		int agentId = 1;

		for (int i = 0; i < agents.length; i++) {
			System.out.println("Agent " + (i + 1) + " info :");
			System.out.println(agents[i]);
		}
		System.out.println();

		// max game size to prevent going into an infinite loop
		int maxGameTurns = (int) Math.pow(allTerritories.size(), 2);
		int turnCount = 0;

		while (!gameOver && turnCount++ < maxGameTurns) {
			System.out.println("Agent " + agentId + " Turn");

			Agent agent = agents[agentId - 1];

			agent.placeArmies();
			agent.attack();

			for (int i = 0; i < agents.length; i++) {
				System.out.println("Agent " + (i + 1) + " info :");
				System.out.println(agents[i]);
			}
			System.out.println();

			gameOver = agent.gameOver();
			if (gameOver)
				System.out.println("It's all over , player " + agentId + " Won!");

			if (agentId == 1)
				agentId = 2;
			else
				agentId = 1;
		}

		scanner.close();
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
			}

			scanner.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}
