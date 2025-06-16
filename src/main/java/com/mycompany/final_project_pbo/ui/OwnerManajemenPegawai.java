/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.final_project_pbo.ui;

import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.UserRepository;
import com.mycompany.final_project_pbo.utils.Response;
import com.mycompany.final_project_pbo.utils.SessionManager;

/**
 *
 * @author muham
 */
public class OwnerManajemenPegawai extends javax.swing.JPanel {

    /**
     * Creates new form ManajemenPegawai
     */
    public OwnerManajemenPegawai() {
        initComponents();
        initializeComponents();
    }

    private void initializeComponents() {
        populateTableUser();
        populateDropdown();
        initListeners();

        clearInputFields();
    }

    User currentUser = SessionManager.getInstance().getCurrentUser();
    UserRepository userRepository = new UserRepository();

    private void initListeners() {
        TambahkanPegawai.addActionListener(e -> addUser());
        EditPegawai.addActionListener(e -> editUser());
        HapusPegawai.addActionListener(e -> deleteUser());
        KosongkanKolomPegawai.addActionListener(e -> clearInputFields());
    }

    private void deleteUser() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            int userId = (Integer) jTable1.getValueAt(selectedRow, 0);
            Integer confirmation = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this user?",
                    "Confirm Deletion",
                    javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirmation != javax.swing.JOptionPane.YES_OPTION) {
                return; // User cancelled the deletion
            }
            Response<Boolean> response = userRepository.deleteById(userId, currentUser.getId());
            if (response.isSuccess()) {
                System.out.println("User deleted successfully");
                populateTableUser();
                clearInputFields();
            } else {
                System.out.println("Error deleting user: " + response.getMessage());
            }
        }
    }

    private void editUser() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            String username = Username.getText();
            String password = Password.getText();
            String email = Email.getText();
            String position = jComboBox1.getSelectedItem().toString();

            User user = new User();
            user.setId((Integer) jTable1.getValueAt(selectedRow, 0));
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setIsOwner(position.equals("Owner"));

            Integer confirmation = javax.swing.JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to update this user?",
                    "Confirm Update",
                    javax.swing.JOptionPane.YES_NO_OPTION);

            if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
                Response<User> response = userRepository.update(user, currentUser.getId());
                if (response.isSuccess()) {
                    System.out.println("User updated successfully: " + response.getData());
                    populateTableUser();
                    clearInputFields();
                } else {
                    System.out.println("Error updating user: " + response.getMessage());
                }
            }
        }
    }

    private void addUser() {
        String username = Username.getText();
        String password = Password.getText();
        String email = Email.getText();
        String position = jComboBox1.getSelectedItem().toString();

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.setIsOwner(position.equals("Owner"));

        Response<User> response = userRepository.save(user, currentUser.getId());
        if (response.isSuccess()) {
            System.out.println("User added successfully: " + response.getData());
            populateTableUser();
            clearInputFields();
        } else {
            System.out.println("Error adding user: " + response.getMessage());
        }
    }

    private void setTextField() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            Username.setText(jTable1.getValueAt(selectedRow, 1).toString());
            Email.setText(jTable1.getValueAt(selectedRow, 2).toString());
            jComboBox1.setSelectedItem(jTable1.getValueAt(selectedRow, 3).toString());
        }
    }

    private void populateTableUser() {
        String[] columnNames = { "IDUser", "Username", "Email", "Posisi" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing of table cells
            }
        };

        Response<ArrayList<User>> response = userRepository.findAll(currentUser.getId());
        if (response.isSuccess()) {
            ArrayList<User> users = response.getData();

            for (User user : users) {
                Object[] row = { user.getId(), user.getUsername(), user.getEmail(),
                        user.getIsOwner() ? "Owner" : "Staff" };
                model.addRow(row);
            }
        } else {
            System.out.println("Error fetching users: " + response.getMessage());
        }

        jTable1.setModel(model);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(50); // Set width for ID column
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(150); // Set width for Username column
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(200); // Set width for Email column
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100); // Set width for Posisi column

        // Event listener for row selection
        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                setTextField();
            }
        });
    }

    private void populateDropdown() {
        // Populate the dropdown with positions
        jComboBox1.removeAllItems(); // Clear existing items
        jComboBox1.addItem("");
        jComboBox1.addItem("Staff");
        jComboBox1.addItem("Owner");
    }

    private void clearInputFields() {
        // Clear the input fields
        Username.setText("");
        Password.setText("");
        Email.setText("");
        jComboBox1.setSelectedIndex(0); // Reset to first item
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        Username = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Password = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        Email = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        TambahkanPegawai = new javax.swing.JButton();
        EditPegawai = new javax.swing.JButton();
        HapusPegawai = new javax.swing.JButton();
        KosongkanKolomPegawai = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(93, 173, 226));
        jLabel1.setText("Manajemen Data Pegawai");

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel2.setText("Username: ");

        Username.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel3.setText("Password:");

        Password.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel4.setText("Email:");

        Email.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        jLabel5.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel5.setText("Posisi:");

        jComboBox1.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

        TambahkanPegawai.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        TambahkanPegawai.setForeground(new java.awt.Color(93, 173, 226));
        TambahkanPegawai.setText("Tambahkan");

        EditPegawai.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        EditPegawai.setForeground(new java.awt.Color(93, 173, 226));
        EditPegawai.setText("Edit Data");

        HapusPegawai.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        HapusPegawai.setForeground(new java.awt.Color(93, 173, 226));
        HapusPegawai.setText("Hapus Data");

        KosongkanKolomPegawai.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        KosongkanKolomPegawai.setForeground(new java.awt.Color(93, 173, 226));
        KosongkanKolomPegawai.setText("Kosongkan Kolom");

        jLabel6.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(93, 173, 226));
        jLabel6.setText("Data Pegawai");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "IDUser", "Nama Pegawai", "Email", "Posisi"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel1)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addGap(180, 180, 180)
                            .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel4)
                                .addComponent(jLabel5))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(Email, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(TambahkanPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EditPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HapusPegawai, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(KosongkanKolomPegawai))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel1)
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(Username, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Password, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Email, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(TambahkanPegawai)
                    .addComponent(EditPegawai)
                    .addComponent(HapusPegawai)
                    .addComponent(KosongkanKolomPegawai))
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(25, Short.MAX_VALUE))
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton EditPegawai;
    private javax.swing.JTextField Email;
    private javax.swing.JButton HapusPegawai;
    private javax.swing.JButton KosongkanKolomPegawai;
    private javax.swing.JTextField Password;
    private javax.swing.JButton TambahkanPegawai;
    private javax.swing.JTextField Username;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
