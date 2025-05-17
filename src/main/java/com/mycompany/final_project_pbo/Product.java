/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0delb08
 */
public abstract class Product {
    private int idProduct;
    private String name;
    private String category;
    private Double price;
    private Integer stock;
    protected final Logger LOGGER;

    public Product(int idProduct, String name, String category, Double price, Integer stock) {
        this.idProduct = idProduct;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
        this.LOGGER = LoggerUtil.getLogger(getClass());
    }

    public int getIdProduct() {
        return idProduct;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "Product{" +
                "idProduct=" + idProduct +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                '}';
    }

    protected boolean executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseUtil.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database update error: " + e.getMessage(), e);
            return false;
        }
    }

        protected ResultSet executeQuery(String sql, Object... params) throws SQLException {
        Connection conn = DatabaseUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        for (int i = 0; i < params.length; i++) {
            ps.setObject(i + 1, params[i]);
        }
        return ps.executeQuery();
    }

    // Abstract methods to be implemented
    protected abstract Response<Product> addProduct(String newName, String newCategory, Double newPrice, Integer newStock);
    protected abstract Response<Product> getProduct(Integer targetIdProduct);
    protected abstract Response<Product> updateProduct(Integer targetIdProduct, String newName, String newCategory, Double newPrice, Integer newStock);
    protected abstract Response<Boolean> deleteProduct(Integer targetIdProduct);
}
