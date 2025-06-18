/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.mycompany.final_project_pbo.ui;

import com.mycompany.final_project_pbo.models.Category;
import com.mycompany.final_project_pbo.models.Dropdown;
import com.mycompany.final_project_pbo.models.Notification;
import com.mycompany.final_project_pbo.models.NotificationType;
import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.models.StockTransaction;
import com.mycompany.final_project_pbo.models.TransactionType;
import com.mycompany.final_project_pbo.models.User;
import com.mycompany.final_project_pbo.repositories.CategoryRepository;
import com.mycompany.final_project_pbo.repositories.NotificationRepository;
import com.mycompany.final_project_pbo.repositories.ProductRepository;
import com.mycompany.final_project_pbo.repositories.StockTransactionRepository;
import com.mycompany.final_project_pbo.repositories.UserRepository;
import com.mycompany.final_project_pbo.utils.Response;
import com.mycompany.final_project_pbo.utils.SessionManager;
import com.mycompany.final_project_pbo.utils.TransactionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;

/**
 *
 * @author muham
 */
public class ManajemenBarang extends javax.swing.JPanel {

        /**
         * Creates new form ManajemenBarang
         */
        public ManajemenBarang() {
                initComponents();
                initializeComponents();
                initializeEventsListener();
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();
        Product product = new Product();
        UserRepository userRepository = new UserRepository();
        ProductRepository productRepository = new ProductRepository();
        CategoryRepository categoryRepository = new CategoryRepository();
        StockTransactionRepository stockTransactionRepository = new StockTransactionRepository();
        NotificationRepository notificationRepository = new NotificationRepository();

        private void initializeEventsListener() {
                ButtonScanBarang.addActionListener(evt -> openScanBarang());
        }

        private void openScanBarang() {
                // Set tipe transaksi
                TransactionManager.getInstance().setTransaction(TransactionType.IN, null);

                // Pastikan Dashboard (parent) di-close jika ada
                JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
                if (parentFrame != null && parentFrame instanceof Dashboard) {
                        parentFrame.dispose();
                }

                // Close frame ini (ManajemenBarang)
                this.setVisible(false);

                // Buka FrameScanBarang
                SwingUtilities.invokeLater(() -> {
                        new FrameScanBarang().setVisible(true);
                });
        }

        private void initializeComponents() {
                populateDropDowns();
                populateTableRiwayatAktivitas();
                initSortRiwayatAktivitas();
                filteredAndSortedProducts("", "");

                // Ambil current product dari TransactionManager
                Product selectedProduct = TransactionManager.getInstance().getCurrentProduct();
                if (selectedProduct != null && selectedProduct.getBarcode() != null
                                && !selectedProduct.getBarcode().isEmpty()) {
                        setSelectedProduct(selectedProduct); // Tampilkan data di seluruh field, walau hanya barcode
                        System.out.println("Current Product: " + selectedProduct.getBarcode());
                        // Hapus clearTransaction di sini, cukup dilakukan saat klik Simpan saja
                } else {
                        clearForm();
                        System.out.println("Current Product: null");
                }

                // Search filter for products
                SearchBarang.addKeyListener(new KeyAdapter() {
                        @Override
                        public void keyReleased(KeyEvent e) {
                                String searchText = SearchBarang.getText().toLowerCase();
                                String sortBy = (String) SortItem.getSelectedItem();
                                filteredAndSortedProducts(searchText, sortBy);
                        }
                });

                // Sort filter for products
                SortItem.addActionListener(evt -> {
                        String searchText = SearchBarang.getText().toLowerCase();
                        String sortBy = (String) SortItem.getSelectedItem();
                        filteredAndSortedProducts(searchText, sortBy);
                });
        }

        private void filteredAndSortedProducts(String searchText, String sortBy) {
                Response<ArrayList<Product>> response = productRepository.findAll(currentUser.getId());
                if (!response.isSuccess()) {
                        JOptionPane.showMessageDialog(this, "Gagal memuat produk: " + response.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                        return;
                }

                ArrayList<Product> products = response.getData();

                // Filter
                List<Product> filteredProducts = products.stream()
                                .filter(p -> p.getName().toLowerCase().contains(searchText) ||
                                                p.getBarcode().toLowerCase().contains(searchText))
                                .toList();

                // Sort
                Comparator<Product> comparator = switch (sortBy) {
                        case "IDBarang" -> Comparator.comparing(Product::getId);
                        case "Nama" -> Comparator.comparing(Product::getName);
                        case "Barcode" -> Comparator.comparing(Product::getBarcode);
                        case "Kategori" -> Comparator.comparing(Product::getCategoryId);
                        case "HargaBeli" -> Comparator.comparing(Product::getPurchasePrice);
                        case "HargaJual" -> Comparator.comparing(Product::getSellingPrice);
                        case "Stok" -> Comparator.comparing(Product::getStock);
                        default -> null;
                };

                if (comparator != null) {
                        filteredProducts = new ArrayList<>(filteredProducts);
                        filteredProducts.sort(comparator);
                }

                populateTableProducts(new ArrayList<>(filteredProducts));
        }

        private void initSortRiwayatAktivitas() {
                String[] sortOptions = { "", "Tanggal & Waktu", "Aktivitas", "Jenis Aktivitas", "Jumlah", "Barang",
                                "Oleh" };
                SortRiwayatAktivitas.setModel(new DefaultComboBoxModel<>(sortOptions));
                SortRiwayatAktivitas.setSelectedIndex(0);

                SortRiwayatAktivitas.addActionListener(evt -> {
                        int columnIndex = SortRiwayatAktivitas.getSelectedIndex();
                        if (columnIndex >= 0) {
                                TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(
                                                (DefaultTableModel) TabelRiwayatAktivitas.getModel());
                                sorter.setSortKeys(List.of(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
                                TabelRiwayatAktivitas.setRowSorter(sorter);
                        }
                });
        }

        private void populateTableRiwayatAktivitas() {
                String[] columnNames = { "Tanggal & Waktu", "Aktivitas", "Jenis Aktivitas", "Jumlah", "Barang",
                                "Oleh" };
                DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false;
                        }
                };

                Response<ArrayList<StockTransaction>> response = stockTransactionRepository
                                .findAll(currentUser.getId());
                if (!response.isSuccess()) {
                        JOptionPane.showMessageDialog(this, "Tidak ada riwayat aktivitas yang ditemukan.", "Info",
                                        JOptionPane.INFORMATION_MESSAGE);
                        return;
                }

                for (StockTransaction transaction : response.getData()) {
                        String typeActivity = switch (transaction.getTransactionType()) {
                                case IN -> "Barang Masuk";
                                case OUT -> "Barang Keluar";
                                default -> "Edit";
                        };

                        Response<Product> productResponse = productRepository.findById(transaction.getProductId(),
                                        currentUser.getId());
                        String productName = productResponse.isSuccess() && productResponse.getData() != null
                                        ? productResponse.getData().getName()
                                        : "Tidak Ditemukan";

                        Response<User> userResponse = userRepository.findById(transaction.getUserId(),
                                        currentUser.getId());
                        String userName = userResponse.isSuccess() && userResponse.getData() != null
                                        ? userResponse.getData().getUsername()
                                        : "Tidak Ditemukan";

                        model.addRow(new Object[] {
                                        transaction.getCreatedAt(),
                                        transaction.getDescription(),
                                        typeActivity,
                                        transaction.getQuantity(),
                                        productName,
                                        userName
                        });
                }

                TabelRiwayatAktivitas.setModel(model);
        }

        private void populateTableProducts(ArrayList<Product> products) {
                String[] columnNames = { "No", "Nama", "Barcode", "Kategori", "HargaBeli", "HargaJual", "Stok" };
                DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
                        @Override
                        public boolean isCellEditable(int row, int column) {
                                return false; // Make table non-editable
                        }
                };

                showLowStockNotification(products);

                // Populate rows
                for (Product product : products) {
                        String categoryName = "Tidak Diketahui";
                        Response<Category> categoryResponse = categoryRepository.findById(product.getCategoryId(),
                                        currentUser.getId());
                        if (categoryResponse.isSuccess() && categoryResponse.getData() != null) {
                                categoryName = categoryResponse.getData().getName();
                        }

                        model.addRow(new Object[] {

                                        product.getId(),
                                        product.getName(),
                                        product.getBarcode(),
                                        categoryName,
                                        product.getPurchasePrice(),
                                        product.getSellingPrice(),
                                        product.getStock()
                        });
                }

                TabelManajemenBarang.setModel(model);

                // Event listener for row selection
                TabelManajemenBarang.getSelectionModel().addListSelectionListener(event -> {
                        if (!event.getValueIsAdjusting() && TabelManajemenBarang.getSelectedRow() != -1) {
                                int selectedRow = TabelManajemenBarang
                                                .convertRowIndexToModel(TabelManajemenBarang.getSelectedRow());
                                int productId = (int) model.getValueAt(selectedRow, 0);

                                Response<Product> productResponse = productRepository.findById(productId,
                                                currentUser.getId());
                                if (productResponse.isSuccess() && productResponse.getData() != null) {
                                        setSelectedProduct(productResponse.getData());
                                } else {
                                        JOptionPane.showMessageDialog(this, "Produk tidak ditemukan.", "Error",
                                                        JOptionPane.ERROR_MESSAGE);
                                        clearForm();
                                }
                        }
                });
        }

        private void showLowStockNotification(ArrayList<Product> products) {
                StringBuilder warningMessages = new StringBuilder();
                boolean foundLowStock = false;

                // Ambil notifikasi yang sudah ada dari DB (bisa null)
                ArrayList<Notification> existingNotifications = null;
                Response<ArrayList<Notification>> notifResponse = notificationRepository.findAll(currentUser.getId());
                if (notifResponse.isSuccess()) {
                        existingNotifications = notifResponse.getData();
                }
                if (existingNotifications == null)
                        existingNotifications = new ArrayList<>();

                for (Product product : products) {
                        Integer stock = product.getStock();
                        if (stock != null && stock <= 5) {
                                foundLowStock = true;

                                // Buat pesan notifikasi sesuai stok
                                String notifMsg = (stock == 0)
                                                ? "Stok produk '" + product.getName() + "' kosong!"
                                                : "Stok produk '" + product.getName() + "' hampir habis (" + stock
                                                                + ").";

                                // Cek apakah notifikasi serupa sudah ada
                                boolean alreadyNotified = existingNotifications.stream()
                                                .anyMatch(n -> n.getType() == NotificationType.PRODUCT &&
                                                                n.getMessage() != null &&
                                                                n.getMessage().contains(product.getName()));

                                if (!alreadyNotified) {
                                        Notification notification = new Notification();
                                        notification.setType(NotificationType.PRODUCT);
                                        notification.setMessage(notifMsg);
                                        notificationRepository.save(notification, currentUser.getId());
                                }

                                // Tambahkan ke pesan peringatan
                                warningMessages.append(notifMsg).append("\n");
                        }
                }

                if (foundLowStock) {
                        JOptionPane.showMessageDialog(
                                        this,
                                        warningMessages.toString(),
                                        "Peringatan Stok",
                                        JOptionPane.WARNING_MESSAGE);
                }
        }

        private void setSelectedProduct(Product product) {
                // ID Barang
                IDBarang.setText(
                                product.getId() != null && product.getId() != 0 ? String.valueOf(product.getId()) : "");
                // Nama Barang
                NamaBarang.setText(product.getName() != null ? product.getName() : "");
                // Barcode (selalu diisi)
                Barcode.setText(product.getBarcode() != null ? product.getBarcode() : "");
                // Kategori
                boolean foundCategory = false;
                if (product.getCategoryId() != null) {
                        Response<Category> categoryResponse = categoryRepository.findById(product.getCategoryId(),
                                        currentUser.getId());
                        if (categoryResponse.isSuccess() && categoryResponse.getData() != null) {
                                String categoryName = categoryResponse.getData().getName();
                                for (int i = 0; i < KategoriBarang.getItemCount(); i++) {
                                        Dropdown item = (Dropdown) KategoriBarang.getItemAt(i);
                                        if (item.getId() != null && item.getId().equals(product.getCategoryId())) {
                                                KategoriBarang.setSelectedIndex(i);
                                                foundCategory = true;
                                                break;
                                        }
                                }
                        }
                }
                if (!foundCategory) {
                        KategoriBarang.setSelectedIndex(0); // Kosong jika kategori tidak ditemukan
                }
                // Harga Beli
                HargaBeliBarang.setText(product.getPurchasePrice() != null && product.getPurchasePrice() > 0
                                ? String.valueOf(product.getPurchasePrice())
                                : "");
                // Harga Jual
                HargaJualBarang.setText(product.getSellingPrice() != null && product.getSellingPrice() > 0
                                ? String.valueOf(product.getSellingPrice())
                                : "");
                // Stok
                StockBarang.setText(product.getStock() != null && product.getStock() > 0
                                ? String.valueOf(product.getStock())
                                : "");
        }

        private void populateDropDowns() {
                // Populate KategoriBarang
                KategoriBarang.removeAllItems();
                KategoriBarang.addItem(new Dropdown(null, "")); // default blank item

                Response<ArrayList<Category>> response = categoryRepository.findAll(currentUser.getId());
                if (response.isSuccess()) {
                        for (Category category : response.getData()) {
                                KategoriBarang.addItem(new Dropdown(category.getId(), category.getName()));
                        }
                } else {
                        JOptionPane.showMessageDialog(this, "Gagal memuat kategori: " + response.getMessage(), "Error",
                                        JOptionPane.ERROR_MESSAGE);
                }

                // Populate SortItem dropdown
                SortItem.removeAllItems();
                SortItem.addItem(""); // default blank item
                String[] sortOptions = { "IDBarang", "Nama", "Barcode", "Kategori", "HargaBeli", "HargaJual", "Stok" };
                for (String option : sortOptions) {
                        SortItem.addItem(option);
                }
        }

        private void clearForm() {
                IDBarang.setText("");
                NamaBarang.setText("");
                Barcode.setText("");
                KategoriBarang.setSelectedIndex(0);
                HargaBeliBarang.setText("");
                HargaJualBarang.setText("");
                StockBarang.setText("");
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
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jPanel1 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                jLabel2 = new javax.swing.JLabel();
                SortItem = new javax.swing.JComboBox<>();
                jScrollPane1 = new javax.swing.JScrollPane();
                TabelManajemenBarang = new javax.swing.JTable();
                jLabel3 = new javax.swing.JLabel();
                IDBarang = new javax.swing.JTextField();
                jLabel4 = new javax.swing.JLabel();
                NamaBarang = new javax.swing.JTextField();
                jLabel5 = new javax.swing.JLabel();
                jLabel6 = new javax.swing.JLabel();
                Barcode = new javax.swing.JTextField();
                jLabel7 = new javax.swing.JLabel();
                HargaJualBarang = new javax.swing.JTextField();
                ButtonTambahBarang = new javax.swing.JButton();
                ButtonEditBarang = new javax.swing.JButton();
                ButtonHapusBarang = new javax.swing.JButton();
                jLabel8 = new javax.swing.JLabel();
                jLabel9 = new javax.swing.JLabel();
                SortRiwayatAktivitas = new javax.swing.JComboBox<>();
                jScrollPane2 = new javax.swing.JScrollPane();
                TabelRiwayatAktivitas = new javax.swing.JTable();
                jLabel10 = new javax.swing.JLabel();
                SearchBarang = new javax.swing.JTextField();
                jPanel2 = new javax.swing.JPanel();
                KategoriBarang = new javax.swing.JComboBox<>();
                clearForm = new javax.swing.JButton();
                jLabel11 = new javax.swing.JLabel();
                StockBarang = new javax.swing.JTextField();
                ButtonScanBarang = new javax.swing.JButton();
                jLabel12 = new javax.swing.JLabel();
                HargaBeliBarang = new javax.swing.JTextField();

                setPreferredSize(new java.awt.Dimension(890, 590));
                setLayout(new java.awt.CardLayout());

                jPanel1.setBackground(new java.awt.Color(255, 255, 255));
                jPanel1.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N

                jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
                jLabel1.setForeground(new java.awt.Color(93, 173, 226));
                jLabel1.setText("Data Barang Toko Berkah Abadi");

                jLabel2.setFont(new java.awt.Font("Tw Cen MT", 0, 16)); // NOI18N
                jLabel2.setText("Urutkan Berdasarkan: ");

                SortItem.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N

                TabelManajemenBarang.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null },
                                                { null, null, null, null, null, null, null }
                                },
                                new String[] {
                                                "IDBarang", "Nama", "Barcode", "Kategori", "HargaBeli", "HargaJual",
                                                "Stok"
                                }));
                jScrollPane1.setViewportView(TabelManajemenBarang);

                jLabel3.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel3.setText("ID Barang: ");

                IDBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                IDBarang.setEnabled(false);

                jLabel4.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel4.setText("Nama Barang: ");

                NamaBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                NamaBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                NamaBarangActionPerformed(evt);
                        }
                });

                jLabel5.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel5.setText("Kategori Barang: ");

                jLabel6.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel6.setText("Harga Beli Barang: ");

                Barcode.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                Barcode.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                // BarcodeActionPerformed(evt);
                        }
                });

                jLabel7.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel7.setText("Stock Barang: ");

                HargaJualBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                HargaJualBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                HargaJualBarangActionPerformed(evt);
                        }
                });

                ButtonTambahBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                ButtonTambahBarang.setForeground(new java.awt.Color(93, 173, 226));
                ButtonTambahBarang.setText("Tambah Barang");
                ButtonTambahBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                ButtonTambahBarangActionPerformed(evt);
                        }
                });

                ButtonEditBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                ButtonEditBarang.setForeground(new java.awt.Color(93, 173, 226));
                ButtonEditBarang.setText("Edit Barang");
                ButtonEditBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                ButtonEditBarangActionPerformed(evt);
                        }
                });

                ButtonHapusBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                ButtonHapusBarang.setForeground(new java.awt.Color(93, 173, 226));
                ButtonHapusBarang.setText("Hapus Barang");
                ButtonHapusBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                ButtonHapusBarangActionPerformed(evt);
                        }
                });

                jLabel8.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
                jLabel8.setForeground(new java.awt.Color(93, 173, 226));
                jLabel8.setText("Riwayat Aktivitas");

                jLabel9.setFont(new java.awt.Font("Tw Cen MT", 0, 16)); // NOI18N
                jLabel9.setText("Urutkan Berdasarkan: ");

                SortRiwayatAktivitas.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                SortRiwayatAktivitas.setModel(new javax.swing.DefaultComboBoxModel<>(
                                new String[] { "Tanggal&Waktu", "Nama", "Tambah/Edit/Hapus", "Jumlah" }));
                SortRiwayatAktivitas.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                SortRiwayatAktivitasActionPerformed(evt);
                        }
                });

                TabelRiwayatAktivitas.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                TabelRiwayatAktivitas.setModel(new javax.swing.table.DefaultTableModel(
                                new Object[][] {
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null },
                                                { null, null, null, null }
                                },
                                new String[] {
                                                "Tanggal&Waktu", "Nama", "Tambah/Hapus/Edit", "Jumlah"
                                }));
                jScrollPane2.setViewportView(TabelRiwayatAktivitas);

                jLabel10.setFont(new java.awt.Font("Tw Cen MT", 1, 16)); // NOI18N
                jLabel10.setForeground(new java.awt.Color(93, 173, 226));
                jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Laporan_Keuangan.png"))); // NOI18N
                jLabel10.setText("Menu Barang");

                SearchBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 18)); // NOI18N

                jPanel2.setBackground(new java.awt.Color(255, 255, 255));

                javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
                jPanel2.setLayout(jPanel2Layout);
                jPanel2Layout.setHorizontalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 214, Short.MAX_VALUE));
                jPanel2Layout.setVerticalGroup(
                                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGap(0, 0, Short.MAX_VALUE));

                KategoriBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N

                clearForm.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                clearForm.setForeground(new java.awt.Color(93, 173, 226));
                clearForm.setText("Bersihkan Kolom");
                clearForm.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                                clearFormMouseClicked(evt);
                        }
                });

                jLabel11.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel11.setText("Harga Jual Barang:");

                StockBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                StockBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                StockBarangActionPerformed(evt);
                        }
                });

                ButtonScanBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 16)); // NOI18N
                ButtonScanBarang.setText("Barang masuk");
                ButtonScanBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                ButtonScanBarangActionPerformed(evt);
                        }
                });

                jLabel12.setFont(new java.awt.Font("Tw Cen MT", 0, 14)); // NOI18N
                jLabel12.setText("Barcode:");

                HargaBeliBarang.setFont(new java.awt.Font("Tw Cen MT", 0, 13)); // NOI18N
                HargaBeliBarang.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                HargaBeliBarangActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(15, 15, 15)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jScrollPane2,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                871,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel1,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                296,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(375, 375, 375)
                                                                                                .addComponent(jLabel10,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                200,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addComponent(jLabel2)
                                                                                                .addGap(18, 18, 18)
                                                                                                .addComponent(SortItem,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                136,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                .addGap(305, 305, 305)
                                                                                                .addComponent(ButtonScanBarang)
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(SearchBarang,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                131,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                .addComponent(jScrollPane1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                871,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createParallelGroup(
                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createSequentialGroup()
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                .addComponent(jLabel3,
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                .addComponent(jLabel5,
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                .addComponent(jLabel12,
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                .addComponent(jLabel6,
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING))
                                                                                                                                                .addGap(24, 24, 24)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                .addComponent(Barcode,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                125,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                .addComponent(HargaBeliBarang,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                125,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                .addComponent(KategoriBarang,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                125,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                .addComponent(IDBarang,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                125,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                                .addGap(70, 70, 70)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                .addComponent(jLabel4)
                                                                                                                                                                .addComponent(jLabel7)
                                                                                                                                                                .addComponent(jLabel11))
                                                                                                                                                .addGap(44, 44, 44)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                                false)
                                                                                                                                                                .addComponent(NamaBarang)
                                                                                                                                                                .addComponent(StockBarang)
                                                                                                                                                                .addComponent(HargaJualBarang,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                125,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                                .addGap(39, 39, 39))
                                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                                .createParallelGroup(
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                                                                                                                .addComponent(jLabel8,
                                                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                                                                                                                .addComponent(jLabel9)
                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                                                                .addComponent(SortRiwayatAktivitas,
                                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                210,
                                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING,
                                                                                                                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                                                                                                                .addComponent(ButtonTambahBarang,
                                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                                                                116,
                                                                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                                                                .addComponent(ButtonEditBarang)
                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                                                                                                .addComponent(ButtonHapusBarang)
                                                                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                                                                                                                .addComponent(clearForm)))
                                                                                                                                                                .addPreferredGap(
                                                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                                                                                                .addComponent(jPanel2,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(10, 10, 10)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jLabel1)
                                                                                .addComponent(jLabel10))
                                                                .addGap(14, 14, 14)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(ButtonScanBarang,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                0,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(SearchBarang))
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                .addComponent(jLabel2)
                                                                                                .addComponent(SortItem,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(10, 10, 10)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                171,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(10, 10, 10)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                                false)
                                                                                .addComponent(jPanel2,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createSequentialGroup()
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(IDBarang,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                16,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addComponent(KategoriBarang,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                16,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                .addGap(26, 26, 26)
                                                                                                                                                                .addComponent(Barcode,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                                16,
                                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(HargaBeliBarang,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                16,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addComponent(jLabel3)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(jLabel5,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                20,
                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(jLabel12)
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addComponent(jLabel6))
                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                .createSequentialGroup()
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                .addComponent(jLabel4)
                                                                                                                                                .addComponent(NamaBarang,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                16,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                                .createSequentialGroup()
                                                                                                                                                                .addGap(4, 4, 4)
                                                                                                                                                                .addComponent(jLabel7))
                                                                                                                                                .addComponent(StockBarang,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                16,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                                                .addPreferredGap(
                                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                                                                .addGroup(jPanel1Layout
                                                                                                                                                .createParallelGroup(
                                                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                                                .addComponent(jLabel11)
                                                                                                                                                .addComponent(HargaJualBarang,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                                                16,
                                                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(ButtonTambahBarang,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                17,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(ButtonEditBarang,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                18,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(ButtonHapusBarang,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                18,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addComponent(clearForm,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                18,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addPreferredGap(
                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                Short.MAX_VALUE)
                                                                                                .addComponent(jLabel8)
                                                                                                .addGap(10, 10, 10)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createParallelGroup(
                                                                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                                                .addComponent(jLabel9)
                                                                                                                .addComponent(SortRiwayatAktivitas,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jScrollPane2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                147,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(15, 15, 15)));

                add(jPanel1, "card2");
        }// </editor-fold>//GEN-END:initComponents

        private void HargaBeliBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_HargaBeliBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_HargaBeliBarangActionPerformed

        private void ButtonScanBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonScanBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_ButtonScanBarangActionPerformed

        private void clearFormMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_clearFormMouseClicked
                clearForm();
        }// GEN-LAST:event_clearFormMouseClicked

        private void HargaJualBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_HargaJualBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_HargaJualBarangActionPerformed

        private void NamaBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_NamaBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_NamaBarangActionPerformed

        private void KategoriBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_KategoriBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_KategoriBarangActionPerformed

        private void HargaBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_HargaBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_HargaBarangActionPerformed

        private void StockBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_StockBarangActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_StockBarangActionPerformed

        private void SortRiwayatAktivitasActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_SortRiwayatAktivitasActionPerformed
                // TODO add your handling code here:
        }// GEN-LAST:event_SortRiwayatAktivitasActionPerformed

        private void ButtonEditBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonEditBarangActionPerformed
                try {
                        Response<ArrayList<Category>> categoryResponse = categoryRepository
                                        .findByName(KategoriBarang.getSelectedItem().toString(), currentUser.getId());
                        if (!categoryResponse.isSuccess() || categoryResponse.getData().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Kategori tidak ditemukan.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                        }

                        product.setId(Integer.parseInt(IDBarang.getText()));
                        product.setName(NamaBarang.getText());
                        product.setBarcode(Barcode.getText());
                        product.setCategoryId(categoryResponse.getData().get(0).getId());
                        product.setPurchasePrice(Double.parseDouble(HargaBeliBarang.getText()));
                        product.setSellingPrice(Double.parseDouble(HargaJualBarang.getText()));
                        product.setStock(Integer.parseInt(StockBarang.getText()));

                        int confirm = JOptionPane.showConfirmDialog(this,
                                        "Yakin ingin memperbarui produk ini?", "Konfirmasi Perbarui",
                                        JOptionPane.YES_NO_OPTION);

                        if (confirm != JOptionPane.YES_OPTION) {
                                return;
                        }

                        Response<Product> response = productRepository.update(product, currentUser.getId());

                        if (response.isSuccess()) {
                                JOptionPane.showMessageDialog(this, "Produk berhasil diperbarui.");
                                clearForm();
                                filteredAndSortedProducts("", "");
                                Notification notification = new Notification();
                                notification.setType(NotificationType.PRODUCT);
                                notification.setMessage("Produk '" + product.getName() + "' telah diperbarui.");
                                notificationRepository.save(notification, currentUser.getId());
                        } else {
                                JOptionPane.showMessageDialog(this,
                                                "Gagal memperbarui produk: " + response.getMessage(),
                                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka valid.",
                                        "Input Error", JOptionPane.WARNING_MESSAGE);
                }
        }// GEN-LAST:event_ButtonEditBarangActionPerformed

        private void ButtonTambahBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonTambahBarangActionPerformed
                try {
                        Response<ArrayList<Category>> categoryResponse = categoryRepository
                                        .findByName(KategoriBarang.getSelectedItem().toString(), currentUser.getId());
                        if (!categoryResponse.isSuccess() || categoryResponse.getData().isEmpty()) {
                                JOptionPane.showMessageDialog(this, "Kategori tidak ditemukan.",
                                                "Error", JOptionPane.ERROR_MESSAGE);
                                return;
                        }

                        product.setName(NamaBarang.getText());
                        product.setBarcode(Barcode.getText());
                        product.setCategoryId(categoryResponse.getData().get(0).getId());
                        product.setPurchasePrice(Double.parseDouble(HargaBeliBarang.getText()));
                        product.setSellingPrice(Double.parseDouble(HargaJualBarang.getText()));
                        product.setStock(Integer.parseInt(StockBarang.getText()));

                        Response<Product> response = productRepository.save(product, currentUser.getId());

                        if (response.isSuccess()) {
                                JOptionPane.showMessageDialog(this, "Produk berhasil ditambahkan.");
                                clearForm();
                                filteredAndSortedProducts("", "");
                                TransactionManager.getInstance().clearTransaction();
                                Notification notification = new Notification();
                                notification.setType(NotificationType.PRODUCT);
                                notification.setMessage("Produk '" + product.getName() + "' telah ditambahkan.");
                                notificationRepository.save(notification, currentUser.getId());
                        } else {
                                JOptionPane.showMessageDialog(this,
                                                "Gagal menambahkan produk: " + response.getMessage(),
                                                "Error", JOptionPane.ERROR_MESSAGE);
                        }
                } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "Harga dan Stok harus berupa angka valid.",
                                        "Input Error", JOptionPane.WARNING_MESSAGE);
                }

        }// GEN-LAST:event_ButtonTambahBarangActionPerformed

        private void ButtonHapusBarangActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_ButtonHapusBarangActionPerformed
                int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus produk ini?",
                                "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                        try {
                                int id = Integer.parseInt(IDBarang.getText());

                                ProductRepository productRepository = new ProductRepository();

                                Response<Boolean> response = productRepository.deleteById(id, currentUser.getId());
                                if (response.isSuccess()) {
                                        JOptionPane.showMessageDialog(this, "Produk berhasil dihapus.");
                                        clearForm();
                                        filteredAndSortedProducts("", "");
                                        Notification notification = new Notification();
                                        notification.setType(NotificationType.PRODUCT);
                                        notification.setMessage("Produk dengan ID " + id + " telah dihapus.");
                                        notificationRepository.save(notification, currentUser.getId());
                                } else {
                                        JOptionPane.showMessageDialog(this,
                                                        "Gagal menghapus produk: " + response.getMessage(),
                                                        "Error", JOptionPane.ERROR_MESSAGE);
                                }
                        } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(this, "ID tidak valid.",
                                                "Input Error", JOptionPane.WARNING_MESSAGE);
                        }
                }
        }// GEN-LAST:event_ButtonHapusBarangActionPerformed

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTextField Barcode;
        private javax.swing.JButton ButtonEditBarang;
        private javax.swing.JButton ButtonHapusBarang;
        private javax.swing.JButton ButtonScanBarang;
        private javax.swing.JButton ButtonTambahBarang;
        private javax.swing.JTextField HargaBeliBarang;
        private javax.swing.JTextField HargaJualBarang;
        private javax.swing.JTextField IDBarang;
        private javax.swing.JComboBox<Dropdown> KategoriBarang;
        private javax.swing.JTextField NamaBarang;
        private javax.swing.JTextField SearchBarang;
        private javax.swing.JComboBox<String> SortItem;
        private javax.swing.JComboBox<String> SortRiwayatAktivitas;
        private javax.swing.JTextField StockBarang;
        private javax.swing.JTable TabelManajemenBarang;
        private javax.swing.JTable TabelRiwayatAktivitas;
        private javax.swing.JButton clearForm;
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
        private javax.swing.JLabel jLabel9;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JScrollPane jScrollPane2;
        // End of variables declaration//GEN-END:variables
}
