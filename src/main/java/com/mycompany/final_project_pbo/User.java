/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo;

import com.mycompany.final_project_pbo.Utils.LoggerUtil;
import com.mycompany.final_project_pbo.Utils.DatabaseUtil;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author c0delb08
 */
public class User {
    private int idUser;
    private String username;
    private String password;
    private String role;

    private final Logger LOGGER;

    public User() {
        this(0, "", "", "STAFF"); // Default to STAFF role
    }

    public User(int idUser, String username, String password, String role) {
        this.idUser = idUser;
        this.username = username;
        this.password = password;
        this.role = role;
        this.LOGGER = LoggerUtil.getLogger(getClass());
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void accessDashboard() {
        LOGGER.log(Level.INFO, "{0} {1} accessing dashboard.", new Object[]{role, username});
    }

    public String hashPassword(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();
        for (byte b : hash)
            sb.append(String.format("%02x", b));
        return sb.toString();
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

    protected ResultSet executeQuery(String sql, Object... params) {
        try {
            Connection conn = DatabaseUtil.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }

            return ps.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database query error: " + e.getMessage(), e);
            return null;
        }
    }

    public Response<User> addUser(String newUsername, String newPassword, String role) {
        try {
            String hashedPassword = hashPassword(newPassword);
            String sql = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            boolean success = executeUpdate(sql, newUsername, hashedPassword, role);

            if (success) {
                String fetchSql = "SELECT * FROM users WHERE username = ?";
                try (ResultSet rs = executeQuery(fetchSql, newUsername)) {
                    if (rs != null && rs.next()) {
                        User user = fromResultSet(rs);
                        return Response.success("User added successfully", user);
                    }
                }
                return Response.failure("User added but could not retrieve user details.");
            } else {
                return Response.failure("Failed to add user.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to add user: " + newUsername, e);
            return Response.failure("Add user error: " + e.getMessage());
        }
    }

    public Response<User> loginUser(String targetUsername, String targetPassword) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (ResultSet rs = executeQuery(sql, targetUsername, hashPassword(targetPassword))) {
            if (rs != null && rs.next()) {
                User user = fromResultSet(rs);
                return Response.success("Login successful", user);
            } else {
                return Response.failure("Invalid username or password");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Login error for user: " + targetUsername, e);
            return Response.failure("Login error: " + e.getMessage());
        }
    }

    public Response<User> getUser(Integer targetIdUser) {
        String sql = "SELECT * FROM users WHERE idUser = ?";

        try (ResultSet rs = executeQuery(sql, targetIdUser)) {
            if (rs != null && rs.next()) {
                User user = fromResultSet(rs);
                return Response.success("User found", user);
            } else {
                return Response.failure("User not found");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user: " + targetIdUser, e);
            return Response.failure("Get user error: " + e.getMessage());
        }
    }

    public Response<User> updateUser(Integer targetIdUser, String newUsername, String newPassword) {
        try {
            String hashedPassword = hashPassword(newPassword);
            String sql = "UPDATE users SET username = ?, password = ? WHERE idUser = ?";

            boolean success = executeUpdate(sql, newUsername, hashedPassword, targetIdUser);
            return success ? getUser(targetIdUser) : Response.failure("Failed to update user.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to update user: " + newUsername, e);
            return Response.failure("Update user error: " + e.getMessage());
        }
    }

    public Response<Boolean> deleteUser(Integer targetIdUser) {
        try {
            String sql = "DELETE FROM users WHERE idUser = ?";
            boolean success = executeUpdate(sql, targetIdUser);

            if (success) {
                LOGGER.log(Level.INFO, "User deleted: {0}", targetIdUser);
                return Response.success("User deleted successfully", true);
            } else {
                return Response.failure("Failed to delete user.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete user: " + targetIdUser, e);
            return Response.failure("Failed to delete user: " + e.getMessage());
        }
    }

    public Response<ArrayList<User>> getAllUsers(String role) {
        String sql = "SELECT * FROM users WHERE role = ?";
        ArrayList<User> users = new ArrayList<>();

        try (ResultSet rs = executeQuery(sql, role)) {
            while (rs != null && rs.next()) {
                users.add(fromResultSet(rs));
            }

            return users.isEmpty() ? Response.failure("No users found.") : Response.success("Users retrieved successfully", users);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error retrieving all users", e);
            return Response.failure("Get all users error: " + e.getMessage());
        }
    }

    private User fromResultSet(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("idUser"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role")
        );
    }

    @Override
    public String toString() {
        return "User{" + "idUser=" + idUser + ", username=" + username + ", role=" + role + '}';
    }
}

