package View;

import App.Main;
import Model.User;
import javax.swing.JOptionPane;

public class PlayersInfoFrame extends javax.swing.JFrame {

    /**
     * Creates new form PlayersInfoFrame
     */
    public PlayersInfoFrame() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbTitle = new javax.swing.JLabel();
        jbInstructions = new javax.swing.JLabel();
        lbUser1 = new javax.swing.JLabel();
        txtUser1 = new javax.swing.JTextField();
        lbUser2 = new javax.swing.JLabel();
        txtUser2 = new javax.swing.JTextField();
        btnStart = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        bg = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("BEYOND THE RIVER");
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lbTitle.setFont(new java.awt.Font("Perpetua Titling MT", 3, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(204, 255, 255));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("WARGAME");
        getContentPane().add(lbTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 80, 150, 40));

        jbInstructions.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        jbInstructions.setForeground(new java.awt.Color(255, 255, 255));
        jbInstructions.setText("Entrez les speudos des jouers :");
        getContentPane().add(jbInstructions, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 190, -1, 28));

        lbUser1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbUser1.setForeground(new java.awt.Color(255, 255, 255));
        lbUser1.setText("Joueur 1 : ");
        getContentPane().add(lbUser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 240, 73, 24));

        txtUser1.setBackground(new java.awt.Color(255, 255, 255));
        txtUser1.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        txtUser1.setForeground(new java.awt.Color(0, 102, 102));
        getContentPane().add(txtUser1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 270, 260, 40));

        lbUser2.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        lbUser2.setForeground(new java.awt.Color(255, 255, 255));
        lbUser2.setText("Joueur 2 : ");
        getContentPane().add(lbUser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 320, 73, 24));

        txtUser2.setBackground(new java.awt.Color(255, 255, 255));
        txtUser2.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        txtUser2.setForeground(new java.awt.Color(0, 102, 102));
        getContentPane().add(txtUser2, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 350, 260, 40));

        btnStart.setBackground(new java.awt.Color(0, 102, 102));
        btnStart.setFont(new java.awt.Font("Perpetua Titling MT", 0, 14)); // NOI18N
        btnStart.setForeground(new java.awt.Color(255, 255, 255));
        btnStart.setText("COMMENCER !");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });
        getContentPane().add(btnStart, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 430, 260, 40));

        btnBack.setBackground(new java.awt.Color(0, 102, 102));
        btnBack.setFont(new java.awt.Font("Perpetua Titling MT", 0, 12)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("< Retour");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });
        getContentPane().add(btnBack, new org.netbeans.lib.awtextra.AbsoluteConstraints(28, 39, 100, -1));

        jLabel1.setFont(new java.awt.Font("Perpetua Titling MT", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("BEYOND THE RIVER");
        getContentPane().add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 30, -1, -1));

        bg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/bg.png"))); // NOI18N
        bg.setPreferredSize(new java.awt.Dimension(860, 570));
        getContentPane().add(bg, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 860, 580));

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        Main mn = new Main();
        mn.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        if(txtUser1.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Merci de renseigner un pseudo pour le joueur1");
        }else if(txtUser2.getText().equals("")){
            JOptionPane.showMessageDialog(this, "Merci de renseigner un pseudo pour le joueur2");
        }else{
            User user1 = new User(1, txtUser1.getText());
            User user2 = new User(2, txtUser2.getText());
            
            PlateauFrame pf = new PlateauFrame(user1, user2);
            pf.setVisible(true);
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnStartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PlayersInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PlayersInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PlayersInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PlayersInfoFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PlayersInfoFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bg;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jbInstructions;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUser1;
    private javax.swing.JLabel lbUser2;
    private javax.swing.JTextField txtUser1;
    private javax.swing.JTextField txtUser2;
    // End of variables declaration//GEN-END:variables
}
