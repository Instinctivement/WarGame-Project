package View;

import Controller.PlateauHexagoneCtr;
import Model.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class PlateauFrame extends javax.swing.JFrame {

    User user1;
    User user2;
    private User currentPlayer;
    private PlateauHexagoneCtr plateauLogique = new PlateauHexagoneCtr();

    private int currentPlayerId;
    private int totalTurns;
    private int turnsCompleted;
    private int maxTurns = 10; // Nombre maximum de tours
    private boolean gameEnded;
    private boolean unitsPlaced;
    private boolean turnPassed;

    /**
     * Creates new form PlateauFrame
     */
    public PlateauFrame(User user1, User user2) {
        initComponents();
        this.user1 = user1;
        this.user2 = user2;
        this.currentPlayer = user1;

        initParam();

        initUnit(this.user1, panelUnite, "B.png");

        currentPlayerId = 1;
        totalTurns = 0;
        turnsCompleted = 0;
        gameEnded = false;
        unitsPlaced = false;
        turnPassed = false;

        plateauVue.setPlateauLogique(plateauLogique);
        plateauLogique.setCurrentPlayerId(currentPlayerId);

    }

    public void initParam() {
        lbUser1.setText(this.user1.getName());
        lbUser2.setText(this.user2.getName());
        lbCurrentUser.setText(this.user1.getName());
        lbNbTourRest.setText(String.valueOf(totalTurns));
    }

    public void initUnit(User user, JPanel targetPanel, String imageSuffix) {
        JPanel panel = new JPanel();
        panel.setPreferredSize(new Dimension(200, 100));
        panel.setLayout(new GridLayout(1, 5));

        Unite[] unites = new Unite[5];
        JLabel[] imageLabels = new JLabel[5];

        // Création des instances des différents types d'unités
        unites[0] = new Archer(user, 1);
        unites[1] = new Soldat(user, 2);
        unites[2] = new Elfe(user, 3);
        unites[3] = new Cavalier(user, 4);
        unites[4] = new Magicien(user, 5);

        // Création des labels d'image correspondants
        for (int i = 0; i < imageLabels.length; i++) {
            imageLabels[i] = new JLabel(new ImageIcon("img/" + unites[i].getName().toLowerCase() + imageSuffix));
            panel.add(imageLabels[i]);
        }

        // Ajout des MouseListeners aux labels d'image
        for (int i = 0; i < imageLabels.length; i++) {
            final int index = i;
            imageLabels[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (currentPlayer == user && plateauLogique.getCurrentUnit() == null) {
                        plateauLogique.setCurrentUnit(unites[index]);
                        imageLabels[index].setVisible(false);
                        panel.revalidate();
                        panel.repaint();

                        // Vérifier si toutes les labels d'image sont invisibles
                        SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                boolean allInvisible = true;
                                for (JLabel label : imageLabels) {
                                    if (label.isVisible()) {
                                        allInvisible = false;
                                        break;
                                    }
                                }

                                if (allInvisible) {
                                    try {
                                        TimeUnit.SECONDS.sleep(1); // Attendre 2 secondes
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    switchPlayer(); // Passer au joueur suivant
                                }
                            }
                        });

                    }
                }
            });
        }

        targetPanel.add(panel, BorderLayout.CENTER);
    }

    private void switchPlayer() {
        if (currentPlayer == user1) {
            currentPlayer = user2;
            panelUnite.removeAll();  // Supprimer les unités du premier joueur du panneau
            initUnit(user2, panelUnite, "R.png");  // Réinitialiser les unités du deuxième joueur
            plateauLogique.setCurrentPlayerId(2);
            lbCurrentUser.setText(this.user2.getName());
            panelUnite.revalidate();
            panelUnite.repaint();
        } else {
            plateauLogique.setCurrentPlayerId(1);
            unitsPlaced();
            updateUI();
        }
    }

    public void unitsPlaced() {
        unitsPlaced = true;
    }

    private void passTurn() {
        if (gameEnded || turnPassed) {
            return;
        }

        // Changer de joueur sans incrémenter le nombre de tours
        currentPlayerId = (currentPlayerId == 1) ? 2 : 1;
        plateauLogique.setCurrentPlayerId(currentPlayerId);
        plateauLogique.setSelectedUnit(null);
        turnPassed = true;

        // Mettre à jour l'interface utilisateur (si nécessaire)
        updateUI();
    }

    private void nextTurn() {
        if (gameEnded) {
            return;
        }

        // Vérifier si les deux joueurs ont placé toutes leurs unités
        if (!unitsPlaced) {
            JOptionPane.showMessageDialog(this, "Les joueurs doivent placer toutes leurs unités avant de commencer le jeu !");
            return;
        }

        // Vérifier les conditions de fin de jeu
        if (isGameOver()) {
            endGame();
            return;
        }

        // Changer de joueur et incrémenter le nombre de tours
        currentPlayerId = (currentPlayerId == 1) ? 2 : 1;
        plateauLogique.setCurrentPlayerId(currentPlayerId);
        plateauLogique.setSelectedUnit(null);
        plateauLogique.Reinitialiser();
        plateauLogique.RecupPV();
        plateauLogique.ReinitialiserPointsDeplacement();
        totalTurns++;
        turnPassed = false;

        // Vérifier si les deux joueurs ont joué leur tour
        /*if (currentPlayerId == 1) {
            turnsCompleted++;
            if (turnsCompleted == 2) {
                turnsCompleted = 0;
                totalTurns++;
            }
        }*/
        // Mettre à jour l'interface utilisateur (si nécessaire)
        updateUI();
    }

    private void updateUI() {
        lbNbTourRest.setText(String.valueOf(totalTurns));
        if (currentPlayerId == 1) {
            lbCurrentUser.setText(this.user1.getName());
        } else {
            lbCurrentUser.setText(this.user2.getName());
        }

        // Logique pour mettre à jour l'interface utilisateur
        // Mettre à jour l'affichage du plateau, des points de déplacement, etc.
    }

    private boolean isGameOver() {
        // Logique pour vérifier si le jeu est terminé
        // Vérifier si l'un des joueurs n'a plus d'unités avec pv > 0
        // ou si le nombre de tours atteint le maximum
        return false; // À implémenter
    }

    private void endGame() {
        // Logique pour terminer le jeu
        gameEnded = true;

        // Afficher le message de fin de jeu
        String message;
        if (isDraw()) {
            message = "Match nul !";
        } else {
            int winnerId = (currentPlayerId == 1) ? 2 : 1;
            message = "Le joueur " + winnerId + " a gagné !";
        }
        JOptionPane.showMessageDialog(this, message);

        // Fermer la fenêtre du jeu
        dispose();
    }

    private boolean isDraw() {
        // Logique pour vérifier s'il y a match nul
        // Vérifier si les deux joueurs ont encore au moins une unité avec pv > 0
        return false; // À implémenter
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        lbTitle = new javax.swing.JLabel();
        lbUser1 = new javax.swing.JLabel();
        lbUser2 = new javax.swing.JLabel();
        lbInfoTour = new javax.swing.JLabel();
        lbNbTourRest = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lbInstruction = new javax.swing.JLabel();
        panelUnite = new javax.swing.JPanel();
        nextTurnButton = new javax.swing.JButton();
        passTurnButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        lbInfoWhoIsTour = new javax.swing.JLabel();
        lbCurrentUser = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        plateauVue = new View.PlateauHexagoneVue();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        jButton1.setForeground(new java.awt.Color(0, 102, 102));
        jButton1.setText("Nouveau");

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 102, 102));
        jButton2.setText("Aide");

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        jButton3.setForeground(new java.awt.Color(0, 102, 102));
        jButton3.setText("QUITTER");

        jButton4.setBackground(new java.awt.Color(255, 255, 255));
        jButton4.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        jButton4.setForeground(new java.awt.Color(0, 102, 102));
        jButton4.setText("REJOUER");

        lbTitle.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(255, 255, 255));
        lbTitle.setText("Informations sur la partie :");

        lbUser1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbUser1.setForeground(new java.awt.Color(255, 255, 255));
        lbUser1.setText("User 1 :");

        lbUser2.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbUser2.setForeground(new java.awt.Color(255, 255, 255));
        lbUser2.setText("User 2 :");

        lbInfoTour.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbInfoTour.setForeground(new java.awt.Color(255, 255, 255));
        lbInfoTour.setText("Nombre de tour :");

        lbNbTourRest.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        lbNbTourRest.setForeground(new java.awt.Color(255, 255, 255));
        lbNbTourRest.setText("10");

        lbInstruction.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbInstruction.setForeground(new java.awt.Color(255, 255, 255));
        lbInstruction.setText("Placez vos unités sur le terrain :");

        nextTurnButton.setBackground(new java.awt.Color(255, 255, 255));
        nextTurnButton.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        nextTurnButton.setForeground(new java.awt.Color(0, 102, 102));
        nextTurnButton.setText("Tour suivant");
        nextTurnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nextTurnButtonActionPerformed(evt);
            }
        });

        passTurnButton1.setBackground(new java.awt.Color(255, 255, 255));
        passTurnButton1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        passTurnButton1.setForeground(new java.awt.Color(0, 102, 102));
        passTurnButton1.setText("Passer la main");
        passTurnButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passTurnButton1ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("/10");

        lbInfoWhoIsTour.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbInfoWhoIsTour.setForeground(new java.awt.Color(255, 255, 255));
        lbInfoWhoIsTour.setText("Tour du joueur ;");

        lbCurrentUser.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        lbCurrentUser.setForeground(new java.awt.Color(255, 255, 204));
        lbCurrentUser.setText("User");

        jPanel2.setBackground(new java.awt.Color(0, 0, 204));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        jPanel3.setBackground(new java.awt.Color(204, 0, 0));
        jPanel3.setPreferredSize(new java.awt.Dimension(50, 15));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 15, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addContainerGap())))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelUnite, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(passTurnButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(nextTurnButton))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lbInfoTour)
                                        .addGap(124, 124, 124)
                                        .addComponent(lbNbTourRest)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(lbInfoWhoIsTour)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lbCurrentUser)))
                                .addGap(0, 22, Short.MAX_VALUE)))
                        .addGap(21, 21, 21))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lbUser1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbUser2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(30, 30, 30))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lbTitle)
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbInfoTour)
                    .addComponent(lbNbTourRest)
                    .addComponent(jLabel2))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lbUser1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lbUser2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passTurnButton1)
                    .addComponent(nextTurnButton))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbInfoWhoIsTour)
                    .addComponent(lbCurrentUser))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbInstruction)
                .addGap(32, 32, 32)
                .addComponent(panelUnite, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(47, 47, 47)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(plateauVue, javax.swing.GroupLayout.DEFAULT_SIZE, 876, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(plateauVue, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void nextTurnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nextTurnButtonActionPerformed
        nextTurn();
    }//GEN-LAST:event_nextTurnButtonActionPerformed

    private void passTurnButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passTurnButton1ActionPerformed
        passTurn();
    }//GEN-LAST:event_passTurnButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbCurrentUser;
    private javax.swing.JLabel lbInfoTour;
    private javax.swing.JLabel lbInfoWhoIsTour;
    private javax.swing.JLabel lbInstruction;
    private javax.swing.JLabel lbNbTourRest;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUser1;
    private javax.swing.JLabel lbUser2;
    private javax.swing.JButton nextTurnButton;
    private javax.swing.JPanel panelUnite;
    private javax.swing.JButton passTurnButton1;
    private View.PlateauHexagoneVue plateauVue;
    // End of variables declaration//GEN-END:variables
}
