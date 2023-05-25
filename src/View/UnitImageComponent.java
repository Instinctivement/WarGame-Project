package view;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import javax.swing.JFrame;
import javax.imageio.ImageIO;

public class UnitImageComponent extends JFrame {
    private Image archImage;
    private int width;
    private int height;

    public UnitImageComponent(String imagePath, int width, int height) {
        try {
            archImage = ImageIO.read(getClass().getResource("img/arch.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.width = width;
        this.height = height;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (archImage != null) {
            g.drawImage(archImage, 12, 23, width, height, this);
        }
    }
}
