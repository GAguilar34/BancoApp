package com.bankApp.customer.controller;

import com.bankApp.customer.dto.CustomerDTO;
import com.bankApp.customer.model.Customer;
import com.bankApp.customer.service.CustomerService;

import java.util.List;
import java.util.Optional;

public class CustomerController {
    CustomerService customerService;

    public CustomerController(){
        this.customerService = new CustomerService();
    }

    public Optional<Customer> findCustomerById(int id) {
        return customerService.findById(id);
    }

    //Metodo para registrar un cliente
    public CustomerDTO register(int id, int edad, String nombreCompleto,
                                String direccion, String email, String password) {
        return customerService.save(id, edad, nombreCompleto, direccion, email, password, 0.0, 0.0);
    }

    //Metodo para actualizar los datos de un cliente
    public Customer updateCustomer(int id, Customer customer) {
        return customerService.update(id, customer);
    }

    //Metodo para eliminar un cliente
    public boolean deleteById(int id){
        return customerService.deleteById(id);
    }

    //Metodo para ordenar todos los clientes por su id
    public List<Customer> findAllOrderedById(){
        return customerService.findAllOrderedById();
    }

    //Metodo para obtener un cliente por su nombre
    public List<Customer> findByName(String nombreCompleto){
        return customerService.findByName(nombreCompleto);
    }

    //Metodo para buscar un cliente por su email
    public Optional<Customer> findByEmail(String email){
        return customerService.findByEmail(email);
    }

    //Metodo para eliminar un cliente por su id
    public boolean deleteCustomer(int id) {return customerService.deleteById(id);}
}
