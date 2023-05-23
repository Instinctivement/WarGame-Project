package View;

import Model.Terrain;
import java.awt.*;

public class Hexagonegraph {
    private int x;
    private int y;
    private int radius;
    private Color borderColor = Color.BLACK;
    private Color fillColor = new Color(255, 255, 255, 0);
    private Polygon p;
    private Terrain terrain;
    private int startX;
    private int startY;

    public Hexagonegraph(int x, int y, int radius, Terrain terrain, int startX, int startY) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.terrain = terrain;
        this.startX = startX;
        this.startY = startY;

        p = new Polygon();
        for (int i = 0; i < 6; i++) {
            int angle = 60 * i - 30;
            int dx = (int) (radius * Math.cos(Math.toRadians(angle)));
            int dy = (int) (radius * Math.sin(Math.toRadians(angle)));
            p.addPoint(x + dx, y + dy);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getMatrixX() {
        int x = this.y % 2 == 0 ? this.x : this.x - (int) (radius * Math.sqrt(3) / 2);
        return (x - (startX - 1) * radius * (int) (Math.sqrt(3) / 2)) / (int) (radius * Math.sqrt(3));
    }
    
    public int getMatrixY() {
        return (this.y - (startY - 1) * radius * 3 / 2) / (int) (radius * 1.5);
    }
    

    public Terrain getTerrain1() {
        return terrain;
    }
    public void setTerrain(Terrain newTerrain) {
        this.terrain = newTerrain;
    }
    
    

    public void dessiner(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke(); // Sauvegarder le trait précédent

        // Définir un trait plus épais
        float thickness = 1.0f; // Épaisseur du trait en pixels
        g2d.setStroke(new BasicStroke(thickness));

        g2d.setColor(fillColor);
        g2d.fillPolygon(p);

        g2d.setColor(borderColor);
        g2d.drawPolygon(p); // Utiliser g2d pour dessiner le polygone

        g2d.setStroke(oldStroke); // Restaurer le trait précédent
    }

    public Polygon getPolygon() {
        return p;
    }

    public void setFillColor(Color fillColor) {
        this.fillColor = new Color(fillColor.getRed(), fillColor.getGreen(), fillColor.getBlue(),64);
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }
    
}