package Model;

import java.io.Serializable;

/**
 *
 * @author franc
 */
public class Terrain implements Serializable {
    private int movementCost; // Le coût en mouvement pour traverser ce terrain

    public Terrain(int movementCost) {
        this.movementCost = movementCost;
    }

    public int getCost() {
        return movementCost;
    }
}
