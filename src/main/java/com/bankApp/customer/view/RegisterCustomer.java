package com.bankApp.customer.view;

import com.bankApp.customer.controller.CustomerController;
import com.bankApp.customer.model.Customer;
import com.bankApp.login.view.LoginFrame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class RegisterCustomer extends JFrame {

    private JPanel panel;
    private JLabel lblTitulo, lblId, lblEdad, lblNombre, lblDireccion, lblEmail, lblPassword, lblConfirmPassword;
    private JTextField txtId, txtEdad, txtNombre, txtDireccion, txtEmail;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnCancelar, btnRegistrar;
    private CustomerController customerController;

    public RegisterCustomer(){
        this.setTitle("Registro de Cliente");
        this.setSize(500, 700);
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.customerController = new CustomerController();
        InitializeComponents();
    }

    public void InitializeComponents(){
        panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(Color.WHITE);
        this.add(panel);

        // Título
        lblTitulo = new JLabel("Registro de Nuevo Cliente");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(new Color(34, 139, 34));
        lblTitulo.setBounds(80, 20, 350, 30);
        panel.add(lblTitulo);

        // ID
        lblId = new JLabel("ID de Cliente:");
        lblId.setFont(new Font("Arial", Font.BOLD, 14));
        lblId.setBounds(50, 80, 150, 25);
        panel.add(lblId);

        txtId = new JTextField();
        txtId.setFont(new Font("Arial", Font.PLAIN, 14));
        txtId.setBounds(50, 110, 400, 35);
        txtId.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtId);

        // Edad
        lblEdad = new JLabel("Edad:");
        lblEdad.setFont(new Font("Arial", Font.BOLD, 14));
        lblEdad.setBounds(50, 155, 150, 25);
        panel.add(lblEdad);

        txtEdad = new JTextField();
        txtEdad.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEdad.setBounds(50, 185, 400, 35);
        txtEdad.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtEdad);

        // Nombre Completo
        lblNombre = new JLabel("Nombre Completo:");
        lblNombre.setFont(new Font("Arial", Font.BOLD, 14));
        lblNombre.setBounds(50, 230, 150, 25);
        panel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Arial", Font.PLAIN, 14));
        txtNombre.setBounds(50, 260, 400, 35);
        txtNombre.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtNombre);

        // Dirección
        lblDireccion = new JLabel("Dirección:");
        lblDireccion.setFont(new Font("Arial", Font.BOLD, 14));
        lblDireccion.setBounds(50, 305, 150, 25);
        panel.add(lblDireccion);

        txtDireccion = new JTextField();
        txtDireccion.setFont(new Font("Arial", Font.PLAIN, 14));
        txtDireccion.setBounds(50, 335, 400, 35);
        txtDireccion.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtDireccion);

        // Email
        lblEmail = new JLabel("Correo Electrónico:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmail.setBounds(50, 380, 150, 25);
        panel.add(lblEmail);

        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setBounds(50, 410, 400, 35);
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtEmail);

        // Contraseña
        lblPassword = new JLabel("Contraseña:");
        lblPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblPassword.setBounds(50, 455, 150, 25);
        panel.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtPassword.setBounds(50, 485, 400, 35);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtPassword);

        // Confirmar Contraseña
        lblConfirmPassword = new JLabel("Confirmar Contraseña:");
        lblConfirmPassword.setFont(new Font("Arial", Font.BOLD, 14));
        lblConfirmPassword.setBounds(50, 530, 180, 25);
        panel.add(lblConfirmPassword);

        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtConfirmPassword.setBounds(50, 560, 400, 35);
        txtConfirmPassword.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        panel.add(txtConfirmPassword);

        // Botón Cancelar
        btnCancelar = new JButton("Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 16));
        btnCancelar.setBounds(50, 615, 190, 40);
        btnCancelar.setBackground(new Color(220, 53, 69));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        null,
                        "¿Estás seguro de cancelar el registro?",
                        "Confirmar",
                        JOptionPane.YES_NO_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    LoginFrame login = new LoginFrame();
                    login.setVisible(true);
                    dispose();
                }
            }
        });
        panel.add(btnCancelar);

        // Botón Registrar
        btnRegistrar = new JButton("Registrar");
        btnRegistrar.setFont(new Font("Arial", Font.BOLD, 16));
        btnRegistrar.setBounds(260, 615, 190, 40);
        btnRegistrar.setBackground(new Color(40, 167, 69));
        btnRegistrar.setForeground(Color.WHITE);
        btnRegistrar.setFocusPainted(false);
        btnRegistrar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegistrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registrarCliente();
            }
        });
        panel.add(btnRegistrar);
    }

    private void registrarCliente() {
        try {
            // Validar campos vacíos
            if (txtId.getText().trim().isEmpty() ||
                    txtEdad.getText().trim().isEmpty() ||
                    txtNombre.getText().trim().isEmpty() ||
                    txtDireccion.getText().trim().isEmpty() ||
                    txtEmail.getText().trim().isEmpty() ||
                    txtPassword.getPassword().length == 0 ||
                    txtConfirmPassword.getPassword().length == 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Por favor, completa todos los campos",
                        "Campos vacíos",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Validar ID (solo números)
            int id;
            try {
                id = Integer.parseInt(txtId.getText().trim());
                if (id <= 0) {
                    JOptionPane.showMessageDialog(
                            this,
                            "El ID debe ser un número positivo",
                            "ID inválido",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "El ID debe ser un número válido",
                        "ID inválido",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Validar Edad
            int edad;
            try {
                edad = Integer.parseInt(txtEdad.getText().trim());
                if (edad < 18 || edad > 120) {
                    JOptionPane.showMessageDialog(
                            this,
                            "La edad debe estar entre 18 y 120 años",
                            "Edad inválida",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "La edad debe ser un número válido",
                        "Edad inválida",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            String nombre = txtNombre.getText().trim();
            String direccion = txtDireccion.getText().trim();
            String email = txtEmail.getText().trim();
            String password = new String(txtPassword.getPassword());
            String confirmPassword = new String(txtConfirmPassword.getPassword());

            // Validar que las contraseñas coincidan
            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(
                        this,
                        "Las contraseñas no coinciden",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Validar fortaleza de contraseña
            if (!Customer.isPasswordStrong(password)) {
                JOptionPane.showMessageDialog(
                        this,
                        Customer.getPasswordRequirements(),
                        "Contraseña débil",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Verificar si el ID ya existe
            if (customerController.findCustomerById(id).isPresent()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El ID ya está registrado. Por favor, usa otro ID.",
                        "ID duplicado",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Verificar si el email ya existe
            if (customerController.findByEmail(email).isPresent()) {
                JOptionPane.showMessageDialog(
                        this,
                        "El email ya está registrado. Por favor, usa otro email.",
                        "Email duplicado",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // Registrar el cliente
            customerController.register(id, edad, nombre, direccion, email, password);

            JOptionPane.showMessageDialog(
                    this,
                    "¡Registro exitoso!\nYa puedes iniciar sesión con tu email y contraseña.",
                    "Éxito",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Volver al login
            LoginFrame login = new LoginFrame();
            login.setVisible(true);
            dispose();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "Error de validación",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al registrar: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
