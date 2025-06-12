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

    public Response<ArrayList<FinancialReport>> getDailyFinancialReports() {
        LocalDate today = LocalDate.now();
        var transactionsResponse = stockTransactionRepository.findByTrasactionDate(today, null);

        if (!transactionsResponse.isSuccess() || transactionsResponse.getData() == null) {
            return Response.failure("Failed to retrieve transactions for today: " + transactionsResponse.getMessage());
        }

        double totalTransactions = 0.0;
        double totalIncome = 0.0;
        double totalExpenses = 0.0;

        for (StockTransaction transaction : transactionsResponse.getData()) {
            var productResponse = productRepository.findById(transaction.getProductId(), null);
            if (!productResponse.isSuccess() || productResponse.getData() == null) {
                System.err.println("Warning: Failed to retrieve product for transaction ID " + transaction.getId());
                continue; // skip and continue instead of failing
            }

            var product = productResponse.getData();
            int quantity = transaction.getQuantity();

            if (transaction.getTransactionType() == TransactionType.OUT) {
                double income = quantity * product.getSellingPrice();
                totalIncome += income;
                totalTransactions += income;
            } else if (transaction.getTransactionType() == TransactionType.IN) {
                totalExpenses += quantity * product.getPurchasePrice();
            }
        }

        FinancialReport financialReport = new FinancialReport();
        financialReport.setDate(today);
        financialReport.setTotalTransactions(totalTransactions);
        financialReport.setTotalExpenses(totalExpenses);
        financialReport.setTotalIncome(totalIncome);
        financialReport.setNetProfit(totalIncome - totalExpenses);

        ArrayList<FinancialReport> reports = new ArrayList<>();
        reports.add(financialReport);

        return Response.success("Daily financial reports retrieved successfully", reports);
    }

    public Response<ArrayList<FinancialReport>> getMonthlyFinancialReports() {
        // TODO: Implement similar logic, grouping by LocalDate month
        return Response.failure("Monthly financial reports not implemented yet.");
    }
}
