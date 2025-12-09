package com.bankApp.transaction.service;

import com.bankApp.transaction.dto.TransactionDTO;
import com.bankApp.transaction.model.Transaction;
import com.bankApp.transaction.model.Type;
import com.bankApp.transaction.repository.TransactionRepository;
import com.bankApp.customer.service.CustomerService;
import com.bankApp.customer.model.Customer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionService {

    private TransactionRepository transactionRepository;
    private CustomerService customerService;

    public TransactionService(TransactionRepository transactionRepository, CustomerService customerService) {
        this.transactionRepository = transactionRepository;
        this.customerService = customerService;
    }

    // Metodo para realizar un deposito
    public TransactionDTO deposit(int customerId, double amount, String description) {
        // Obtiene el cliente para validar que existe
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        Customer customer = customerOpt.get();

        // Valida que el monto sea positivo
        if (amount <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        // Obtiene el saldo actual del cliente
        double currentBalance = customer.getSaldo();

        // Crea la transaccion de deposito
        Transaction transaction = new Transaction(
                Type.DEPOSIT,
                customerId,
                customer.getNombreCompleto(),
                amount,
                currentBalance,
                description != null ? description : "Deposito en cuenta"
        );

        // Actualiza el saldo del cliente
        customer.setSaldo(currentBalance + amount);
        customerService.update(customerId, customer);

        // Guarda la transaccion
        Transaction savedTransaction = transactionRepository.save(transaction);

        // Retorna como DTO
        return TransactionDTO.fromTransaction(savedTransaction);
    }

    // Metodo para realizar un retiro
    public TransactionDTO withdraw(int customerId, double amount, String description) {
        // Obtiene el cliente
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        Customer customer = customerOpt.get();

        // Valida que el monto sea positivo
        if (amount <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        // Obtiene el saldo actual
        double currentBalance = customer.getSaldo();

        // Valida que tenga saldo suficiente
        if (currentBalance < amount) {
            throw new RuntimeException("Saldo insuficiente. Saldo actual: " + currentBalance);
        }

        // Crea la transaccion de retiro
        Transaction transaction = new Transaction(
                Type.WITHDRAWAL,
                customerId,
                customer.getNombreCompleto(),
                amount,
                currentBalance,
                description != null ? description : "Retiro de cuenta"
        );

        // Actualiza el saldo del cliente
        customer.setSaldo(currentBalance - amount);
        customerService.update(customerId, customer);

        // Guarda la transaccion
        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionDTO.fromTransaction(savedTransaction);
    }

    // Metodo para realizar una transferencia
    public TransactionDTO transfer(int fromCustomerId, int toCustomerId, double amount, String description) {
        // Valida que no sea la misma cuenta
        if (fromCustomerId == toCustomerId) {
            throw new RuntimeException("No se puede transferir a la misma cuenta");
        }

        // Obtiene ambos clientes
        Optional<Customer> fromCustomerOpt = customerService.findById(fromCustomerId);
        Optional<Customer> toCustomerOpt = customerService.findById(toCustomerId);

        if (fromCustomerOpt.isEmpty()) {
            throw new RuntimeException("Cliente origen no encontrado");
        }
        if (toCustomerOpt.isEmpty()) {
            throw new RuntimeException("Cliente destino no encontrado");
        }

        Customer fromCustomer = fromCustomerOpt.get();
        Customer toCustomer = toCustomerOpt.get();

        // Valida que el monto sea positivo
        if (amount <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        // Valida saldo suficiente en cuenta origen
        double fromBalance = fromCustomer.getSaldo();
        if (fromBalance < amount) {
            throw new RuntimeException("Saldo insuficiente. Saldo actual: " + fromBalance);
        }

        double toBalance = toCustomer.getSaldo();

        // Crea transaccion para el cliente que envia
        Transaction outTransaction = new Transaction(
                Type.TRANSFER_OUT,
                fromCustomerId,
                fromCustomer.getNombreCompleto(),
                String.valueOf(toCustomerId),
                amount,
                fromBalance,
                description != null ? description : "Transferencia a cuenta " + toCustomerId
        );

        // Crea transaccion para el cliente que recibe
        Transaction inTransaction = new Transaction(
                Type.TRANSFER_IN,
                toCustomerId,
                toCustomer.getNombreCompleto(),
                String.valueOf(fromCustomerId),
                amount,
                toBalance,
                description != null ? description : "Transferencia de cuenta " + fromCustomerId
        );

        // Actualiza saldos
        fromCustomer.setSaldo(fromBalance - amount);
        toCustomer.setSaldo(toBalance + amount);

        customerService.update(fromCustomerId, fromCustomer);
        customerService.update(toCustomerId, toCustomer);

        // Guarda ambas transacciones
        transactionRepository.save(outTransaction);
        Transaction savedTransaction = transactionRepository.save(inTransaction);

        return TransactionDTO.fromTransaction(savedTransaction);
    }

    // Metodo para realizar un pago de servicio
    public TransactionDTO payService(int customerId, String serviceName, double amount, String description) {
        // Obtiene el cliente
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        Customer customer = customerOpt.get();

        // Valida que el monto sea positivo
        if (amount <= 0) {
            throw new RuntimeException("El monto debe ser mayor a cero");
        }

        // Obtiene el saldo actual
        double currentBalance = customer.getSaldo();

        // Valida que tenga saldo suficiente
        if (currentBalance < amount) {
            throw new RuntimeException("Saldo insuficiente. Saldo actual: " + currentBalance);
        }

        // Crea la transaccion de pago
        Transaction transaction = new Transaction(
                Type.PAYMENT,
                customerId,
                customer.getNombreCompleto(),
                serviceName,
                amount,
                currentBalance,
                description != null ? description : "Pago de servicio: " + serviceName
        );

        // Actualiza el saldo del cliente
        customer.setSaldo(currentBalance - amount);
        customerService.update(customerId, customer);

        // Guarda la transaccion
        Transaction savedTransaction = transactionRepository.save(transaction);

        return TransactionDTO.fromTransaction(savedTransaction);
    }

    // Metodo para obtener el historial de transacciones de un cliente
    public List<TransactionDTO> getTransactionHistory(int customerId) {
        // Valida que el cliente exista
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        // Obtiene todas las transacciones del cliente
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);

        // Convierte a DTO
        return transactions.stream()
                .map(TransactionDTO::fromTransaction)
                .collect(Collectors.toList());
    }

    // Metodo para obtener las ultimas N transacciones
    public List<TransactionDTO> getRecentTransactions(int customerId, int limit) {
        // Valida que el cliente exista
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        // Valida el limite
        if (limit <= 0) {
            limit = 10; // Valor por defecto
        }

        // Obtiene las transacciones recientes
        List<Transaction> transactions = transactionRepository.findRecentByCustomerId(customerId, limit);

        // Convierte a DTO
        return transactions.stream()
                .map(TransactionDTO::fromTransaction)
                .collect(Collectors.toList());
    }

    // Metodo para consultar el saldo actual
    public double getCurrentBalance(int customerId) {
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        return customerOpt.get().getSaldo();
    }

    // Metodo para buscar una transaccion por su ID
    public Optional<TransactionDTO> findTransactionById(String transactionId) {
        Optional<Transaction> transactionOpt = transactionRepository.findByTransactionId(transactionId);
        return transactionOpt.map(TransactionDTO::fromTransaction);
    }

    // Metodo para verificar si una transaccion existe
    public boolean transactionExists(String transactionId) {
        return transactionRepository.findByTransactionId(transactionId).isPresent();
    }

    // Metodo para obtener transacciones por tipo
    public List<TransactionDTO> getTransactionsByType(int customerId, Type type) {
        // Valida que el cliente exista
        Optional<Customer> customerOpt = customerService.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Cliente no encontrado con ID: " + customerId);
        }

        // Obtiene transacciones por tipo
        List<Transaction> transactions = transactionRepository.findByCustomerIdAndType(customerId, type);

        // Convierte a DTO
        return transactions.stream()
                .map(TransactionDTO::fromTransaction)
                .collect(Collectors.toList());
    }
}
