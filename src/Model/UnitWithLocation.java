package Model;

import View.Hexagonegraph;

public class UnitWithLocation {

    private Unite unit;
    private int centerX, centerY;
    private Hexagonegraph hexagone;
    private BarreDeVie lifeBar = new BarreDeVie(0,0,0);

    public BarreDeVie getLifeBar() {
        return lifeBar;
    }

    public void setLifeBar(BarreDeVie lifeBar) {
        this.lifeBar = lifeBar;
    }

    public UnitWithLocation(Unite unit, int centerX, int centerY, Hexagonegraph hexagone) {
        this.unit = unit;
        this.centerX = centerX;
        this.centerY = centerY;
        this.hexagone = hexagone;
        this.lifeBar.setX(centerX-40);
        this.lifeBar.setY(centerY-60);
        this.lifeBar.setValeurMax(unit.nbPv);
        this.lifeBar.setValeurActuelle(unit.nbPv);
    }

    public Unite getUnit() {
        return unit;
    }

    public int getCenterX() {
        return centerX;
    }

    public int getCenterY() {
        return centerY;
    }

    public void setCenterX(int centerX) {
        this.centerX = centerX;
    }

    public void setCenterY(int centerY) {
        this.centerY = centerY;
    }

    public Hexagonegraph getHexagone() {
        return hexagone;
    }

    public void setHexagone(Hexagonegraph hexagone) {
        this.hexagone = hexagone;
    }
    
    public int getUnitId() {
        return unit.getUserID();
    }

}
