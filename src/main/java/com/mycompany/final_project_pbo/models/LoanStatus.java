/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.models;

/**
 *
 * @author c0delb08
 */
public enum LoanStatus {
    BELUM_LUNAS("Belum Lunas"),
    LUNAS("Lunas"),
    TERLAMBAT("Terlambat"),
    DIBATALKAN("Dibatalkan");

    private final String label;

    LoanStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    public String toString() {
        return label;
    }
}
