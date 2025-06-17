/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.final_project_pbo.utils;

import com.mycompany.final_project_pbo.models.Product;
import com.mycompany.final_project_pbo.models.TransactionType;

/**
 *
 * @author workspace
 */
public class TransactionManager {
    private TransactionType currentTransactionType;
    private Product currentProduct;

    private TransactionManager() {}

    private static class Holder {
        private static final TransactionManager INSTANCE = new TransactionManager();
    }
    public static TransactionManager getInstance() {
        return Holder.INSTANCE;
    }

    public synchronized void setTransaction(TransactionType type, Product product) {
        this.currentTransactionType = type;
        this.currentProduct = product;
    }

    public synchronized void clearTransaction() {
        this.currentTransactionType = null;
        this.currentProduct = null;
    }

    public synchronized TransactionType getCurrentTransactionType() {
        return currentTransactionType;
    }

    public synchronized Product getCurrentProduct() {
        return currentProduct;
    }

    public synchronized boolean isTransactionActive() {
        return currentTransactionType != null && currentProduct != null;
    }
}
