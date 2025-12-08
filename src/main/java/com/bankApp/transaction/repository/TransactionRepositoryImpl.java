package com.bankApp.transaction.repository;

import com.bankApp.transaction.model.Status;
import com.bankApp.transaction.model.Transaction;
import com.bankApp.transaction.model.Type;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.mongodb.client.model.Filters.*;

public class TransactionRepositoryImpl implements TransactionRepository {

    private MongoCollection<Document> collection;

    // Constructor que recibe la conexion a la base de datos
    public TransactionRepositoryImpl(MongoDatabase database) {
        this.collection = database.getCollection("transactions");
    }

    @Override
    public Transaction save(Transaction transaction) {
        try {
            // Convierte el objeto Transaction a Document de MongoDB
            Document doc = new Document()
                    .append("transactionId", transaction.getTransactionId())
                    .append("type", transaction.getType().toString())
                    .append("status", transaction.getStatus().toString())
                    .append("customerId", transaction.getCustomerId())
                    .append("customerName", transaction.getCustomerName())
                    .append("targetAccount", transaction.getTargetAccount())
                    .append("amount", transaction.getAmount())
                    .append("previousBalance", transaction.getPreviousBalance())
                    .append("newBalance", transaction.getNewBalance())
                    .append("description", transaction.getDescription())
                    .append("reference", transaction.getReference())
                    .append("transactionDate", Date.from(transaction.getTransactionDate()
                            .atZone(ZoneId.systemDefault()).toInstant()))
                    .append("createdAt", Date.from(transaction.getCreatedAt()
                            .atZone(ZoneId.systemDefault()).toInstant()));

            // Inserta el documento en la coleccion
            collection.insertOne(doc);

            // Asigna el ID generado por MongoDB al objeto Transaction
            transaction.setId(doc.getObjectId("_id").toString());

            return transaction;

        } catch (Exception e) {
            System.out.println("Error al guardar la transaccion: " + e.getMessage());
            return null;
        }
    }

    @Override
    public Optional<Transaction> findById(String id) {
        try {
            // Busca por el ID de MongoDB (_id)
            Document doc = collection.find(eq("_id", new org.bson.types.ObjectId(id))).first();
            return doc != null ? Optional.of(documentToTransaction(doc)) : Optional.empty();
        } catch (Exception e) {
            System.out.println("Error al buscar transaccion por ID: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public Optional<Transaction> findByTransactionId(String transactionId) {
        try {
            // Busca por nuestro transactionId legible
            Document doc = collection.find(eq("transactionId", transactionId)).first();
            return doc != null ? Optional.of(documentToTransaction(doc)) : Optional.empty();
        } catch (Exception e) {
            System.out.println("Error al buscar transaccion por transactionId: " + e.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public List<Transaction> findByCustomerId(int customerId) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            // Busca todas las transacciones de un cliente, ordenadas por fecha descendente
            for (Document doc : collection.find(eq("customerId", customerId))
                    .sort(Sorts.descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transacciones por customerId: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByCustomerIdAndType(int customerId, Type type) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            // Filtra por customerId y tipo de transaccion
            Bson filter = and(eq("customerId", customerId), eq("type", type.toString()));
            for (Document doc : collection.find(filter).sort(Sorts.descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transacciones por tipo: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByCustomerIdAndStatus(int customerId, Status status) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            // Filtra por customerId y estado
            Bson filter = and(eq("customerId", customerId), eq("status", status.toString()));
            for (Document doc : collection.find(filter).sort(Sorts.descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transacciones por estado: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public List<Transaction> findRecentByCustomerId(int customerId, int limit) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            // Obtiene las ultimas N transacciones de un cliente
            int count = 0;
            for (Document doc : collection.find(eq("customerId", customerId))
                    .sort(Sorts.descending("transactionDate"))) {
                if (count >= limit) break;
                transactions.add(documentToTransaction(doc));
                count++;
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transacciones recientes: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public List<Transaction> findByCustomerIdAndDateRange(int customerId, LocalDateTime startDate, LocalDateTime endDate) {
        List<Transaction> transactions = new ArrayList<>();
        try {
            // Convierte LocalDateTime a Date para MongoDB
            Date start = Date.from(startDate.atZone(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDate.atZone(ZoneId.systemDefault()).toInstant());

            // Filtra por customerId y rango de fechas
            Bson filter = and(
                    eq("customerId", customerId),
                    gte("transactionDate", start),
                    lte("transactionDate", end)
            );

            for (Document doc : collection.find(filter).sort(Sorts.descending("transactionDate"))) {
                transactions.add(documentToTransaction(doc));
            }
        } catch (Exception e) {
            System.out.println("Error al buscar transacciones por rango de fechas: " + e.getMessage());
        }
        return transactions;
    }

    @Override
    public boolean existsByReference(String reference) {
        try {
            // Verifica si ya existe una transaccion con esta referencia
            long count = collection.countDocuments(eq("reference", reference));
            return count > 0;
        } catch (Exception e) {
            System.out.println("Error al verificar referencia: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateStatus(String transactionId, Status newStatus) {
        try {
            // Actualiza el estado de una transaccion
            Bson filter = eq("transactionId", transactionId);
            Bson update = new Document("$set", new Document("status", newStatus.toString()));

            UpdateResult result = collection.updateOne(filter, update);
            return result.getModifiedCount() > 0;
        } catch (Exception e) {
            System.out.println("Error al actualizar estado de transaccion: " + e.getMessage());
            return false;
        }
    }

    // Metodo privado para convertir Document de MongoDB a objeto Transaction
    private Transaction documentToTransaction(Document doc) {
        Transaction transaction = new Transaction();

        transaction.setId(doc.getObjectId("_id").toString());
        transaction.setTransactionId(doc.getString("transactionId"));
        transaction.setType(Type.valueOf(doc.getString("type")));
        transaction.setStatus(Status.valueOf(doc.getString("status")));
        transaction.setCustomerId(doc.getInteger("customerId"));
        transaction.setCustomerName(doc.getString("customerName"));
        transaction.setTargetAccount(doc.getString("targetAccount"));
        transaction.setAmount(doc.getDouble("amount"));
        transaction.setPreviousBalance(doc.getDouble("previousBalance"));
        transaction.setNewBalance(doc.getDouble("newBalance"));
        transaction.setDescription(doc.getString("description"));
        transaction.setReference(doc.getString("reference"));

        // Convierte Date de MongoDB a LocalDateTime
        Date transactionDate = doc.getDate("transactionDate");
        Date createdAt = doc.getDate("createdAt");

        transaction.setTransactionDate(transactionDate.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());
        transaction.setCreatedAt(createdAt.toInstant()
                .atZone(ZoneId.systemDefault()).toLocalDateTime());

        return transaction;
    }
}
