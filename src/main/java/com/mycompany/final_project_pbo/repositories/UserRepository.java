/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.models.User;
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
public class UserRepository implements CrudRepository<User> {
    LogActivityService logActivityService = new LogActivityService();
    private static final String MODULE_NAME = "UserRepository";

    @Override
    public Response<User> save(User entity, Integer userId) {
        String query = "INSERT INTO users (username, password_hash, email, is_owner) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setBoolean(4, entity.getIsOwner());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                var generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getInt(1));
                    logActivityService.logAction(userId, "Saved user with ID: " + entity.getId(), MODULE_NAME, LogLevel.INFO);
                    return Response.success("User saved successfully", entity);
                } else {
                    logActivityService.logAction(userId, "Failed to retrieve generated key for user", MODULE_NAME, LogLevel.ERROR);
                    return Response.failure("Failed to retrieve generated key");
                }
            } else {
                logActivityService.logAction(userId, "Failed to save user", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to save user");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.failure("Error saving user: " + e.getMessage());
        }
    }

    @Override
    public Response<User> update(User entity, Integer userId) {
        String query = "UPDATE users SET username = ?, password_hash = ?, email = ?, is_owner = ? WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, entity.getUsername());
            preparedStatement.setString(2, entity.getPassword());
            preparedStatement.setString(3, entity.getEmail());
            preparedStatement.setBoolean(4, entity.getIsOwner());
            preparedStatement.setInt(5, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Updated user with ID: " + entity.getId(), MODULE_NAME, LogLevel.INFO);
                return Response.success("User updated successfully", entity);
            } else {
                logActivityService.logAction(userId, "Failed to update user", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to update user");
            }
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error occurred while updating user: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error updating user: " + e.getMessage());
        }
    }

    @Override
    public Response<User> findById(Integer id, Integer userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password_hash"));
                user.setEmail(resultSet.getString("email"));
                user.setIsOwner(resultSet.getBoolean("is_owner"));

                logActivityService.logAction(userId, "Found user with ID: " + user.getId(), MODULE_NAME, LogLevel.INFO);
                return Response.success("User found", user);
            } else {
                return Response.failure("User not found");
            }
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error finding user: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error finding user: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id, Integer userId) {
        String query = "DELETE FROM users WHERE user_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Deleted user with ID: " + id, MODULE_NAME, LogLevel.INFO);
                return Response.success("User deleted successfully", true);
            } else {
                logActivityService.logAction(userId, "Failed to delete user", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to delete user");
            }
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error deleting user: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error deleting user: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<User>> findAll(Integer userId) {
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();

            ArrayList<User> users = new ArrayList<>();
            while (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password_hash"));
                user.setEmail(resultSet.getString("email"));
                user.setIsOwner(resultSet.getBoolean("is_owner"));
                users.add(user);
            }

            logActivityService.logAction(userId, "Found " + users.size() + " users", MODULE_NAME, LogLevel.INFO);
            return Response.success("Users found", users);
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error finding users: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error finding users: " + e.getMessage());
        }
    }

    public Response<User> findByUsername(String username, Integer userId) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, username);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                var user = new User();
                user.setId(resultSet.getInt("user_id"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password_hash"));
                user.setEmail(resultSet.getString("email"));
                user.setIsOwner(resultSet.getBoolean("is_owner"));
                logActivityService.logAction(userId, "Found user with username: " + username, MODULE_NAME, LogLevel.INFO);
                return Response.success("User found", user);
            } else {
                logActivityService.logAction(userId, "User not found with username: " + username, MODULE_NAME, LogLevel.WARNING);
                return Response.failure("User not found");
            }
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error finding user: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error finding user: " + e.getMessage());
        }
    }

    public Response<Boolean> deleteAll(Integer userId) {
        String query = "DELETE FROM users";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                logActivityService.logAction(userId, "Deleted all users", MODULE_NAME, LogLevel.INFO);
                return Response.success("All users deleted successfully", true);
            } else {
                logActivityService.logAction(userId, "Failed to delete all users", MODULE_NAME, LogLevel.ERROR);
                return Response.failure("Failed to delete all users");
            }
        } catch (Exception e) {
            logActivityService.logAction(userId, "Error deleting all users: " + e.getMessage(), MODULE_NAME, LogLevel.ERROR);
            return Response.failure("Error deleting all users: " + e.getMessage());
        }
    }
}
