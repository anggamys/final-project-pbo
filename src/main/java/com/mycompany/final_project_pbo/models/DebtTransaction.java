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
public class DebtTransaction extends BaseEntity {
    private String debtorName;
    private String address;
    private String phoneNumber;
    private Date loanDate;
    private Date dueDate;
    private Double amount;
    private LoanStatus status;
    private Integer createdBy;

    public DebtTransaction() {
        super();
    }

    public DebtTransaction(String debtorName, String address, String phoneNumber, Date loanDate, Date dueDate,
            Double amount, LoanStatus status, Integer createdBy) {
        this.debtorName = debtorName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.loanDate = loanDate;
        this.dueDate = dueDate;
        this.amount = amount;
        this.status = status;
        this.createdBy = createdBy;
    }

    // Getter dan Setter
    public String getDebtorName() {
        return debtorName;
    }

    public void setDebtorName(String debtorName) {
        this.debtorName = debtorName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public String toString() {
        return "DebtTransaction{" + "debtorName=" + debtorName + ", address=" + address + ", phoneNumber=" + phoneNumber
                + ", loanDate=" + loanDate + ", dueDate=" + dueDate + ", amount=" + amount + ", status=" + status
                + ", createdBy=" + createdBy + '}';
    }
}
