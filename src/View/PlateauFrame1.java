package View;

import Controller.PlateauLogique;
import Model.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class PlateauFrame1 extends javax.swing.JFrame {

    User user1;
    User user2;
    private User currentPlayer;
    private PlateauLogique plateauLogique = new PlateauLogique();
    
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
    public PlateauFrame1(User user1, User user2) {
        initComponents();
        this.user1 = user1;
        this.user2 = user2;
        this.currentPlayer = user1;
        
        plateauVue.setPlateauLogique(plateauLogique);

        initParam();

        initUnit(this.user1, panelUnite, "B.png");
        
        currentPlayerId = 1;
        totalTurns = 0;
        turnsCompleted = 0;
        gameEnded = false;
        unitsPlaced = false;
        turnPassed = false;

    }

    public void initParam() {
        lbUser1.setText(this.user1.getName());
        lbUser2.setText(this.user2.getName());
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
            panelUnite.revalidate();
            panelUnite.repaint();
        }else{
            unitsPlaced();
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
        totalTurns++;
        turnPassed = false;

        // Vérifier si les deux joueurs ont joué leur tour
        if (currentPlayerId == 1) {
            turnsCompleted++;
            if (turnsCompleted == 2) {
                turnsCompleted = 0;
                totalTurns++;
            }
        }

        // Mettre à jour l'interface utilisateur (si nécessaire)
        updateUI();
    }
    
    private void updateUI() {
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
        jLabel1 = new javax.swing.JLabel();
        lbUser1 = new javax.swing.JLabel();
        lbUser2 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        lbNbTour = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        panelUnite = new javax.swing.JPanel();
        nextTurnButton = new javax.swing.JButton();
        passTurnButton1 = new javax.swing.JButton();
        plateauVue = new View.PlateauVue();

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

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Informations sur la partie :");

        lbUser1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbUser1.setForeground(new java.awt.Color(255, 255, 255));
        lbUser1.setText("User 1 :");

        lbUser2.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbUser2.setForeground(new java.awt.Color(255, 255, 255));
        lbUser2.setText("User 2 :");

        jButton5.setBackground(new java.awt.Color(0, 0, 204));

        jButton6.setBackground(new java.awt.Color(204, 0, 0));

        jLabel4.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nombre de tour :");

        lbNbTour.setFont(new java.awt.Font("Perpetua Titling MT", 1, 14)); // NOI18N
        lbNbTour.setForeground(new java.awt.Color(255, 255, 255));
        lbNbTour.setText("10/10");

        jLabel6.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Placez vos unités sur le terrain :");

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

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator1))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jSeparator2)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelUnite, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(lbNbTour, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(lbUser1)
                                .addGap(32, 32, 32)
                                .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(46, 46, 46)
                                .addComponent(lbUser2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(passTurnButton1)
                                .addGap(18, 18, 18)
                                .addComponent(nextTurnButton)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lbNbTour))
                .addGap(40, 40, 40)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(lbUser1))
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbUser2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nextTurnButton)
                    .addComponent(passTurnButton1))
                .addGap(29, 29, 29)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel6)
                .addGap(48, 48, 48)
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
                .addComponent(plateauVue, javax.swing.GroupLayout.DEFAULT_SIZE, 870, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
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
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lbNbTour;
    private javax.swing.JLabel lbUser1;
    private javax.swing.JLabel lbUser2;
    private javax.swing.JButton nextTurnButton;
    private javax.swing.JPanel panelUnite;
    private javax.swing.JButton passTurnButton1;
    private View.PlateauVue plateauVue;
    // End of variables declaration//GEN-END:variables
}
