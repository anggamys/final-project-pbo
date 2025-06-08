/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.models;

import java.util.Date;

/**
 *
 * @author c0delb08
 */
public class StockTransaction extends BaseEntity {
    private Integer productId;
    private Integer quantity;
    private TransactionType transactionType;
    private String description;
    private Integer userId;
    private Date createdAt;

    public StockTransaction() {
        super();
    }

    public StockTransaction(Integer productId, Integer quantity, TransactionType transactionType, String description,
            Integer userId, Date createdAt) {
        super();
        this.productId = productId;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.description = description;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "StockTransaction{id=" + id + ", productId=" + productId + ", quantity=" + quantity + ", transactionType="
                + transactionType + ", description=" + description + ", userId=" + userId + ", createdAt=" + createdAt
                + '}';
    }
}
