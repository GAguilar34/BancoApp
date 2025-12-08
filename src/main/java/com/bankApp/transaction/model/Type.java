package com.bankApp.transaction.model;

public enum Type {
    DEPOSIT,      // Deposito de dinero
    WITHDRAWAL,   // Retiro de dinero
    TRANSFER_OUT, // Transferencia que sale de la cuenta
    TRANSFER_IN,  // Transferencia que entra a la cuenta
    PAYMENT,      // Pago de servicio
    FEE,          // Comision o cargo
    INTEREST      // Interes ganado
}
