package ar.com.hmu.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Método para hashear una contraseña antes de almacenarla
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    // Método para validar una contraseña ingresada contra el hash almacenado
    public static boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
