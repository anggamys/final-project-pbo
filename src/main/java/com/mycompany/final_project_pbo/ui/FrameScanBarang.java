package com.mycompany.final_project_pbo.ui;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.*;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
// import java.lang.reflect.Type; // Remove this import to avoid ambiguity
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class FrameScanBarang extends javax.swing.JFrame implements Runnable, ThreadFactory {

    private WebcamPanel panel = null;
    private Webcam webcam = null;
    private ExecutorService executor = Executors.newSingleThreadExecutor(this);
    private volatile boolean running = true;

    private Map<String, String> resultMap = new HashMap<>();

    public FrameScanBarang() {
        initComponents();
        initWebcam();
    }

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
            new Object[][] {
                {null, null, null, null}
            },
            new String[] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        Kategori.setFont(new java.awt.Font("Tw Cen MT", 1, 24));
        Kategori.setForeground(new java.awt.Color(93, 173, 226));
        Kategori.setText("Scan Barang");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(39, 39, 39)
                    .addComponent(Kategori))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(210, 210, 210)
                    .addComponent(webCamPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(298, 298, 298)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 475, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(20)
                    .addComponent(Kategori)
                    .addGap(31)
                    .addComponent(webCamPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(148, Short.MAX_VALUE))
        );

        getContentPane().add(jPanel1);
        pack();
    }

    private void initWebcam() {
        webcam = Webcam.getDefault();
        if (webcam != null) {
            Dimension[] resolutions = webcam.getViewSizes();
            Dimension maxResolution = resolutions[resolutions.length - 1];

            webcam.setViewSize(maxResolution);
            webcam.open();

            panel = new WebcamPanel(webcam);
            panel.setPreferredSize(maxResolution);
            panel.setFPSDisplayed(true);

            webCamPanel.add(panel);
            webCamPanel.revalidate();

            executor.execute(this);
        } else {
            JOptionPane.showMessageDialog(this, "Webcam not detected.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000);

                if (webcam != null && webcam.isOpen()) {
                    BufferedImage image = webcam.getImage();
                    if (image == null) continue;

                    LuminanceSource source = new BufferedImageLuminanceSource(image);
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                    try {
                        Result result = new MultiFormatReader().decode(bitmap);
                        if (result != null) {
                            Gson gson = new Gson();
                            java.lang.reflect.Type type = new TypeToken<Map<String, String>>(){}.getType();
                            resultMap = gson.fromJson(result.getText(), type);

                            String finalPath = "images/" + resultMap.get("email") + ".jpg";
                            showCircularImageDialog(finalPath);
                        }
                    } catch (NotFoundException ignore) {
                    }
                }

            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                break;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showCircularImageDialog(String imagePath) {
        try {
            BufferedImage img = ImageIO.read(new File(imagePath));
            if (img != null) {
                ImageIcon icon = new ImageIcon(img);
                JOptionPane.showMessageDialog(this, "", "Scanned Image", JOptionPane.INFORMATION_MESSAGE, icon);
            } else {
                JOptionPane.showMessageDialog(this, "Image not found: " + imagePath, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load image: " + imagePath, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    @Override
    public Thread newThread(Runnable r) {
        return new Thread(r);
    }

    // Variables declaration
    private javax.swing.JLabel Kategori;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel webCamPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            new FrameScanBarang().setVisible(true);
        });
    }
}
