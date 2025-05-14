/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.security.MessageDigest;
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
public abstract class User {
    protected String username;
    protected String password;
    protected String role;
    
    protected final Logger LOGGER = Logger.global.getLogger(this.getClass().getName());

    public User(String username, String password, String role){
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() { return username; }
    public String getRole() { return role; }

    public String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) sb.append(String.format("%02x", b));
        return sb.toString();
    }
    
    protected void executeUpdate(String sql, Object... params) {
        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            ps.executeUpdate();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Database update error", e);
        }
    }

    protected ResultSet executeQuery(String sql, Object... params) {
        try {
            Connection conn = DatabaseUtil.getConnection(); // Not auto-closed because ResultSet must remain open
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database query error", e);
            return null;
        }
    }

    protected abstract void accessDashboard();
    protected abstract void addUser(String username, String password);
    protected abstract void getUser(String username);
    protected abstract void updateUser(String username, String password);
    protected abstract void deleteUser(String username);
}
