/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.sql.ResultSet;
import java.util.logging.Level;

/**
 *
 * @author c0delb08
 */
public class Staff extends User {

    public Staff(String username, String password) {
        super(username, password, "STAFF");
    }

    @Override
    protected void accessDashboard() {
        LOGGER.log(Level.INFO, "Staff {0} accessed staff dashboard.", username);
    }

    @Override
    protected Response<String> addUser(String newUsername, String newPassword) {
        try {
            String hashedPassword = hashPassword(newPassword);
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            executeUpdate(sql, newUsername, hashedPassword, getRole());

            LOGGER.log(Level.INFO, "User added: {0} by Owner", newUsername);
            return Response.success("User added successfully", newUsername);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add user: " + newUsername, e);
            return Response.failure("Failed to add user: " + e.getMessage());
        }
    }

    @Override
    protected Response<String> loginUser(String targetUsername, String targetPassword) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (ResultSet rs = executeQuery(sql, targetUsername)) {
            // Mengecek apakah user ditemukan
            if (rs != null && rs.next()) {
                String storedPassword = rs.getString("password"); // Ambil password yang di-hash dari DB
                String storedUsername = rs.getString("username");

                // Hash password yang diberikan untuk dibandingkan
                String hashedInputPassword = hashPassword(targetPassword);

                // Memeriksa apakah password yang di-hash cocok dengan password yang ada di
                // database
                if (storedPassword.equals(hashedInputPassword)) {
                    // Jika password cocok, login berhasil
                    LOGGER.log(Level.INFO, "User {0} logged in successfully.", targetUsername);
                    return Response.success("Login successful.", storedUsername);
                } else {
                    // Jika password tidak cocok
                    String msg = "Incorrect password for user: " + targetUsername;
                    LOGGER.warning(msg);
                    return Response.failure(msg);
                }
            } else {
                // Jika username tidak ditemukan di database
                String msg = "User not found: " + targetUsername;
                LOGGER.warning(msg);
                return Response.failure(msg);
            }
        } catch (Exception e) {
            String err = "Error during login for user: " + targetUsername;
            LOGGER.log(Level.SEVERE, err, e);
            return Response.failure("Error during login: " + e.getMessage());
        }
    }

    @Override
    protected Response<String> getUser(String targetUsername) {
        String sql = "SELECT * FROM users WHERE username = ?";

        try (ResultSet rs = executeQuery(sql, targetUsername)) {
            if (rs != null && rs.next()) {
                String foundUser = rs.getString("username") + " (" + rs.getString("role") + ")";
                LOGGER.log(Level.INFO, "User retrieved: {0}", foundUser);
                return Response.success("User found", foundUser);
            } else {
                LOGGER.log(Level.WARNING, "User not found: {0}", targetUsername);
                return Response.failure("User not found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user: " + targetUsername, e);
            return Response.failure("Failed to retrieve user: " + e.getMessage());
        }
    }

    @Override
    protected Response<String> updateUser(String newUsername, String newPassword) {
        try {
            String hashedPassword = hashPassword(newPassword);
            String sql = "UPDATE users SET password = ? WHERE username = ?";
            executeUpdate(sql, hashedPassword, newUsername);

            LOGGER.log(Level.INFO, "User updated: {0}", newUsername);
            return Response.success("User updated successfully", newUsername);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update user: " + newUsername, e);
            return Response.failure("Failed to update user: " + e.getMessage());
        }
    }

    @Override
    protected Response<String> deleteUser(String targetUsername) {
        try {
            String sql = "DELETE FROM users WHERE username = ?";
            executeUpdate(sql, targetUsername);

            LOGGER.log(Level.INFO, "User deleted: {0}", targetUsername);
            return Response.success("User deleted successfully", targetUsername);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete user: " + targetUsername, e);
            return Response.failure("Failed to delete user: " + e.getMessage());
        }
    }
}
