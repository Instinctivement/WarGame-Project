package Model;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.Serializable;

public class BarreDeVie extends JComponent implements Serializable {
    private int valeurMax;
    private int valeurActuelle;
    private int x;
    private int y;

    public int getValeurMax() {
        return valeurMax;
    }

    public void setValeurMax(int valeurMax) {
        this.valeurMax = valeurMax;
    }
    public int getValeurActuelle() {
        return valeurActuelle;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public BarreDeVie(int valeurMax, int x, int y) {
        this.valeurMax = valeurMax;
        this.valeurActuelle = valeurMax;
        this.x = x;
        this.y = y;
    }

    public void setValeurActuelle(int valeurActuelle) {
        this.valeurActuelle = Math.max(0, Math.min(valeurActuelle, valeurMax));
        repaint(); // Redessine la barre de vie
    }
    
}
