package Model;

public class Magicien extends Unite {
    private int id;
    
    public Magicien(Arme[] armes, int id) {
        super(2, 3, 7, 15, 6, 25, armes);
        this.id = id;
    }
    
    @Override
    public void attaquer() {
        // Logique spécifique à l'attaque du magicien
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

