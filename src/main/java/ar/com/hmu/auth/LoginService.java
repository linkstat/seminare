package ar.com.hmu.auth;

import ar.com.hmu.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.SQLException;

/**
 * Servicio que gestiona la lógica de negocio relacionada con el inicio de sesión de usuarios.
 */
public class LoginService {

    private UsuarioRepository usuarioRepository;

    /**
     * Constructor que inicializa el `LoginService` con un objeto {@link UsuarioRepository}.
     *
     * @param usuarioRepository el repositorio de usuarios.
     */
    public LoginService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Valida las credenciales de un usuario.
     *
     * @param cuil el CUIL del usuario que intenta iniciar sesión.
     * @param password la contraseña ingresada por el usuario.
     * @return true si las credenciales son válidas, false de lo contrario.
     */
    public boolean validateUser(long cuil, String password) {
        try {
            String storedHash = usuarioRepository.findPasswordByCuil(cuil);
            if (storedHash != null) {
                // Validar la contraseña con BCrypt
                return BCrypt.checkpw(password, storedHash);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
