
package com.mycompany.final_project_pbo.ui;

import com.mycompany.final_project_pbo.Response;
import com.mycompany.final_project_pbo.User;
import javax.swing.JOptionPane;


public class Login extends javax.swing.JFrame {
    
//    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Login.class.getName());
    User userService = new User();
    
    public Login() {
        initComponents();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Right = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        usernameForm = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        phoneForm = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        passwordForm = new javax.swing.JPasswordField();
        loginBtn = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        toSignUpBtn = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGIN");

        jPanel1.setBackground(new java.awt.Color(93, 173, 226));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 600));
        jPanel1.setLayout(null);

        Right.setBackground(new java.awt.Color(253, 254, 254));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/Img/Icon.png"))); // NOI18N

        jLabel7.setBackground(new java.awt.Color(93, 173, 226));
        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(93, 173, 226));
        jLabel7.setText("Selamat Datang Di Sistem POS TOSERBA Berkah Abadi ");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel8.setText("Aplikasi ini digunakan untuk memudahkan proses transaksi  ");

        jLabel10.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel10.setText("Penjualan, Manajemen Stock Barang, serta Laporan Keuangan");

        jLabel11.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        jLabel11.setText("harian di TOSERBA Berkah Abadi");

        jLabel12.setText("@ 2025 TOSERBA Berkah Abadi. All rights reserved");

        javax.swing.GroupLayout RightLayout = new javax.swing.GroupLayout(Right);
        Right.setLayout(RightLayout);
        RightLayout.setHorizontalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(123, 123, 123)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addComponent(jLabel7)
                        .addGap(14, 14, 14))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addGroup(RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel10)
                            .addComponent(jLabel8))
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addComponent(jLabel11)
                        .addGap(124, 124, 124))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, RightLayout.createSequentialGroup()
                        .addComponent(jLabel12)
                        .addGap(83, 83, 83))))
        );
        RightLayout.setVerticalGroup(
            RightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(RightLayout.createSequentialGroup()
                .addGap(54, 54, 54)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 174, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(50, 50, 50))
        );

        jPanel1.add(Right);
        Right.setBounds(0, 0, 450, 600);

        Left.setBackground(new java.awt.Color(93, 173, 226));
        Left.setMinimumSize(new java.awt.Dimension(450, 600));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 40)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(253, 254, 254));
        jLabel1.setText("LOGIN");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(242, 242, 242));
        jLabel2.setText("Username");

        usernameForm.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        usernameForm.setForeground(new java.awt.Color(86, 101, 115));
        usernameForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFormActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(242, 242, 242));
        jLabel3.setText("Nomor");

        phoneForm.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        phoneForm.setForeground(new java.awt.Color(86, 101, 115));
        phoneForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phoneFormActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Segoe UI", 0, 17)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(242, 242, 242));
        jLabel4.setText("Password");

        passwordForm.setText("jPasswordField1");
        passwordForm.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFormActionPerformed(evt);
            }
        });

        loginBtn.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        loginBtn.setForeground(new java.awt.Color(93, 173, 226));
        loginBtn.setText("Login");
        loginBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginBtnActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(242, 242, 242));
        jLabel5.setText("I don't have an account");

        toSignUpBtn.setBackground(new java.awt.Color(255, 252, 252));
        toSignUpBtn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        toSignUpBtn.setForeground(new java.awt.Color(93, 173, 226));
        toSignUpBtn.setText("Sign Up");
        toSignUpBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                toSignUpBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(160, 160, 160)
                        .addComponent(jLabel1))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(146, 146, 146)
                        .addComponent(loginBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(LeftLayout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(toSignUpBtn))
                            .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel2)
                                .addComponent(usernameForm)
                                .addComponent(jLabel3)
                                .addComponent(phoneForm, javax.swing.GroupLayout.DEFAULT_SIZE, 365, Short.MAX_VALUE)
                                .addComponent(jLabel4)
                                .addComponent(passwordForm)))))
                .addContainerGap(52, Short.MAX_VALUE))
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addComponent(jLabel1)
                .addGap(32, 32, 32)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameForm, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(phoneForm, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(12, 12, 12)
                .addComponent(passwordForm, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(loginBtn)
                .addGap(46, 46, 46)
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(toSignUpBtn))
                .addGap(45, 45, 45))
        );

        jPanel1.add(Left);
        Left.setBounds(450, 0, 450, 600);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void loginBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginBtnActionPerformed
        String username = usernameForm.getText();
        String password = passwordForm.getText();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Response<User> response = userService.loginUser(username, password);
        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(this, "Login successful. Welcome, " + response.getData().getUsername() + "!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
            this.setVisible(false); // ✅ Gunakan instance saat ini, bukan new Login()

            ManajemenBarang manajemenBarangFrame = new ManajemenBarang(response.getData().getUsername(), response.getData().getRole());
            manajemenBarangFrame.setVisible(true);
            
        } else {
            JOptionPane.showMessageDialog(this, "Login failed: " + response.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_loginBtnActionPerformed

    private void toSignUpBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_toSignUpBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_toSignUpBtnActionPerformed

    private void usernameFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFormActionPerformed

    private void phoneFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phoneFormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_phoneFormActionPerformed

    private void passwordFormActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFormActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFormActionPerformed


    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Left;
    private javax.swing.JPanel Right;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JButton loginBtn;
    private javax.swing.JPasswordField passwordForm;
    private javax.swing.JTextField phoneForm;
    private javax.swing.JButton toSignUpBtn;
    private javax.swing.JTextField usernameForm;
    // End of variables declaration//GEN-END:variables
}
