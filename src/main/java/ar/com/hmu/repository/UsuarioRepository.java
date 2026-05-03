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
        // Class Table Inheritance: todo Usuario del HMU es también Empleado
        // (convención del seed). Se inserta en ambas tablas en una transacción.
        Connection connection = null;
        try {
            connection = databaseConnector.getConnection();
            connection.setAutoCommit(false);

            String insertUsuario = "INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, passwd, domicilioID, cargoID, servicioID) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(insertUsuario)) {
                stmt.setObject(1, usuario.getId());
                stmt.setDate(2, Date.valueOf(usuario.getFechaAlta()));
                stmt.setBoolean(3, usuario.getEstado());
                stmt.setLong(4, usuario.getCuil());
                stmt.setString(5, usuario.getApellidos());
                stmt.setString(6, usuario.getNombres());
                stmt.setObject(7, usuario.getSexo().name(), Types.OTHER);
                stmt.setString(8, usuario.getMail());
                stmt.setObject(9, usuario.getTel() != 0 ? usuario.getTel() : null, Types.BIGINT);
                stmt.setString(10, usuario.getEncryptedPassword());
                stmt.setObject(11, usuario.getDomicilioId());
                stmt.setObject(12, usuario.getCargoId());
                stmt.setObject(13, usuario.getServicioId());
                stmt.executeUpdate();
            }

            String insertEmpleado = "INSERT INTO Empleado (id, francosCompensatoriosUtilizados, horarioActualID) VALUES (?, NULL, NULL)";
            try (PreparedStatement stmt = connection.prepareStatement(insertEmpleado)) {
                stmt.setObject(1, usuario.getId());
                stmt.executeUpdate();
            }

            connection.commit();
        } catch (SQLException e) {
            if (connection != null) {
                try { connection.rollback(); } catch (SQLException ignored) {}
            }
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario", e);
        } finally {
            if (connection != null) {
                try { connection.setAutoCommit(true); connection.close(); } catch (SQLException ignored) {}
            }
        }
    }

    @Override
    public Usuario readByUUID(UUID id) throws SQLException {
        String query = "SELECT id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                "domicilioID, cargoID, servicioID, passwd, profile_image " +
                "FROM Usuario WHERE estado = TRUE AND id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return usuarioFactory.createUsuario(rs);
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
        String query = "SELECT id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                "domicilioID, cargoID, servicioID, passwd, profile_image " +
                "FROM Usuario WHERE estado = TRUE";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                usuarios.add(usuarioFactory.createUsuario(rs));
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
                "domicilioID = ?, cargoID = ?, servicioID = ?, passwd = ?, profile_image = ? " +
                "WHERE id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setDate(1, usuario.getFechaAlta() != null ? java.sql.Date.valueOf(usuario.getFechaAlta()) : java.sql.Date.valueOf(java.time.LocalDate.now()));
            stmt.setBoolean(2, usuario.getEstado());
            stmt.setLong(3, usuario.getCuil());
            stmt.setString(4, usuario.getApellidos());
            stmt.setString(5, usuario.getNombres());
            stmt.setObject(6, usuario.getSexo().name(), Types.OTHER);
            stmt.setString(7, usuario.getMail());
            stmt.setObject(8, usuario.getTel() != 0 ? usuario.getTel() : null, Types.BIGINT);
            stmt.setObject(9, usuario.getDomicilioId());
            stmt.setObject(10, usuario.getCargoId());
            stmt.setObject(11, usuario.getServicioId());
            stmt.setString(12, usuario.getEncryptedPassword());
            stmt.setBytes(13, usuario.getProfileImage() != null && usuario.getProfileImage().length > 0
                    ? usuario.getProfileImage()
                    : null);
            stmt.setObject(14, usuario.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void delete(Usuario usuario) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String query = "DELETE FROM Usuario WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setObject(1, usuario.getId());
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
        String query = "SELECT id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                "domicilioID, cargoID, servicioID, passwd, profile_image " +
                "FROM Usuario WHERE estado = TRUE AND cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return usuarioFactory.createUsuario(rs);
                }
            } catch (ServiceException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }


    public Usuario findUsuarioByCuil(long cuil, boolean includeDisabled) throws SQLException, ServiceException {
        if (includeDisabled) {
            String query = "SELECT id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, tel, " +
                    "domicilioID, cargoID, servicioID, passwd, profile_image " +
                    "FROM Usuario WHERE cuil = ?";
            try (Connection connection = databaseConnector.getConnection();
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setLong(1, cuil);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        return usuarioFactory.createUsuario(rs);
                    }
                }
            }
            return null;
        } else {
            return findUsuarioByCuil(cuil);
        }
    }


    /**
     * Devuelve el UUID de un usuario en base a su CUIL
     * @param cuil Número de CUIL
     * @return el UUID del usuario buscado
     * @throws SQLException
     */
    public UUID findUUIDByCuil(long cuil) throws SQLException {
        String query = "SELECT id FROM Usuario WHERE estado = TRUE AND cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject("id", UUID.class);
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
        String query = "SELECT passwd FROM Usuario WHERE estado = TRUE AND cuil = ?";
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
        String query = "SELECT COUNT(*) AS total FROM Usuario WHERE estado = TRUE";

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

        return 0;
    }


    public int countUsuarios(boolean includeDisabled) {
        String query = includeDisabled
                ? "SELECT COUNT(*) AS total FROM Usuario"
                : "SELECT COUNT(*) AS total FROM Usuario WHERE estado = TRUE";

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

        return 0;
    }

    public int countUsuariosByServicio(UUID servicioId) throws SQLException {
        String query = "SELECT COUNT(*) FROM Usuario WHERE servicioID = ? AND estado = TRUE";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, servicioId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public String findJefeByServicio(UUID servicioId) throws SQLException {
        // El "tipo" del usuario ya no se discrimina por columna en Usuario;
        // se resuelve uniendo contra Usuario_Rol + Rol y filtrando por nombre.
        String query = "SELECT u.apellidos || ', ' || u.nombres AS Jefe " +
                "FROM Usuario u " +
                "JOIN Usuario_Rol ur ON u.id = ur.usuario_id " +
                "JOIN Rol r ON ur.rol_id = r.id " +
                "WHERE u.servicioID = ? AND r.nombre = ? AND u.estado = TRUE " +
                "LIMIT 1";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, servicioId);
            stmt.setString(2, TipoUsuario.JEFATURADESERVICIO.getInternalName());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Jefe");
                }
            }
        }
        return "";
    }


}
