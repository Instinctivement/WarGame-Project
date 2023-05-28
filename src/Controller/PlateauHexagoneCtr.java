package Controller;

import Model.*;
import View.Hexagonegraph;

import java.util.List;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 *
 * @author mathistelle
 */
public class PlateauHexagoneCtr {

    private static final int RADIUS = 40;
    private static final int WIDTH = 12;
    private static final int HEIGHT = 11;
    private int currentPlayerId;

    private Hexagonegraph[][] hexagones = new Hexagonegraph[WIDTH][HEIGHT];
    private List<UnitWithLocation> unitLocations = new ArrayList<>();

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

    private UnitWithLocation selectedUnit;
    //private UnitWithLocation aimedUnit;
    private volatile Unite currentUnit;

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
            this.unitLocations.add(new UnitWithLocation(unit, centerX, centerY, hexagone));
        }
    }
    
    public void Reinitialiser(){
         for (UnitWithLocation unite : unitLocations) {
             unite.getUnit().setPASEteAttaquee();     
         }
    }
    
    public void ReinitialiserPointsDeplacement(){
         for (UnitWithLocation unite : unitLocations) {
             unite.getUnit().setNbDeplacement(unite.getUnit().nbDeplacementMax);     
         }
    }
    
    public void RecupPV(){
        for (UnitWithLocation unite : unitLocations){
            if (unite.getUnit().isAEteAttaquee()==false && unite.getUnit().isAEteDeplace()){
                unite.getUnit().recuperer();
            }
        }
    }

    public void Selectionner_bouger(UnitWithLocation unitAtHexagone, int centerX, int centerY, Hexagonegraph h) {
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

                if (unitAtDestination == null && isAdjacent(selectedUnit.getHexagone(), h) && movementRange >= terrain.getCost()) {
                    // Move the selected unit to this hexagon
                    movementRange = movementRange - terrain.getCost();
                    System.out.println("il reste " + movementRange + " Déplacements");
                    selectedUnit.getUnit().setNbDeplacement(movementRange);
                    selectedUnit.getUnit().isAEteDeplace();
                    unitLocations.remove(selectedUnit); // remove the old location from the list
                    selectedUnit.setCenterX(centerX);
                    selectedUnit.setCenterY(centerY);
                    selectedUnit.setHexagone(h);
                    unitLocations.add(selectedUnit); // add the updated location to the list
                    //selectedUnit = null;
                    
                } else {
                    selectedUnit = null;
                    JOptionPane.showMessageDialog(null, "Vous n'avez plus assez de points de déplacement");
                }
                if (selectedUnit.getUnit().getNbDeplacement() <= terrain.getCost()) {
                    selectedUnit = null;
                }
            } else {
                selectedUnit = null;
                JOptionPane.showMessageDialog(null, "Merci d'attendre votre tour de jeu pour déplacer ces unités");
            }

        } else if (selectedUnit != null && unitAtHexagone != null) {
            if (selectedUnit.getUnit().getUserID() == currentPlayerId) {
                Terrain terrain = h.getTerrain1();
                UnitWithLocation unitAtDestination = Position_unite(h);
                int movementRange = selectedUnit.getUnit().getNbDeplacement();

                if (unitAtDestination != null && movementRange >= terrain.getCost()) {
                    movementRange = movementRange - terrain.getCost();
                    System.out.println("il reste " + movementRange + " Déplacements");
                    selectedUnit.getUnit().setNbDeplacement(movementRange);

                    if (selectedUnit.getUnitId() != unitAtHexagone.getUnitId()) {
                        selectedUnit.getUnit().attaquer(unitAtHexagone.getUnit());
                        unitAtHexagone.getUnit().setAEteAttaquee();
                        System.out.println(selectedUnit.getUnit().getName() + " de user " + selectedUnit.getUnitId() + " a attaqué " + unitAtHexagone.getUnit().getName() + " de user " + unitAtHexagone.getUnitId());
                        System.out.println("Nb pv restant pour " + unitAtHexagone.getUnit().getName() + " de user " + unitAtHexagone.getUnitId() + " = " + unitAtHexagone.getUnit().getNbPv());
                        selectedUnit.getUnit().setNbDeplacement(0);
                        selectedUnit = null;

                    } else if (selectedUnit.getUnitId() == unitAtHexagone.getUnitId()) {
                        selectedUnit = null;
                        JOptionPane.showMessageDialog(null, "Vous ne pouvez pas attaquer un allié");
                    }

                } else if (selectedUnit.getUnit().getNbDeplacement() <= terrain.getCost()) {
                    selectedUnit = null;
                    JOptionPane.showMessageDialog(null, "Vous n'avez plus assez de points de déplacement");
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
