package Model;

public class Archer extends Unite {
    private int id;
    
    public Archer(Arme[] armes, int id) {
        super(2, 7, 2, 20, 10, 5, armes);
        this.id = id;
    }
    
    @Override
    public void attaquer() {
        // Logique spécifique à l'attaque de l'archer
    }
    
    @Override
    public void deplacer() {
        // Logique spécifique au déplacement de l'archer
    }
    
    @Override
    public void recuperer() {
        // Logique spécifique à la récupération de l'archer
    }
}
