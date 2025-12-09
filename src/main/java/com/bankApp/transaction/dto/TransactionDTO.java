package com.bankApp.transaction.dto;

import java.time.LocalDateTime;

public class TransactionDTO {

    // DTO para transferir datos de transacciones sin exponer el modelo completo
    private String transactionId;
    private String type;
    private String status;
    private String customerName;
    private String targetAccount;
    private double amount;
    private String description;
    private String reference;
    private LocalDateTime transactionDate;
    private double newBalance;

    // Constructor vacio
    public TransactionDTO() {
    }

    // Constructor que convierte de Transaction a TransactionDTO
    public TransactionDTO(String transactionId, String type, String status,
                          String customerName, String targetAccount, double amount,
                          String description, String reference, LocalDateTime transactionDate,
                          double newBalance) {
        this.transactionId = transactionId;
        this.type = type;
        this.status = status;
        this.customerName = customerName;
        this.targetAccount = targetAccount;
        this.amount = amount;
        this.description = description;
        this.reference = reference;
        this.transactionDate = transactionDate;
        this.newBalance = newBalance;
    }

    // Getters y Setters
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTargetAccount() {
        return targetAccount;
    }

    public void setTargetAccount(String targetAccount) {
        this.targetAccount = targetAccount;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    // Metodo estatico para convertir de Transaction a TransactionDTO
    public static TransactionDTO fromTransaction(com.bankApp.transaction.model.Transaction transaction) {
        return new TransactionDTO(
                transaction.getTransactionId(),
                transaction.getType().toString(),
                transaction.getStatus().toString(),
                transaction.getCustomerName(),
                transaction.getTargetAccount(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getReference(),
                transaction.getTransactionDate(),
                transaction.getNewBalance()
        );
    }
}
