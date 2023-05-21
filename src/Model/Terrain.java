package Model;

/**
 *
 * @author franc
 */
public class Terrain {
    private int movementCost; // Le coût en mouvement pour traverser ce terrain

    public Terrain(int movementCost) {
        this.movementCost = movementCost;
    }

    public int getCost() {
        return movementCost;
    }
}
