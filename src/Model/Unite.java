package Model;

public abstract class Unite {
    protected int nb_deplacement;
    protected int nb_attaque;
    protected int nb_defense;
    protected int nb_pv;
    protected int nb_vision;
    protected double taux_recuperation;
    protected Arme[] armes;
    
    public Unite(int nb_deplacement, int nb_attaque, int nb_defense, int nb_pv, int nb_vision, double taux_recuperation, Arme[] armes) {
        this.nb_deplacement = nb_deplacement;
        this.nb_attaque = nb_attaque;
        this.nb_defense = nb_defense;
        this.nb_pv = nb_pv;
        this.nb_vision = nb_vision;
        this.taux_recuperation = taux_recuperation;
        this.armes = armes;
    }
    
    public abstract void attaquer();
    
    public abstract void deplacer();
    
    public abstract void recuperer();
}

