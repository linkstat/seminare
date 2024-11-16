package ar.com.hmu.repository;

import ar.com.hmu.constants.NombreServicio;
import ar.com.hmu.constants.TipoUsuario;
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
    private final DomicilioRepository domicilioRepository;
    private final CargoRepository cargoRepository;
    private final ServicioRepository servicioRepository;

    public UsuarioRepository(DatabaseConnector databaseConnector, DomicilioRepository domicilioRepository, CargoRepository cargoRepository, ServicioRepository servicioRepository) {
        this.databaseConnector = databaseConnector;
        this.domicilioRepository = domicilioRepository;
        this.cargoRepository = cargoRepository;
        this.servicioRepository = servicioRepository;
    }

    @Override
    public void create(Usuario usuario) {
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
    public Usuario readByUUID(UUID id) {
        String query = "SELECT *, BIN_TO_UUID(id) AS id FROM Usuario WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return UsuarioFactory.createUsuario(rs);  // UsuarioFactory maneja los valores opcionales
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el usuario por UUID", e);
        }
        return null;
    }

    @Override
    public List<Usuario> readAll() {
        List<Usuario> usuarios = new ArrayList<>();
        String query = "SELECT *, BIN_TO_UUID(id) AS id FROM Usuario";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Usuario usuario = UsuarioFactory.createUsuario(rs);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer todos los usuarios", e);
        }
        return usuarios;
    }

    @Override
    public void update(Usuario usuario) {
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
    public void delete(Usuario usuario) {
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
        String query = "SELECT BIN_TO_UUID(id) AS id, " +
                "fechaAlta, estado, cuil, apellidos, nombres, " +
                "sexo, mail, tel, " +
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
                    return UsuarioFactory.createUsuario(rs); // Delegación a UsuarioFactory
                }
            }
        }
        return null;
    }


    // Cargar datos completos de Empleado, incluyendo servicioID
    private Empleado loadEmpleadoByUUID(UUID id) throws SQLException {
        String query = "SELECT BIN_TO_UUID(u.id) AS id, " +
                "u.fechaAlta, u.cuil, u.apellidos, u.nombres, " +
                "u.sexo, u.estado, u.mail, u.tel, u.tipoUsuario, " +
                "BIN_TO_UUID(u.domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(u.cargoID) AS cargoID, " +
                "u.passwd, u.profile_image, " +
                "e.francosCompensatoriosUtilizados, BIN_TO_UUID(e.horarioActualID), " +
                "BIN_TO_UUID(e.jefaturaID), BIN_TO_UUID(e.servicioID) " +
                "FROM Usuario u JOIN Empleado e ON u.id = e.id " +
                "WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empleado empleado = (Empleado) UsuarioFactory.createUsuario(rs);

                    // Cargar el servicio con el ID y asignarlo al empleado
                    UUID servicioId = UUID.fromString(rs.getString("servicioID"));
                    empleado.setServicio(servicioRepository.readByUUID(servicioId));

                    return empleado;
                }
            }
        }
        return null;
    }

    // Cargar datos completos de JefaturaDeServicio, incluyendo servicioID
    private JefaturaDeServicio loadJefaturaDeServicioByUUID(UUID id) throws SQLException {
        String query = "SELECT BIN_TO_UUID(u.id) AS id, " +
                "u.fechaAlta, u.cuil, u.apellidos, u.nombres, " +
                "u.sexo, u.estado, u.mail, u.tel, u.tipoUsuario, " +
                "BIN_TO_UUID(u.domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(u.cargoID) AS cargoID, " +
                "u.passwd, u.profile_image, j.servcioID " +
                "FROM Usuario u JOIN JefaturaDeServicio j ON u.id = j.id " +
                "WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    JefaturaDeServicio jefaturaDeServicio = (JefaturaDeServicio) UsuarioFactory.createUsuario(rs);

                    // Cargar el servicio con el ID y asignarlo a jefaturaDeServicio
                    UUID servicioId = UUID.fromString(rs.getString("servicioID"));
                    jefaturaDeServicio.setServicio(servicioRepository.readByUUID(servicioId));

                    return jefaturaDeServicio;

                }
            }
        }
        return null;
    }

    // Cargar datos completos de OficinaDePersonal, incluyendo el servicioID correspondiente a Oficina de Personal
    private OficinaDePersonal loadOficinaDePersonalByUUID(UUID id) throws SQLException {
        // Consulta con BIN_TO_UUID para obtener el UUID como un String legible
        String query = "SELECT BIN_TO_UUID(u.id) AS id, " +
                "u.fechaAlta, u.cuil, u.apellidos, u.nombres, " +
                "u.sexo, u.estado, u.mail, u.tel, u.tipoUsuario, " +
                "BIN_TO_UUID(u.domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(u.cargoID) AS cargoID, " +
                "u.passwd, u.profile_image, o.reportesGenerados " +
                "FROM Usuario u JOIN OficinaDePersonal o ON u.id = o.id " +
                "WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString()); // Pasamos el UUID como String
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OficinaDePersonal oficinaDePersonal = (OficinaDePersonal) UsuarioFactory.createUsuario(rs);

                    // Obtener el ID de "Oficina de Personal" desde el repositorio de Servicio o definir un ID constante, o un método que devuelva el ID directamente
                    UUID servicioOficinaPersonalId = servicioRepository.findIdByName(NombreServicio.OFICINA_DE_PERSONAL);
                    oficinaDePersonal.setServicioId(servicioOficinaPersonalId);

                    return oficinaDePersonal;
                }
            }
        }
        return null;
    }

    // Cargar datos completos de Direccion, incluyendo el servicioID correspondiente a Dirección
    private Direccion loadDireccionByUUID(UUID id) throws SQLException {
        String query = "SELECT BIN_TO_UUID(u.id) AS id, " +
                "u.fechaAlta, u.cuil, u.apellidos, u.nombres, " +
                "u.sexo, u.estado, u.mail, u.tel, u.tipoUsuario, " +
                "BIN_TO_UUID(u.domicilioID) AS domicilioID, " +
                "BIN_TO_UUID(u.cargoID) AS cargoID, " +
                "u.passwd, u.profile_image " +
                "FROM Usuario u JOIN Direccion d ON u.id = d.id " +
                "WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Direccion direccion = (Direccion) UsuarioFactory.createUsuario(rs);

                    // Obtener el ID de "Dirección" desde el repositorio de Servicio o definir un ID constante, o un método que devuelva el ID directamente
                    UUID servicioDireccionId = servicioRepository.findIdByName(NombreServicio.DIRECCION);
                    direccion.setServicioId(servicioDireccionId);

                    return direccion;
                }
            }
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
        String query = "SELECT BIN_TO_UUID(id) AS id FROM Usuario WHERE cuil = ?";
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
        String query = "SELECT COUNT(*) AS total FROM Usuario";

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


}
