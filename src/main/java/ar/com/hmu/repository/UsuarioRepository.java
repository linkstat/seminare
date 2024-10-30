package ar.com.hmu.repository;

import ar.com.hmu.factory.UsuarioFactory;
import ar.com.hmu.model.*;
import ar.com.hmu.repository.dao.GenericDAO;
import java.sql.*;
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
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setLong(1, usuario.getCuil());
                stmt.setString(2, usuario.getApellidos());
                stmt.setString(2, usuario.getNombres());
                stmt.setString(3, usuario.getMail());
                stmt.setString(4, usuario.getEncryptedPassword());
                stmt.executeUpdate();
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

    /**
     * Recupera un usuario de la base de datos basado en el CUIL proporcionado.
     * Además, carga la información de cargo asociada al usuario si está disponible.
     *
     * @param cuil El número de CUIL del usuario.
     * @return El objeto Usuario si se encuentra en la base de datos, o null si no existe.
     * @throws SQLException En caso de errores durante la consulta SQL.
     */
    public Usuario getUsuarioByCuil(long cuil) throws SQLException {
        Usuario usuario = null;
        String query = "SELECT *, BIN_TO_UUID2(cargoID) AS cargoUUID FROM Usuario WHERE cuil = ?";

        try (Connection conn = databaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Primero, crear el usuario utilizando UsuarioFactory
                    usuario = UsuarioFactory.createUsuario(rs);

                    // Luego, cargar el Cargo asociado si existe
                    String cargoIdString = rs.getString("cargoUUID");
                    if (cargoIdString != null && !cargoIdString.isEmpty()) {
                        UUID cargoId = UUID.fromString(cargoIdString);
                        Cargo cargo = getCargoById(cargoId);
                        if (cargo != null) {
                            usuario.setCargo(cargo);
                        }
                    }

                    return usuario; // Retornar el objeto Usuario cargado completamente.
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

    public Cargo getCargoById(UUID cargoId) throws SQLException {
        String query = "SELECT * FROM Cargo WHERE id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, cargoId.toString()); // Pasamos el UUID como un String para la consulta

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int numero = rs.getInt("numero");
                    String descripcion = rs.getString("descripcion");
                    String agrupacionStr = rs.getString("agrupacion");

                    // Convertir el valor de agrupación en el Enum correspondiente
                    Agrupacion agrupacion = Agrupacion.valueOf(agrupacionStr);

                    // Crear y retornar un objeto Cargo
                    return new Cargo(cargoId, numero, descripcion, agrupacion);
                } else {
                    return null; // No se encontró el cargo
                }
            }
        }
    }

}
