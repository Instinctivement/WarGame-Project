package Model;

public class Elfe extends Unite {
    private int id;
    
    public Elfe(Arme[] armes, int id) {
        super(4, 8, 4, 25, 9, 10, armes);
        this.id = id;
    }
    
    @Override
    public void attaquer() {
        // Logique spécifique à l'attaque de l'elfe
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

