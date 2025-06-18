/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.final_project_pbo.ui;

import com.mycompany.final_project_pbo.models.DebtTransaction;
import com.mycompany.final_project_pbo.models.LoanStatus;
import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.DebtTransactionRepository;
import com.mycompany.final_project_pbo.services.DebtTransactionService;
import com.mycompany.final_project_pbo.utils.Response;
import com.mycompany.final_project_pbo.utils.SessionManager;

import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author muham
 */
public final class Hutang extends javax.swing.JPanel {

    /**
     * Creates new form HutangPiutang
     */
    public Hutang() {
        initComponents();
        initializeComponents();
        initializeEventListeners();
    }

    User currentUser = SessionManager.getInstance().getCurrentUser();
    DebtTransactionRepository debtTransactionRepository = new DebtTransactionRepository();
    DebtTransactionService debtTransactionService = new DebtTransactionService();

    private void initializeComponents() {
        populateTableHutang();
        populateDropDowns();
    }

    private void initializeEventListeners() {
        SimpanPinjaman.addActionListener(evt -> addNewDebtTransaction());
        EditPinjaman.addActionListener(evt -> editDebtTransaction());
        HapusPinjaman.addActionListener(evt -> deleteDebtTransaction());

        utangTableSelectionChanged();

        LoanStatusOption.addActionListener(evt -> {
            if (utangTable.getSelectedRow() != -1) {
                LoanStatusOption.setEnabled(true);
            }
        });
    }

    private void deleteDebtTransaction() {
        int selectedRow = utangTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin dihapus.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idStr = IDPeminjaman.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Peminjaman tidak boleh kosong.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);
        Integer confirmation = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin menghapus data Hutang Piutang ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return; // User chose not to proceed
        }

        Response<Boolean> response = debtTransactionRepository.deleteById(id, currentUser.getId());
        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(this, "Data Hutang Piutang berhasil dihapus.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            populateTableHutang();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editDebtTransaction() {
        int selectedRow = utangTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang ingin diubah.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idStr = IDPeminjaman.getText();
        if (idStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ID Peminjaman tidak boleh kosong.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        int id = Integer.parseInt(idStr);
        DebtTransaction debtTransaction = new DebtTransaction();
        debtTransaction.setId(id);
        debtTransaction.setDebtorName(NamaPeminjam.getText());
        debtTransaction.setAddress(AlamatPeminjam.getText());
        debtTransaction.setPhoneNumber(NoTelpPeminjam.getText());
        debtTransaction.setAmount(Double.parseDouble(JumlahPeminjaman.getText()));
        debtTransaction.setStatus((LoanStatus) LoanStatusOption.getSelectedItem());
        debtTransaction.setLoanDate(LocalDate.parse(TanggalPeminjaman.getText()));
        debtTransaction.setDueDate(LocalDate.parse(TanggalPelunasanPeminjaman.getText()));
        debtTransaction.setCreatedBy(currentUser.getId());

        Integer confirmation = JOptionPane.showConfirmDialog(this,
                "Apakah Anda yakin ingin mengubah data Hutang Piutang ini?", "Konfirmasi",
                JOptionPane.YES_NO_OPTION);
        if (confirmation != JOptionPane.YES_OPTION) {
            return; // User chose not to proceed
        }
        
        Response<DebtTransaction> response = debtTransactionService.update(debtTransaction, currentUser.getId());
        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(this, "Data Hutang Piutang berhasil diubah.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            populateTableHutang();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNewDebtTransaction() {
        String debtorName = NamaPeminjam.getText();
        String address = AlamatPeminjam.getText();
        String phoneNumber = NoTelpPeminjam.getText();
        String amountStr = JumlahPeminjaman.getText();

        if (debtorName.isEmpty() || address.isEmpty() || phoneNumber.isEmpty() || amountStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Semua field harus diisi.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Jumlah Peminjaman harus berupa angka.", "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        DebtTransaction debtTransaction = new DebtTransaction();
        debtTransaction.setDebtorName(debtorName);
        debtTransaction.setAddress(address);
        debtTransaction.setPhoneNumber(phoneNumber);
        debtTransaction.setAmount(amount);

        Response<DebtTransaction> response = debtTransactionService.save(debtTransaction, currentUser.getId());
        if (response.isSuccess()) {
            JOptionPane.showMessageDialog(this, "Data Hutang Piutang berhasil disimpan.", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            populateTableHutang();
            clearFields();
        } else {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void populateTableHutang() {
        String[] columnNames = { "ID", "Nama", "Alamat Peminjam", "No. Telp Peminjam", "Tanggal Peminjaman",
                "Tanggal Pelunasan Peminjaman", "Jumlah Peminjaman", "Status Peminjaman" };

        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Response<ArrayList<DebtTransaction>> response = debtTransactionRepository.findAll(currentUser.getId());

        if (response.isSuccess()) {
            for (DebtTransaction debt : response.getData()) {
                model.addRow(new Object[] {
                        debt.getId(),
                        debt.getDebtorName(),
                        debt.getAddress(),
                        debt.getPhoneNumber(),
                        debt.getLoanDate(),
                        debt.getDueDate(),
                        debt.getAmount(),
                        debt.getStatus() // Enum will show as string
                });
            }
        } else {
            JOptionPane.showMessageDialog(this, response.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        utangTable.setModel(model);
        utangTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        utangTableSelectionChanged();
    }

    private void utangTableSelectionChanged() {
        ListSelectionModel selectionModel = utangTable.getSelectionModel();
        selectionModel.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = utangTable.getSelectedRow();
                if (selectedRow != -1) {
                    IDPeminjaman.setText(utangTable.getValueAt(selectedRow, 0).toString());
                    NamaPeminjam.setText(utangTable.getValueAt(selectedRow, 1).toString());
                    AlamatPeminjam.setText(utangTable.getValueAt(selectedRow, 2).toString());
                    NoTelpPeminjam.setText(utangTable.getValueAt(selectedRow, 3).toString());
                    TanggalPeminjaman.setText(utangTable.getValueAt(selectedRow, 4).toString());
                    TanggalPelunasanPeminjaman.setText(utangTable.getValueAt(selectedRow, 5).toString());
                    JumlahPeminjaman.setText(utangTable.getValueAt(selectedRow, 6).toString());

                    Object statusObj = utangTable.getValueAt(selectedRow, 7);
                    if (statusObj instanceof LoanStatus) {
                        LoanStatusOption.setSelectedItem((LoanStatus) statusObj);
                    } else if (statusObj != null) {
                        // In case it's stored as String
                        try {
                            LoanStatusOption.setSelectedItem(LoanStatus.valueOf(statusObj.toString()));
                        } catch (IllegalArgumentException ex) {
                            LoanStatusOption.setSelectedIndex(0); // fallback
                        }
                    }

                    LoanStatusOption.setEnabled(true);
                }
            }
        });
    }

    private void populateDropDowns() {
        LoanStatusOption.removeAllItems();
        LoanStatusOption.addItem(null); // optional default value
        for (LoanStatus status : LoanStatus.values()) {
            LoanStatusOption.addItem(status.name());
        }
    }

    private void clearFields() {
        IDPeminjaman.setText("");
        NamaPeminjam.setText("");
        AlamatPeminjam.setText("");
        NoTelpPeminjam.setText("");
        TanggalPeminjaman.setText("");
        TanggalPelunasanPeminjaman.setText("");
        JumlahPeminjaman.setText("");
        LoanStatusOption.setSelectedIndex(0);
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        utangTable = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        IDPeminjaman = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        NamaPeminjam = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        AlamatPeminjam = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        NoTelpPeminjam = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        TanggalPeminjaman = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        TanggalPelunasanPeminjaman = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        JumlahPeminjaman = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        StatusPeminjaman = new javax.swing.JTextField();
        SimpanPinjaman = new javax.swing.JButton();
        HapusPinjaman = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        LoanStatusOption = new javax.swing.JComboBox<>();
        EditPinjaman = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(890, 590));
        setLayout(new java.awt.CardLayout());

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setPreferredSize(new java.awt.Dimension(890, 590));

        jLabel1.setBackground(new java.awt.Color(93, 173, 226));
        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(93, 173, 226));
        jLabel1.setText("Data Hutang Piutang Pelanggan dan Pegawai");

        utangTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "ID ", "Nama ", "Status (Pegawai/Pelanggan)", "Alamat Peminjam", "No. Telp Peminjam", "Tanggal Peminjaman", "Tanggal Pelunasan Peminjaman", "Jumlah Peminjaman", "Status Peminjaman"
            }
        ));
        utangTable.setPreferredSize(new java.awt.Dimension(300, 870));
        jScrollPane1.setViewportView(utangTable);
        if (utangTable.getColumnModel().getColumnCount() > 0) {
            utangTable.getColumnModel().getColumn(0).setPreferredWidth(20);
            utangTable.getColumnModel().getColumn(1).setPreferredWidth(60);
        }

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel2.setText("ID Peminjaman:");

        IDPeminjaman.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        IDPeminjaman.setEnabled(false);
        IDPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IDPeminjamanActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel3.setText("Nama Peminjam :");

        NamaPeminjam.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        NamaPeminjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NamaPeminjamActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel4.setText("Status Peminjam :");

        jLabel5.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel5.setText("Alamat Peminjam :");

        AlamatPeminjam.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        AlamatPeminjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlamatPeminjamActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel6.setText("No. Telp Peminjam :");

        NoTelpPeminjam.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        NoTelpPeminjam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoTelpPeminjamActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel7.setText("Tanggal Peminjaman :");

        TanggalPeminjaman.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        TanggalPeminjaman.setEnabled(false);
        TanggalPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalPeminjamanActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel8.setText("Tanggal Pelunasan Peminjaman : ");

        TanggalPelunasanPeminjaman.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        TanggalPelunasanPeminjaman.setEnabled(false);
        TanggalPelunasanPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TanggalPelunasanPeminjamanActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel9.setText("Jumlah Peminjaman : ");

        JumlahPeminjaman.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        JumlahPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JumlahPeminjamanActionPerformed(evt);
            }
        });

        jLabel10.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel10.setText("Status Peminjaman : ");

        StatusPeminjaman.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        StatusPeminjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StatusPeminjamanActionPerformed(evt);
            }
        });

        SimpanPinjaman.setFont(new java.awt.Font("Tw Cen MT", 1, 10)); // NOI18N
        SimpanPinjaman.setForeground(new java.awt.Color(93, 173, 226));
        SimpanPinjaman.setText("Simpan Pinjaman");
        SimpanPinjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SimpanPinjamanActionPerformed(evt);
            }
        });

        HapusPinjaman.setFont(new java.awt.Font("Tw Cen MT", 1, 10)); // NOI18N
        HapusPinjaman.setForeground(new java.awt.Color(93, 173, 226));
        HapusPinjaman.setText("Hapus Pinjaman");
        HapusPinjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HapusPinjamanActionPerformed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tw Cen MT", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(93, 173, 226));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Peminjaman.png"))); // NOI18N
        jLabel11.setText("Menu Peminjaman");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 234, Short.MAX_VALUE)
        );

        LoanStatusOption.setFont(new java.awt.Font("Tw Cen MT", 0, 12)); // NOI18N
        LoanStatusOption.setEnabled(false);

        EditPinjaman.setFont(new java.awt.Font("Tw Cen MT", 1, 10)); // NOI18N
        EditPinjaman.setForeground(new java.awt.Color(93, 173, 226));
        EditPinjaman.setText("Edit Pinjaman");
        EditPinjaman.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditPinjamanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 870, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(JumlahPeminjaman)
                                    .addComponent(TanggalPelunasanPeminjaman)
                                    .addComponent(TanggalPeminjaman)
                                    .addComponent(NoTelpPeminjam)
                                    .addComponent(AlamatPeminjam)
                                    .addComponent(LoanStatusOption, 0, 153, Short.MAX_VALUE)
                                    .addComponent(NamaPeminjam)
                                    .addComponent(IDPeminjaman, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(SimpanPinjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(HapusPinjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(EditPinjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(IDPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(NamaPeminjam, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(LoanStatusOption, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(AlamatPeminjam, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(NoTelpPeminjam, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TanggalPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(TanggalPelunasanPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(JumlahPeminjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(SimpanPinjaman, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(HapusPinjaman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(EditPinjaman, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1))))
        );

        add(jPanel2, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void IDPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_IDPeminjamanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_IDPeminjamanActionPerformed

    private void NamaPeminjamActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_NamaPeminjamActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_NamaPeminjamActionPerformed

    private void AlamatPeminjamActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_AlamatPeminjamActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_AlamatPeminjamActionPerformed

    private void NoTelpPeminjamActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_NoTelpPeminjamActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_NoTelpPeminjamActionPerformed

    private void TanggalPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_TanggalPeminjamanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_TanggalPeminjamanActionPerformed

    private void TanggalPelunasanPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_TanggalPelunasanPeminjamanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_TanggalPelunasanPeminjamanActionPerformed

    private void JumlahPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_JumlahPeminjamanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_JumlahPeminjamanActionPerformed

    private void StatusPeminjamanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_StatusPeminjamanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_StatusPeminjamanActionPerformed

    private void SimpanPinjamanActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Implement your logic for saving a loan here
    }

    private void HapusPinjamanActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Implement your logic for deleting a loan here
    }

    private void EditPinjamanActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        // Implement your logic for editing a loan here
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField AlamatPeminjam;
    private javax.swing.JButton EditPinjaman;
    private javax.swing.JButton HapusPinjaman;
    private javax.swing.JTextField IDPeminjaman;
    private javax.swing.JTextField JumlahPeminjaman;
    private javax.swing.JComboBox<LoanStatus> LoanStatusOption;
    private javax.swing.JTextField NamaPeminjam;
    private javax.swing.JTextField NoTelpPeminjam;
    private javax.swing.JButton SimpanPinjaman;
    private javax.swing.JTextField StatusPeminjaman;
    private javax.swing.JTextField TanggalPelunasanPeminjaman;
    private javax.swing.JTextField TanggalPeminjaman;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable utangTable;
    // End of variables declaration//GEN-END:variables
}
