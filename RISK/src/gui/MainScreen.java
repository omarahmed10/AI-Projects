package gui;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants;
import org.graphstream.ui.graphicGraph.stylesheet.StyleConstants.Shape;
import org.graphstream.ui.swingViewer.ViewPanel;
import org.graphstream.ui.view.Viewer;
import org.graphstream.ui.view.ViewerListener;
import org.graphstream.ui.view.ViewerPipe;

import agent.AStar;
import agent.Action;
import agent.Agent;
import agent.ArmyPlacement;
import agent.Attack;
import game.InputReader;
import javafx.util.Pair;
import map.Continent;
import map.Territory;
import sun.management.resources.agent;

public class MainScreen extends JFrame implements ViewerListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	static InputReader ir;
	protected static boolean loop = true;

	public MainScreen(Viewer viewer, Graph graph) {
		// The default action when closing the view is to quit
		// the program.
		viewer.setCloseFramePolicy(Viewer.CloseFramePolicy.HIDE_ONLY);

		// We connect back the viewer to the graph,
		// the graph becomes a sink for the viewer.
		// We also install us as a viewer listener to
		// intercept the graphic events.
		ViewerPipe fromViewer = viewer.newViewerPipe();
		fromViewer.addViewerListener(this);
		fromViewer.addSink(graph);

		// Then we need a loop to do our work and to wait for events.
		// In this loop we will need to call the
		// pump() method before each use of the graph to copy back events
		// that have already occurred in the viewer thread inside
		// our thread.

		while (loop) {
			fromViewer.pump(); // or fromViewer.blockingPump(); in the nightly builds

			// here your simulation code.

			// You do not necessarily need to use a loop, this is only an example.
			// as long as you call pump() before using the graph. pump() is non
			// blocking. If you only use the loop to look at event, use blockingPump()
			// to avoid 100% CPU usage. The blockingPump() method is only available from
			// the nightly builds.
		}

	}

	static List<Territory> allTerritories;
	static List<Continent> continents;
	static Graph graph;
	static Agent[] agents;
	static JLabel announcement;
	static Map<String, Integer> agentIDS = new HashMap<>();

	public static void main(String[] args) {
		agentIDS.put("Aggresive", 0);
		agentIDS.put("Astar", 1);
		agentIDS.put("Greedy", 2);
		agentIDS.put("Human", 3);
		agentIDS.put("Pacifist", 4);
		agentIDS.put("RTAstar", 5);
		agentIDS.put("Passive", 6);
		JFrame frame = new JFrame("RISK");
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Can not set look and feel");
		}
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setJMenuBar(new MenuBar());

		initGraph();

		ir = new InputReader("input.txt");
		allTerritories = ir.getTerritories();
		continents = ir.getContinents();
		for (Continent continent : continents) {
			for (Territory territory : continent.getTerritories()) {
				territory.setContinent(continent);
			}
		}

		intialPlace("initialPlacement.txt", allTerritories);

		for (int i = 0; i < agents.length; i++) {
			System.out.println("Agent " + (i + 1) + " info :");
			System.out.println(agents[i]);
		}
		System.out.println();

		buildGraph();

		frame.add(new Intro(frame), BorderLayout.EAST);
		Viewer viewer = graph.display();
		ViewPanel graphView = viewer.getDefaultView();
		// viewer.disableAutoLayout();
		// new MainScreen(viewer,graph);

		// view.getCamera().setViewPercent(0.5);
		frame.add(graphView, BorderLayout.CENTER);

		announcement = new JLabel("");
		announcement.setHorizontalAlignment(SwingConstants.CENTER);
		announcement.setFont(announcement.getFont().deriveFont(30.0f));
		frame.add(announcement, BorderLayout.PAGE_START);

		frame.setSize(700, 700);
		frame.setVisible(true);

		makeAnnouncment("Starting the Game");
		sleep(2);

		if (agents[0].id == 1 || agents[0].id == 2 || agents[0].id == 5)
			startAIGame();
		else
			startGame();
	}

	private static void startGame() {
		boolean haveingAWinner = false;
		int agentId = 1;
		int maxGameTurns = (int) Math.pow(allTerritories.size(), 2);
		int turnCount = 0;

		while (!haveingAWinner && turnCount++ < maxGameTurns) {
			makeAnnouncment("Agent " + agentId + " Turn");
			sleep(2);

			Agent agent = agents[agentId - 1];

			Action action = agent.move();

			if (action != null) {
				System.out.println("still in the game " + action);
				long threadId = Thread.currentThread().getId();
				System.out.println("Thread # " + threadId + " is doing this task");

				// placing armies for AI player.
				placeArmies(action.agentPlacement);

				// making attack
				showAttack(action.attack);
			} else {
				makeAnnouncment("no Action for Agent " + agentId);
				sleep(2);
			}
			haveingAWinner = agent.isWinner();
			if (haveingAWinner)
				makeAnnouncment("It's all over , player " + agentId + " Won!");

			if (agentId == 1)
				agentId = 2;
			else
				agentId = 1;
		}

	}

	private static void startAIGame() {
		Agent ai = agents[0];
		ai.buildPath(agents[1]);
		boolean haveingAWinner = false;

		while (!ai.gameOver() && !ai.isWinner()) {
			Action action = ai.move();

			if (action == null)
				break; // Game End
			System.out.println("still in the game " + action);
			long threadId = Thread.currentThread().getId();
			System.out.println("Thread # " + threadId + " is doing this task");

			// placing armies for Passive player.
			placeArmies(action.passivePlacement);

			// placing armies for AI player.
			placeArmies(action.agentPlacement);

			// making attack
			makeAttack(action.attack);

			haveingAWinner = ai.isWinner();
			if (haveingAWinner)
				makeAnnouncment("It's all over , AI Won!");

		}
	}

	private static void placeArmies(ArmyPlacement placement) {
		if (placement != null && placement.terrID >= 0) {
			makeAnnouncment("adding " + placement.bonusAdded + " bonus for " + (placement.terrID + 1));

			graph.getNode((placement.terrID + 1) + "").addAttribute("ui.style", "text-color:yellow;");
			graph.getNode((placement.terrID + 1) + "").changeAttribute("ui.label",
					(placement.terrID + 1) + "_" + placement.armyCount);
			sleep(5);
			graph.getNode((placement.terrID + 1) + "").addAttribute("ui.style", "text-color:black;");
		}
	}

	private static void showAttack(Attack attack) {
		if (attack != null && attack.agentTerritory != null && attack.enemyTerritory != null) {
			int enemyTerrID = attack.enemyTerritory.getId() + 1, agetTerrID = attack.agentTerritory.getId() + 1;
			Edge e = findEdge(agetTerrID, enemyTerrID);
			if (e == null)
				return;

			makeAnnouncment(
					"attacking " + enemyTerrID + " from " + agetTerrID + " using " + attack.attackArmies + " armies");

			e.changeAttribute("ui.style", "fill-color: yellow;shape: cubic-curve; size:2px;");
			graph.getNode(agetTerrID + "").changeAttribute("ui.label",
					agetTerrID + "_" + attack.agentTerritory.getArmies());
			graph.getNode(enemyTerrID + "").changeAttribute("ui.label",
					enemyTerrID + "_" + attack.enemyTerritory.getArmies());
			graph.getNode(enemyTerrID + "").changeAttribute("ui.style", "fill-color: white,blue;");
			sleep(5);
			e.changeAttribute("ui.style", "shape: cubic-curve; size:2px; fill-color: black;");
		}
	}

	private static void makeAttack(Attack attack) {
		if (attack != null && attack.agentTerritory != null && attack.enemyTerritory != null) {
			int enemyTerrID = attack.enemyTerritory.getId() + 1, agetTerrID = attack.agentTerritory.getId() + 1;
			Edge e = findEdge(agetTerrID, enemyTerrID);
			if (e == null)
				return;

			makeAnnouncment(
					"attacking " + enemyTerrID + " from " + agetTerrID + " using " + attack.attackArmies + " armies");

			e.changeAttribute("ui.style", "fill-color: yellow;shape: cubic-curve; size:2px;");
			graph.getNode(agetTerrID + "").changeAttribute("ui.label",
					agetTerrID + "_" + (attack.agentTerritory.getArmies() - attack.attackArmies));
			graph.getNode(enemyTerrID + "").changeAttribute("ui.label",
					enemyTerrID + "_" + (attack.attackArmies - attack.enemyTerritory.getArmies()));
			graph.getNode(enemyTerrID + "").changeAttribute("ui.style", "fill-color: white,blue;");
			sleep(5);
			e.changeAttribute("ui.style", "shape: cubic-curve; size:2px; fill-color: black;");
		}
	}

	private static Edge findEdge(int from, int to) {
		String edgeID = from + "" + to;
		Edge e = graph.getEdge(edgeID);
		if (e == null) {
			edgeID = to + "" + from;
			e = graph.getEdge(edgeID);
		}
		return e;
	}

	private static void makeAnnouncment(String text) {
		announcement.setText(text);
		System.out.println(text);
	}

	static void initGraph() {
		System.setProperty("org.graphstream.ui.renderer", "org.graphstream.ui.j2dviewer.J2DGraphRenderer");
		graph = new SingleGraph("RISK");
		graph.addAttribute("ui.stylesheet", "graph {fill-color: gray;}"
				+ "node { text-alignment: justify; size: 70px, 40px; "
				+ "text-offset: 0px, 4px; text-size:20px; text-color: black; fill-color:yellow,white; fill-mode: gradient-vertical;}"
				+ "edge { shape: cubic-curve; size:2px; fill-color: black;}");
		graph.addAttribute("ui.quality");
		graph.addAttribute("ui.antialias");
	}

	public static void intialPlace(String filePath, List<Territory> allTerritories) {
		try {
			Scanner scanner = new Scanner(new File(filePath));
			int playerCount = scanner.nextInt();
			agents = new Agent[playerCount];
			for (int i = 0; i < playerCount; i++) {
				int agentID = agentIDS.get(scanner.next());
				agents[i] = Agent.agentFactory(agentID, null, continents, allTerritories);
				if (i > 0) {
					agents[i - 1].setEnemy(agents[i]);
				}
			}
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

	static void buildGraph() {
		Map<Integer, GNode> allGraphNode = new HashMap<>();
		String[] colors = { "blue", "red", "darkGray", "gray", "green", "magenta", "lightGray", "orange", "yellow",
				"pink", "cyan" };
		Shape shapes[] = StyleConstants.Shape.values();

		// building nodes.
		for (Territory territory : allTerritories) {
			graph.addNode((territory.getId() + 1) + "").addAttribute("ui.label",
					(territory.getId() + 1) + "_" + (territory.getArmies()));
			Node node = graph.getNode((territory.getId() + 1) + "");
			GNode gn = new GNode(node, territory);
			allGraphNode.put(territory.getId(), gn);
		}
		// building edges.
		for (Pair<Integer, Integer> pair : ir.edges) {
			graph.addEdge((pair.getKey() + 1) + "" + (pair.getValue() + 1), (pair.getKey() + 1) + "",
					"" + (pair.getValue() + 1));
		}
		// marking continent.
		int i = 0;
		String[] style = new String[graph.getNodeCount()];
		for (Continent continent : continents) {
			for (Territory territory : continent.getTerritories()) {
				// graph.getNode((territory.getId() + 1) + "").addAttribute(
				// "ui.style" + "shape:" + shapes[i % shapes.length].toString().toLowerCase() +
				// ";");
				style[territory.getId()] = "shape: " + shapes[i % shapes.length].toString().toLowerCase() + ";";
			}
			i++;
		}
		// marking players territories.
		i = 0;
		for (int j = 0; j < agents.length; j++) {
			for (Territory territory : agents[j].getTerritories()) {
				// graph.getNode((territory.getId() + 1) + "")
				// .addAttribute("ui.style" + "fill-color: " + colors[i % colors.length] + ";");
				style[territory.getId()] += "fill-color: white," + colors[j % colors.length] + ";";
			}
		}

		for (int j = 0; j < graph.getNodeCount(); j++) {
			graph.getNode((j + 1) + "").addAttribute("ui.style", style[j]);
		}
	}

	/* Viewer Listener. */
	public void viewClosed(String id) {
		loop = false;
	}

	public void buttonPushed(String id) {
		System.out.println("Button pushed on node " + id);
	}

	public void buttonReleased(String id) {
		System.out.println("Button released on node " + id);
	}

	static void sleep(int sec) {
		try {
			Thread.sleep(sec * 1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void buildNeighbors(List<Territory> all) {
		Collections.sort(all, new Comparator<Territory>() {
			@Override
			public int compare(Territory o1, Territory o2) {
				if (o1.getId() > o2.getId())
					return 1;
				if (o1.getId() < o2.getId())
					return -1;
				return 0;
			}

		});
		for (Pair<Integer, Integer> pair : ir.edges) {
			Territory V1 = all.get(pair.getKey());
			Territory V2 = all.get(pair.getValue());
			V1.addNeighbor(V2);
			V2.addNeighbor(V1);
		}
	}
}
