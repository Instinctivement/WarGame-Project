package Model;

import java.io.Serializable;

public class Magicien extends Unite implements Serializable {
    private int id;
    private String name = "Magicien";

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
    
    public Magicien(User user, int id) {
        super(3, 3, 7, 15, 4, 25, user, 3, 15);
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
        // Logique spécifique au déplacement du magicien
    }
    
    @Override
    public void recuperer() {
        // Logique spécifique à la récupération du magicien
    }
}

