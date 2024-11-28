package ar.com.hmu.repository;

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.factory.UsuarioFactory;
import ar.com.hmu.model.*;
import ar.com.hmu.repository.dao.GenericDAO;
import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Clase encargada de las operaciones relacionadas con la entidad Usuario en la base de datos.
 */
public class UsuarioRepository implements GenericDAO<Usuario> {

    private DatabaseConnector databaseConnector;
    private UsuarioFactory usuarioFactory;

    public UsuarioRepository(DatabaseConnector databaseConnector, UsuarioFactory usuarioFactory) {
        this.databaseConnector = databaseConnector;
        this.usuarioFactory = usuarioFactory;
    }

    @Override
    public void create(Usuario usuario) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String query = "INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, passwd, domicilioID, cargoID, servicioID) " +
                    "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, UUID_TO_BIN(?), UUID_TO_BIN(?), UUID_TO_BIN(?))";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {

                // Almacenar datos comunes de usuario
                stmt.setString(1, usuario.getId().toString());
                stmt.setDate(2, Date.valueOf(usuario.getFechaAlta()));
                stmt.setBoolean(3, usuario.getEstado());
                stmt.setLong(4, usuario.getCuil());
                stmt.setString(5, usuario.getApellidos());
                stmt.setString(6, usuario.getNombres());
                stmt.setString(7, usuario.getSexo().name());
                stmt.setString(8, usuario.getMail());
                stmt.setString(9, usuario.getEncryptedPassword());
                stmt.setString(10, usuario.getDomicilioId().toString());
                stmt.setString(11, usuario.getCargoId().toString());
                stmt.setString(12, usuario.getServicioId().toString());
                stmt.executeUpdate();

            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    @Override
    public Usuario readByUUID(UUID id) throws SQLException {
        String query = "SELECT BIN_TO_UUID(id) AS id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                "BIN_TO_UUID(domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(cargoID) AS cargoID, " +
                "BIN_TO_UUID(servicioID) AS servicioID, " +
                "tipoUsuario, passwd, profile_image " +
                "FROM Usuario WHERE estado = 1 AND id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Fabrica de usuarios
                    Usuario usuario = usuarioFactory.createUsuario(rs);

                    return usuario;
                }
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el usuario por UUID", e);
        }
        return null;
    }

    @Override
    public List<Usuario> readAll() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT BIN_TO_UUID(id) AS id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                "BIN_TO_UUID(domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(cargoID) AS cargoID, " +
                "BIN_TO_UUID(servicioID) AS servicioID, " +
                "tipoUsuario, passwd, profile_image " +
                "FROM Usuario WHERE estado = 1 ORDER BY apellidos ASC";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Fabrica de usuarios
                Usuario usuario = usuarioFactory.createUsuario(rs);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer todos los usuarios", e);
        } catch (ServiceException e) {
            throw new RuntimeException(e);
        }
        return usuarios;
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        String query = "UPDATE Usuario SET fechaAlta = ?, estado = ?, cuil = ?, apellidos = ?, nombres = ?, sexo = ?, mail = ?, tel = ?, " +
                "domicilioID = UUID_TO_BIN(?), cargoID = UUID_TO_BIN(?), servicioID = UUID_TO_BIN(?), passwd = ?, profile_image = ? " +
                "WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, usuario.getFechaAlta() != null ? java.sql.Date.valueOf(usuario.getFechaAlta()) : java.sql.Date.valueOf(java.time.LocalDate.now()));
            stmt.setBoolean(2, usuario.getEstado());
            stmt.setLong(3, usuario.getCuil());
            stmt.setString(4, usuario.getApellidos());
            stmt.setString(5, usuario.getNombres());
            stmt.setString(6, usuario.getSexo().name());
            stmt.setString(7, usuario.getMail());
            stmt.setObject(8, usuario.getTel() != 0 ? usuario.getTel() : null, Types.BIGINT);
            stmt.setString(9, usuario.getDomicilioId() != null ? usuario.getDomicilioId().toString() : null);
            stmt.setString(10, usuario.getCargoId() != null ? usuario.getCargoId().toString() : null);
            stmt.setString(11, usuario.getServicioId() != null ? usuario.getServicioId().toString() : null);
            stmt.setString(12, usuario.getEncryptedPassword());
            stmt.setBytes(13, usuario.getProfileImage() != null && usuario.getProfileImage().length > 0
                    ? usuario.getProfileImage()
                    : null);
            stmt.setString(14, usuario.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void delete(Usuario usuario) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String query = "DELETE FROM Usuario WHERE id = UUID_TO_BIN(?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, usuario.getId().toString());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar el usuario", e);
        }
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
    public Usuario findUsuarioByCuil(long cuil) throws SQLException {
        String query = "SELECT BIN_TO_UUID(id) AS id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                "BIN_TO_UUID(domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(cargoID) AS cargoID, " +
                "BIN_TO_UUID(servicioID) AS servicioID, " +
                "passwd, profile_image " +
                "FROM Usuario WHERE estado = 1 AND cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = usuarioFactory.createUsuario(rs);
                    return usuario;
                }
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public Usuario findUsuarioByCuil(long cuil, boolean includeDisabled) throws SQLException, ServiceException {
        if(includeDisabled) {
            String query = "SELECT BIN_TO_UUID(id) AS id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                    "BIN_TO_UUID(domicilioID) AS domicilioID, " +
                    "BIN_TO_UUID(cargoID) AS cargoID, " +
                    "BIN_TO_UUID(servicioID) AS servicioID, " +
                    "tipoUsuario, passwd, profile_image " +
                    "FROM Usuario WHERE cuil = ?";
            try (Connection connection = databaseConnector.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setLong(1, cuil);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        Usuario usuario = usuarioFactory.createUsuario(rs);
                        return usuario;
                    }
                }
            }
            return null;
        } else {
            findUsuarioByCuil(cuil);
        }
        return null;
    }


    /**
     * Devuelve el UUID de un usuario en base a su CUIL
     * @param cuil Número de CUIL
     * @return el UUID del usuario buscado
     * @throws SQLException
     */
    public UUID findUUIDByCuil(long cuil) throws SQLException {
        String query = "SELECT BIN_TO_UUID(id) AS id FROM Usuario WHERE estado = 1 AND cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Leer el UUID como String y convertirlo a UUID
                    String uuidString = rs.getString("id");
                    return UUID.fromString(uuidString);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar UUID por CUIL", e);
        }
        return null;
    }

    /**
     * Busca la contraseña hasheada de un usuario por su CUIL.
     *
     * @param cuil el CUIL del usuario.
     * @return la contraseña hasheada del usuario o null si no se encuentra.
     * @throws SQLException si ocurre un error durante la consulta.
     */
    public String findPasswordByCuil(long cuil) throws SQLException {
        String query = "SELECT passwd FROM Usuario WHERE estado = 1 AND cuil = ?";
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

    public void updatePassword(long cuil, String hashedPassword) throws SQLException {
        String query = "UPDATE Usuario SET passwd = ? WHERE cuil = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, hashedPassword);
            stmt.setLong(2, cuil);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No se pudo actualizar la contraseña; el usuario con CUIL " + cuil + " no existe.\nHash: " + hashedPassword);
            }
        }
    }

    public void updateProfileImage(long cuil, byte[] imageBytes) throws SQLException {
        String query = "UPDATE Usuario SET profile_image = ? WHERE cuil = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setBytes(1, imageBytes);
            stmt.setLong(2, cuil);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No se pudo actualizar la imagen de perfil; el usuario con CUIL " + cuil + " no existe.");
            }
        }
    }


    public int countUsuarios() {
        String query = "SELECT COUNT(*) AS total FROM Usuario WHERE estado = 1";

        try (Connection connection = databaseConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al contar los usuarios", e);
        }

        return 0; // Retornar 0 si no hay registros (BD vacía por ejemplo)
    }


    public int countUsuarios(boolean includeDisabled) {
        if(includeDisabled) {
            String query = "SELECT COUNT(*) AS total FROM Usuario WHERE estado = 1";

            try (Connection connection = databaseConnector.getConnection();
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                if (rs.next()) {
                    return rs.getInt("total");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error al contar los usuarios", e);
            }

            return 0; // Retornar 0 si no hay registros (BD vacía por ejemplo)
        } else {
            countUsuarios();
        }
        return 0;
    }

    public int countUsuariosByServicio(UUID servicioId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Usuario WHERE servicioID = UUID_TO_BIN(?) AND estado = 1";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, servicioId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public String findJefeByServicio(UUID servicioId) throws SQLException {
        String query = "SELECT CONCAT(apellidos, ', ', nombres) AS Jefe FROM Usuario WHERE servicioID = UUID_TO_BIN(?) AND tipoUsuario = ? AND estado = 1 LIMIT 1";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, servicioId.toString());
            stmt.setString(2, TipoUsuario.JEFEDESERVICIO.getInternalName());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Jefe");
                }
            }
        }
        return "";
    }


}
