package Model;

import java.io.Serializable;

public class Arme implements Serializable {
    private String nom;
    private int potentielAttaque;
    
    public Arme() {
        this.nom = "Excalibur";
        this.potentielAttaque = 2;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getPotentiel_attaque() {
        return potentielAttaque;
    }

    public void setPotentiel_attaque(int potentiel_attaque) {
        this.potentielAttaque = potentiel_attaque;
    }
    
    
}
