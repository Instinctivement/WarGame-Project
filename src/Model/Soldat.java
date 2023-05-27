package Model;

public class Soldat extends Unite {
    private int id;
    private String name = "Soldat";

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
    
    
    public Soldat(User user, int id) {
        super(4, 5, 3, 20, 4, 5, user);
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
        // Logique spécifique au déplacement du soldat
    }
    
    @Override
    public void recuperer() {
        // Logique spécifique à la récupération du soldat
    }
}
