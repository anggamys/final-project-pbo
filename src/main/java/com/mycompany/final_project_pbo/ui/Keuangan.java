/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.final_project_pbo.ui;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import com.mycompany.final_project_pbo.models.FinancialReport;
import com.mycompany.final_project_pbo.services.FinanceService;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author muham
 */
public class Keuangan extends javax.swing.JPanel {

    /**
     * Creates new form Keuangan
     */
    public Keuangan() {
        initComponents();
        initializeComponents(); // Call the method to initialize components
    }

    private final FinanceService financeService = new FinanceService();

    private void initializeComponents() {
        populateDropDowns();
        clearFields();
        ButtonTampilkan.addActionListener(evt -> showReportsSelected());
    }

    private void populateDropDowns() {
        // Clear combo boxes to prevent duplicate entries
        BulanLaporan.removeAllItems();
        yearList.removeAllItems();
        JenisLaporan.removeAllItems();

        // Add type options
        String[] typeReports = { "", "Harian", "Bulanan", "Tahunan" };
        for (String typeReport : typeReports) {
            JenisLaporan.addItem(typeReport);
        }

        // Fetch available dates from reports
        Response<ArrayList<FinancialReport>> response = financeService.getAllFinancialReports();
        if (!response.isSuccess() || response.getData() == null) {
            JOptionPane.showMessageDialog(this,
                    "Failed to retrieve financial reports: " + response.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Set<String> uniqueMonths = new TreeSet<>();
        Set<Integer> uniqueYears = new TreeSet<>();

        for (FinancialReport report : response.getData()) {
            LocalDate date = report.getDate();
            if (date != null) {
                uniqueMonths.add(String.format("%02d", date.getMonthValue()));
                uniqueYears.add(date.getYear());
            }
        }

        // Add default empty option once
        BulanLaporan.addItem("");
        for (String month : uniqueMonths) {
            BulanLaporan.addItem(month);
        }

        yearList.addItem("");
        for (Integer year : uniqueYears) {
            yearList.addItem(String.valueOf(year));
        }
    }

    private void showReportsSelected() {
        String selectedType = (String) JenisLaporan.getSelectedItem();
        String selectedDate = TanggalLaporan.getText().trim(); // Format: e.g., 11
        String selectedMonth = (String) BulanLaporan.getSelectedItem(); // Format: e.g., "06"
        String selectedYear = (String) yearList.getSelectedItem(); // Format: e.g., "2025"

        LocalDate reportDate = null;

        try {
            if (!selectedDate.isEmpty() && !selectedMonth.isEmpty() && !selectedYear.isEmpty()) {
                int day = Integer.parseInt(selectedDate);
                int month = Integer.parseInt(selectedMonth);
                int year = Integer.parseInt(selectedYear);
                reportDate = LocalDate.of(year, month, day);
            }
        } catch (NumberFormatException | DateTimeException e) {
            JOptionPane.showMessageDialog(this, "Tanggal tidak valid. Pastikan format tanggal benar (dd-MM-yyyy).",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Response<ArrayList<FinancialReport>> response = financeService.getAllFinancialReports();
        if (!response.isSuccess() || response.getData() == null) {
            JOptionPane.showMessageDialog(this,
                    "Failed to retrieve financial reports: " + response.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ArrayList<FinancialReport> filteredReports = new ArrayList<>();

        for (FinancialReport report : response.getData()) {
            LocalDate date = report.getDate();
            if (date != null) {
                boolean matchesType = selectedType == null || selectedType.isEmpty()
                        || (selectedType.equals("Harian") && reportDate != null && date.equals(reportDate))
                        || (selectedType.equals("Bulanan") && selectedMonth != null && !selectedMonth.isEmpty()
                                && date.getMonthValue() == Integer.parseInt(selectedMonth)
                                && selectedYear != null && date.getYear() == Integer.parseInt(selectedYear))
                        || (selectedType.equals("Tahunan") && selectedYear != null
                                && date.getYear() == Integer.parseInt(selectedYear));

                if (matchesType) {
                    filteredReports.add(report);
                }
            }
        }

        populateReports(filteredReports);

        TotalPenjualan.setText(String.valueOf(
                filteredReports.stream().mapToDouble(FinancialReport::getTotalIncome).sum()));
        TotalPengeluaran.setText(String.valueOf(
                filteredReports.stream().mapToDouble(FinancialReport::getTotalExpenses).sum()));
        LabaBersih.setText(String.valueOf(
                filteredReports.stream().mapToDouble(FinancialReport::getNetProfit).sum()));
        PriodeLaporan.setText(selectedType + " " + selectedDate + "-" + selectedMonth + "-" + selectedYear);
        TanggalLaporan.setText(selectedDate);
        if (reportDate != null) {
            TanggalLaporan.setText(reportDate.toString());
        } else {
            TanggalLaporan.setText("");
        }
    }

    private void populateReports(ArrayList<FinancialReport> reports) {
        String[] columns = { "Tanggal", "Total Transaksi", "Pendapatan", "Pengeluaran", "Laba" };
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        if (reports != null && !reports.isEmpty()) {
            for (FinancialReport report : reports) {
                LocalDate date = report.getDate();
                if (date != null) {
                    Object[] row = {
                            date.toString(),
                            safeDouble(report.getTotalTransactions()),
                            safeDouble(report.getTotalIncome()),
                            safeDouble(report.getTotalExpenses()),
                            safeDouble(report.getNetProfit())
                    };
                    model.addRow(row);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "No matching financial reports found.",
                    "Informasi", JOptionPane.INFORMATION_MESSAGE);
        }

        TabelLaporaKeuangan.setModel(model);
    }

    private void clearFields() {
        TanggalLaporan.setText("");
        TotalPenjualan.setText("");
        TotalPengeluaran.setText("");
        LabaBersih.setText("");
        PriodeLaporan.setText("");

        JenisLaporan.setSelectedIndex(0);
        BulanLaporan.setSelectedIndex(0);
        yearList.setSelectedIndex(0);
    }

    /**
     * Helper method to safely return a Double value or 0.0 if null
     */
    private double safeDouble(Double value) {
        return value != null ? value : 0.0;
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
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        JenisLaporan = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        TanggalLaporan = new javax.swing.JTextField();
        ButtonTampilkan = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        TotalPenjualan = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TotalPengeluaran = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        LabaBersih = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        PriodeLaporan = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        TabelLaporaKeuangan = new javax.swing.JTable();
        jLabel9 = new javax.swing.JLabel();
        BulanLaporan = new javax.swing.JComboBox<>();
        yearList = new javax.swing.JComboBox<>();

        setPreferredSize(new java.awt.Dimension(890, 590));
        setLayout(new java.awt.CardLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(890, 590));

        jLabel1.setBackground(new java.awt.Color(255, 255, 255));
        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(93, 173, 226));
        jLabel1.setText("Laporan Keuangan  Toko Berkah Abadi");

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel2.setText("Jenis Laporan : ");

        JenisLaporan.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        JenisLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JenisLaporanActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel3.setText("Tanggal Laporan :");

        TanggalLaporan.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

        ButtonTampilkan.setBackground(new java.awt.Color(93, 173, 226));
        ButtonTampilkan.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        ButtonTampilkan.setForeground(new java.awt.Color(255, 255, 255));
        ButtonTampilkan.setText("Tampilkan");
        ButtonTampilkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonTampilkanActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel4.setText("Total Penjualan : ");

        TotalPenjualan.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
        TotalPenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalPenjualanActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel5.setText("Total Pengeluaran : ");

        TotalPengeluaran.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
        TotalPengeluaran.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TotalPengeluaranActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel6.setText("Laba Bersih :");

        LabaBersih.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
        LabaBersih.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LabaBersihActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        jLabel7.setText("Priode Laporan: ");

        PriodeLaporan.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
        PriodeLaporan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PriodeLaporanActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(93, 173, 226));
        jLabel8.setText("Tabel Laporan Keuangan:");

        TabelLaporaKeuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Tanggal", "Total Transaksi", "Pendapatan", "Pengeluaran", "Laba"
            }
        ));
        TabelLaporaKeuangan.setPreferredSize(new java.awt.Dimension(200, 1000));
        jScrollPane1.setViewportView(TabelLaporaKeuangan);

        jLabel9.setFont(new java.awt.Font("Tw Cen MT", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(93, 173, 226));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/mycompany/Img/Laporan_Keuangan.png"))); // NOI18N
        jLabel9.setText("Menu Laporan Keuangan");

        BulanLaporan.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

        yearList.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
        yearList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yearListActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(ButtonTampilkan, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(315, 315, 315)
                        .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel8)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel4))
                        .addGap(73, 73, 73)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(TotalPengeluaran, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 111, Short.MAX_VALUE)
                            .addComponent(TotalPenjualan, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(LabaBersih, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(PriodeLaporan, javax.swing.GroupLayout.Alignment.LEADING)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(JenisLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(TanggalLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(BulanLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(yearList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane1))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(JenisLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 18, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(BulanLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel3)
                        .addComponent(yearList, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(TanggalLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(ButtonTampilkan)
                .addGap(15, 15, 15)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(TotalPenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(TotalPengeluaran, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(LabaBersih, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(PriodeLaporan, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(jLabel8)
                .addGap(20, 20, 20)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        add(jPanel1, "card2");
    }// </editor-fold>//GEN-END:initComponents

    private void yearListActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_yearListActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_yearListActionPerformed

    private void JenisLaporanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_JenisLaporanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_JenisLaporanActionPerformed

    private void ButtonTampilkanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonTampilkanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_ButtonTampilkanActionPerformed

    private void TotalPenjualanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_TotalPenjualanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_TotalPenjualanActionPerformed

    private void TotalPengeluaranActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_TotalPengeluaranActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_TotalPengeluaranActionPerformed

    private void LabaBersihActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_LabaBersihActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_LabaBersihActionPerformed

    private void PriodeLaporanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_PriodeLaporanActionPerformed
        // TODO add your handling code here:
    }// GEN-LAST:event_PriodeLaporanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JComboBox<String> BulanLaporan;
    private javax.swing.JButton ButtonTampilkan;
    private javax.swing.JComboBox<String> JenisLaporan;
    private javax.swing.JTextField LabaBersih;
    private javax.swing.JTextField PriodeLaporan;
    private javax.swing.JTable TabelLaporaKeuangan;
    private javax.swing.JTextField TanggalLaporan;
    private javax.swing.JTextField TotalPengeluaran;
    private javax.swing.JTextField TotalPenjualan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox<String> yearList;
    // End of variables declaration//GEN-END:variables
}
