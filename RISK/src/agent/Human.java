package agent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import map.Continent;
import map.Territory;
import sun.net.www.content.audio.wav;

/*
 * An human agent, i.e. read the next move from the user.
 * */

public class Human extends Agent {

	public Human(int id, Agent enemy, List<Continent> continents, List<Territory> allTerritories) {
		super(id, enemy, continents, allTerritories);
	}

	@Override
	public ArmyPlacement placeArmies() {
		if (bonusArmies == 0)
			return null;

		boolean wrongId = true;
		ArmyPlacement ap = null;
		while (wrongId) {
			Window w = new Window("Enter id of the territory you want to place the armies in", false);
			if (w.getInput() == null)
				return null;
			int territoryId = Integer.parseInt(w.getInput()) - 1;

			// If the agent owns this territory , then assign the armies.
			// otherwise user re-enter the id
			for (Territory territory : territories) {
				if (territory.getId() == territoryId) {
					territory.setArmies(territory.getArmies() + bonusArmies);
					wrongId = false;

					ap = new ArmyPlacement();
					ap.terrID = territory.getId();
					ap.armyCount = territory.getArmies();
					ap.bonusAdded = bonusArmies;

					break;
				}
			}

			if (wrongId)
				w = new Window("Wrong id! Please insert a valid id", true);
		}

		bonusArmies = 0;

		return ap;
	}

	@Override
	public Action move() {
		Action action = new Action();
		action.agentPlacement = placeArmies();
		action.attack = attack();
		addContBonus();
		return action.agentPlacement == null && action.attack == null ? null : action;
	}

	@Override
	public Attack attack() {

		while (true) {

			List<Territory> possAttTerrs = possAttTerrs();

			if (possAttTerrs.isEmpty()) {
				System.out.println("No territory can be attacked!");
				return null;
			}

			Window w = new Window("<html>Enter ids of the territory you want to attack with:<br>the attacked one and"
					+ " number of armies you want to attack with seperated</html>", false);
			if (w.getInput() == null || w.getInput().isEmpty())
				return null;
			Scanner input = new Scanner(w.getInput());
			int agentTerritoryId = input.nextInt() - 1;
			int enemyTerritoryId = input.nextInt() - 1;
			int attackArmies = input.nextInt();
			input.close();

			Territory agentTerritory = allTerritories.get(agentTerritoryId);
			Territory enemyTerritory = allTerritories.get(enemyTerritoryId);

			// agent territory must be owned by the player
			if (!territories.contains(agentTerritory)) {
				w = new Window("First territory is not owned by you!", true);
				continue;
			}

			// enemy territory must be attackable & must be neighbor to the agent's
			if (!possAttTerrs.contains(enemyTerritory) || !agentTerritory.getNeighbors().contains(enemyTerritory)) {
				w = new Window("You can't attack this territory!", true);
				continue;
			}

			// agent territory armies must be larger than the attack's by at least 1
			if (agentTerritory.getArmies() - attackArmies < 1) {
				w = new Window("Agent territory armies not larger than the attack's by at least 1!", true);
				continue;
			}

			// attack armies must be larger than the enemy's by at least 1
			if (attackArmies - enemyTerritory.getArmies() < 1) {
				w = new Window("Attack armies not larger than the enemy's by at least 1!", true);
				continue;
			}

			// Do the attack
			Attack attack = new Attack();
			attack.agentTerritory = agentTerritory;
			attack.enemyTerritory = enemyTerritory;
			attack.attackArmies = attackArmies;
			doAttack(attack);
			return attack;
		}
	}

	public static class Window extends JDialog {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private JButton btnNewButton;
		private JPanel panel;
		private JTextField inputField;
		private String pin = null;
		private boolean warning;

		public Window(String msg, boolean warning) {
			this.warning = warning;
			this.setModal(true);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			this.setLocationRelativeTo(null);
			this.setSize(370, 200);
			this.setForeground(new Color(192, 192, 192));
			this.setTitle("User Input");
			this.setResizable(false);

			panel = new JPanel();
			panel.setLayout(new GridLayout(2 + (warning ? 0 : 1), 1));
			getContentPane().add(panel, BorderLayout.CENTER);

			JLabel lblNewLabel = new JLabel(msg);
			panel.add(lblNewLabel);

			if (!warning) {
				inputField = new JTextField();
				inputField.setColumns(13);
				panel.add(inputField);
			}

			btnNewButton = new JButton("OK");
			ListenForButton listener = new ListenForButton();

			btnNewButton.addActionListener(listener);
			panel.add(btnNewButton);

			this.setVisible(true);

		}

		private class ListenForButton implements ActionListener {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == btnNewButton) {
					if (!warning) {
						pin = inputField.getText();
					}
					dispose();
				}
			}
		}

		public String getInput() {
			return pin;
		}

	}
}
