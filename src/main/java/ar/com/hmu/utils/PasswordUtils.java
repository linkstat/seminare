package ar.com.hmu.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Clase utilitaria para gestionar el hash y la validación de contraseñas.
 *
 * La clase `PasswordUtils` proporciona métodos estáticos para realizar operaciones relacionadas
 * con la seguridad de contraseñas, tales como el hashing de contraseñas antes de almacenarlas
 * y la validación de contraseñas ingresadas por los usuarios. Utiliza el algoritmo BCrypt,
 * implementado por {@link BCryptPasswordEncoder}, para asegurar que las contraseñas sean almacenadas
 * de manera segura.
 */
public class PasswordUtils {

    private static BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Genera el hash de una contraseña antes de almacenarla.
     *
     * Este método recibe una contraseña en texto plano y la transforma en una versión
     * hasheada utilizando el algoritmo BCrypt. Esta versión hasheada se puede almacenar
     * de forma segura en la base de datos.
     *
     * @param password la contraseña en texto plano que se desea hashear.
     * @return la contraseña hasheada usando BCrypt.
     */
    public static String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * Valida una contraseña ingresada contra el hash almacenado.
     *
     * Este método compara una contraseña ingresada por el usuario en texto plano con
     * el hash almacenado previamente en la base de datos. Utiliza BCrypt para realizar
     * la comparación y determinar si la contraseña es correcta.
     *
     * @param rawPassword la contraseña en texto plano ingresada por el usuario.
     * @param encodedPassword la contraseña hasheada almacenada.
     * @return true si la contraseña ingresada coincide con el hash almacenado, de lo contrario false.
     */
    public static boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
