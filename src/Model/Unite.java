package Model;

public abstract class Unite {
    protected int nbDeplacement;
    protected int nbAttaque;
    protected int nbDefense;
    protected int nbPv;
    protected int nbVision;
    protected double tauxRecuperation;
    protected Arme arme;
    protected User user;
    protected String name;
    
    public Unite(int nbDeplacement, int nbAttaque, int nbDefense, int nbPv, int nbVision, double tauxRecuperation, User user) {
        this.nbDeplacement = nbDeplacement;
        this.nbAttaque = nbAttaque;
        this.nbDefense = nbDefense;
        this.nbPv = nbPv;
        this.nbVision = nbVision;
        this.tauxRecuperation = tauxRecuperation;
        this.arme = new Arme();
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getNbDeplacement() {
        return nbDeplacement;
    }

    public void setNbDeplacement(int nbDeplacement) {
        this.nbDeplacement = nbDeplacement;
    }

    public int getNbAttaque() {
        return nbAttaque;
    }

    public void setNbAttaque(int nbAttaque) {
        this.nbAttaque = nbAttaque;
    }

    public int getNbDefense() {
        return nbDefense;
    }

    public void setNbDefense(int nbDefense) {
        this.nbDefense = nbDefense;
    }

    public int getNbPv() {
        return nbPv;
    }

    public void setNbPv(int nbPv) {
        this.nbPv = nbPv;
    }

    public int getNbVision() {
        return nbVision;
    }

    public void setNbVision(int nbVision) {
        this.nbVision = nbVision;
    }

    public double getTauxRecuperation() {
        return tauxRecuperation;
    }

    public void setTauxRecuperation(double tauxRecuperation) {
        this.tauxRecuperation = tauxRecuperation;
    }

    public Arme getArme() {
        return arme;
    }

    public void setArme(Arme arme) {
        this.arme = arme;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public int getUserID() {
        return user.getId();
    }
    
    public abstract void attaquer(Unite unite);
    
    public abstract void deplacer();
    
    public abstract void recuperer();
}

