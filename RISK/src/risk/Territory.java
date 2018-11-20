package risk;

import java.util.ArrayList;

public class Territory {
    private int id;
    private Player owner;
    private int armies;
    ArrayList<Territory> neighbors;

    public Territory(int id) {
        this.id = id;
        this.neighbors = new ArrayList<>();
    }

    public ArrayList<Territory> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(ArrayList<Territory> neighbors) {
        this.neighbors = neighbors;
    }

    public void addNeighbor(Territory country) {
        neighbors.add(country);
    }

}
