package Model;

public class Cavalier extends Unite {
    private int id;
    
    public Cavalier(User user, int id) {
        super(6, 5, 3, 25, 8, 5, user);
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
        // Logique spécifique au déplacement du cavalier
    }
    
    @Override
    public void recuperer() {
        // Logique spécifique à la récupération du cavalier
    }
}

