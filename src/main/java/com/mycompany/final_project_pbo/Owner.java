/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.logging.Level;

/**
 *
 * @author c0delb08
 */
//public class Owner extends User {
//
//    public Owner() {
//        super(0, "", "", "OWNER");
//    }
//
//    @Override
//    protected void accessDashboard() {
//        LOGGER.log(Level.INFO, "Owner {0} accessing dashboard.", username);
//    }
//
//    @Override
//    protected Response<User> addUser(String newUsername, String newPassword) {
//        try {
//            String hashedPassword = hashPassword(newPassword);
//            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
//            boolean success = executeUpdate(sql, newUsername, hashedPassword, "OWNER");
//
//            if (success) {
//                String fetchSql = "SELECT * FROM users WHERE username = ?";
//                try (ResultSet rs = executeQuery(fetchSql, newUsername)) {
//                    if (rs != null && rs.next()) {
//                        User user = new Owner();
//
//                        user.setIdUser(rs.getInt("idUser"));
//                        user.setUsername(rs.getString("username"));
//                        user.setPassword(rs.getString("password"));
//                        user.setRole(rs.getString("role"));
//
//                        LOGGER.log(Level.INFO, "User added: {0}", user);
//                        return Response.success("User added successfully", user);
//                    }
//                }
//                // If success but user not found in fetch, return failure
//                return Response.failure("User added but could not retrieve user details.");
//            } else {
//                return Response.failure("Failed to add user.");
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Failed to add user: " + newUsername, e);
//            return Response.failure("Add user error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    public Response<User> loginUser(String targetUsername, String targetPassword) {
//        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
//
//        try (ResultSet rs = executeQuery(sql, targetUsername, hashPassword(targetPassword))) {
//            if (rs != null && rs.next()) {
//                User user = new Owner();
//                user.setIdUser(rs.getInt("idUser"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setRole(rs.getString("role"));
//
//                LOGGER.log(Level.INFO, "User logged in: {0}", user);
//                return Response.success("Login successful", user);
//            } else {
//                LOGGER.log(Level.WARNING, "Login failed for user: {0}", targetUsername);
//                return Response.failure("Invalid username or password");
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Login error for user: " + targetUsername, e);
//            return Response.failure("Login error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected Response<User> getUser(Integer targetIdUser) {
//        String sql = "SELECT * FROM users WHERE idUser = ?";
//
//        try (ResultSet rs = executeQuery(sql, targetIdUser)) {
//            if (rs != null && rs.next()) {
//                User user = new Owner();
//                user.setIdUser(rs.getInt("idUser"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setRole(rs.getString("role"));
//
//                LOGGER.log(Level.INFO, "User found: {0}", user);
//                return Response.success("User found", user);
//            } else {
//                LOGGER.log(Level.WARNING, "User not found: {0}", targetIdUser);
//                return Response.failure("User not found");
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error retrieving user: " + targetIdUser, e);
//            return Response.failure("Get user error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected Response<User> updateUser(Integer targetIdUser, String newUsername, String newPassword) {
//        try {
//            String hashedPassword = hashPassword(newPassword);
//            String sql = "UPDATE users SET username = ?, password = ? WHERE idUser = ?";
//            
//            boolean success = executeUpdate(sql, newUsername, hashedPassword, targetIdUser);
//
//            if (success) {
//                return getUser(targetIdUser);
//            } else {
//                return Response.failure("Failed to update user.");
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Failed to update user: " + newUsername, e);
//            return Response.failure("Update user error: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected Response<Boolean> deleteUser(Integer targetIdUser) {
//        try {
//            String sql = "DELETE FROM users WHERE idUser = ?";
//            boolean success = executeUpdate(sql, targetIdUser);
//
//            if (success) {
//                LOGGER.log(Level.INFO, "User deleted: {0}", targetIdUser);
//                return Response.success("User deleted successfully", true);
//            } else {
//                return Response.failure("Failed to delete user.");
//            }
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Failed to delete user: " + targetIdUser, e);
//            return Response.failure("Failed to delete user: " + e.getMessage());
//        }
//    }
//
//    @Override
//    protected Response<ArrayList<User>> getAllUsers() {
//        String sql = "SELECT * FROM users WHERE role = 'OWNER'";
//        ArrayList<User> users = new ArrayList<>();
//
//        try (ResultSet rs = executeQuery(sql)) {
//            while (rs != null && rs.next()) {
//                User user = new Owner();
//                user.setIdUser(rs.getInt("idUser"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                user.setRole(rs.getString("role"));
//                users.add(user);
//            }
//
//            if (users.isEmpty()) {
//                return Response.failure("No users found.");
//            }
//
//            return Response.success("Users retrieved successfully", users);
//        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error retrieving all users", e);
//            return Response.failure("Get all users error: " + e.getMessage());
//        }
//    }
//}
