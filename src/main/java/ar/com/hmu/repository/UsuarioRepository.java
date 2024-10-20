package ar.com.hmu.repository;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.DatabaseConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Clase encargada de las operaciones relacionadas con la entidad Usuario en la base de datos.
 */
public class UsuarioRepository {

    private DatabaseConnector databaseConnector;

    public UsuarioRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Busca la contraseña hasheada de un usuario por su CUIL.
     *
     * @param cuil el CUIL del usuario.
     * @return la contraseña hasheada del usuario o null si no se encuentra.
     * @throws SQLException si ocurre un error durante la consulta.
     */
    public String findPasswordByCuil(long cuil) throws SQLException {
        String query = "SELECT passwd FROM Usuario WHERE cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("passwd");
                }
            }
        }
        return null;
    }
}
