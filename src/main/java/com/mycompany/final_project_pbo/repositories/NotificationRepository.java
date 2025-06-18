/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.repositories;

import com.mycompany.final_project_pbo.models.Notification;
import com.mycompany.final_project_pbo.models.NotificationType;
import com.mycompany.final_project_pbo.utils.CrudRepository;
import com.mycompany.final_project_pbo.utils.DatabaseUtil;
import com.mycompany.final_project_pbo.utils.Response;

import java.sql.Connection;
import java.util.ArrayList;

/**
 *
 * @author workspace
 */
public class NotificationRepository implements CrudRepository<Notification> {

    @Override
    public Response<Notification> save(Notification entity, Integer userId) {
        String query = "INSERT INTO notifications (type, message) VALUES (?, ?)";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, entity.getType().name()); // SAVE ENUM NAME!
            preparedStatement.setString(2, entity.getMessage());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Notification saved successfully.", entity);
            } else {
                return Response.failure("Failed to save notification.");
            }
        } catch (Exception e) {
            return Response.failure("Error saving notification: " + e.getMessage());
        }
    }

    @Override
    public Response<Notification> update(Notification entity, Integer userId) {
        String query = "UPDATE notifications SET type = ?, message = ? WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setString(1, entity.getType().name());
            preparedStatement.setString(2, entity.getMessage());
            preparedStatement.setInt(3, entity.getId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Notification updated successfully.", entity);
            } else {
                return Response.failure("Failed to update notification.");
            }
        } catch (Exception e) {
            return Response.failure("Error updating notification: " + e.getMessage());
        }
    }

    @Override
    public Response<Notification> findById(Integer id, Integer userId) {
        String query = "SELECT * FROM notifications WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            var resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Notification notification = new Notification();
                notification.setId(resultSet.getInt("id"));
                notification.setType(NotificationType.valueOf(resultSet.getString("type"))); // from enum name
                notification.setMessage(resultSet.getString("message"));
                notification.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                return Response.success("Notification found.", notification);
            } else {
                return Response.failure("Notification not found.");
            }
        } catch (Exception e) {
            return Response.failure("Error finding notification: " + e.getMessage());
        }
    }

    @Override
    public Response<Boolean> deleteById(Integer id, Integer userId) {
        String query = "DELETE FROM notifications WHERE id = ?";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                return Response.success("Notification deleted successfully.", true);
            } else {
                return Response.failure("Failed to delete notification.");
            }
        } catch (Exception e) {
            return Response.failure("Error deleting notification: " + e.getMessage());
        }
    }

    @Override
    public Response<ArrayList<Notification>> findAll(Integer userId) {
        String query = "SELECT * FROM notifications";

        try (Connection conn = DatabaseUtil.getConnection()) {
            var preparedStatement = conn.prepareStatement(query);

            var resultSet = preparedStatement.executeQuery();
            ArrayList<Notification> notifications = new ArrayList<>();
            while (resultSet.next()) {
                Notification notification = new Notification();
                notification.setId(resultSet.getInt("id"));
                notification.setType(NotificationType.valueOf(resultSet.getString("type"))); // from enum name
                notification.setMessage(resultSet.getString("message"));
                notification.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                notifications.add(notification);
            }

            return Response.success("Notifications found.", notifications);
        } catch (Exception e) {
            return Response.failure("Error finding notifications: " + e.getMessage());
        }
    }
}
