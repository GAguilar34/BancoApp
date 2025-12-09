package com.bankApp.transaction.model;

import java.time.LocalDateTime;

public class Transaction {

    private String id;
    private String transactionId;
    private Type type;
    private Status status;
    private int customerId;
    private String customerName;
    private String targetAccount;
    private double amount;
    private double previousBalance;
    private double newBalance;
    private String description;
    private String reference;
    private LocalDateTime transactionDate;
    private LocalDateTime createdAt;

    // Constructor vacio necesario para MongoDB
    public Transaction() {
        this.transactionDate = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.status = Status.COMPLETED;
        this.transactionId = generateTransactionId();
    }

    // Constructor para transacciones basicas como deposito o retiro
    public Transaction(Type type, int customerId, String customerName,
                       double amount, double previousBalance, String description) {
        this(); // Llama al constructor vacio
        this.type = type;
        this.customerId = customerId;
        this.customerName = customerName;
        this.amount = amount;
        this.previousBalance = previousBalance;
        this.newBalance = calculateNewBalance();
        this.description = description;
    }

    // Constructor para transferencias y pagos que tienen cuenta destino
    public Transaction(Type type, int customerId, String customerName,
                       String targetAccount, double amount, double previousBalance,
                       String description) {
        this(type, customerId, customerName, amount, previousBalance, description);
        this.targetAccount = targetAccount;
    }

    // Metodo privado para calcular el nuevo saldo basado en el tipo de transaccion
    private double calculateNewBalance() {
        if (type == Type.DEPOSIT || type == Type.TRANSFER_IN || type == Type.INTEREST) {
            // Para transacciones que aumentan el saldo
            return previousBalance + amount;
        } else {
            // Para transacciones que disminuyen el saldo
            return previousBalance - amount;
        }
    }

    // Metodo privado para generar un ID de transaccion legible
    private String generateTransactionId() {
        // Genera un numero aleatorio de 6 digitos
        int randomNum = 100000 + (int)(Math.random() * 900000);
        return "TRX-" + randomNum;
    }

    // Getters y Setters para todos los atributos
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        // Recalcula el nuevo saldo cuando cambia el tipo
        this.newBalance = calculateNewBalance();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
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
        this.newBalance = calculateNewBalance();
    }

    public double getPreviousBalance() {
        return previousBalance;
    }

    public void setPreviousBalance(double previousBalance) {
        this.previousBalance = previousBalance;
        this.newBalance = calculateNewBalance();
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
