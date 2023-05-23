package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class ImageClickable extends JPanel {
    private Image image;
    private Color defaultColor;
    private Color clickedColor;

    public ImageClickable(Image image, Color defaultColor, Color clickedColor) {
        this.image = image;
        this.defaultColor = defaultColor;
        this.clickedColor = clickedColor;

        setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setBackground(clickedColor);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Création d'une fenêtre pour tester la classe ImageClickable
                JFrame frame = new JFrame("Image Clickable Example");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                
                // Chargement de l'image depuis un fichier (remplacez "path/vers/image.png" par le chemin de votre image)
                Image image = ImageIO.read(new File("SoldR.png"));
                
                
                // Création d'une instance de ImageClickable avec l'image chargée
                ImageClickable imageClickable = new ImageClickable(image, Color.WHITE, Color.YELLOW);
                
                // Ajout de l'instance à la fenêtre
                frame.add(imageClickable, BorderLayout.CENTER);
                
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            } catch (IOException ex) {
                Logger.getLogger(ImageClickable.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }
}
