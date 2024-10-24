package ar.com.hmu.repository;

import ar.com.hmu.factory.UsuarioFactory;
import ar.com.hmu.model.*;
import ar.com.hmu.repository.dao.GenericDAO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Clase encargada de las operaciones relacionadas con la entidad Usuario en la base de datos.
 */
public class UsuarioRepository implements GenericDAO<Usuario> {

    private DatabaseConnector databaseConnector;

    public UsuarioRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void create(Usuario usuario) {
        try (Connection connection = databaseConnector.getConnection()) {
            String query = "INSERT INTO Usuario (cuil, nombre, mail, passwd) VALUES (?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setLong(1, usuario.getCuil());
                statement.setString(2, usuario.getApellidos());
                statement.setString(2, usuario.getNombres());
                statement.setString(3, usuario.getMail());
                statement.setString(4, usuario.getEncryptedPassword());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    @Override
    public Usuario readByUUID(UUID id) {
        // Implementación para buscar un usuario por ID
        return null;
    }

    @Override
    public List<Usuario> readAll() {
        // Implementación para leer todos los usuarios
        return null;
    }

    @Override
    public void update(Usuario usuario) {
        // Implementación para actualizar un usuario
    }

    @Override
    public void delete(Usuario usuario) {
        // Implementación para eliminar un usuario
    }

    // Métodos específicos para Usuario

    // Implementación del método findByCuil específico
    public Usuario findByCuil(long cuil) throws SQLException {
        String query = "SELECT * FROM Usuario WHERE cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, cuil);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return UsuarioFactory.createUsuario(resultSet);
                } else {
                    return null; // No se encontró el usuario
                }
            }
        }
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
