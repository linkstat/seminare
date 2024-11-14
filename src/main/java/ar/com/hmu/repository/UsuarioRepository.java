package ar.com.hmu.repository;

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
            String query = "INSERT INTO Usuario (id, cuil, apellidos, nombres, mail, passwd) VALUES (UUID_TO_BIN(?), ?, ?, ?, ?, ?)";
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, usuario.getId().toString());
                stmt.setLong(2, usuario.getCuil());
                stmt.setString(3, usuario.getApellidos());
                stmt.setString(4, usuario.getNombres());
                stmt.setString(5, usuario.getMail());
                stmt.setString(6, usuario.getEncryptedPassword());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    @Override
    public Usuario readByUUID(UUID id) {
        String query = "SELECT *, BIN_TO_UUID2(id) AS id FROM Usuario WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return UsuarioFactory.createUsuario(rs);
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
        String query = "SELECT *, BIN_TO_UUID2(id) AS id FROM Usuario";

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
    public Usuario findUsuarioByCuil(long cuil) throws SQLException {
        // Primero obtenemos el UUID usando el CUIL
        UUID uuid = findUUIDByCuil(cuil);
        if (uuid == null) {
            return null; // Si no se encuentra UUID para el CUIL, retornar null
        }

        Usuario usuario = null;
        TipoUsuario tipoUsuario = null;

        // Ahora obtenemos el tipo de usuario basado en el UUID
        String query = "SELECT tipoUsuario FROM Usuario WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, uuid.toString()); // Usamos el UUID obtenido
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tipoUsuario = TipoUsuario.valueOf(rs.getString("tipoUsuario"));
                }
            }
        }

        // Determinar el tipo de usuario y cargar la información correspondiente
        if (tipoUsuario != null) {
            switch (tipoUsuario) {
                case EMPLEADO:
                    usuario = loadEmpleadoByUUID(uuid);
                    break;
                case JEFATURA_DE_SERVICIO:
                    usuario = loadJefaturaDeServicioByUUID(uuid);
                    break;
                case OFICINA_DE_PERSONAL:
                    usuario = loadOficinaDePersonalByUUID(uuid);
                    break;
                case DIRECCION:
                    usuario = loadDireccionByUUID(uuid);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipoUsuario);
            }
        }
        return usuario;
    }

    // Cargar datos completos de Empleado, incluyendo servicioID
    private Empleado loadEmpleadoByUUID(UUID id) throws SQLException {
        String query = "SELECT u.*, e.servicioID FROM Usuario u "
                + "JOIN Empleado e ON u.id = e.id WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Empleado empleado = (Empleado) UsuarioFactory.createUsuario(rs);
                    UUID servicioId = UUID.fromString(rs.getString("servicioID"));
                    // Cargar el servicio con el ID y asignarlo al empleado
                    empleado.setServicio(servicioRepository.readByUUID(servicioId));
                    return empleado;
                }
            }
        }
        return null;
    }

    // Cargar datos completos de Empleado, incluyendo servicioID
    private JefaturaDeServicio loadJefaturaDeServicioByUUID(UUID id) throws SQLException {
        String query = "SELECT u.*, j.servicioID FROM Usuario u "
                + "JOIN JefaturaDeServicio j ON u.id = j.id WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    JefaturaDeServicio jefaturaDeServicio = (JefaturaDeServicio) UsuarioFactory.createUsuario(rs);
                    UUID servicioId = UUID.fromString(rs.getString("servicioID"));
                    // Cargar el servicio con el ID y asignarlo al jefaturaDeServicio
                    jefaturaDeServicio.setServicio(servicioRepository.readByUUID(servicioId));
                    return jefaturaDeServicio;
                }
            }
        }
        return null;
    }

    // Cargar datos completos de Empleado, incluyendo servicioID
    private OficinaDePersonal loadOficinaDePersonalByUUID(UUID id) throws SQLException {
        String query = "SELECT u.*, o.reportesGenerados FROM Usuario u "
                + "JOIN OficinaDePersonal o ON u.id = o.id WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    OficinaDePersonal oficinaDePersonal = (OficinaDePersonal) UsuarioFactory.createUsuario(rs);
                    //UUID servicioId = UUID.fromString(rs.getString("servicioID"));
                    // Cargar el servicio con el ID y asignarlo al jefaturaDeServicio
                    //oficinaDePersonal.setServicio(servicioRepository.readByUUID(servicioId));
                    return oficinaDePersonal;
                }
            }
        }
        return null;
    }

    // Cargar datos completos de Empleado, incluyendo servicioID
    private Direccion loadDireccionByUUID(UUID id) throws SQLException {
        String query = "SELECT u.*, d.* FROM Usuario u "
                + "JOIN Direccion d ON u.id = d.id WHERE u.id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Direccion direccion = (Direccion) UsuarioFactory.createUsuario(rs);
                    //UUID servicioId = UUID.fromString(rs.getString("servicioID"));
                    // Cargar el servicio con el ID y asignarlo al jefaturaDeServicio
                    //direccion.setServicio(servicioRepository.readByUUID(servicioId));
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
        String query = "SELECT BIN_TO_UUID2(id) AS id FROM Usuario WHERE cuil = ?";
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


}
