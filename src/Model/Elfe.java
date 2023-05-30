package Model;

import java.io.Serializable;

public class Elfe extends Unite implements Serializable {
    private int id;
    private String name = "Elfe";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Elfe(User user, int id) {
        super(4, 8, 4, 25, 4, 10, user, 4, 25);
        this.id = id;
    }
    
    @Override
    public void attaquer(Unite unite) {
        int totalAttque = this.nbAttaque + this.arme.getPotentiel_attaque();
        int resultatAttaque = totalAttque - unite.getNbDefense();
        if (resultatAttaque > 0) {
            int pvRestants = unite.getNbPv() - resultatAttaque;
            unite.setNbPv(pvRestants);
        }
    }
    
    @Override
    public void deplacer() {
        // Logique spécifique au déplacement de l'elfe
    }
    
    @Override
    public void recuperer() {
        // Logique spécifique à la récupération de l'elfe
    }
}

