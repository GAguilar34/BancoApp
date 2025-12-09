package com.bankApp.login.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptPassword {

    // Metodo para encriptar password usando SHA-256
    public static String encryptPassword(String password) {
        try {
            // Crea instancia de MessageDigest con algoritmo SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Convierte el password a bytes y aplica el hash
            byte[] hashBytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));

            // Convierte el hash a string en formato hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            System.out.println("Error al encriptar password: Algoritmo no disponible");
            // Si falla el algoritmo, retorna el password sin encriptar (no deberia pasar)
            return password;
        }
    }

    // Metodo para verificar si un password coincide con el hash almacenado
    public static boolean verifyPassword(String inputPassword, String storedHash) {
        String inputHash = encryptPassword(inputPassword);
        return inputHash.equals(storedHash);
    }

    // Metodo para generar un salt
    public static String generateSalt() {
        byte[] salt = new byte[16];
        new java.security.SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    // Metodo para encriptar password con salt
    public static String encryptPasswordWithSalt(String password, String salt) {
        return encryptPassword(password + salt);
    }
}
