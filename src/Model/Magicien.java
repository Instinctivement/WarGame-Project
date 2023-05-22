package Model;

public class Magicien extends Unite {
    private int id;
    
    public Magicien(User user, int id) {
        super(2, 3, 7, 15, 6, 25, user);
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

