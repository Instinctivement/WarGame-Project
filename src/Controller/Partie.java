package Controller;

import Model.User;
import java.io.Serializable;


public class Partie implements Serializable {
    User user1;
    User user2;
    private int totalTurns;
    private PlateauHexagoneCtr plateauLogique = new PlateauHexagoneCtr();

    public Partie(User user1, User user2, int totalTurns) {
        this.user1 = user1;
        this.user2 = user2;
        this.totalTurns = totalTurns;
    }

    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
    }

    public PlateauHexagoneCtr getPlateauLogique() {
        return plateauLogique;
    }

    public void setPlateauLogique(PlateauHexagoneCtr plateauLogique) {
        this.plateauLogique = plateauLogique;
    }
}
