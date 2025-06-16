/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.final_project_pbo.ui;

import java.util.ArrayList;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.mycompany.final_project_pbo.models.Category;
import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.CategoryRepository;
import com.mycompany.final_project_pbo.utils.Response;
import com.mycompany.final_project_pbo.utils.SessionManager;

/**
 *
 * @author muham
 */
public class OwnerKategori extends javax.swing.JPanel {

    /**
     * Creates new form OwnerKategori
     */
    public OwnerKategori() {
        initComponents();
        initializeComponents();
    }

    CategoryRepository categoryRepository = new CategoryRepository();

    private void initializeComponents() {
        populateTableCategory();
        initListeners();

        clearInputFields();
    }

    User currentUser = SessionManager.getInstance().getCurrentUser();

    private void initListeners() {
        ButtonTambahkanKategori.addActionListener(e -> addCategory());
        ButtonEditKategori.addActionListener(e -> editCategory());
        ButtonHapusKategori.addActionListener(e -> deleteCategory());
        ButtonKosongkanKolomKategori.addActionListener(e -> clearInputFields());

        jTable1.getSelectionModel().addListSelectionListener(e -> setTextField());
    }

    private void deleteCategory() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            // Show error message if no row is selected
            JOptionPane.showMessageDialog(this, "Please select a category to delete.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = (int) jTable1.getValueAt(selectedRow, 0);

        Integer confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this category?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            // User chose not to delete
            return;
        }

        Response<Boolean> response = categoryRepository.deleteById(id, currentUser.getId());

        if (response.isSuccess()) {
            populateTableCategory();
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error deleting category: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error deleting category: " + response.getMessage());
        }
    }

    private void editCategory() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            // Show error message if no row is selected
            JOptionPane.showMessageDialog(this, "Please select a category to edit.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String name = KategoriBarang.getText();
        String description = KategoriBarang1.getText();
        int id = (int) jTable1.getValueAt(selectedRow, 0);

        if (name.isEmpty() || description.isEmpty()) {
            // Show error message for empty fields
            System.err.println("Fields cannot be empty");
            return;
        }

        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setDescription(description);

        Integer confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to update this category?", "Confirm Update", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            // User chose not to update
            return;
        }

        Response<Category> response = categoryRepository.update(category, currentUser.getId());

        if (response.isSuccess()) {
            populateTableCategory();
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error updating category: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error updating category: " + response.getMessage());
        }
    }

    private void addCategory() {
        String name = KategoriBarang.getText();
        String description = KategoriBarang1.getText();

        if (name.isEmpty() || description.isEmpty()) {
            // Show error message for empty fields
            System.err.println("Fields cannot be empty");
            return;
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        Response<Category> response = categoryRepository.save(category, currentUser.getId());

        if (response.isSuccess()) {
            populateTableCategory();
            clearInputFields();
        } else {
            JOptionPane.showMessageDialog(this, "Error adding category: " + response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.err.println("Error adding category: " + response.getMessage());
        }
    }

    private void setTextField() {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow != -1) {
            KategoriBarang.setText(jTable1.getValueAt(selectedRow, 1).toString());
            KategoriBarang1.setText(jTable1.getValueAt(selectedRow, 2).toString());
        } else {
            clearInputFields();
        }
    }

    private void clearInputFields() {
        KategoriBarang.setText("");
        KategoriBarang1.setText("");
    }

    private void populateTableCategory() {
        String[] columnNames = {"IDKategori", "Nama Kategori", "Description"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Prevent editing of table cells
            }
        };

        Response<ArrayList<Category>> response = categoryRepository.findAll(currentUser.getId());
        if (response.isSuccess()) {
            ArrayList<Category> categories = response.getData();
            for (Category category : categories) {
                Object[] row = {category.getId(), category.getName(), category.getDescription()};
                model.addRow(row);
            }
        } else {
            // Handle error case, e.g., show a message dialog
            System.err.println("Error fetching categories: " + response.getMessage());
        }

        jTable1.setModel(model);
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(50); // Set width for IDKategori column
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(200); // Set width for Nama Kategori column
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(300); // Set width for Description column

        jTable1.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                setTextField();
            }
        });
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
        Kategori = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        KategoriBarang = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        KategoriBarang1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        ButtonTambahkanKategori = new javax.swing.JButton();
        ButtonEditKategori = new javax.swing.JButton();
        ButtonHapusKategori = new javax.swing.JButton();
        ButtonKosongkanKolomKategori = new javax.swing.JButton();

        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        Kategori.setBackground(new java.awt.Color(255, 255, 255));
        Kategori.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        Kategori.setForeground(new java.awt.Color(93, 173, 226));
        Kategori.setText("Data Kategori Barang");

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel1.setText("Kategori:");

        KategoriBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        KategoriBarang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KategoriBarangActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel2.setText("Deskripsi Kategori Barang:");

        KategoriBarang1.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N

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
                "IDKategori", "Nama Kategori", "Description", "Tanggal"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        ButtonTambahkanKategori.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        ButtonTambahkanKategori.setForeground(new java.awt.Color(93, 173, 226));
        ButtonTambahkanKategori.setText("Tambahkan");

        ButtonEditKategori.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        ButtonEditKategori.setForeground(new java.awt.Color(93, 173, 226));
        ButtonEditKategori.setText("Edit Data");

        ButtonHapusKategori.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        ButtonHapusKategori.setForeground(new java.awt.Color(93, 173, 226));
        ButtonHapusKategori.setText("Hapus Data");

        ButtonKosongkanKolomKategori.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        ButtonKosongkanKolomKategori.setForeground(new java.awt.Color(93, 173, 226));
        ButtonKosongkanKolomKategori.setText("Kosongkan Kolom");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(Kategori)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel2))
                                .addGap(45, 45, 45)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(KategoriBarang1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(KategoriBarang, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(ButtonTambahkanKategori)
                                .addGap(29, 29, 29)
                                .addComponent(ButtonEditKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(ButtonHapusKategori, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(ButtonKosongkanKolomKategori)))))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(Kategori)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(KategoriBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(KategoriBarang1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ButtonTambahkanKategori)
                    .addComponent(ButtonEditKategori)
                    .addComponent(ButtonHapusKategori)
                    .addComponent(ButtonKosongkanKolomKategori))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 339, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void KategoriBarangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KategoriBarangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_KategoriBarangActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEditKategori;
    private javax.swing.JButton ButtonHapusKategori;
    private javax.swing.JButton ButtonKosongkanKolomKategori;
    private javax.swing.JButton ButtonTambahkanKategori;
    private javax.swing.JLabel Kategori;
    private javax.swing.JTextField KategoriBarang;
    private javax.swing.JTextField KategoriBarang1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
