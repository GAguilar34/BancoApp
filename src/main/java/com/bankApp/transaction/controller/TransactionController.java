package com.bankApp.transaction.controller;

import com.bankApp.transaction.dto.TransactionDTO;
import com.bankApp.transaction.model.Type;
import com.bankApp.transaction.service.TransactionService;
import com.bankApp.transaction.model.Transaction;
import java.util.List;
import java.util.Optional;

public class TransactionController {

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // Metodo para realizar un deposito
    public TransactionDTO deposit(int customerId, double amount, String description) {
        try {
            return transactionService.deposit(customerId, amount, description);
        } catch (RuntimeException e) {
            System.out.println("Error en deposito: " + e.getMessage());
            throw e; // Re-lanza la excepcion para que la capa superior la maneje
        }
    }

    // Metodo para realizar un retiro
    public TransactionDTO withdraw(int customerId, double amount, String description) {
        try {
            return transactionService.withdraw(customerId, amount, description);
        } catch (RuntimeException e) {
            System.out.println("Error en retiro: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para realizar una transferencia
    public TransactionDTO transfer(int fromCustomerId, int toCustomerId, double amount, String description) {
        try {
            return transactionService.transfer(fromCustomerId, toCustomerId, amount, description);
        } catch (RuntimeException e) {
            System.out.println("Error en transferencia: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para realizar un pago de servicio
    public TransactionDTO payService(int customerId, String serviceName, double amount, String description) {
        try {
            return transactionService.payService(customerId, serviceName, amount, description);
        } catch (RuntimeException e) {
            System.out.println("Error en pago de servicio: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para obtener el historial completo de transacciones
    public List<TransactionDTO> getTransactionHistory(int customerId) {
        try {
            return transactionService.getTransactionHistory(customerId);
        } catch (RuntimeException e) {
            System.out.println("Error al obtener historial: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para obtener las ultimas N transacciones
    public List<TransactionDTO> getRecentTransactions(int customerId, int limit) {
        try {
            return transactionService.getRecentTransactions(customerId, limit);
        } catch (RuntimeException e) {
            System.out.println("Error al obtener transacciones recientes: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para consultar el saldo actual
    public double getCurrentBalance(int customerId) {
        try {
            return transactionService.getCurrentBalance(customerId);
        } catch (RuntimeException e) {
            System.out.println("Error al consultar saldo: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para buscar una transaccion especifica por su ID
    public Optional<TransactionDTO> findTransactionById(String transactionId) {
        try {
            return transactionService.findTransactionById(transactionId);
        } catch (Exception e) {
            System.out.println("Error al buscar transaccion: " + e.getMessage());
            return Optional.empty();
        }
    }

    // Metodo para verificar si una transaccion existe
    public boolean transactionExists(String transactionId) {
        try {
            return transactionService.transactionExists(transactionId);
        } catch (Exception e) {
            System.out.println("Error al verificar transaccion: " + e.getMessage());
            return false;
        }
    }

    // Metodo para obtener transacciones por tipo
    public List<TransactionDTO> getTransactionsByType(int customerId, String type) {
        try {
            // Convierte el string a enum
            Type transactionType = Type.valueOf(type.toUpperCase());
            return transactionService.getTransactionsByType(customerId, transactionType);
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de transaccion invalido: " + type);
            throw new RuntimeException("Tipo de transaccion invalido. Tipos validos: DEPOSIT, WITHDRAWAL, TRANSFER_OUT, TRANSFER_IN, PAYMENT, FEE, INTEREST");
        } catch (RuntimeException e) {
            System.out.println("Error al obtener transacciones por tipo: " + e.getMessage());
            throw e;
        }
    }

    // Metodo para obtener el resumen financiero (solo saldo actual)
    public String getAccountSummary(int customerId) {
        try {
            double balance = transactionService.getCurrentBalance(customerId);
            return "Saldo actual: $" + String.format("%.2f", balance);
        } catch (RuntimeException e) {
            System.out.println("Error al obtener resumen: " + e.getMessage());
            return "Error al obtener informacion de la cuenta";
        }
    }
}