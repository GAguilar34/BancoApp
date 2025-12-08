package com.bankApp.transaction.model;

public enum Status {
    COMPLETED,    // Transaccion completada con exito
    PENDING,      // Transaccion pendiente de procesar
    FAILED,       // Transaccion fallida
    CANCELLED     // Transaccion cancelada
}
