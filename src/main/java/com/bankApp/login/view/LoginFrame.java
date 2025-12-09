package com.bankApp.login.view;

import com.bankApp.bankAppFrame;
import com.bankApp.customer.controller.CustomerController;
import com.bankApp.customer.view.RegisterCustomer;
import com.bankApp.login.controller.LoginController;
import com.bankApp.login.service.LoginService;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

public class LoginFrame extends JFrame{

    JPanel panel;
    JLabel label, label2, label3, label4, label5, labelImage;
    JTextField text, text2;
    JButton boton, boton2;
    LoginService loginService;
    CustomerController customerController;
    LoginController loginController; // Agregar esta variable

    public LoginFrame(){
        this.setTitle("Login");
        this.setSize(600,700);
        this.setLayout(new BorderLayout());
        this.setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        // INICIALIZAR LOS SERVICIOS AQUÍ
        this.customerController = new CustomerController();
        this.loginService = new LoginService(customerController);
        this.loginController = new LoginController(loginService, customerController);

        InitializeComponents();
    }

    public void InitializeComponents(){
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color (255, 255, 255));
        this.getContentPane().add(panel, BorderLayout.CENTER);

        ImageIcon icon = new ImageIcon(getClass().getResource("/icons/LogoBanco.png"));
        labelImage = new JLabel (icon);
        labelImage.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(labelImage);

        label = new JLabel("Mi Banco Digital");
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);

        panel.add(Box.createVerticalStrut(15));

        label2 = new JLabel("Bienvenido");
        label2.setForeground(Color.BLACK);
        label2.setFont(new Font("Arial", Font.BOLD, 20));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label2);

        panel.add(Box.createVerticalStrut(15));

        label3 = new JLabel("Inicio Sesion");
        label3.setForeground(Color.BLACK);
        label3.setFont(new Font("Arial", Font.BOLD, 20));
        label3.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label3);

        panel.add(Box.createVerticalStrut(15));

        // Panel para el label de Usuario para desplazarlo a la derecha
        JPanel userLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        userLabelPanel.setBackground(Color.WHITE);
        userLabelPanel.setMaximumSize(new Dimension(600, 30));

        label4 = new JLabel("Usuario");
        label4.setForeground(Color.BLACK);
        label4.setFont(new Font("Arial", Font.BOLD, 14));

        // Agrega espacio a la izquierda para desplazar a la derecha
        userLabelPanel.add(Box.createHorizontalStrut(100));
        userLabelPanel.add(label4);
        panel.add(userLabelPanel);

        panel.add(Box.createVerticalStrut(5));

        // Panel para el TextField de Usuario para desplazarlo a la derecha
        JPanel userTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        userTextPanel.setBackground(Color.WHITE);
        userTextPanel.setMaximumSize(new Dimension(600, 40));

        text = new JTextField("Ingrese su usario");
        text.setForeground(Color.BLACK);
        text.setFont(new Font("Arial", Font.BOLD, 14));
        text.setPreferredSize(new Dimension(350,30));
        text.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        // Agrega espacio a la izquierda para desplazar a la derecha
        userTextPanel.add(Box.createHorizontalStrut(100));
        userTextPanel.add(text);
        panel.add(userTextPanel);

        text.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                if(text.getText().equals("Ingrese su usario")) {
                    text.setText("");
                    text.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e){
                if(text.getText().isEmpty()){
                    text.setText("Ingrese su usario");
                    text.setForeground(Color.GRAY);
                }
            }
        });

        panel.add(Box.createVerticalStrut(15));

        // Panel para el label de Contraseña
        JPanel passwordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passwordLabelPanel.setBackground(Color.WHITE);
        passwordLabelPanel.setMaximumSize(new Dimension(600, 40));

        label5 = new JLabel("Contraseña");
        label5.setForeground(Color.BLACK);
        label5.setFont(new Font("Arial", Font.BOLD, 14));

        passwordLabelPanel.add(Box.createHorizontalStrut(100));
        passwordLabelPanel.add(label5);
        panel.add(passwordLabelPanel);

        panel.add(Box.createVerticalStrut(5));

        // Panel para el TextField de Contraseña
        JPanel passwordTextPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passwordTextPanel.setBackground(Color.WHITE);
        passwordTextPanel.setMaximumSize(new Dimension(600, 40));

        text2 = new JTextField("Ingrese su Contraseña");
        text2.setForeground(Color.BLACK);
        text2.setFont(new Font("Arial", Font.BOLD, 14));
        text2.setPreferredSize(new Dimension(350,30));
        text2.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true));

        passwordTextPanel.add(Box.createHorizontalStrut(100));
        passwordTextPanel.add(text2);
        panel.add(passwordTextPanel);

        text2.addFocusListener(new FocusListener(){
            public void focusGained(FocusEvent e){
                if(text2.getText().equals("Ingrese su Contraseña")){
                    text2.setText("");
                    text2.setForeground(Color.BLACK);

                }
            }

            public void focusLost(FocusEvent e){
                if(text2.getText().isEmpty()){
                    text2.setText("Ingrese su Contraseña");
                    text2.setForeground(Color.GRAY);
                }
            }
        });

        panel.add(Box.createVerticalStrut(15));

        // Panel para el botón Registrarse alineado a la derecha
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setMaximumSize(new Dimension(600, 40));

        boton = new JButton("Registrarse");
        boton.setPreferredSize(new Dimension(120, 30));
        boton.setBorderPainted(false);
        boton.setContentAreaFilled(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setFont(new Font("Arial", Font.PLAIN, 14));
        boton.setForeground(new Color(100, 110, 120));

        boton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RegisterCustomer v1 = new RegisterCustomer();
                v1.setVisible(true);
                dispose();
            }
        });

        buttonPanel.add(Box.createHorizontalStrut(200));
        buttonPanel.add(boton);
        panel.add(buttonPanel);

        panel.add(Box.createVerticalStrut(25));

        JPanel button2Panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        button2Panel.setBackground(Color.WHITE);
        button2Panel.setMaximumSize(new Dimension(600, 60));

        boton2 = new JButton("Iniciar Sesion");
        boton2.setPreferredSize(new Dimension(220, 40));
        boton2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton2.setFont(new Font("Arial", Font.BOLD, 17));
        boton2.setForeground(new Color(255, 255, 255));
        boton2.setBackground(new Color(49, 122, 18));

        boton2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                String email = text.getText();
                String password = text2.getText();

                // Validar que los campos no estén vacíos NI contengan el placeholder
                if(email.isEmpty() || email.equals("Ingrese su usario") ||
                        password.isEmpty() || password.equals("Ingrese su Contraseña")){
                    JOptionPane.showMessageDialog(null, "Por favor llene todos los campos",
                            "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // USAR LA INSTANCIA YA INICIALIZADA
                var result = loginController.login(email, password);

                if(result.isPresent()) {
                    JOptionPane.showMessageDialog(null, "Login exitoso. Cargando Dashboard...",
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Abre el dashboard
                    bankAppFrame dashboard = new bankAppFrame(result.get());
                    dashboard.setVisible(true);
                    dispose(); // Cierra el login
                } else {
                    JOptionPane.showMessageDialog(null, "Credenciales incorrectas",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        button2Panel.add(Box.createHorizontalStrut(180));
        button2Panel.add(boton2);
        panel.add(button2Panel);
    }
}
