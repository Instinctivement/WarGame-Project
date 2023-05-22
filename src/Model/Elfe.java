package Model;

public class Elfe extends Unite {
    private int id;
    
    public Elfe(User user, int id) {
        super(4, 8, 4, 25, 9, 10, user);
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

