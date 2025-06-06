/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.final_project_pbo.ui;

import com.mycompany.final_project_pbo.utils.SessionManager;

/**
 *
 * @author muham
 */
public class LogOutui extends javax.swing.JPanel {

        /**
         * Creates new form LogOutui
         */
        public LogOutui() {
                initComponents();
        }

        /**
         * This method is called from within the constructor to initialize the form.
         * WARNING: Do NOT modify this code. The content of this method is always
         * regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jLabel4 = new javax.swing.JLabel();
                jPanel1 = new javax.swing.JPanel();
                ButtonCancel = new javax.swing.JButton();
                ButtonLogout1 = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                jLabel3 = new javax.swing.JLabel();
                jLabel5 = new javax.swing.JLabel();
                jLabel6 = new javax.swing.JLabel();

                jLabel4.setText("jLabel4");

                setBackground(new java.awt.Color(255, 255, 255));
                setLayout(new java.awt.CardLayout());

                jPanel1.setBackground(new java.awt.Color(255, 255, 255));

                ButtonCancel.setFont(new java.awt.Font("Tw Cen MT", 1, 22)); // NOI18N
                ButtonCancel.setForeground(new java.awt.Color(93, 173, 226));
                ButtonCancel.setText("Cancel");

                ButtonLogout1.setBackground(new java.awt.Color(93, 173, 226));
                ButtonLogout1.setFont(new java.awt.Font("Tw Cen MT", 1, 22)); // NOI18N
                ButtonLogout1.setForeground(new java.awt.Color(255, 255, 255));
                ButtonLogout1.setText("LogOut");
                ButtonLogout1.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                ButtonLogout1MouseClicked(evt);
                        }
                });

                jLabel1.setFont(new java.awt.Font("Tw Cen MT", 0, 20)); // NOI18N
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("Anda akan log out dari aplikasi. Pastikan Anda telah menyimpan ");

                jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 20)); // NOI18N
                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel2.setText("semua perkerjaan yang telat Anda kerjakan.");

                jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 20)); // NOI18N
                jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel3.setText("Lanjutkan ?");

                jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_logout.png"))); // NOI18N

                jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/logo_logout.png"))); // NOI18N

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(150, 150, 150)
                                                                .addComponent(ButtonLogout1)
                                                                .addGap(106, 106, 106)
                                                                .addComponent(ButtonCancel)
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE))
                                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 600,
                                                                Short.MAX_VALUE)
                                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jLabel5,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addGap(30, 30, 30)
                                                                .addComponent(jLabel6)
                                                                .addGap(275, 275, 275)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jLabel5,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                98,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addContainerGap()
                                                                                                .addComponent(jLabel6)))
                                                                .addGap(10, 10, 10)
                                                                .addComponent(jLabel1)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(jLabel2)
                                                                .addGap(10, 10, 10)
                                                                .addComponent(jLabel3)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                41, Short.MAX_VALUE)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(ButtonLogout1)
                                                                                .addComponent(ButtonCancel))
                                                                .addGap(30, 30, 30)));

                add(jPanel1, "card2");
        }// </editor-fold>//GEN-END:initComponents

        private void ButtonLogout1MouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_ButtonLogout1MouseClicked
                // TODO add your handling code here:
                this.setVisible(false);
                SessionManager.getInstance().logout();
                Login loginui = new Login();
                loginui.setVisible(true);
        }// GEN-LAST:event_ButtonLogout1MouseClicked

        private void ButtonLogout1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonLogout1ActionPerformed
                // TODO add your handling code here:
                this.setVisible(false);
                SessionManager.getInstance().logout();
                Login loginui = new Login();
                loginui.setVisible(true);
        }// GEN-LAST:event_ButtonLogout1ActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton ButtonCancel;
        private javax.swing.JButton ButtonLogout1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JLabel jLabel3;
        private javax.swing.JLabel jLabel4;
        private javax.swing.JLabel jLabel5;
        private javax.swing.JLabel jLabel6;
        private javax.swing.JPanel jPanel1;
        // End of variables declaration//GEN-END:variables
}
