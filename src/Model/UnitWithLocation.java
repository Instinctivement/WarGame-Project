package Model;

import View.Hexagonegraph;

public class UnitWithLocation {
    private Unite unit;
    private int centerX, centerY;
      private Hexagonegraph hexagone;

    public UnitWithLocation(Unite unit, int centerX, int centerY, Hexagonegraph hexagone) {
        this.unit = unit;
        this.centerX = centerX;
        this.centerY = centerY;
        this.hexagone = hexagone;

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

}