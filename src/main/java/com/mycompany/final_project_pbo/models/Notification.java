/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.models;

/**
 *
 * @author workspace
 */
public class Notification extends BaseEntity{
    private NotificationType type;
    private String message;

    public Notification() {
    }

    public Notification(NotificationType type, String message) {
        super();
        this.type = type;
        this.message = message;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Notification{" + "type=" + type + ", message=" + message + ", id=" + getId() + ", createdAt=" + getCreatedAt() + ", updatedAt=" + getUpdatedAt() + '}';
    }
}
