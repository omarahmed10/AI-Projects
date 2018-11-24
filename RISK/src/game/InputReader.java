package game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.util.Pair;
import map.Continent;
import map.Territory;

public class InputReader {
	private int numTerritory, numEdges, numPartitions;
	private String[] splited;
	String line;
	public List<Territory> territories;
	public List<Pair<Integer, Integer>> edges;
	private List<Continent> continents;

	public InputReader(String fileName) {
		line = null;
		territories = new ArrayList<>();
		continents = new ArrayList<>();
		try {
			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(fr);
			// # of Territory
			line = br.readLine();
			splited = line.split("\\s+");
			numTerritory = Integer.parseInt(splited[1]);
			for (int i = 0; i < numTerritory; i++) {
				Territory territory = new Territory(i);
				territories.add(territory);
				System.out.println("Territory " + i + " is added");
			}
			// #Edges
			readEdges(br);
			// Continents
			readContinents(br);

			br.close();
		} catch (IOException ex) {
			System.out.println("Error in File '" + fileName + "'");
		}
	}

	private void readEdges(BufferedReader br) throws IOException {
		line = br.readLine();
		splited = line.split("\\s+");
		numEdges = Integer.parseInt(splited[1]);
		edges = new ArrayList<>();
		for (int i = 0; i < numEdges; i++) {
			line = br.readLine();
			line = line.replace("(", "");
			line = line.replace(")", "");
			splited = line.split("\\s+");
			int V1 = Integer.parseInt(splited[0]) - 1;
			int V2 = Integer.parseInt(splited[1]) - 1;
			edges.add(new Pair<Integer, Integer>(V1, V2));
			territories.get(V1).addNeighbor(territories.get(V2));
			territories.get(V2).addNeighbor(territories.get(V1));
			System.out.println("Edge " + V1 + " , " + V2 + " is added");
		}
	}

	private void readContinents(BufferedReader br) throws IOException {
		line = br.readLine();
		splited = line.split("\\s+");
		numPartitions = Integer.parseInt(splited[1]);
		for (int i = 0; i < numPartitions; i++) {
			line = br.readLine();

			Scanner scanner = new Scanner(line);

			int val = scanner.nextInt();
			continents.add(new Continent(i, val));

			System.out.print("Continent " + i + " is added " + "with value " + val + " and its members ");

			// the territories numvber in a continent may vary
			while (scanner.hasNextInt()) {
				int terrId = scanner.nextInt() - 1;
				continents.get(i).addTerritory(territories.get(terrId));
				System.out.print(terrId + " ");
			}

			System.out.println();

			scanner.close();
		}
	}

	public List<Territory> getTerritories() {
		return territories;
	}

	public List<Continent> getContinents() {
		return continents;
	}

	// public static void main(String[] args) {
	// InputReader in = new InputReader("src\\risk\\Input.txt");
	// }
}
