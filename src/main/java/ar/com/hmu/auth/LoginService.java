package ar.com.hmu.auth;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.SQLException;
import java.util.Arrays;

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
     * @param passwordCharArray la contraseña ingresada por el usuario, almacenada como char[].
     * @return true si las credenciales son válidas, false de lo contrario.
     * @throws SQLException si ocurre un error con la conexión a la base de datos.
     */
    public boolean validateUser(long cuil, char[] passwordCharArray) throws SQLException {
        String rawPassword = null;
        try {
            String storedHash = usuarioRepository.findPasswordByCuil(cuil);

            if (storedHash == null) {
                return false; // Usuario no encontrado
            }

            // Convertir el char[] en String y verificar contraseña con BCrypt.checkpw
            rawPassword = new String(passwordCharArray);
            boolean passwordMatches = BCrypt.checkpw(rawPassword, storedHash);
            // Limpiar el String y el char[] para evitar que la contraseña en crudo permanezca en la memoria...
            // (este es un riesgo controlado, ya que no es posible limpiar completamente el String)
            rawPassword = null;
            Arrays.fill(passwordCharArray, '\0');

            return passwordMatches;

        } catch (SQLException e) {
            throw new SQLException("Error al conectar con la base de datos para validar el usuario", e);
        } finally {
            // Limpiar el char[] para evitar que la contraseña quede en la memoria
            if (passwordCharArray != null) {
                Arrays.fill(passwordCharArray, '\0');
            }

            // Limpiar el String para evitar que la contraseña en crudo permanezca en la memoria
            rawPassword = null; // El garbage collector eventualmente lo eliminará
        }

    }

    public Usuario getUsuarioByCuil(long cuil) throws SQLException {
        return usuarioRepository.findUsuarioByCuil(cuil);
    }


}
