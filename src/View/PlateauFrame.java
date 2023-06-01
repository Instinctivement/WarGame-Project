package View;

import Controller.Partie;
import Controller.PlateauHexagoneCtr;
import Model.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class PlateauFrame extends javax.swing.JFrame implements Serializable {

    User user1;
    User user2;
    private User currentPlayer;
    private PlateauHexagoneCtr plateauLogique = new PlateauHexagoneCtr();

    private int currentPlayerId;
    private int totalTurns;
    private int maxTurns = 10; // Nombre maximum de tours
    private boolean unitsPlaced;
    private boolean turnPassed;
    DynamicLabel dynamicLabel = new DynamicLabel();
    

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
        totalTurns = 1;
        unitsPlaced = false;
        turnPassed = false;

        plateauVue.setPlateauLogique(plateauLogique);
        plateauLogique.setCurrentPlayerId(currentPlayerId);
        plateauLogique.setDynamicLabel(dynamicLabel);
    }
    
    public PlateauFrame(User user1, User user2, int tTurns, PlateauHexagoneCtr ph) {
        initComponents();
        this.user1 = user1;
        this.user2 = user2;
        this.currentPlayer = user1;

        currentPlayerId = ph.getCurrentPlayerId();
        totalTurns = tTurns;
        unitsPlaced = true;
        turnPassed = false;
        setPlateauLogique(ph);

        plateauVue.setPlateauLogique(ph);
        plateauLogique.setCurrentPlayerId(ph.getCurrentPlayerId());
        plateauLogique.setDynamicLabel(dynamicLabel);
        
        lbInstruction.setText("Informations sur la partie");
        panelUnite.removeAll();
        panelUnite.revalidate();
        panelUnite.setPreferredSize(new Dimension(300, 50));
        panelUnite.revalidate();
        panelUnite.repaint();
        panelUnite.add(dynamicLabel, BorderLayout.CENTER);
        unitsPlaced();
        
        updateUI();
        
        initParam();
    }

    public PlateauHexagoneCtr getPlateauLogique() {
        return plateauLogique;
    }

    public void setPlateauLogique(PlateauHexagoneCtr plateauLogique) {
        this.plateauLogique = plateauLogique;
    }

    public int getTotalTurns() {
        return totalTurns;
    }

    public void setTotalTurns(int totalTurns) {
        this.totalTurns = totalTurns;
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
            lbInstruction.setText("Informations sur la partie");
            panelUnite.removeAll();
            panelUnite.revalidate();
            panelUnite.setPreferredSize(new Dimension(300, 50));
            panelUnite.revalidate();
            panelUnite.repaint();
            panelUnite.add(dynamicLabel, BorderLayout.CENTER);
            unitsPlaced();
            updateUI();
        }
    }

    public void unitsPlaced() {
        unitsPlaced = true;
    }

    private void passTurn() {
        if (!unitsPlaced) {
            JOptionPane.showMessageDialog(this, "Les joueurs doivent placer toutes leurs unités avant de commencer le jeu !");
            return;
        }
        
        if (turnPassed) {
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
        plateauLogique.RecupPV();
        plateauLogique.Reinitialiser();
        plateauLogique.ReinitialiserPointsDeplacement();
        totalTurns++;
        turnPassed = false;
        
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
        if (totalTurns == maxTurns) {
            return true;
        } else if (plateauLogique.endgame(user1.getId()) && plateauLogique.endgame(user2.getId())) {
            return false;
        } else {
            return true;
        }
        // Logique pour vérifier si le jeu est terminé
        // Vérifier si l'un des joueurs n'a plus d'unités avec pv > 0
        // ou si le nombre de tours atteint le maximum
        // À implémenter
    }

    private void endGame() {
        // Logique pour terminer le jeu
        EndGameFrame endGameVue = new EndGameFrame();

        if (isDraw()) {
            endGameVue.lbInformation.setText("Match Null !");
            endGameVue.setVisible(true);
            this.hide();
        } else {
            if (plateauLogique.endgame(user1.getId())) {
                endGameVue.lbInformation.setText("Victoire du Joueur " + user1.getName());
                endGameVue.setVisible(true);
                this.hide();
            }
            if (plateauLogique.endgame(user2.getId())) {
                endGameVue.lbInformation.setText("Victoire du Joueur " + user2.getName());
                endGameVue.setVisible(true);
                this.hide();
            }

        }

        // Fermer la fenêtre du jeu
    }

    private boolean isDraw() {
        if (plateauLogique.endgame(user1.getId()) && plateauLogique.endgame(user2.getId())) {
            return true;
        } else {
            return false;
        }
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
        btnSavePartie = new javax.swing.JButton();
        btnHelp = new javax.swing.JButton();
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

        btnSavePartie.setBackground(new java.awt.Color(255, 255, 255));
        btnSavePartie.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        btnSavePartie.setForeground(new java.awt.Color(0, 102, 102));
        btnSavePartie.setText("Sauvegarder Partie");
        btnSavePartie.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePartieActionPerformed(evt);
            }
        });

        btnHelp.setBackground(new java.awt.Color(255, 255, 255));
        btnHelp.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        btnHelp.setForeground(new java.awt.Color(0, 102, 102));
        btnHelp.setText("Consulter Aide");
        btnHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHelpActionPerformed(evt);
            }
        });

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

        lbInfoWhoIsTour.setFont(new java.awt.Font("Perpetua Titling MT", 1, 12)); // NOI18N
        lbInfoWhoIsTour.setForeground(new java.awt.Color(255, 255, 255));
        lbInfoWhoIsTour.setText("Tour du joueur ;");

        lbCurrentUser.setFont(new java.awt.Font("Perpetua Titling MT", 1, 16)); // NOI18N
        lbCurrentUser.setForeground(new java.awt.Color(51, 255, 51));
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator2)
                        .addContainerGap())))
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
                                        .addGap(18, 18, 18)
                                        .addComponent(lbCurrentUser)))
                                .addGap(0, 0, Short.MAX_VALUE)))
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
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(lbInstruction, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(25, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(panelUnite, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(20, 20, 20))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnHelp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnSavePartie, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                        .addGap(41, 41, 41))))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lbInstruction)
                .addGap(32, 32, 32)
                .addComponent(panelUnite, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(btnSavePartie, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnHelp, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(plateauVue, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btnSavePartieActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePartieActionPerformed
        if (!unitsPlaced) {
            JOptionPane.showMessageDialog(this, "Les joueurs doivent placer toutes leurs unités avant de commencer le jeu !");
            return;
        }
        if (turnPassed) {
            JOptionPane.showMessageDialog(this, "Il faut terminer un tour pour pouvoir sauvegarder la partie !");
            return;
        }
        Partie partie = new Partie(user1, user2, totalTurns); // Initialisez avec les données du jeu
        partie.setPlateauLogique(this.plateauLogique);
        try {
            FileOutputStream fichier = new FileOutputStream("sauvegarde.bin");
            ObjectOutputStream sortie = new ObjectOutputStream(fichier);
            sortie.writeObject(partie);
            sortie.close();
            fichier.close();
            JOptionPane.showMessageDialog(this, "Partie sauvegardée avec succès !");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnSavePartieActionPerformed

    private void btnHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHelpActionPerformed
        HelpMenu hp = new HelpMenu();
        hp.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fermer uniquement la boîte de dialogue
        hp.setVisible(true);
    }//GEN-LAST:event_btnHelpActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHelp;
    private javax.swing.JButton btnSavePartie;
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
