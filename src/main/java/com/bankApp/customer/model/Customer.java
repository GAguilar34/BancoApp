package com.bankApp.customer.model;

import java.math.BigDecimal;

public class Customer {
    private int id;
    private int edad;
    private String nombreCompleto;
    private String direccion;
    private String email;
    private String password;
    private BigDecimal saldo;
    private BigDecimal credito;

    public Customer(int id, int edad, String nombreCompleto, String direccion, String email, String password) {
        this.id = id;
        this.edad = edad;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
        this.saldo = new BigDecimal(0);
        this.credito = new BigDecimal(0);

    }

    //Getters
    public int getId() {return id;}

    public int getEdad() {return edad;}

    public String getNombreCompleto() {return nombreCompleto;}

    public String getDireccion() {return direccion;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public BigDecimal getSaldo() {return saldo;}

    public BigDecimal getCredito() {return credito;}

    //Setters
    public void setId(int id) {this.id = id;}

    public void setEdad(int edad) {this.edad = edad;}

    public void setNombreCompleto(String nombreCompleto) {this.nombreCompleto = nombreCompleto;}

    public void setDireccion(String direccion) {this.direccion = direccion;}

    public void setEmail(String email) {this.email = email;}

    public void setPassword(String password) {this.password = password;}

    public void setSaldo(BigDecimal saldo) {this.saldo = saldo;}

    public void setCredito(BigDecimal credito) {this.credito = credito;}

}
