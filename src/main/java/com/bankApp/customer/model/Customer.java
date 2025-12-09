package com.bankApp.customer.model;

public class Customer {
    private int id;
    private int edad;
    private String nombreCompleto;
    private String direccion;
    private String email;
    private String password;
    private Double saldo;
    private Double credito;

    public Customer(int id, int edad, String nombreCompleto, String direccion, String email, String password, Double saldo, Double credito) {
        this.id = id;
        this.edad = edad;
        this.nombreCompleto = nombreCompleto;
        this.direccion = direccion;
        this.email = email;
        this.password = password;
        this.saldo = saldo;
        this.credito = credito;

    }

    // Metodo para validar fortaleza de password
    public static boolean isPasswordStrong(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        // Verifica que tenga al menos una letra mayuscula, una minuscula y un numero
        boolean hasUpper = false;
        boolean hasLower = false;
        boolean hasDigit = false;

        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            if (Character.isLowerCase(c)) hasLower = true;
            if (Character.isDigit(c)) hasDigit = true;
        }

        return hasUpper && hasLower && hasDigit;
    }

    // Metodo para obtener requisitos de password
    public static String getPasswordRequirements() {
        return "El password debe tener:\n" +
                "- Al menos 8 caracteres\n" +
                "- Al menos una letra mayuscula\n" +
                "- Al menos una letra minuscula\n" +
                "- Al menos un numero";
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

    //Setters
    public void setId(int id) {this.id = id;}

    public void setEdad(int edad) {this.edad = edad;}

    public void setNombreCompleto(String nombreCompleto) {this.nombreCompleto = nombreCompleto;}

    public void setDireccion(String direccion) {this.direccion = direccion;}

    public void setEmail(String email) {this.email = email;}

    public void setPassword(String password) {this.password = password;}

    public void setSaldo(Double saldo) {this.saldo = saldo;}

    public void setCredito(Double credito) {this.credito = credito;}

}
