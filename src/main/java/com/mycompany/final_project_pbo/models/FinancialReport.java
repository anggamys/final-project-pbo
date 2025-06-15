/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.models;

import java.time.LocalDate;

/**
 *
 * @author c0delb08
 */
public class FinancialReport {
    private LocalDate date;
    private Double totalTransactions;
    private Double totalIncome;
    private Double totalExpenses;
    private Double netProfit;

    public FinancialReport() {
    }

    public FinancialReport(LocalDate date, Double totalTransactions, Double totalIncome, Double totalExpenses,
            Double netProfit) {
        this.date = date;
        this.totalTransactions = totalTransactions;
        this.totalIncome = totalIncome;
        this.totalExpenses = totalExpenses;
        this.netProfit = netProfit;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Double getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Double totalTransactions) {
        this.totalTransactions = totalTransactions != null ? totalTransactions : 0.0;
    }

    public Double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(Double totalIncome) {
        this.totalIncome = totalIncome != null ? totalIncome : 0.0;
    }

    public Double getTotalExpenses() {
        return totalExpenses;
    }

    public void setTotalExpenses(Double totalExpenses) {
        this.totalExpenses = totalExpenses != null ? totalExpenses : 0.0;
    }

    public Double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(Double netProfit) {
        this.netProfit = netProfit != null ? netProfit : 0.0;
    }

    @Override
    public String toString() {
        return "FinancialReport{" +
                "date=" + date +
                ", totalTransactions=" + totalTransactions +
                ", totalIncome=" + totalIncome +
                ", totalExpenses=" + totalExpenses +
                ", netProfit=" + netProfit +
                '}';
    }
}
