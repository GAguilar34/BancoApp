package com.bankApp.login.controller;

import com.bankApp.login.service.LoginService;
import com.bankApp.customer.model.Customer;
import com.bankApp.customer.controller.CustomerController;
import com.bankApp.customer.model.Customer;

import java.util.Optional;

public class LoginController {

        private LoginService loginService;
        private CustomerController customerController;

        public LoginController(LoginService loginService, CustomerController customerController) {
            this.loginService = loginService;
            this.customerController = customerController;
        }

        // Metodo principal para login
        public Optional<Customer> login(String email, String password) {
            try {
                // Valida que email y password no esten vacios
                if (email == null || email.trim().isEmpty()) {
                    System.out.println("Error: Email no puede estar vacio");
                    return Optional.empty();
                }

                if (password == null || password.trim().isEmpty()) {
                    System.out.println("Error: Password no puede estar vacio");
                    return Optional.empty();
                }

                // Llama al servicio de autenticacion (que ya usa password encriptado)
                if (loginService.authenticate(email, password)) {
                    Optional<Customer> customer = loginService.getCustomerByEmail(email);
                    if (customer.isPresent()) {
                        System.out.println("Login exitoso para: " + customer.get().getNombreCompleto());
                        return customer;
                    }
                }

                System.out.println("Login fallido: Credenciales incorrectas");
                return Optional.empty();

            } catch (Exception e) {
                System.out.println("Error en login: " + e.getMessage());
                return Optional.empty();
            }
        }

        // Metodo para registro de nuevo cliente con validaciones
        public boolean register(int id, int edad, String nombreCompleto,
                                String direccion, String email, String password) {
            try {
                System.out.println("Procesando registro para: " + email);

                // Validaciones basicas
                if (nombreCompleto == null || nombreCompleto.trim().isEmpty()) {
                    System.out.println("Error: Nombre completo es requerido");
                    return false;
                }

                if (email == null || email.trim().isEmpty()) {
                    System.out.println("Error: Email es requerido");
                    return false;
                }

                if (password == null || password.trim().isEmpty()) {
                    System.out.println("Error: Password es requerido");
                    return false;
                }

                // Validar fortaleza del password
                if (!Customer.isPasswordStrong(password)) {
                    System.out.println("Error: Password no cumple con los requisitos de seguridad");
                    System.out.println(Customer.getPasswordRequirements());
                    return false;
                }

                // Verifica si el email ya existe
                if (loginService.emailExists(email)) {
                    System.out.println("Error: El email ya esta registrado");
                    return false;
                }

                // Verificar si el ID ya existe (opcional)
                Optional<Customer> existingCustomer = customerController.findCustomerById(id);
                if (existingCustomer.isPresent()) {
                    System.out.println("Error: El ID ya esta en uso");
                    return false;
                }

                // Registra el nuevo cliente
                System.out.println("Registrando nuevo cliente...");
                customerController.register(id, edad, nombreCompleto, direccion, email, password);

                System.out.println("Cliente registrado exitosamente");
                return true;

            } catch (IllegalArgumentException e) {
                System.out.println("Error en registro: " + e.getMessage());
                return false;
            } catch (Exception e) {
                System.out.println("Error inesperado en registro: " + e.getMessage());
                return false;
            }
        }

        // Metodo para cambiar contraseña
        public boolean changePassword(String email, String oldPassword, String newPassword) {
            try {
                System.out.println("Solicitud para cambiar password de: " + email);

                // Validaciones
                if (oldPassword == null || oldPassword.trim().isEmpty() ||
                        newPassword == null || newPassword.trim().isEmpty()) {
                    System.out.println("Error: Passwords no pueden estar vacios");
                    return false;
                }

                // Validar que nuevo password sea fuerte
                if (!Customer.isPasswordStrong(newPassword)) {
                    System.out.println("Error: Nuevo password no cumple con los requisitos de seguridad");
                    System.out.println(Customer.getPasswordRequirements());
                    return false;
                }

                // Verificar que no sea el mismo password
                if (oldPassword.equals(newPassword)) {
                    System.out.println("Error: El nuevo password debe ser diferente al anterior");
                    return false;
                }

                // Intentar cambiar el password
                boolean success = loginService.changePassword(email, oldPassword, newPassword);

                if (success) {
                    System.out.println("Password cambiado exitosamente para: " + email);
                } else {
                    System.out.println("Error al cambiar password: Credenciales incorrectas");
                }

                return success;

            } catch (Exception e) {
                System.out.println("Error al cambiar password: " + e.getMessage());
                return false;
            }
        }

        // Metodo para recuperar cuenta (version basica)
        public boolean recoverAccount(String email) {
            try {
                Optional<Customer> customer = loginService.getCustomerByEmail(email);
                if (customer.isPresent()) {
                    System.out.println("Cuenta encontrada para: " + email);
                    System.out.println("Nombre: " + customer.get().getNombreCompleto());
                    System.out.println("Contacte al administrador para restablecer su password");
                    return true;
                } else {
                    System.out.println("No se encontro cuenta con el email: " + email);
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Error en recuperacion de cuenta: " + e.getMessage());
                return false;
            }
        }

        // Metodo para verificar si un usuario esta autenticado
        public boolean isAuthenticated(Optional<Customer> currentCustomer) {
            return currentCustomer != null && currentCustomer.isPresent();
        }

        // Metodo para obtener informacion del usuario autenticado
        public String getUserInfo(Optional<Customer> currentCustomer) {
            if (isAuthenticated(currentCustomer)) {
                Customer customer = currentCustomer.get();
                return "Usuario: " + customer.getNombreCompleto() +
                        " | Email: " + customer.getEmail() +
                        " | ID: " + customer.getId();
            }
            return "No hay usuario autenticado";
        }

        // Metodo para cerrar sesion
        public Optional<Customer> logout(Optional<Customer> currentCustomer) {
            if (isAuthenticated(currentCustomer)) {
                System.out.println("Cerrando sesion para: " + currentCustomer.get().getNombreCompleto());
            }
            System.out.println("Sesion cerrada exitosamente");
            return Optional.empty();
        }

        // Metodo para validar credenciales sin hacer login
        public boolean validateCredentials(String email, String password) {
            return loginService.authenticate(email, password);
        }

        // Metodo para obtener cliente por ID (delegado)
        public Optional<Customer> getCustomerById(int id) {
            return customerController.findCustomerById(id);
        }

        // Metodo para obtener cliente por email (delegado)
        public Optional<Customer> getCustomerByEmail(String email) {
            return customerController.findByEmail(email);
        }
}
