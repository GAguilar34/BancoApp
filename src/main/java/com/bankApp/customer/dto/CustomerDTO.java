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
    private BigDecimal saldo;
    private BigDecimal credito;

    public CustomerDTO(int id, int edad, String nombreCompleto, String direccion, String email, String password){
        this.id = id;
        this.edad = edad;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
        this.saldo = new BigDecimal(0);
        this.credito = new BigDecimal(0);
    }
    //Convierte customer en un dto para que el frontend pueda acceder a la informacion necesaria
    public static CustomerDTO fromCustomer(Customer customer){
       return new CustomerDTO(
               customer.getId(),
               customer.getEdad(),
               customer.getNombreCompleto(),
               customer.getDireccion(),
               customer.getEmail(),
               customer.getPassword()
       );
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
}
