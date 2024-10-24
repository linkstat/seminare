package ar.com.hmu.auth;

import ar.com.hmu.model.Usuario;
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
     * @throws SQLException si ocurre un error con la conexión a la base de datos.
     */
    public boolean validateUser(long cuil, String password) throws SQLException {
        try {
            String storedHash = usuarioRepository.findPasswordByCuil(cuil);

            if (storedHash == null) {
                return false; // Usuario no encontrado
            }

            // Validar la contraseña con BCrypt
            return BCrypt.checkpw(password, storedHash);
        } catch (SQLException e) {
            throw new SQLException("Error al conectar con la base de datos para validar el usuario", e);
        }
    }

    public Usuario getUsuarioByCuil(long cuil) throws SQLException {
        return usuarioRepository.findByCuil(cuil);
    }


}
