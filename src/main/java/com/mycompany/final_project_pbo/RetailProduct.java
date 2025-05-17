/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author c0delb08
 */
public class RetailProduct extends Product {

    public RetailProduct() {
        super(0, "", "", 0.0, 0);
    }

    @Override
    protected Response<Product> addProduct(String newName, String newCategory, Double newPrice, Integer newStock) {
        try {
            String sql = "INSERT INTO products (name, category, price, stock) VALUES (?, ?, ?, ?)";
            boolean success = executeUpdate(sql, newName, newCategory, newPrice, newStock);

            if (success) {
                String fetchSql = "SELECT * FROM products WHERE name = ?";
                try (ResultSet rs = executeQuery(fetchSql, newName)) {
                    if (rs != null && rs.next()) {
                        Product product = new Grosir();

                        product.setIdProduct(rs.getInt("idProduct"));
                        product.setName(rs.getString("name"));
                        product.setCategory(rs.getString("category"));
                        product.setPrice(rs.getDouble("price"));
                        product.setStock(rs.getInt("stock"));

                        LOGGER.log(Level.INFO, "Product added: {0}", product);
                        return Response.success("Product added successfully", product);
                    }
                }
            }
            return Response.failure("Failed to retrieve added product.");
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to add product: " + newName, e);
            return Response.failure("Add product error: " + e.getMessage());
        }
    }

    @Override
    protected Response<Product> getProduct(Integer targetIdProduct) {
        try {
            String sql = "SELECT * FROM products WHERE idProduct = ?";
            try (ResultSet rs = executeQuery(sql, targetIdProduct)) {
                if (rs != null && rs.next()) {
                    Product product = new Grosir();

                    product.setIdProduct(rs.getInt("idProduct"));
                    product.setName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));

                    LOGGER.log(Level.INFO, "Product found: {0}", product);
                    return Response.success("Product found", product);
                } else {
                    return Response.failure("Product not found");
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get product: " + targetIdProduct, e);
            return Response.failure("Get product error: " + e.getMessage());
        }
    }

    @Override
    protected Response<Product> updateProduct(Integer targetIdProduct, String newName, String newCategory,
            Double newPrice, Integer newStock) {
        try {
            String sql = "UPDATE products SET name = ?, category = ?, price = ?, stock = ? WHERE idProduct = ?";
            boolean success = executeUpdate(sql, newName, newCategory, newPrice, newStock, targetIdProduct);

            if (success) {
                return getProduct(targetIdProduct);
            } else {
                return Response.failure("Failed to update product");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update product: " + targetIdProduct, e);
            return Response.failure("Update product error: " + e.getMessage());
        }
    }

    @Override
    protected Response<Boolean> deleteProduct(Integer targetIdProduct) {
        try {
            String sql = "DELETE FROM products WHERE idProduct = ?";
            boolean success = executeUpdate(sql, targetIdProduct);
            if (success) {
                return Response.success("Product deleted successfully", true);
            } else {
                return Response.failure("Failed to delete product");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product: " + targetIdProduct, e);
            return Response.failure("Delete product error: " + e.getMessage());
        }
    }

    @Override
    protected Response<ArrayList<Product>> getAllProducts() {
        try {
            String sql = "SELECT * FROM products";
            ArrayList<Product> products = new ArrayList<>();
            try (ResultSet rs = executeQuery(sql)) {
                while (rs != null && rs.next()) {
                    Product product = new Grosir();

                    product.setIdProduct(rs.getInt("idProduct"));
                    product.setName(rs.getString("name"));
                    product.setCategory(rs.getString("category"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));

                    products.add(product);
                }
            }
            return Response.success("Products retrieved successfully", products);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to get all products", e);
            return Response.failure("Get all products error: " + e.getMessage());
        }
    }
}
