package ar.com.hmu.service;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.UsuarioRepository;
import ar.com.hmu.utils.PasswordUtils;

import java.sql.SQLException;
import java.util.Arrays;

public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cambia la contraseña de un usuario y persiste el cambio en la base de datos.
     *
     * @param usuario           El usuario que cambiará la contraseña.
     * @param currentPassword   La contraseña actual ingresada por el usuario.
     * @param newPassword       La nueva contraseña.
     * @param confirmNewPassword   Confirmación de la nueva contraseña.
     */
    public boolean changePassword(Usuario usuario, char[] currentPassword, char[] newPassword, char[] confirmNewPassword) {
        try {
            // Paso 1: Validar la contraseña actual
            if (!PasswordUtils.validatePassword(currentPassword, usuario.getEncryptedPassword())) {
                throw new IllegalArgumentException("La contraseña actual no es correcta.");
            }

            // Paso 2: Validar que la nueva contraseña coincide con la confirmación
            if (!Arrays.equals(newPassword, confirmNewPassword)) {
                throw new IllegalArgumentException("Las nuevas contraseñas no coinciden.");
            }

            // Paso 3: Establecer la nueva contraseña cifrada en el objeto Usuario
            String hashedPassword = PasswordUtils.hashPassword(newPassword);
            usuario.setPasswordHash(hashedPassword);

            // Paso 4: Actualizar la contraseña en la base de datos
            usuarioRepository.updatePassword(usuario.getCuil(), hashedPassword);

            return true; // Si el cambio es exitoso, retorna true
        } catch (SQLException e) {
            // Manejar la excepción y lanzar una excepción de tiempo de ejecución
            throw new RuntimeException("Error al actualizar la contraseña en la base de datos", e);
        } finally {
            // Paso 5: Limpiar los arrays de contraseñas para evitar que permanezcan en memoria
            Arrays.fill(currentPassword, '\0');
            Arrays.fill(newPassword, '\0');
            Arrays.fill(confirmNewPassword, '\0');
        }
    }

}
