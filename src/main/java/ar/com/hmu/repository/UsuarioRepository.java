package ar.com.hmu.repository;

import ar.com.hmu.factory.UsuarioFactory;
import ar.com.hmu.model.*;
import ar.com.hmu.repository.dao.GenericDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Clase encargada de las operaciones relacionadas con la entidad Usuario en la base de datos.
 */
public class UsuarioRepository implements GenericDAO<Usuario> {

    private DatabaseConnector databaseConnector;
    private RolRepository rolRepository;

    public UsuarioRepository(DatabaseConnector databaseConnector, RolRepository rolRepository) {
        this.databaseConnector = databaseConnector;
        this.rolRepository = rolRepository;
    }

    @Override
    public void create(Usuario usuario) throws SQLException {
        try (Connection connection = databaseConnector.getConnection()) {
            String query = "INSERT INTO Usuario (id, fechaAlta, estado, cuil, apellidos, nombres, sexo, mail, passwd, tipoUsuario) " +
                    "VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, usuario.getId().toString());
                stmt.setDate(2, Date.valueOf(usuario.getFechaAlta()));
                stmt.setBoolean(3, usuario.getEstado());
                stmt.setLong(4, usuario.getCuil());
                stmt.setString(5, usuario.getApellidos());
                stmt.setString(6, usuario.getNombres());
                stmt.setString(7, usuario.getSexo().name());
                stmt.setString(8, usuario.getMail());
                stmt.setString(9, usuario.getEncryptedPassword());
                stmt.setString(10, usuario.getTipoUsuario().getInternalName());
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
                    Usuario usuario = UsuarioFactory.createUsuario(rs);

                    // Asignación de roles
                    usuario.setRoles(findRolesByUsuarioId(usuario.getId()));

                    return usuario;
                }
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
                "FROM Usuario WHERE estado = 1";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Fabrica de usuarios
                Usuario usuario = UsuarioFactory.createUsuario(rs);

                // Asignación de roles
                usuario.setRoles(findRolesByUsuarioId(usuario.getId()));

                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer todos los usuarios", e);
        }
        return usuarios;
    }

    @Override
    public void update(Usuario usuario) throws SQLException {
        String query = "UPDATE Usuario SET fechaAlta = ?, estado = ?, cuil = ?, apellidos = ?, nombres = ?, sexo = ?, mail = ?, tel = ?, " +
                "domicilioID = UUID_TO_BIN(?), cargoID = UUID_TO_BIN(?), servicioID = UUID_TO_BIN(?), tipoUsuario = ?, passwd = ?, profile_image = ? " +
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
            stmt.setString(12, usuario.getTipoUsuario().getInternalName());
            stmt.setString(13, usuario.getEncryptedPassword());
            stmt.setBytes(14, usuario.getProfileImage() != null && usuario.getProfileImage().length > 0
                    ? usuario.getProfileImage()
                    : null);

            stmt.setString(15, usuario.getId().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void delete(Usuario usuario) throws SQLException {
        String query = "DELETE FROM Usuario WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, usuario.getId().toString());
            stmt.executeUpdate();
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
                "tipoUsuario, passwd, profile_image " +
                "FROM Usuario WHERE estado = 1 AND cuil = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setLong(1, cuil);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = UsuarioFactory.createUsuario(rs);
                    usuario.setRoles(findRolesByUsuarioId(usuario.getId()));  // Asignación de roles aquí
                    return usuario;
                }
            }
        }
        return null;
    }


    public Usuario findUsuarioByCuil(long cuil, boolean includeDisabled) throws SQLException {
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
                        Usuario usuario = UsuarioFactory.createUsuario(rs);
                        usuario.setRoles(findRolesByUsuarioId(usuario.getId()));  // Asignación de roles aquí
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


    public List<Rol> findRolesByUsuarioId(UUID usuarioId) throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String query = "SELECT r.*, BIN_TO_UUID(r.id) AS id_str FROM Rol r " +
                "JOIN Usuario_Rol ur ON r.id = ur.rol_id " +
                "WHERE ur.usuario_id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuarioId.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Rol rol = new Rol();
                    rol.setId(UUID.fromString(rs.getString("id_str")));
                    rol.setNombre(rs.getString("nombre"));
                    rol.setDescripcion(rs.getString("descripcion"));
                    roles.add(rol);
                }
            }
        }

        return roles;
    }

    // Métodos CRUD adicionales, incluyendo asignar y revocar roles
    public void asignarRol(UUID usuarioId, UUID rolId) throws SQLException {
        String query = "INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (BIN_TO_UUID(?), BIN_TO_UUID(?))";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuarioId.toString());
            stmt.setString(2, rolId.toString());
            stmt.executeUpdate();
        }
    }

    public void revocarRol(UUID usuarioId, UUID rolId) throws SQLException {
        String query = "DELETE FROM Usuario_Rol WHERE usuario_id = BIN_TO_UUID(?) AND rol_id = BIN_TO_UUID(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuarioId.toString());
            stmt.setString(2, rolId.toString());
            stmt.executeUpdate();
        }
    }

}
