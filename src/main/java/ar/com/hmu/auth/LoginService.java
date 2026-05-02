package ar.com.hmu.auth;

import java.sql.SQLException;
import java.util.Arrays;

import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.service.UsuarioService;
import ar.com.hmu.util.PasswordUtils;

/**
 * Servicio que gestiona la lógica de negocio relacionada con el inicio de sesión de usuarios.
 */
public class LoginService {

    private UsuarioService usuarioService;

    public LoginService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    /**
     * Valida las credenciales de un usuario.
     * <p>
     * Si la validación es exitosa y el hash almacenado pertenece a un algoritmo
     * legacy (BCrypt), se re-hashea con Argon2id y se persiste de forma
     * transparente. Una falla en esa persistencia no bloquea el login.
     *
     * @param cuil el CUIL del usuario que intenta iniciar sesión.
     * @param passwordCharArray la contraseña ingresada por el usuario, almacenada como char[].
     * @return true si las credenciales son válidas, false de lo contrario.
     * @throws SQLException si ocurre un error con la conexión a la base de datos.
     */
    public boolean validateUser(long cuil, char[] passwordCharArray) throws SQLException {
        char[] passwordCopy = null;
        try {
            String storedHash = usuarioService.findPasswordByCuil(cuil);
            if (storedHash == null) {
                return false; // Usuario no encontrado
            }

            // PasswordUtils.validatePassword wipea el char[] al terminar; clonamos
            // antes para poder re-hashear sin pedirle de nuevo el password al usuario.
            passwordCopy = passwordCharArray.clone();
            boolean passwordMatches = PasswordUtils.validatePassword(passwordCharArray, storedHash);

            if (passwordMatches && PasswordUtils.needsRehash(storedHash)) {
                try {
                    String newHash = PasswordUtils.hashPassword(passwordCopy);
                    passwordCopy = null; // hashPassword también limpió el clon
                    usuarioService.updatePasswordHash(cuil, newHash);
                } catch (ServiceException e) {
                    // Migración silenciosa: si falla la persistencia del nuevo hash,
                    // el login es igual exitoso. Reintentaremos en el próximo login.
                    System.err.println("Falla en el re-hash transparente para CUIL " + cuil
                            + ": " + e.getMessage());
                }
            }

            return passwordMatches;

        } catch (ServiceException e) {
            throw new RuntimeException("Error al validar el usuario contra la base de datos", e);
        } finally {
            // Garantizar que ambos arrays queden limpios incluso ante errores
            if (passwordCharArray != null) {
                Arrays.fill(passwordCharArray, '\0');
            }
            if (passwordCopy != null) {
                Arrays.fill(passwordCopy, '\0');
            }
        }
    }

    public Usuario getUsuarioByCuil(long cuil) throws ServiceException {
        return usuarioService.findUsuarioByCuil(cuil);
    }

}
