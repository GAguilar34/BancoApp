package com.bankApp.transaction.repository;

import com.bankApp.transaction.model.Status;
import com.bankApp.transaction.model.Transaction;
import com.bankApp.transaction.model.Type;

import java.util.List;
import java.util.Optional;

public interface TransactionRepository {
    Transaction save(Transaction transaction); // Metodo para guardar una nueva transaccion
    Optional<Transaction> findById(String id); // Metodo para buscar una transaccion por su ID de MongoDB
    Optional<Transaction> findByTransactionId(String transactionId); // Metodo para buscar transacciones por ID de transaccion legible
    List<Transaction> findByCustomerId(int customerId);  // Metodo para buscar todas las transacciones de un cliente
    List<Transaction> findByCustomerIdAndType(int customerId, Type type); // Metodo para buscar transacciones por tipo
    List<Transaction> findByCustomerIdAndStatus(int customerId, Status status);  // Metodo para buscar transacciones por estado
    List<Transaction> findRecentByCustomerId(int customerId, int limit); // Metodo para obtener las ultimas N transacciones de un cliente
    List<Transaction> findByCustomerIdAndDateRange(int customerId, java.time.LocalDateTime startDate, java.time.LocalDateTime endDate);  // Metodo para buscar transacciones por rango de fechas
    boolean existsByReference(String reference);  // Metodo para verificar si existe una transaccion con cierta referencia
    boolean updateStatus(String transactionId, Status newStatus);  // Metodo para actualizar el estado de una transaccion
}
