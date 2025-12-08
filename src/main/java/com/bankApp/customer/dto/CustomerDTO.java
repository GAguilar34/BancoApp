package com.bankApp.customer.dto;

import com.bankApp.customer.model.Customer;

import java.math.BigDecimal;

public class CustomerDTO {
    Customer customer;
    private int id;
    private int edad;
    private String nombreCompleto;
    private String direccion;
    private String email;
    private String password;
    private Double saldo;
    private Double credito;

    public CustomerDTO(int id, int edad, String nombreCompleto, String direccion, String email, String password,  Double saldo, Double credito) {
        this.id = id;
        this.edad = edad;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
        this.saldo = saldo;
        this.credito = credito;
    }
    //Convierte customer en un dto para que el frontend pueda acceder a la informacion necesaria
    public static CustomerDTO fromCustomer(Customer customer){
       return new CustomerDTO(
               customer.getId(),
               customer.getEdad(),
               customer.getNombreCompleto(),
               customer.getDireccion(),
               customer.getEmail(),
               customer.getPassword(),
               customer.getSaldo(),
               customer.getCredito()
       );
    }

    //Getters
    public int getId() {return id;}

    public int getEdad() {return edad;}

    public String getNombreCompleto() {return nombreCompleto;}

    public String getDireccion() {return direccion;}

    public String getEmail() {return email;}

    public String getPassword() {return password;}

    public Double getSaldo() {return saldo;}

    public Double getCredito() {return credito;}
}
