package View;

import Model.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.Timer;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.io.File;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.List;
import java.util.ArrayList;

public class PlateauHexagone extends JPanel implements ImageObserver {

    private static final int RADIUS = 40;
    private static final int WIDTH = 12;
    private static final int HEIGHT = 11;
    private int imageWidth = 866; // Largeur de l'image
    private int imageHeight = 680; // Hauteur de l'image
    int startX = 1; // Dimension de départ en x
    int startY = 1; // Dimension de départ en y
    int centerX = 0;
    int centerY = 0;

    private Hexagonegraph[][] hexagones = new Hexagonegraph[WIDTH][HEIGHT];
    private Hexagonegraph highlightedHexagone = null;
    private List<UnitWithLocation> unitLocations = new ArrayList<>();

    private Image bgImage;
    private Image arch;
    private Image soldat;
    private Image cavalier;
    private Image elfe;
    private Image magicien;

    private volatile Unite currentUnit;

    public void setCurrentUnit(Unite unit) {
        this.currentUnit = unit;
    }

    public Unite getCurrentUnit() {
        return currentUnit;
    }
    

    public void addUnit(Unite unit, int centerX, int centerY, Hexagonegraph hexagone) {
        if (!checkUnitInHexagone(hexagone)) {
            this.unitLocations.add(new UnitWithLocation(unit, centerX, centerY, hexagone));
        }
    }

    public boolean checkUnitInHexagone(Hexagonegraph hexagone) {
        for (UnitWithLocation unitLocation : unitLocations) {
            if (unitLocation.getHexagone().equals(hexagone)) {
                return true; // retourne true si une unité existe dans cet hexagone
            }
        }
        return false; // retourne false si aucune unité n'existe dans cet hexagone
    }

    public PlateauHexagone() {
        try {
            bgImage = ImageIO.read(new File("img/finalwargame.png"));
            arch = ImageIO.read(new File("img/ArcherB.png"));
            soldat = ImageIO.read(new File("img/SoldatB.png"));
            cavalier = ImageIO.read(new File("img/CavalierB.png"));
            elfe = ImageIO.read(new File("img/ElfeB.png"));
            magicien = ImageIO.read(new File("img/MagicienB.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        setBackground(Color.WHITE);

        setLayout(new BorderLayout());

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                int clickX = e.getX();
                int clickY = e.getY();
                System.out.println("mmouse clicked: " + clickX + "  " + clickY);

                // Check if the click coordinates are within the bounds of the arch image
                if (clickX >= 600 && clickX <= 630 && clickY >= 100 && clickY <= 175) {
                    System.out.println("Clicked on the arch image");
                }
                for (int i = 0; i < WIDTH; i++) {
                    for (int j = 0; j < HEIGHT; j++) {
                        Hexagonegraph h = hexagones[i][j];
                        if (h.getPolygon().contains(e.getPoint())) {
                            // Calculate the center coordinates of the clicked hexagon
                            // Calculate the center coordinates of the clicked hexagon
                            centerX = h.getX() + (RADIUS / 2);
                            centerY = h.getY() + (RADIUS / 2);
                            addUnit(currentUnit, centerX, centerY, h);
                            System.out.println("Center coordinates of the clicked hexagon: (" + centerX + ", " + centerY + ")");

                            h.setBorderColor(Color.red);
                            Terrain terrain = h.getTerrain1();
                            System.out.println("Hexagone cliqué à la position matricielle (x, y) : (" + h.getMatrixX() + ", " + h.getMatrixY() + ")");
                            System.out.println(terrain.getClass().getSimpleName());

                            addUnit(currentUnit, centerX, centerY, h);

                            repaint();  // Mettre à jour le dessin du plateau
                            setCurrentUnit(null);

                            Timer timer = new Timer(300, new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    // Get the last mouse location from the MouseInfo
                                    Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
                                    SwingUtilities.convertPointFromScreen(mouseLocation, getParent());

                                    if (h.getPolygon().contains(mouseLocation)) {//on verifie si la souris est toujours dans l'hexagone
                                        h.setBorderColor(Color.YELLOW);//c'est du detail visuel
                                    } else {
                                        h.setBorderColor(Color.BLACK);
                                    }

                                    repaint();
                                }
                            });
                            timer.setRepeats(false);
                            timer.start();

                            repaint();
                            return;
                        }
                    }
                }
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                if (highlightedHexagone != null) {
                    highlightedHexagone.setBorderColor(Color.BLACK);
                    highlightedHexagone = null;
                }

                for (int i = 0; i < WIDTH; i++) {
                    for (int j = 0; j < HEIGHT; j++) {
                        Hexagonegraph h = hexagones[i][j];
                        if (h.getPolygon().contains(e.getPoint())) {
                            h.setBorderColor(Color.YELLOW);
                            highlightedHexagone = h;
                            repaint();
                            return;
                        }
                    }
                }
            }
        });

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bgImage != null) {
            int x = 0; // Coordonnée x souhaitée
            int y = 0;  // Coordonnée y souhaitée
            g.drawImage(bgImage, x, y, imageWidth, imageHeight, this);
        }

        for (int i = startX; i < startX + WIDTH; i++) {
            for (int j = startY; j < startY + HEIGHT; j++) {
                int offsetX = startX * RADIUS;
                int offsetY = startY * RADIUS / 2;
                int x = (int) (i * RADIUS * Math.sqrt(3)) + 5 - offsetX;
                int y = (int) (j * RADIUS * 1.5) - offsetY;
                if (j % 2 != 0) {
                    x += (int) (RADIUS * Math.sqrt(3) / 2);
                }
                if (hexagones[i - startX][j - startY] == null) {
                    hexagones[i - startX][j - startY] = new Hexagonegraph(x, y, RADIUS, new Plaine(), startX, startY);//plaine
                }

                //i=colonnes j=lignes 
                if (i == 3 && j == 2
                        || // Montagne 
                        i >= 2 && i <= 4 && j == 3
                        || i == 9 && j == 11
                        || i >= 9 && i <= 10 && j == 10
                        || i >= 9 && i <= 10 && j == 9) {
                    hexagones[i - startX][j - startY].setTerrain(new Montagne());
                }
                if (i >= 1 && i <= 4 && j == 4
                        || //Riviere
                        i >= 4 && i <= 7 && j == 5
                        || i >= 9 && i <= 12 && j == 8
                        || i == 8 && j == 7
                        || i == 8 && j == 6) {
                    hexagones[i - startX][j - startY].setTerrain(new Riviere());
                }
                if (i >= 4 && i <= 7 && j == 6
                        || //Foret
                        i >= 7 && i <= 9 && j == 4
                        || i >= 6 && i <= 7 && j == 3
                        || i >= 8 && i <= 9 && j == 5
                        || i == 4 && j == 7
                        || i == 6 && j == 7
                        || i == 7 && j == 8) {
                    hexagones[i - startX][j - startY].setTerrain(new Foret());
                }
                if (i >= 1 && i <= 6 && j == 1
                        || //Neige
                        i >= 1 && i <= 2 && j == 2
                        || i >= 4 && i <= 6 && j == 2
                        || i >= 4 && i <= 5 && j == 3
                        || i == 1 && j == 3
                        || i >= 7 && i <= 8 && j == 9
                        || i >= 11 && i <= 12 && j == 9
                        || i >= 7 && i <= 8 && j == 10
                        || i >= 11 && i <= 12 && j == 10
                        || i >= 6 && i <= 8 && j == 11
                        || i >= 10 && i <= 12 && j == 11) {
                    hexagones[i - startX][j - startY].setTerrain(new Glace());
                }

                hexagones[i - startX][j - startY].dessiner(g);
            }

        }
        for (UnitWithLocation u : unitLocations) {
            if (u.getUnit() instanceof Archer) {
                g.drawImage(arch, u.getCenterX() - 30, u.getCenterY() - 60, 30, 75, this);

            } else if (u.getUnit() instanceof Soldat) {
                g.drawImage(soldat, u.getCenterX() - 30, u.getCenterY() - 60, 30, 75, this);
            } else if (u.getUnit() instanceof Cavalier) {
                g.drawImage(cavalier, u.getCenterX() - 30, u.getCenterY() - 60, 30, 75, this);
            } else if (u.getUnit() instanceof Elfe) {
                g.drawImage(elfe, u.getCenterX() - 30, u.getCenterY() - 60, 30, 75, this);
            } else if (u.getUnit() instanceof Magicien) {
                g.drawImage(magicien, u.getCenterX() - 30, u.getCenterY() - 60, 30, 75, this);
            }
        }
    }
}
