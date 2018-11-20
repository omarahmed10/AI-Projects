package risk;

import java.util.ArrayList;

public class Continent {
    private int id;
    private ArrayList<Territory> members;
    private int value;

    public Continent(int id, ArrayList<Territory> members, int val) {
        this.id = id;
        this.members = members;
        this.value = val;
    }
}
