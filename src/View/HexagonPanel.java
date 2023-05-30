import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

public class HexagonPanel extends JPanel implements Serializable {
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        int[] xPoints = {getWidth() / 2, (int) (getWidth() * 0.75), (int) (getWidth() * 0.75), getWidth() / 2, (int) (getWidth() * 0.25), (int) (getWidth() * 0.25)};
        int[] yPoints = {0, getHeight() / 4, (int) (getHeight() * 0.75), getHeight(), (int) (getHeight() * 0.75), getHeight() / 4};

        g.setColor(Color.RED); // Couleur de remplissage de l'hexagone
        g.fillPolygon(xPoints, yPoints, 6);

        g.setColor(Color.BLACK); // Couleur des contours de l'hexagone
        g.drawPolygon(xPoints, yPoints, 6);
    }
}
