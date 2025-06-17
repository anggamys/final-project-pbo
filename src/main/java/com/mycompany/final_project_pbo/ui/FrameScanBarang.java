
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import java.awt.Dimension;  // Pastikan menggunakan java.awt.Dimension
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FrameScanBarang extends javax.swing.JFrame implements Runnable {
    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean running = true; // Untuk mengatur jalannya thread
    private Map<String, String> resultMap = new HashMap<>(); // Untuk menyimpan hasil pemindaian barcode

    // Konstruktor
    public FrameScanBarang() {
        initComponents();
        initWebcam();
    }

    // Inisialisasi komponen GUI
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        webCamPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        Kategori = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1080, 720));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout webCamPanelLayout = new javax.swing.GroupLayout(webCamPanel);
        webCamPanel.setLayout(webCamPanelLayout);
        webCamPanelLayout.setHorizontalGroup(
            webCamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        webCamPanelLayout.setVerticalGroup(
            webCamPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 376, Short.MAX_VALUE)
        );

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        Kategori.setBackground(new java.awt.Color(255, 255, 255));
        Kategori.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); 
        Kategori.setForeground(new java.awt.Color(93, 173, 226));
        Kategori.setText("Scan Barang");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addComponent(Kategori))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(210, 210, 210)
                        .addComponent(webCamPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(298, 298, 298)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(453, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(Kategori)
                .addGap(31, 31, 31)
                .addComponent(webCamPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(148, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_START);

        pack();
    }

    // Menjalankan aplikasi
    public static void main(String args[]) {
    java.awt.EventQueue.invokeLater(new Runnable() {
        @Override
        public void run() {
            new FrameScanBarang().setVisible(true);  // Menampilkan FrameScanBarang
        }
    });
}


    // Inisialisasi webcam
    private void initWebcam() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            Dimension[] resolutions = webcam.getViewSizes();
            Dimension maxResolution = resolutions[resolutions.length - 1];

            if (webcam.isOpen()) {
                webcam.close();
            }

            webcam.setViewSize(maxResolution); // Set resolusi webcam
            webcam.open();

            panel = new WebcamPanel(webcam);
            panel.setPreferredSize(maxResolution);
            panel.setFPSDisplayed(true); // Menampilkan FPS

            webCamPanel.setLayout(new java.awt.BorderLayout());
            webCamPanel.add(panel, java.awt.BorderLayout.CENTER);

            executor.execute(this); // Menjalankan thread untuk pemrosesan
        } else {
            System.out.println("Webcam tidak ditemukan.");
        }
    }

    // Menjalankan thread untuk pemrosesan barcode
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // Sleep selama 1 detik

                if (webcam.isOpen()) {
                    BufferedImage image = webcam.getImage();
                    if (image != null) {
                        LuminanceSource source = new BufferedImageLuminanceSource(image);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Result result = null;
                        try {
                            result = new MultiFormatReader().decode(bitmap);
                        } catch (NotFoundException ex) {
                            // Tidak ada barcode yang ditemukan
                        }

                        if (result != null) {
                            String jsonString = result.getText();
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<Map<String, String>>() {}.getType();
                            resultMap = gson.fromJson(jsonString, type);

                            String finalPath = "images\\" + resultMap.get("email") + ".jpg";
                            CircularImageFrame(finalPath); // Tampilkan gambar jika ditemukan
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Untuk membersihkan webcam saat aplikasi ditutup
    @Override
    public void dispose() {
        super.dispose();
        if (webcam != null && webcam.isOpen()) {
            webcam.close();
        }
    }

    // Menampilkan gambar dengan bingkai melingkar
    private void CircularImageFrame(String imagePath) {
        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            ImageIcon icon = new ImageIcon(img);
            JLabel label = new JLabel(icon);
            
            // Menambahkan label gambar ke panel atau tempat lain dalam GUI
            JOptionPane.showMessageDialog(this, label, "Image", JOptionPane.PLAIN_MESSAGE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Variabel GUI
    private javax.swing.JLabel Kategori;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel webCamPanel;
}