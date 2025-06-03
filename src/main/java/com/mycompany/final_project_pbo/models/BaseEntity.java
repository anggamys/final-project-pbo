/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.models;

import java.util.Date;

public abstract class BaseEntity {
    protected Integer id;
    protected Date createdAt;
    protected Date updatedAt;

    public BaseEntity() {
        Date now = new Date();
        this.createdAt = now;
        this.updatedAt = now;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatedAt() {
        return new Date(createdAt.getTime());
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = new Date(createdAt.getTime());
    }

    public Date getUpdatedAt() {
        return new Date(updatedAt.getTime());
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = new Date(updatedAt.getTime());
    }
}
