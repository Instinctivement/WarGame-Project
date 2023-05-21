package Model;

/**
 *
 * @author franc
 */
public class Arme {
    private int id;
    private String nom;
    private int potentielAttaque;
    
    public Arme(int id, String nom, int potentiel_attaque) {
        this.id = id;
        this.nom = nom;
        this.potentielAttaque = potentiel_attaque;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
