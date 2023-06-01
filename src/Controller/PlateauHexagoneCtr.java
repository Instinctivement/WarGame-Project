package Controller;

import Model.*;
import View.DynamicLabel;
import View.Hexagonegraph;
import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mathistelle
 */
public class PlateauHexagoneCtr implements Serializable {

    private static final int RADIUS = 40;
    private static final int WIDTH = 12;
    private static final int HEIGHT = 11;
    private int currentPlayerId;
    private volatile Unite currentUnit;
    DynamicLabel dynamicLabel = new DynamicLabel();

    private List<UnitWithLocation> unitLocations = new ArrayList<>();
    private List<BarreDeVie> barresDeVie = new ArrayList<>();
    
    public List<BarreDeVie> getBarresDeVie() {
        return barresDeVie;
    }

    public void setBarresDeVie(List<BarreDeVie> barresDeVie) {
        this.barresDeVie = barresDeVie;
    }

    public DynamicLabel getDynamicLabel() {
        return dynamicLabel;
    }

    public void setDynamicLabel(DynamicLabel dynamicLabel) {
        this.dynamicLabel = dynamicLabel;
    }

    public int getCurrentPlayerId() {
        return currentPlayerId;
    }

    public void setCurrentPlayerId(int currentPlayerId) {
        this.currentPlayerId = currentPlayerId;
    }

    public List<UnitWithLocation> getUnitLocations() {
        return unitLocations;
    }

    public void setUnitLocations(List<UnitWithLocation> unitLocations) {
        this.unitLocations = unitLocations;
    }

    public UnitWithLocation selectedUnit;

    public UnitWithLocation getSelectedUnit() {
        return selectedUnit;
    }

    public void setSelectedUnit(UnitWithLocation selectedUnit) {
        this.selectedUnit = selectedUnit;
    }

    public void setCurrentUnit(Unite unit) {
        this.currentUnit = unit;
    }

    public Unite getCurrentUnit() {
        return currentUnit;
    }

    public void addUnit(Unite unit, int centerX, int centerY, Hexagonegraph hexagone) {
        if (!checkUnitInHexagone(hexagone)) {
            UnitWithLocation unitWlocation = new UnitWithLocation(unit, centerX, centerY, hexagone);
            this.unitLocations.add(unitWlocation);
            this.barresDeVie.add(unitWlocation.getLifeBar());
        }
    }

    public void Reinitialiser() {
        for (UnitWithLocation unite : unitLocations) {
            unite.getUnit().setPASEteAttaquee();
            //unite.getUnit().setPASEteDeplace();
        }
    }

    public void ReinitialiserPointsDeplacement() {
        for (UnitWithLocation unite : unitLocations) {
            unite.getUnit().setNbDeplacement(unite.getUnit().nbDeplacementMax);
            unite.getUnit().setRecentAction(false);
        }
    }

    public void RecupPV() {
        for (UnitWithLocation unite : unitLocations) {
            if (unite.getUnit().isAEteAttaquee() == false && unite.getUnit().isAEteDeplace() == false) {
                unite.getUnit().recuperer();
                barresDeVie.remove(unite.getLifeBar());// remove the old lifebar from the list
                unite.getLifeBar().setX(unite.getCenterX() - 40);
                unite.getLifeBar().setY(unite.getCenterY() - 60);
                unite.getLifeBar().setValeurActuelle(unite.getUnit().getNbPv());
                barresDeVie.add(unite.getLifeBar()); // add the updated bar to the list
            }

        }
    }

    public boolean endgame(int userId) {

        boolean isIt = false;
        for (UnitWithLocation unite : unitLocations) {
            if (unite.getUnitId() == userId && unite.getUnit().getNbPv() > 0) {
                isIt = true;
            }
            if (unite.getUnitId() == userId && unite.getUnit().getNbPv() < 0) {
                return false;
            }
        }
        return isIt;
    }

    public void Selectionner_bouger(UnitWithLocation unitAtHexagone, int centerX, int centerY, Hexagonegraph h) {
        int nbrs_hexa = 0;
        boolean peutAttaq = false;
        if (selectedUnit == null && unitAtHexagone != null) {
            // Select the unit at this hexagon
            selectedUnit = unitAtHexagone;
            // && selectedUnit.getUnit().getUserID() == currentPlayerId
        } else if (selectedUnit != null && unitAtHexagone == null) {
            if (selectedUnit.getUnit().getUserID() == currentPlayerId) {
                Terrain terrain = h.getTerrain1();
                UnitWithLocation unitAtDestination = Position_unite(h);

                System.out.println("Coût du terrain: " + terrain.getCost());
                System.out.println(selectedUnit.getUnit().getName());

                int movementRange = selectedUnit.getUnit().getNbDeplacement();

                if (selectedUnit.getUnit().isRecentAction()) {
                    selectedUnit = null;
                    JOptionPane.showMessageDialog(null, "Après une attaque votre unité est immobilisée pour les reste du tour encours");
                } else {
                    if (unitAtDestination == null && isAdjacent(selectedUnit.getHexagone(), h) && movementRange >= terrain.getCost()) {
                        // Move the selected unit to this hexagon
                        movementRange = movementRange - terrain.getCost();
                        dynamicLabel.setText(selectedUnit.getUnit().getName() + " de user " + selectedUnit.getUnitId() + "il reste " + movementRange + " points de déplacements");
                        System.out.println("il reste " + movementRange + " Déplacements");
                        selectedUnit.getUnit().setAEteDeplace();
                        selectedUnit.getUnit().setNbDeplacement(movementRange);
                        selectedUnit.getUnit().isAEteDeplace();
                        unitLocations.remove(selectedUnit); // remove the old location from the list
                        selectedUnit.setCenterX(centerX);
                        selectedUnit.setCenterY(centerY);
                        selectedUnit.setHexagone(h);
                        unitLocations.add(selectedUnit); // add the updated location to the list
                        barresDeVie.remove(selectedUnit.getLifeBar());// remove the old lifebar from the list
                        selectedUnit.getLifeBar().setX(centerX - 40);
                        selectedUnit.getLifeBar().setY(centerY - 60);
                        selectedUnit.getLifeBar().setValeurActuelle(selectedUnit.getUnit().getNbPv());
                        barresDeVie.add(selectedUnit.getLifeBar()); // add the updated bar to the list

                    } else {
                        JOptionPane.showMessageDialog(null, "Vous n'avez plus assez de points de déplacement");
                    }
                }
                if (selectedUnit.getUnit().getNbDeplacement() <= terrain.getCost()) {
                    selectedUnit = null;
                }
            } else {
                selectedUnit = null;
                JOptionPane.showMessageDialog(null, "Merci d'attendre votre tour de jeu pour déplacer ces unités");
            }
            selectedUnit = null;

        } else if (selectedUnit != null && unitAtHexagone != null) {
            if (selectedUnit.getUnit().getUserID() == currentPlayerId) {
                Terrain terrain = h.getTerrain1();
                UnitWithLocation unitAtDestination = Position_unite(h);
                int movementRange = selectedUnit.getUnit().getNbDeplacement();
                if (selectedUnit.getUnit().isRecentAction()) {
                    selectedUnit = null;
                    JOptionPane.showMessageDialog(null, "Après une attaque votre unité est immobilisée pour les reste du tour encours");
                } else {
                    if (selectedUnit.getUnitId() != unitAtHexagone.getUnitId()) {
                        if (selectedUnit.getUnit().getName() == "Archer" && movementRange >= countHexagonesBetween(selectedUnit.getHexagone(), unitAtHexagone.getHexagone())) {
                            nbrs_hexa = countHexagonesBetween(selectedUnit.getHexagone(), unitAtHexagone.getHexagone());
                            movementRange = movementRange - nbrs_hexa;
                            System.out.println("il reste " + movementRange + " Déplacements");
                            selectedUnit.getUnit().setNbDeplacement(movementRange);
                            peutAttaq = true;
                        } else if (unitAtDestination != null && movementRange >= terrain.getCost() && isAdjacent(selectedUnit.getHexagone(), unitAtHexagone.getHexagone())) {
                            movementRange = movementRange - terrain.getCost();
                            System.out.println("il reste " + movementRange + " Déplacements");
                            selectedUnit.getUnit().setNbDeplacement(movementRange);
                            peutAttaq = true;
                        } else if (selectedUnit.getUnit().getNbDeplacement() <= terrain.getCost() || selectedUnit.getUnit().getNbDeplacement() <= nbrs_hexa) {
                            selectedUnit = null;
                            JOptionPane.showMessageDialog(null, "Vous n'avez plus assez de points de déplacement");
                            peutAttaq = false;
                        } else {
                            System.out.println("Distanceee: " + countHexagonesBetween(selectedUnit.getHexagone(), unitAtHexagone.getHexagone()));
                            JOptionPane.showMessageDialog(null, "Vous ne pouvez pas attaquer de cette distance");
                            selectedUnit = null;
                            peutAttaq = false;
                        }
                        if (peutAttaq == true) {
                            selectedUnit.getUnit().setRecentAction(true);
                            selectedUnit.getUnit().attaquer(unitAtHexagone.getUnit());
                            unitAtHexagone.getUnit().setAEteAttaquee();
                            dynamicLabel.setText(selectedUnit.getUnit().getName() + " de user " + selectedUnit.getUnitId() + " a attaqué " + unitAtHexagone.getUnit().getName() + " de user " + unitAtHexagone.getUnitId());
                            System.out.println(selectedUnit.getUnit().getName() + " de user " + selectedUnit.getUnitId() + " a attaqué " + unitAtHexagone.getUnit().getName() + " de user " + unitAtHexagone.getUnitId());
                            System.out.println("Nb pv restant pour " + unitAtHexagone.getUnit().getName() + " de user " + unitAtHexagone.getUnitId() + " = " + unitAtHexagone.getUnit().getNbPv());
                            if (unitAtHexagone.getUnit().getNbPv() <= 0) {
                                unitLocations.remove(unitAtHexagone);
                                barresDeVie.remove(unitAtHexagone.getLifeBar());
                                dynamicLabel.setText(selectedUnit.getUnit().getName() + " de user " + selectedUnit.getUnitId() + " a trépassé");
                                selectedUnit.getUnit().setAEteDeplace();
                                selectedUnit.getUnit().setNbDeplacement(movementRange);
                                selectedUnit.getUnit().isAEteDeplace();
                                unitLocations.remove(selectedUnit); // remove the old location from the list
                                selectedUnit.setCenterX(centerX);
                                selectedUnit.setCenterY(centerY);
                                selectedUnit.setHexagone(h);
                                unitLocations.add(selectedUnit); // add the updated location to the list
                                barresDeVie.remove(selectedUnit.getLifeBar());// remove the old lifebar from the list
                                selectedUnit.getLifeBar().setX(centerX - 40);
                                selectedUnit.getLifeBar().setY(centerY - 60);
                                selectedUnit.getLifeBar().setValeurActuelle(selectedUnit.getUnit().getNbPv());
                                barresDeVie.add(selectedUnit.getLifeBar()); // add the updated bar to the list
                            } else {
                                barresDeVie.remove(unitAtHexagone.getLifeBar());// remove the old lifebar from the list
                                unitAtHexagone.getLifeBar().setX(centerX - 40);
                                unitAtHexagone.getLifeBar().setY(centerY - 60);
                                unitAtHexagone.getLifeBar().setValeurActuelle(unitAtHexagone.getUnit().getNbPv());
                                barresDeVie.add(unitAtHexagone.getLifeBar()); // add the updated bar to the list
                            }
                            selectedUnit.getUnit().setNbDeplacement(0);
                            selectedUnit = null;
                        }
                    } else {
                        selectedUnit = null;
                        JOptionPane.showMessageDialog(null, "Vous ne pouvez pas attaquer un allié");
                    }
                }

            } else {
                selectedUnit = null;
                JOptionPane.showMessageDialog(null, "Merci d'attendre votre tour de jeu pour déplacer ces unités");
            }
        }
    }

    public boolean isAdjacent(Hexagonegraph from, Hexagonegraph to) {
        int dx = from.getCenterX() - to.getCenterX();
        int dy = from.getCenterY() - to.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Ici, j'assume que la distance entre les centres de deux hexagones adjacents est égale au diamètre d'un hexagone, c'est-à-dire 2 * radius.
        // Vous devrez peut-être ajuster cette valeur en fonction de la façon dont vous avez défini vos hexagones.
        return distance <= 2 * RADIUS;
    }

    public int countHexagonesBetween(Hexagonegraph from, Hexagonegraph to) {
        int dx = from.getCenterX() - to.getCenterX();
        int dy = from.getCenterY() - to.getCenterY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Ici, j'assume que la distance entre les centres de deux hexagones adjacents est égale au diamètre d'un hexagone, c'est-à-dire 2 * radius.
        // Vous devrez peut-être ajuster cette valeur en fonction de la façon dont vous avez défini vos hexagones.
        double hexagonDiameter = 2 * RADIUS;
        double hexagonesBetween = distance / hexagonDiameter;
        int nbhexagones = (int) Math.ceil(hexagonesBetween);

        return nbhexagones;
    }

    public boolean checkUnitInHexagone(Hexagonegraph hexagone) {
        for (UnitWithLocation unitLocation : unitLocations) {
            if (unitLocation.getHexagone().equals(hexagone)) {
                return true; // retourne true si une unité existe dans cet hexagone
            }
        }
        return false; // retourne false si aucune unité n'existe dans cet hexagone
    }

    public boolean checkUnitInHexagone2(Hexagonegraph hexagone, int n) {
        for (UnitWithLocation unitLocation : unitLocations) {
            if (unitLocation.getHexagone().equals(hexagone)) {
                System.out.println("le USERID vaut: " + unitLocation.getUnit().getUserID());
                if (unitLocation.getUnit().getUserID() == n) {
                    //Attaque possible
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    public UnitWithLocation Position_unite(Hexagonegraph hexagone) {
        for (UnitWithLocation unitLocation : unitLocations) {
            if (unitLocation.getHexagone().equals(hexagone)) {
                return unitLocation;
            }
        }
        return null;
    }
}
