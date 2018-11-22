package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import agent.Agent;
import agent.Human;
import agent.Passive;
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

		System.out.println("Choose the first player from :");
		System.out.println("Aggressive=0 , AStar=1 , Greedy=2 "
				+ " Human=3 , Pacifist=4 , RtAStar=5 , otherwise Passive");

		System.out.println("=> ");
		int agentId1 = scanner.nextInt();

		System.out.println("Choose the second player :");
		System.out.println("=> ");
		int agentId2 = scanner.nextInt();

//		Agent a1 = Agent.agentFactory(agentId1);
//		Agent a2 = Agent.agentFactory(agentId2);

//		while (true) {
//
//		}
	}

}
