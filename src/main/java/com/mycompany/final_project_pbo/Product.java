/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0delb08
 */
public class Product implements BaseEntity, CrudRepository<Product> {
    private int idProduct;
    private String name;
    private String category;
    private Double price;
    private Integer stock;

    private static final Logger LOGGER = Logger.getLogger(Product.class.getName());

    // Constructors
    public Product() {}

    public Product(int idProduct, String name, String category, Double price, Integer stock) {
        this.idProduct = idProduct;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }

    // Encapsulation: Getters & Setters
    public int getIdProduct() { return idProduct; }
    public void setIdProduct(int idProduct) { this.idProduct = idProduct; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    // Polymorphism: Override methods from BaseEntity
    @Override
    public Response<Product> save() {
        String sql = "INSERT INTO products (name, category, price, stock) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    this.idProduct = rs.getInt(1);
                }
            }

            return Response.success("Product added successfully", this);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to save product", e);
            return Response.failure("Error saving product: " + e.getMessage());
        }
    }

    @Override
    public Response<Product> update() {
        String sql = "UPDATE products SET name = ?, category = ?, price = ?, stock = ? WHERE idProduct = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, category);
            ps.setDouble(3, price);
            ps.setInt(4, stock);
            ps.setInt(5, idProduct);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Product updated successfully", this);
            }
            return Response.failure("Product not updated");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update product", e);
            return Response.failure("Error updating product: " + e.getMessage());
        }
    }

    // Static methods for CRUD interface
    @Override
    public Response<Product> findById(int id) {
        String sql = "SELECT * FROM products WHERE idProduct = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Product product = fromResultSet(rs);
                    return Response.success("Product found", product);
                }
            }
            return Response.failure("Product not found");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to find product", e);
            return Response.failure("Error finding product: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(int id) {
        String sql = "DELETE FROM products WHERE idProduct = ?";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                return Response.success("Product deleted", true);
            }
            return Response.failure("Product not deleted");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete product", e);
            return Response.failure("Error deleting product: " + e.getMessage());
        }
    }

    public Response<ArrayList<Product>> findAll() {
        ArrayList<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                products.add(fromResultSet(rs));
            }

            return Response.success("Product list retrieved", products);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to get all products", e);
            return Response.failure("Error getting products: " + e.getMessage());
        }
    }

    private static Product fromResultSet(ResultSet rs) throws SQLException {
        return new Product(
            rs.getInt("idProduct"),
            rs.getString("name"),
            rs.getString("category"),
            rs.getDouble("price"),
            rs.getInt("stock")
        );
    }

    @Override
    public String toString() {
        return String.format("Product{id=%d, name='%s', category='%s', price=%.2f, stock=%d}",
                idProduct, name, category, price, stock);
    }
} 

