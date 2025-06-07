/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.services;

import java.util.ArrayList;

import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.repositories.ProductRepository;
import com.mycompany.final_project_pbo.utils.Response;

/**
 *
 * @author c0delb08
 */
public class ProductService {
    ProductRepository productRepository = new ProductRepository();
    LogActivityService logActivityService = new LogActivityService();

    private static final String MODULE_NAME = "ProductService";

    public Response<Product> addStock(String barcode, Integer quantity, Integer userId) {
        if (quantity < 0) {
            logActivityService.logAction(userId, "Attempted to add negative stock for barcode: " + barcode, MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Stock cannot be negative");
        }

        Response<ArrayList<Product>> productResponse = productRepository.findByBarcode(barcode, userId);
        if (!productResponse.isSuccess() || productResponse.getData() == null || productResponse.getData().isEmpty()) {
            logActivityService.logAction(userId, "Product not found for barcode: " + barcode, MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure(
                    "Product not found: " + (productResponse.getMessage() != null ? productResponse.getMessage() : ""));
        }

        Product product = productResponse.getData().get(0);
        product.setStock(product.getStock() + quantity);

        Response<Product> response = productRepository.update(product, userId);
        if (response.isSuccess()) {
            logActivityService.logAction(userId,
                    "Added stock for product with barcode: " + barcode + ", quantity: " + quantity, MODULE_NAME,
                    LogLevel.INFO);
            return Response.success("Stock added successfully", response.getData());
        } else {
            logActivityService.logAction(userId,
                    "Failed to add stock for product with barcode: " + barcode + ", error: " + response.getMessage(),
                    MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Failed to add stock: " + response.getMessage());
        }
    }

    public Response<Product> removeStock(String barcode, Integer quantity, Integer userId) {
        if (quantity < 0) {
            logActivityService.logAction(userId, "Attempted to remove negative stock for barcode: " + barcode, MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure("Stock cannot be negative");
        }

        Response<ArrayList<Product>> productResponse = productRepository.findByBarcode(barcode, userId);
        if (!productResponse.isSuccess() || productResponse.getData() == null || productResponse.getData().isEmpty()) {
            logActivityService.logAction(userId, "Product not found for barcode: " + barcode, MODULE_NAME,
                    LogLevel.ERROR);
            return Response.failure(
                    "Product not found: " + (productResponse.getMessage() != null ? productResponse.getMessage() : ""));
        }

        Product product = productResponse.getData().get(0);
        if (product.getStock() < quantity) {
            logActivityService.logAction(userId,
                    "Insufficient stock for product with barcode: " + barcode + ", current stock: " + product.getStock(),
                    MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Insufficient stock for product with barcode: " + barcode);
        }

        product.setStock(product.getStock() - quantity);

        Response<Product> response = productRepository.update(product, userId);
        if (response.isSuccess()) {
            logActivityService.logAction(userId,
                    "Removed stock for product with barcode: " + barcode + ", quantity: " + quantity, MODULE_NAME,
                    LogLevel.INFO);
            return Response.success("Stock removed successfully", response.getData());
        } else {
            logActivityService.logAction(userId,
                    "Failed to remove stock for product with barcode: " + barcode + ", error: " + response.getMessage(),
                    MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Failed to remove stock: " + response.getMessage());
        }
    }

    public Response<ArrayList<Product>> sortBy(String sortBy, Integer userId) {
        if (sortBy == null || sortBy.isEmpty()) {
            logActivityService.logAction(userId, "Sort by parameter is null or empty", MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Sort by parameter cannot be null or empty");
        }

        return null; // Implement sorting logic here
    }
}
