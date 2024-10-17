package ar.com.hmu.auth;

import ar.com.hmu.repository.DatabaseConnector;
import org.springframework.security.crypto.bcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
/*
 * Gestiona la lógica de negocio relacionada con el inicio de sesión,
 * incluyendo la validación de las credenciales del usuario.
 * Esta clase no representa un modelo de datos, ni es una clase de configuración, ni es un controlador de interfaz de usuario.
 * Más bien, su propósito es encapsular la lógica central de la aplicación.
 * Por tanto, tiene más sentido que se coloque en un paquete destinado específicamente a autenticación y autorización.
 * Eventualmente tendremos múltiples servicios relacionados con la seguridad.
 */
public class LoginService {

    private DatabaseConnector databaseConnector;

    public LoginService(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public boolean validateUser(long cuil, String password) {
        String query = "SELECT passwd FROM Usuario WHERE cuil = ?";
        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("passwd");
                // Validar la contraseña con BCrypt
                return BCrypt.checkpw(password, storedHash);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
