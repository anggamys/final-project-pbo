/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import com.github.sarxos.webcam.Webcam;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

import com.mycompany.final_project_pbo.models.LogLevel;

/**
 *
 * @author c0delb08
 */
public class ScannerService {
    LogActivityService logActivityService = new LogActivityService();
    private static final String MODULE_NAME = "ScannerService";

    public String scanBarcode() {
        Webcam webcam = Webcam.getDefault();
        if (webcam == null) {
            logActivityService.logAction(null, "No webcam detected", MODULE_NAME, LogLevel.ERROR);
            System.err.println("No webcam detected");
            return null;
        }
        
        try {
            webcam.open();
            long timeout = System.currentTimeMillis() + 5000;

            while (System.currentTimeMillis() < timeout) {
                LuminanceSource luminanceSource = new BufferedImageLuminanceSource(webcam.getImage());
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(luminanceSource));

                try {
                    com.google.zxing.Result result = new com.google.zxing.MultiFormatReader().decode(bitmap);
                    if (result != null) {
                        String barcode = result.getText();
                        logActivityService.logAction(null, "Scanned barcode: " + barcode, MODULE_NAME, LogLevel.INFO);
                        return barcode;
                    }
                } catch (Exception e) {
                    logActivityService.logAction(null, "Failed to decode barcode: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
                }
            }
        } catch (Exception e) {
            logActivityService.logAction(null, "Error occurred while scanning: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
        } finally {
            if (webcam != null && webcam.isOpen()) {
                webcam.close();
            }
        }

        logActivityService.logAction(null, "Barcode scanning timed out", MODULE_NAME, LogLevel.WARNING);
        return null;
    }
}
