package View;

import Controller.PlateauHexagoneCtr;
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



public class PlateauHexagoneVue extends JPanel implements ImageObserver {
    private static final int RADIUS = 40;
    private static final int WIDTH = 12;
    private static final int HEIGHT = 11;
    private int imageWidth = 866; // Largeur de l'image
    private int imageHeight = 680; // Hauteur de l'image
    private int startX = 1; // Dimension de départ en x
    private int startY = 1; // Dimension de départ en y
    private int centerX = 0;
    private int centerY = 0;

    private Hexagonegraph[][] hexagones = new Hexagonegraph[WIDTH][HEIGHT];
    private Hexagonegraph highlightedHexagone = null;

    private Image bgImage;

    private Image archer1;
    private Image soldat1;
    private Image cavalier1;
    private Image elfe1;
    private Image magicien1;

    private Image archer2;
    private Image soldat2;
    private Image cavalier2;
    private Image elfe2;
    private Image magicien2;
    
    private PlateauHexagoneCtr plateauLogique = new PlateauHexagoneCtr();

    public PlateauHexagoneCtr getPlateauLogique() {
        return plateauLogique;
    }

    public void setPlateauLogique(PlateauHexagoneCtr plateauLogique) {
        this.plateauLogique = plateauLogique;
    }
    
    public PlateauHexagoneVue() {
        try {
            bgImage = ImageIO.read(new File("img/finalwargame.png"));

            archer1 = ImageIO.read(new File("img/ArcherB.png"));
            soldat1 = ImageIO.read(new File("img/SoldatB.png"));
            cavalier1 = ImageIO.read(new File("img/CavalierB.png"));
            elfe1 = ImageIO.read(new File("img/ElfeB.png"));
            magicien1 = ImageIO.read(new File("img/MagicienB.png"));

            archer2 = ImageIO.read(new File("img/ArcherR.png"));
            soldat2 = ImageIO.read(new File("img/SoldatR.png"));
            cavalier2 = ImageIO.read(new File("img/CavalierR.png"));
            elfe2 = ImageIO.read(new File("img/ElfeR.png"));
            magicien2 = ImageIO.read(new File("img/MagicienR.png"));

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

                            System.out.println("Center coordinates of the clicked hexagon: (" + centerX + ", " + centerY + ")");

                            h.setBorderColor(Color.red);
                            Terrain terrain = h.getTerrain1();
                            System.out.println("Hexagone cliqué à la position matricielle (x, y) : (" + h.getMatrixX() + ", " + h.getMatrixY() + ")");
                            System.out.println(terrain.getClass().getSimpleName());
                            System.out.println(terrain.getCost());

                            //PlateauLogique plateauLogique = new PlateauHexagoneCtr();
                            UnitWithLocation unitAtHexagone = plateauLogique.Position_unite(h);
                            if (plateauLogique.getSelectedUnit() == null && unitAtHexagone == null && plateauLogique.getCurrentUnit() != null) {
                                
                                plateauLogique.addUnit(plateauLogique.getCurrentUnit(), centerX, centerY, h);
                            }

                            plateauLogique.Selectionner_bouger(unitAtHexagone, centerX, centerY, h);
                            
                            //highlight movable:
                            
                            if(plateauLogique.selectedUnit!=null){
                                Unite unit = plateauLogique.selectedUnit.getUnit();
                                int movementRange = unit.getNbDeplacement();

                                // Parcourir tous les hexagones du plateau
                                for (int t = 0; t < WIDTH; t++) {
                                    for (int k = 0; k < HEIGHT; k++) {
                                        Hexagonegraph hexagone = hexagones[t][k];

                                        // Vérifier si l'hexagone est adjacent à l'unité sélectionnée
                                        if (plateauLogique.isAdjacent(plateauLogique.selectedUnit.getHexagone(), hexagone)) {
                                            // Vérifier si l'hexagone est libre ou occupé par une unité adverse non alliée
                                            if (!plateauLogique.checkUnitInHexagone(hexagone) || !plateauLogique.checkUnitInHexagone2(hexagone, unit.getUserID())) {
                                                // Vérifier si le coût du terrain est inférieur ou égal aux points de déplacement restants de l'unité
                                                Terrain terrain2 = hexagone.getTerrain1();
                                                if (terrain2.getCost() <= movementRange) {
                                                    hexagone.setBorderColor(Color.GREEN);
                                                }

                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                for (int t = 0; t < WIDTH; t++) {
                                    for (int k = 0; k < HEIGHT; k++) {
                                        Hexagonegraph hexagone = hexagones[t][k];
                                        hexagone.setBorderColor(Color.BLACK); // Changer la couleur de la bordure à la couleur par défaut (par exemple, noir)
                                    }
                                }
                            }

                            repaint();  // Mettre à jour le dessin du plateau
                            plateauLogique.setCurrentUnit(null);

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
       
            
        
        for (UnitWithLocation u : this.plateauLogique.getUnitLocations()) {
            if (u.getUnit() instanceof Archer && u.getUnit().getUserID() == 1) {
                g.drawImage(archer1, u.getCenterX() - 33, u.getCenterY() - 60, 40, 73, this);
            } else if (u.getUnit() instanceof Archer && u.getUnit().getUserID() == 2) {
                g.drawImage(archer2, u.getCenterX() - 42, u.getCenterY() - 60, 40, 73, this);
            } else if (u.getUnit() instanceof Soldat && u.getUnit().getUserID() == 1) {
                g.drawImage(soldat1, u.getCenterX() - 39, u.getCenterY() - 58, 40, 72, this);
            } else if (u.getUnit() instanceof Soldat && u.getUnit().getUserID() == 2) {
                g.drawImage(soldat2, u.getCenterX() - 39, u.getCenterY() - 58, 40, 72, this);
            } else if (u.getUnit() instanceof Cavalier && u.getUnit().getUserID() == 1) {
                g.drawImage(cavalier1, u.getCenterX() - 52, u.getCenterY() - 51, 75, 60, this);
            } else if (u.getUnit() instanceof Cavalier && u.getUnit().getUserID() == 2) {
                g.drawImage(cavalier2, u.getCenterX() - 61, u.getCenterY() - 51, 75, 60, this);
            } else if (u.getUnit() instanceof Elfe && u.getUnit().getUserID() == 1) {
                g.drawImage(elfe1, u.getCenterX() - 31, u.getCenterY() - 54, 30, 70, this);
            } else if (u.getUnit() instanceof Elfe && u.getUnit().getUserID() == 2) {
                g.drawImage(elfe2, u.getCenterX() - 37, u.getCenterY() - 54, 30, 70, this);
            } else if (u.getUnit() instanceof Magicien && u.getUnit().getUserID() == 1) {
                g.drawImage(magicien1, u.getCenterX() - 33, u.getCenterY() - 59, 40, 70, this);
            } else if (u.getUnit() instanceof Magicien && u.getUnit().getUserID() == 2) {
                g.drawImage(magicien2, u.getCenterX() - 42, u.getCenterY() - 59, 40, 70, this);
            }
           for (BarreDeVie barreDeVie : this.plateauLogique.getBarresDeVie()) {
                int x = barreDeVie.getX();
                int y = barreDeVie.getY();
                int largeur = 40;
                int hauteur = 5;
                int valeurActuelle = barreDeVie.getValeurActuelle();
                int valeurMaximale = barreDeVie.getValeurMax();
                // Calculer la longueur de la barre de vie en fonction de la valeur actuelle et maximale
                int longueurBarre = (int) ((double) largeur * valeurActuelle / valeurMaximale);
                // Dessiner la barre de vie
                g.setColor(Color.RED);
                g.fillRect(x, y, largeur, hauteur);
                g.setColor(Color.GREEN);
                g.fillRect(x, y, longueurBarre, hauteur);
            }
        }
        

    }
}
