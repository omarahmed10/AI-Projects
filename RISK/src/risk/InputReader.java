package risk;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class InputReader {
    private int numTerritory, numEdges, numPartitions;
    private String[] splited;
    String line;
    private ArrayList<Territory> Countries;
    private ArrayList<Continent> Continents;

    public InputReader(String fileName) {
        line = null;
        Countries = new ArrayList<>();
        Continents = new ArrayList<>();
        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);
            // # of Territory
            line = br.readLine();
            splited = line.split("\\s+");
            numTerritory = Integer.parseInt(splited[1]);
            for (int i = 0; i < numTerritory; i++) {
                Territory country = new Territory(i);
                Countries.add(country);
                System.out.println("Country " + i + " is added");
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
        for (int i = 0; i < numEdges; i++) {
            line = br.readLine();
            line = line.replace("(", "");
            line = line.replace(")", "");
            splited = line.split("\\s+");
            int V1 = Integer.parseInt(splited[0]) - 1;
            int V2 = Integer.parseInt(splited[1]) - 1;
            Countries.get(V1).addNeighbor(Countries.get(V2));
            Countries.get(V2).addNeighbor(Countries.get(V1));
            System.out.println("Edge " + V1 + " , " + V2 + " is added");
        }
    }

    private void readContinents(BufferedReader br) throws IOException {
        line = br.readLine();
        splited = line.split("\\s+");
        numPartitions = Integer.parseInt(splited[1]);
        for (int i = 0; i < numPartitions; i++) {
            line = br.readLine();
            splited = line.split("\\s+");
            int val = Integer.parseInt(splited[0]);
            ArrayList<Territory> members = new ArrayList<>();
            int V1 = Integer.parseInt(splited[1]) - 1;
            int V2 = Integer.parseInt(splited[2]) - 1;
            members.add(Countries.get(V1));
            members.add(Countries.get(V2));
            Continents.add(new Continent(i, members, val));
            System.out.println("Continent " + i + " and its members " + V1 + " " + V2 + " is added" + "with value "
                    + val);
        }
    }

    public ArrayList<Territory> getCountries() {
        return Countries;
    }

    public ArrayList<Continent> getContinents() {
        return Continents;
    }

    public static void main(String[] args) {
        InputReader in = new InputReader("src\\risk\\Input.txt");
    }
}
