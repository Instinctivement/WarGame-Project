package Model;

public class Soldat extends Unite {
    private int id;
    
    public Soldat(Arme[] armes, int id) {
        super(4, 5, 3, 20, 8, 5, armes);
        this.id = id;
    }
    
    @Override
    public void attaquer() {
        // Logique spécifique à l'attaque du soldat
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
