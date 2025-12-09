package com.bankApp.login.service;

import com.bankApp.customer.controller.CustomerController;
import com.bankApp.customer.model.Customer;
import java.util.Optional;

public class LoginService {

    private CustomerController customerController;

    public LoginService(CustomerController customerController) {
        this.customerController = customerController;
    }

    // Metodo para autenticar usuario
    public boolean authenticate(String email, String password) {
        Optional<Customer> customerOpt = customerController.findByEmail(email);

        if (customerOpt.isEmpty()) {
            return false;
        }

        Customer customer = customerOpt.get();

        // Verifica la contraseña encriptada
        return EncryptPassword.verifyPassword(password, customer.getPassword());
    }

    // Metodo para obtener cliente por email
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerController.findByEmail(email);
    }

    // Metodo para verificar si un email existe
    public boolean emailExists(String email) {
        return customerController.findByEmail(email).isPresent();
    }

    // Metodo para cambiar contraseña
    public boolean changePassword(String email, String oldPassword, String newPassword) {
        if (!authenticate(email, oldPassword)) {
            return false;
        }

        Optional<Customer> customerOpt = customerController.findByEmail(email);
        if (customerOpt.isEmpty()) {
            return false;
        }

        Customer customer = customerOpt.get();
        String encryptedNewPassword = EncryptPassword.encryptPassword(newPassword);
        customer.setPassword(encryptedNewPassword);

        customerController.updateCustomer(customer.getId(), customer);
        return true;
    }

    // Metodo para encriptar password (delegado)
    public static String encryptPassword(String password) {
        return EncryptPassword.encryptPassword(password);
    }
}
