package agent;

import java.util.List;
import java.util.Scanner;

import map.Continent;
import map.Territory;

/*
 * An human agent, i.e. read the next move from the user.
 * */

public class Human extends Agent {


	public Human(Agent enemy, List<Continent> continents,
			List<Territory> allTerritories) {
		super(enemy, continents, allTerritories);
		// TODO Auto-generated constructor stub
	}

	private Scanner scanner = new Scanner(System.in);

	@Override
	public void placeArmies() {
		System.out
				.print("Enter id of the territory you want to place the armies in : ");

		boolean wrongId = true;

		while (wrongId) {
			int territoryId = scanner.nextInt();

			// If the agent owns this territory , then assign the armies.
			// otherwise user re-enter the id
			for (Territory territory : territories) {
				if (territory.getId() == territoryId) {
					territory.setArmies(territory.getArmies() + bonusArmies);
					wrongId = false;
					break;
				}
			}

			if (wrongId)
				System.out.println("Wrong id! Please insert a valid id");
		}
	}

	@Override
	public void attack() {
		System.out.print(
				"Enter ids of the territory you want to attack with, the attacked one and"
						+ " number of armies you want to attack with seperated (Enter -1 to skip) : ");

		while (true) {
			int agentTerritoryId = scanner.nextInt();
			if (agentTerritoryId == -1)
				return;
			int enemyTerritoryId = scanner.nextInt();
			int attackArmies = scanner.nextInt();

			Territory agentTerritory = allTerritories.get(agentTerritoryId);
			Territory enemyTerritory = allTerritories.get(enemyTerritoryId);

			// agent territory must be owned by the player
			if (!territories.contains(agentTerritory)) {
				System.out.println("First territory is not owned by you!");
				continue;
			}

			// attacked territory must not be owned by the player
			if (territories.contains(enemyTerritory)) {
				System.out.println("Second territory is owned by you!");
				continue;
			}

			// agent and attacked territories must be neighbors
			if (agentTerritory.getNeighbors().contains(enemyTerritory)) {
				System.out.println("The territories are not neighbors!");
				continue;
			}

			// agent territory armies must be larger than the attack's by at least 1
			if (agentTerritory.getArmies() - attackArmies < 1) {
				System.out.println(
						"Agent territory armies not larger than the attack's by at least 1!");
				continue;
			}

			// attack armies must be larger than the enemy's by at least 1
			if (attackArmies - enemyTerritory.getArmies() < 1) {
				System.out.println(
						"Attack armies not larger than the enemy's by at least 1!");
				continue;
			}

			// Do the attack
			doAttack(agentTerritory, enemyTerritory, attackArmies);
			break;
		}
	}
}
