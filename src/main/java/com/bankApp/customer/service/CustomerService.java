package com.bankApp.customer.service;

import com.bankApp.customer.model.Customer;
import com.bankApp.customer.dto.CustomerDTO;
import com.bankApp.customer.repository.CustomerRepositorylmpl;
import com.bankApp.customer.repository.CustomerRepository;

import javax.swing.*;
import java.util.List;
import java.util.Optional;

public class CustomerService {

    CustomerRepository customerRepository;

    public CustomerService(){
        this.customerRepository = new CustomerRepositorylmpl();
    }

    //Metodo para validar un email
    public boolean isValidEmail(String email){
        if(email == null || email.isEmpty()){
            System.out.println("Email vacio o nulo");
            return false;
        }

        //Validamos que email contenga estos caracteres y sea mayor a 6 caracteres
        boolean hasASteal = email.contains("@");
        boolean hasPoint =  email.contains(".");
        boolean eldestAsix = email.length() > 6;

        //Verificamos que primero este el arroba antes del punto
        int atIndex = email.indexOf("@");
        int dotIndex = email.lastIndexOf(".");
        boolean correctOrder = atIndex > 0 &&  dotIndex > atIndex + 1 && dotIndex < email.length() -2;

        // No debe haber espacios
        boolean noSpaces = !email.contains(" ");

        // Solo debe haber un @
        boolean singleAt = email.indexOf("@") == email.lastIndexOf("@");

        // Valida que cumpla con todas las validaciones
        boolean isValid = hasASteal && hasPoint && eldestAsix && correctOrder && noSpaces && singleAt;

        //Si email no es valido mostramos un mensaje
        if(!isValid){
            System.out.println("Invalid Email.");
        }

        return isValid; //Retornamos el email si es valido
    }

    //Metodo para agregar un cliente
    public CustomerDTO save(int id, int edad, String nombreCompleto, String direccion, String email, String password, double v, double v1){
        //Validamos el email
        if(!isValidEmail(email)){
            JOptionPane.showMessageDialog(null, "Email invalido");
            throw new IllegalArgumentException("El email es invalido");
        }

        //Agregamos el cliente
        Customer customer = new Customer(
                id,
                edad,
                nombreCompleto,
                direccion,
                email,
                password,
                0.0,
                0.0
        );

        //Agrega el customer al repository y lo guarda en la base de datos
        Customer savedCustomer = customerRepository.save(customer);
        //Convierte el customer a dto(data transfer object) y este los retorna
        return CustomerDTO.fromCustomer(savedCustomer);
    }

    //Metodo para obtener todos los clientes ordenados por id
    public List<Customer> findAllOrderedById(){
        return customerRepository.findAllOrderedById();
    }

    //Metodo para buscar un cliente por su id
    public Optional<Customer> findById(int id){
        return customerRepository.findById(id);
    }

    //Metodo para buscar un cliente por su nombre
    public List<Customer> findByName(String nombreCompleto){
        return customerRepository.findByName(nombreCompleto);
    }
    //Metodo para buscar un cliente por su email
    public Optional<Customer> findByEmail(String email){
        return customerRepository.findByEmail(email);
    }

    //Metodo para eliminar un cliente por su id
    public boolean delteById(int id){
        return customerRepository.delteById(id);
    }

    //Metodo para actualizar un cliente
    public Customer update(int id, Customer customer){
        return customerRepository.update(id, customer);
    }

    // Metodo para verificar si hay saldo suficiente
    public boolean tieneSaldoSuficiente(int idCliente, double montoPago) {
        Optional<Customer> optionalCustomer = findById(idCliente);

        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        Customer cliente = optionalCustomer.get();
        return cliente.getSaldo() >= montoPago;
    }

    // Metodo para obtener el saldo actual
    public double consultarSaldo(int idCliente) {
        Optional<Customer> optionalCustomer = findById(idCliente);

        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        return optionalCustomer.get().getSaldo();
    }

    // Metodo para realizar un pago (si hay saldo suficiente)
    public boolean realizarPago(int idCliente, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        Optional<Customer> optionalCustomer = findById(idCliente);

        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        Customer cliente = optionalCustomer.get();

        // Verificar saldo suficiente
        if (cliente.getSaldo() < monto) {
            return false;
        }

        // Actualizar saldo
        double nuevoSaldo = cliente.getSaldo() - monto;
        cliente.setSaldo(nuevoSaldo);

        // Actualizar en la base de datos
        update(idCliente, cliente);
        return true;
    }

    // Metodo para recargar saldo
    public void recargarSaldo(int idCliente, double monto) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El monto debe ser positivo");
        }

        Optional<Customer> optionalCustomer = findById(idCliente);

        if (optionalCustomer.isEmpty()) {
            throw new IllegalArgumentException("Cliente no encontrado");
        }

        Customer cliente = optionalCustomer.get();
        double nuevoSaldo = cliente.getSaldo() + monto;
        cliente.setSaldo(nuevoSaldo);

        update(idCliente, cliente);
    }


}
