package com.bankApp.customer.repository;

import com.bankApp.customer.model.Customer;

import java.util.Optional;
import java.util.List;

public interface CustomerRepository {
    Optional<Customer> findById(int id); //Buscamos un cliente por su id
    List<Customer> findAllOrderedById(); //Ordenamos un cliente por su id
    Customer save(Customer customer); //Agregamos un cliene
    boolean delteById(int id); //Eliminamos un cliente por su id
    Optional <Customer> findByName(String nombreCompleto); //Buscamos un cliente por su nombre
    Optional <Customer> findByEmail(String email); //Buscamos un cliente por su email
    Customer update(Customer customer); //Actualizamos los datos de un cliente
}
