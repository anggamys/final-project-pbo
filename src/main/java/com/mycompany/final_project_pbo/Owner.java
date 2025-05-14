/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0delb08
 */
public class Owner extends User {
    private static final Logger LOGGER = Logger.getLogger(Owner.class.getName());

    public Owner(String username, String password) {
        super(username, password, "OWNER");
    }

    @Override
    public void accessDashboard() {
        System.out.println("Owner " + username + ": access owner features.");
    }

    @Override
    public void addUser(String newUsername, String newPassword) {
        try {
            String hashedPassword = hashPassword(newPassword);
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            executeUpdate(sql, newUsername, hashedPassword, getRole());
            System.out.println("User " + newUsername + " added successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add user: " + newUsername, e);
        }
    }

    @Override
    public void getUser(String targetUsername) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (ResultSet rs = executeQuery(sql, targetUsername)) {
            if (rs != null && rs.next()) {
                System.out.println("User found: " + rs.getString("username") + ", Role: " + rs.getString("role"));
            } else {
                System.out.println("User " + targetUsername + " not found.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to retrieve user: " + targetUsername, e);
        }
    }

    @Override
    public void updateUser(String targetUsername, String newPassword) {
        try {
            String hashedPassword = hashPassword(newPassword);
            String sql = "UPDATE users SET password = ? WHERE username = ?";
            executeUpdate(sql, hashedPassword, targetUsername);
            System.out.println("User " + targetUsername + " updated successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update user: " + targetUsername, e);
        }
    }

    @Override
    public void deleteUser(String targetUsername) {
        String sql = "DELETE FROM users WHERE username = ?";
        try {
            executeUpdate(sql, targetUsername);
            System.out.println("User " + targetUsername + " deleted successfully.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete user: " + targetUsername, e);
        }
    }
}
