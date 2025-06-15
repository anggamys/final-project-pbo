/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import java.time.LocalDate;
import java.util.ArrayList;

import com.mycompany.final_project_pbo.models.FinancialReport;
import com.mycompany.final_project_pbo.models.StockTransaction;
import com.mycompany.final_project_pbo.models.TransactionType;
import com.mycompany.final_project_pbo.repositories.ProductRepository;
import com.mycompany.final_project_pbo.repositories.StockTransactionRepository;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author c0delb08NewClass
 */
public class FinanceService {

    private final ProductRepository productRepository = new ProductRepository();
    private final StockTransactionRepository stockTransactionRepository = new StockTransactionRepository();

    public Response<ArrayList<FinancialReport>> getAllFinancialReports() {
        var transactionsResponse = stockTransactionRepository.findAll(null);
        if (!transactionsResponse.isSuccess() || transactionsResponse.getData() == null) {
            return Response.failure("Failed to retrieve transactions: " + transactionsResponse.getMessage());
        }

        ArrayList<FinancialReport> reports = new ArrayList<>();

        for (StockTransaction transaction : transactionsResponse.getData()) {
            var productResponse = productRepository.findById(transaction.getProductId(), null);
            if (!productResponse.isSuccess() || productResponse.getData() == null) {
                System.err.println("Warning: Failed to retrieve product for transaction ID " + transaction.getId());
                continue;
            }

            var product = productResponse.getData();
            int quantity = transaction.getQuantity();
            if (transaction.getCreatedAt() == null) continue;

            LocalDate transactionDate = transaction.getCreatedAt().toLocalDate();

            FinancialReport report = reports.stream()
                    .filter(r -> r.getDate().isEqual(transactionDate))
                    .findFirst()
                    .orElseGet(() -> {
                        FinancialReport newReport = new FinancialReport();
                        newReport.setDate(transactionDate);
                        newReport.setTotalIncome(0.0);
                        newReport.setTotalExpenses(0.0);
                        newReport.setNetProfit(0.0);
                        reports.add(newReport);
                        return newReport;
                    });

            switch (transaction.getTransactionType()) {
                case OUT -> {
                    double income = quantity * product.getSellingPrice();
                    report.setTotalIncome(report.getTotalIncome() + income);
                }
                case IN -> {
                    double expense = quantity * product.getPurchasePrice();
                    report.setTotalExpenses(report.getTotalExpenses() + expense);
                }
            }
        }

        // Finalize net profit calculation
        for (FinancialReport report : reports) {
            double income = report.getTotalIncome() != null ? report.getTotalIncome() : 0.0;
            double expense = report.getTotalExpenses() != null ? report.getTotalExpenses() : 0.0;
            report.setNetProfit(income - expense);
            report.setTotalTransactions(income + expense);
        }

        return Response.success("All financial reports retrieved successfully", reports);
    }

    public Response<ArrayList<FinancialReport>> getDailyFinancialReports(LocalDate date) {
        var transactionsResponse = stockTransactionRepository.findByTrasactionDate(date, null);

        if (!transactionsResponse.isSuccess() || transactionsResponse.getData() == null) {
            return Response.failure("Failed to retrieve transactions for today: " + transactionsResponse.getMessage());
        }

        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        for (StockTransaction transaction : transactionsResponse.getData()) {
            var productResponse = productRepository.findById(transaction.getProductId(), null);
            if (!productResponse.isSuccess() || productResponse.getData() == null) {
                System.err.println("Warning: Failed to retrieve product for transaction ID " + transaction.getId());
                continue;
            }

            var product = productResponse.getData();
            int quantity = transaction.getQuantity();

            switch (transaction.getTransactionType()) {
                case OUT -> totalIncome += quantity * product.getSellingPrice();
                case IN -> totalExpenses += quantity * product.getPurchasePrice();
            }
        }

        FinancialReport report = new FinancialReport();
        report.setDate(date);
        report.setTotalIncome(totalIncome);
        report.setTotalExpenses(totalExpenses);
        report.setNetProfit(totalIncome - totalExpenses);
        report.setTotalTransactions(totalIncome + totalExpenses);

        ArrayList<FinancialReport> reports = new ArrayList<>();
        reports.add(report);

        return Response.success("Daily financial report retrieved successfully", reports);
    }

    public Response<ArrayList<FinancialReport>> getMonthlyFinancialReports() {
        return Response.failure("Monthly financial reports not implemented yet.");
    }
}
