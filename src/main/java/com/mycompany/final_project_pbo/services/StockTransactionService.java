/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import java.util.ArrayList;

import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.models.StockTransaction;
import com.mycompany.final_project_pbo.models.TransactionType;
import com.mycompany.final_project_pbo.repositories.ProductRepository;
import com.mycompany.final_project_pbo.repositories.StockTransactionRepository;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author c0delb08
 */
public class StockTransactionService {
    private final ProductRepository productRepository = new ProductRepository();
    private final StockTransactionRepository stockTransactionRepository = new StockTransactionRepository();
    private final LogActivityService logActivityService = new LogActivityService();

    private static final String MODULE_NAME = "StockTransactionService";

    public Response<Boolean> incrementStock(String barcode, int quantity, Integer userId) {
        if (barcode == null || barcode.isBlank())
            return Response.failure("Barcode is required");
        if (quantity <= 0)
            return Response.failure("Quantity must be greater than zero");

        Response<ArrayList<Product>> productResponse = productRepository.findByBarcode(barcode, userId);
        if (!productResponse.isSuccess() || productResponse.getData().isEmpty())
            return Response.failure("Product not found with barcode: " + barcode);

        Product product = productResponse.getData().get(0);
        int newStock = product.getStock() + quantity;

        product.setStock(newStock);
        Response<Product> updateResponse = productRepository.update(product, userId);
        if (!updateResponse.isSuccess())
            return Response.failure("Failed to update product stock for ID: " + product.getId());

        Response<StockTransaction> transactionResponse = stockTransactionRepository.save(
                new StockTransaction(product.getId(), quantity, TransactionType.IN, "Stock incremented", userId),
                userId);
        if (!transactionResponse.isSuccess())
            return Response.failure("Failed to log stock increment transaction for product ID: " + product.getId());

        logActivityService.logAction(userId,
                String.format("Incremented stock by %d units for product ID %d. New stock: %d",
                        quantity, product.getId(), newStock),
                MODULE_NAME, LogLevel.INFO);

        return Response.success("Stock incremented successfully", true);
    }

    public Response<Boolean> decrementStock(String barcode, int quantity, Integer userId) {
        if (barcode == null || barcode.isBlank())
            return Response.failure("Barcode is required");
        if (quantity <= 0)
            return Response.failure("Quantity must be greater than zero");

        Response<ArrayList<Product>> productResponse = productRepository.findByBarcode(barcode, userId);
        if (!productResponse.isSuccess() || productResponse.getData().isEmpty())
            return Response.failure("Product not found with barcode: " + barcode);

        Product product = productResponse.getData().get(0);
        int newStock = product.getStock() - quantity;

        if (newStock < 0)
            return Response.failure("Insufficient stock for product ID: " + product.getId());

        product.setStock(newStock);
        Response<Product> updateResponse = productRepository.update(product, userId);
        if (!updateResponse.isSuccess())
            return Response.failure("Failed to update product stock for ID: " + product.getId());

        Response<StockTransaction> transactionResponse = stockTransactionRepository.save(
                new StockTransaction(product.getId(), quantity, TransactionType.OUT, "Stock decremented", userId),
                userId);
        if (!transactionResponse.isSuccess())
            return Response.failure("Failed to log stock decrement transaction for product ID: " + product.getId());

        logActivityService.logAction(userId,
                String.format("Decremented stock by %d units for product ID %d. New stock: %d",
                        quantity, product.getId(), newStock),
                MODULE_NAME, LogLevel.INFO);

        return Response.success("Stock decremented successfully", true);
    }

    public Response<StockTransaction> createTransaction(StockTransaction transaction, String barcode, Integer userId) {
        if (transaction == null)
            return Response.failure("Transaction cannot be null");
        if (barcode == null || barcode.isBlank())
            return Response.failure("Barcode is required");
        if (transaction.getQuantity() == null || transaction.getQuantity() <= 0)
            return Response.failure("Quantity must be greater than zero");
        if (transaction.getTransactionType() == null)
            return Response.failure("Transaction type is required");

        Response<ArrayList<Product>> productResponse = productRepository.findByBarcode(barcode, userId);
        if (!productResponse.isSuccess() || productResponse.getData().isEmpty())
            return Response.failure("Product not found with barcode: " + barcode);

        Product product = productResponse.getData().get(0);
        int newStock = calculateNewStock(product.getStock(), transaction.getQuantity(),
                transaction.getTransactionType());

        if (newStock < 0)
            return Response.failure("Insufficient stock for product ID: " + product.getId());

        product.setStock(newStock);
        transaction.setProductId(product.getId());
        Response<Product> updateResponse = productRepository.update(product, userId);
        if (!updateResponse.isSuccess())
            return Response.failure("Failed to update product stock for ID: " + product.getId());

        logActivityService.logAction(userId,
                String.format("Stock %s: %d units for product ID %d. New stock: %d",
                        transaction.getTransactionType(), transaction.getQuantity(), product.getId(), newStock),
                MODULE_NAME, LogLevel.INFO);

        return stockTransactionRepository.save(transaction, userId);
    }

    private int calculateNewStock(int currentStock, int quantity, TransactionType type) {
        return switch (type) {
            case IN -> currentStock + quantity;
            case OUT -> currentStock - quantity;
            default -> currentStock;
        };
    }

}
