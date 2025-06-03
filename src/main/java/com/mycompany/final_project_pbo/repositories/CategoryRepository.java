/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.Category;
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

    @Override
    public Response<Category> save(Category entity) {
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
                    return Response.success("Category saved successfully", entity);
                } else {
                    return Response.failure("Failed to retrieve generated key");
                }
            } else {
                return Response.failure("Failed to save category");
            }

        } catch (Exception e) {
            return Response.failure("Error occurred while saving category: " + e.getMessage());
        }
    }

    @Override
    public Response<Category> update(Category entity) {

        String query = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, entity.getName());
            preparedStatement.setString(2, entity.getDescription());
            preparedStatement.setInt(3, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Category updated successfully", entity);
            } else {
                return Response.failure("Failed to update category");
            }

        } catch (Exception e) {
            return Response.failure("Error occurred while updating category: " + e.getMessage());
        }
    }

    @Override
    public Response<Category> findById(Integer id) {
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
                return Response.success("Category found", category);
            } else {
                return Response.failure("Category not found");
            }

        } catch (Exception e) {
            return Response.failure("Error occurred while finding category: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id) {
        String query = "DELETE FROM categories WHERE category_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Category deleted successfully", true);
            } else {
                return Response.failure("Failed to delete category");
            }

        } catch (Exception e) {
            return Response.failure("Error occurred while deleting category: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<Category>> findAll() {
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
            return Response.success("Categories found", categories);

        } catch (Exception e) {
            return Response.failure("Error occurred while finding categories: " + e.getMessage());
        }
    }
}
