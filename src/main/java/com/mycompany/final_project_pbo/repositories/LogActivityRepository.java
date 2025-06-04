/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.LogActivity;
import com.mycompany.final_project_pbo.models.LogLevel;
import com.mycompany.final_project_pbo.utils.DatabaseUtil;
import com.mycompany.final_project_pbo.utils.Response;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author c0delb08
 */
public class LogActivityRepository {

    public Response<LogActivity> save(LogActivity entity) {
        String query = "INSERT INTO log_activities (user_id, action, module, log_level) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setObject(1, entity.getUserId(), java.sql.Types.INTEGER);
            preparedStatement.setString(2, entity.getAction());
            preparedStatement.setString(3, entity.getModule());
            preparedStatement.setString(4, entity.getLogLevel().name());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Log activity saved successfully.", entity);
            } else {
                return Response.failure("Failed to save log activity.");
            }
        } catch (Exception e) {
            return Response.failure("Error saving log activity: " + e.getMessage());
        }
    }

    public Response<LogActivity> update(LogActivity entity) {
        String query = "UPDATE log_activities SET user_id = ?, action = ?, module = ?, log_level = ? WHERE log_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setObject(1, entity.getUserId(), java.sql.Types.INTEGER);
            preparedStatement.setString(2, entity.getAction());
            preparedStatement.setString(3, entity.getModule());
            preparedStatement.setString(4, entity.getLogLevel().name());
            preparedStatement.setInt(5, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Log activity updated successfully.", entity);
            } else {
                return Response.failure("Failed to update log activity.");
            }
        } catch (Exception e) {
            return Response.failure("Error updating log activity: " + e.getMessage());
        }
    }

    public Response<LogActivity> findById(Integer id) {
        String query = "SELECT * FROM log_activities WHERE log_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);
            var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                LogActivity logActivity = new LogActivity();
                logActivity.setId(resultSet.getInt("id"));
                logActivity.setUserId(resultSet.getInt("user_id"));
                logActivity.setAction(resultSet.getString("action"));
                logActivity.setModule(resultSet.getString("module"));
                logActivity.setLogLevel(LogLevel.valueOf(resultSet.getString("log_level")));

                return Response.success("Log activity found.", logActivity);
            } else {
                return Response.failure("Log activity not found.");
            }
        } catch (Exception e) {
            return Response.failure("Error finding log activity: " + e.getMessage());
        }
    }

    public Response<Boolean> deleteById(Integer id) {
        String query = "DELETE FROM log_activities WHERE log_id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Log activity deleted successfully.", true);
            } else {
                return Response.failure("Failed to delete log activity.");
            }
        } catch (Exception e) {
            return Response.failure("Error deleting log activity: " + e.getMessage());
        }
    }

    public Response<ArrayList<LogActivity>> findAll() {
        String query = "SELECT * FROM log_activities";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            var resultSet = preparedStatement.executeQuery();

            ArrayList<LogActivity> logActivities = new ArrayList<>();
            while (resultSet.next()) {
                LogActivity logActivity = new LogActivity();
                logActivity.setId(resultSet.getInt("log_id"));
                logActivity.setUserId(resultSet.getInt("user_id"));
                logActivity.setAction(resultSet.getString("action"));
                logActivity.setModule(resultSet.getString("module"));
                logActivity.setLogLevel(LogLevel.valueOf(resultSet.getString("log_level")));
                logActivities.add(logActivity);
            }

            return Response.success("Log activities found.", logActivities);
        } catch (Exception e) {
            return Response.failure("Error finding log activities: " + e.getMessage());
        }
    }
}
