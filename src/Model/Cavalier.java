package Model;

public class Cavalier extends Unite {
    private int id;
    
    public Cavalier(Arme[] armes, int id) {
        super(6, 5, 3, 25, 8, 5, armes);
        this.id = id;
    }
    
    @Override
    public void attaquer() {
        // Logique spécifique à l'attaque du cavalier
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

