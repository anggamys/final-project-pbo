/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.Category;
import com.mycompany.final_project_pbo.models.LogLevel;
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
public class CategoryRepository implements CrudRepository<Category> {
    LogActivityService logActivityService = new LogActivityService();
    private static final String MODULE_NAME = "CategoryRepository";

    @Override
    public Response<Category> save(Category entity, Integer userId) {
        String query = "INSERT INTO categories (category_name, description) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                    logActivityService.logAction(userId, "Saved category with ID: " + entity.getId(), MODULE_NAME, LogLevel.INFO);
                    return Response.success("Category saved successfully", entity);
                } else {
                    logActivityService.logAction(userId, "Failed to retrieve generated key for category", MODULE_NAME, LogLevel.ERROR);
                    return Response.failure("Failed to retrieve generated key");
                }
            } else {
                logActivityService.logAction(userId, "Failed to save category", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to save category");
            }
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error occurred while saving category: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error occurred while saving category: " + e.getMessage());
        }
    }

    @Override
    public Response<Category> update(Category entity, Integer userId) {
        String query = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Updated category with ID: " + entity.getId(), MODULE_NAME, LogLevel.INFO);
                return Response.success("Category updated successfully", entity);
            } else {
                logActivityService.logAction(userId, "Failed to update category", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to update category");
            }

        } catch (Exception e) {
            logActivityService.logAction(userId, "Error occurred while updating category: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error occurred while updating category: " + e.getMessage());
        }
    }

    @Override
    public Response<Category> findById(Integer id, Integer userId) {
        String query = "SELECT * FROM categories WHERE category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var category = new Category();
                category.setId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("category_name"));
                category.setDescription(resultSet.getString("description"));

                logActivityService.logAction(userId, "Found category with ID: " + id, MODULE_NAME, LogLevel.INFO);
                return Response.success("Category found", category);
            } else {
                logActivityService.logAction(userId, "Category not found", MODULE_NAME, LogLevel.WARNING);
                return Response.failure("Category not found");
            }

        } catch (Exception e) {
            logActivityService.logAction(userId, "Error occurred while finding category: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error occurred while finding category: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id, Integer userId) {
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Deleted category with ID: " + id, MODULE_NAME, LogLevel.INFO);
                return Response.success("Category deleted successfully", true);
            } else {
                return Response.failure("Failed to delete category");
            }

        } catch (Exception e) {
            logActivityService.logAction(userId, "Error occurred while deleting category: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error occurred while deleting category: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<Category>> findAll(Integer userId) {
        String query = "SELECT * FROM categories";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();

            ArrayList<Category> categories = new ArrayList<>();
            while (resultSet.next()) {
                var category = new Category();
                category.setId(resultSet.getInt("category_id"));
                category.setName(resultSet.getString("category_name"));
                category.setDescription(resultSet.getString("description"));
                categories.add(category);
            }

            logActivityService.logAction(userId, "Found " + categories.size() + " categories", MODULE_NAME, LogLevel.INFO);
            return Response.success("Categories found", categories);
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error occurred while finding categories: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error occurred while finding categories: " + e.getMessage());
        }
    }
}
