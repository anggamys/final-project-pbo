/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.services.LogActivityService;
import com.mycompany.final_project_pbo.utils.CrudRepository;
import com.mycompany.final_project_pbo.utils.DatabaseUtil;
import com.mycompany.final_project_pbo.utils.Response;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class ProductRepository implements CrudRepository<Product> {
    LogActivityService logActivityService = new LogActivityService();
    private static final String MODULE_NAME = "ProductRepository";

    @Override
    public Response<Product> save(Product entity, Integer userId) {
        String query = "INSERT INTO products (name, barcode, category_id, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getBarcode());
            preparedStatement.setInt(3, entity.getCategoryId());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getStock());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));

                    logActivityService.logAction(userId, "Saved product with ID: " + entity.getId(), MODULE_NAME, LogLevel.INFO);
                    return Response.success("Product saved successfully", entity);
                } else {
                    logActivityService.logAction(userId, "Failed to retrieve generated ID for product", MODULE_NAME, LogLevel.ERROR);
                    return Response.failure("Failed to retrieve generated key");
                }
            } else {
                logActivityService.logAction(userId, "Failed to save product", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to save product");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error saving product: " + e.getMessage());
        }
    }

    @Override
    public Response<Product> update(Product entity, Integer userId) {
        String query = "UPDATE products SET name = ?, barcode = ?, category_id = ?, price = ?, stock = ? WHERE product_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getBarcode());
            preparedStatement.setInt(3, entity.getCategoryId());
            preparedStatement.setDouble(4, entity.getPrice());
            preparedStatement.setInt(5, entity.getStock());
            preparedStatement.setInt(6, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Updated product with ID: " + entity.getId(), MODULE_NAME, LogLevel.INFO);
                return Response.success("Product updated successfully", entity);
            } else {
                logActivityService.logAction(userId, "Failed to update product", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to update product");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error updating product: " + e.getMessage());
        }
    }

    @Override
    public Response<Product> findById(Integer id, Integer userId) {
        String query = "SELECT * FROM products WHERE product_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setCategoryId(resultSet.getInt("category_id"));
                product.setPrice(resultSet.getDouble("price"));
                product.setStock(resultSet.getInt("stock"));

                logActivityService.logAction(userId, "Found product with ID: " + product.getId(), MODULE_NAME, LogLevel.INFO);
                return Response.success("Product found", product);
            } else {
                logActivityService.logAction(userId, "Product not found", MODULE_NAME, LogLevel.WARNING);
                return Response.failure("Product not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error finding product: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id, Integer userId) {
        String query = "DELETE FROM products WHERE product_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Deleted product with ID: " + id, MODULE_NAME, LogLevel.INFO);
                return Response.success("Product deleted successfully", true);
            } else {
                return Response.failure("Failed to delete product");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error deleting product: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<Product>> findAll(Integer userId) {
        String query = "SELECT * FROM products";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setCategoryId(resultSet.getInt("category_id"));
                product.setPrice(resultSet.getDouble("price"));
                product.setStock(resultSet.getInt("stock"));
                products.add(product);
            }

            logActivityService.logAction(userId, "Retrieved all products", MODULE_NAME, LogLevel.INFO);
            return Response.success("Products found", products);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error finding products: " + e.getMessage());
        }
    }

    public Response<ArrayList<Product>> findByBarcode(String barcode, Integer userId) {
        String query = "SELECT * FROM products WHERE barcode = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, barcode);
            var resultSet = preparedStatement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setCategoryId(resultSet.getInt("category_id"));
                product.setPrice(resultSet.getDouble("price"));
                product.setStock(resultSet.getInt("stock"));
                products.add(product);
            }

            logActivityService.logAction(userId, "Found products with barcode: " + barcode, MODULE_NAME, LogLevel.INFO);
            return Response.success("Products found", products);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error finding products by barcode: " + e.getMessage());
        }
    }

    public Response<ArrayList<Product>> searchByName(String name, Integer userId) {
        String query = "SELECT * FROM products WHERE name LIKE ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, "%" + name + "%");
            var resultSet = preparedStatement.executeQuery();

            ArrayList<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = new Product();
                product.setId(resultSet.getInt("product_id"));
                product.setName(resultSet.getString("name"));
                product.setBarcode(resultSet.getString("barcode"));
                product.setCategoryId(resultSet.getInt("category_id"));
                product.setPrice(resultSet.getDouble("price"));
                product.setStock(resultSet.getInt("stock"));
                products.add(product);
            }

            logActivityService.logAction(userId, "Searched products by name: " + name, MODULE_NAME, LogLevel.INFO);
            return Response.success("Products found", products);
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error searching products by name: " + e.getMessage());
        }
    }
}
