package ar.com.hmu.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ar.com.hmu.model.Agrupacion;
import ar.com.hmu.model.Servicio;
import ar.com.hmu.repository.dao.GenericDAO;


public class ServicioRepository implements GenericDAO<Servicio> {

    private DatabaseConnector databaseConnector;

    public ServicioRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void create(Servicio servicio) throws SQLException {
        String query = "INSERT INTO Servicio (id, nombre, agrupacion, direccionID) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, servicio.getId());
            stmt.setString(2, servicio.getNombre());
            stmt.setObject(3, servicio.getAgrupacion().name(), Types.OTHER);
            stmt.setObject(4, servicio.getDireccionId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el servicio", e);
        }
    }

    public void createWithoutDireccionId(Servicio servicio) throws SQLException {
        String query = "INSERT INTO Servicio (id, nombre, agrupacion) VALUES (?, ?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, servicio.getId());
            stmt.setString(2, servicio.getNombre());
            stmt.setObject(3, servicio.getAgrupacion().name(), Types.OTHER);

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el servicio (usando el método sin direccionID)", e);
        }
    }

    @Override
    public Servicio readByUUID(UUID id) throws SQLException {
        String query = "SELECT id, nombre, agrupacion, direccionID FROM Servicio WHERE id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToServicio(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el servicio por UUID", e);
        }
        return null;
    }

    @Override
    public List<Servicio> readAll() throws SQLException {
        List<Servicio> servicios = new ArrayList<>();
        String query = "SELECT id, nombre, agrupacion, direccionID FROM Servicio";
        try (Connection connection = databaseConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                servicios.add(mapResultSetToServicio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer todos los servicios", e);
        }
        return servicios;
    }

    @Override
    public void update(Servicio servicio) throws SQLException {
        String query = "UPDATE Servicio SET nombre = ?, agrupacion = ?, direccionID = ? WHERE id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, servicio.getNombre());
            stmt.setObject(2, servicio.getAgrupacion().name(), Types.OTHER);
            stmt.setObject(3, servicio.getDireccionId());
            stmt.setObject(4, servicio.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el servicio", e);
        }
    }

    public void updateWithoutDireccionId(Servicio servicio) throws SQLException {
        String query = "UPDATE Servicio SET nombre = ?, agrupacion = ? WHERE id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, servicio.getNombre());
            stmt.setObject(2, servicio.getAgrupacion().name(), Types.OTHER);
            stmt.setObject(3, servicio.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el servicio", e);
        }
    }

    @Override
    public void delete(Servicio servicio) throws SQLException {
        String query = "DELETE FROM Servicio WHERE id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, servicio.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el servicio", e);
        }
    }

    public Servicio findById(UUID id) throws SQLException {
        String query = "SELECT id, nombre, agrupacion, direccionID FROM Servicio WHERE id = ?";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setObject(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Servicio servicio = new Servicio();
                    servicio.setId(rs.getObject("id", UUID.class));
                    servicio.setNombre(rs.getString("nombre"));
                    servicio.setAgrupacion(Agrupacion.valueOf(rs.getString("agrupacion")));
                    servicio.setDireccionId(rs.getObject("direccionID", UUID.class));
                    return servicio;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Error al buscar la descripción por número", e);
        }
        return null;
    }

    /**
     * Devuelve el UUID de Servicio en base a un nombre dado
     * @param name Nombre del servicio del cual se quiere obtener el UUID
     * @return UUID del servicio buscado
     */
    public UUID findIdByName(String name) throws SQLException {
        String query = "SELECT id FROM Servicio WHERE nombre = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getObject("id", UUID.class);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al buscar el UUID de servicio por nombre", e);
        }
        return null;
    }


    /**
     * Método para mapear filas a un ResultSet de Servicio.
     */
    private Servicio mapResultSetToServicio(ResultSet rs) throws SQLException {
        UUID id = rs.getObject("id", UUID.class);
        String nombre = rs.getString("nombre");
        Agrupacion agrupacion = Agrupacion.valueOf(rs.getString("agrupacion"));
        UUID direccionId = rs.getObject("direccionID", UUID.class);
        if (direccionId == null) {
            //TODO: Decidir cómo manejar este caso: lanzar una excepción o establecer como null.
            System.out.println("direccionID es null para ServicioID: " + nombre);
        }

        return new Servicio(id, nombre, agrupacion, direccionId);
    }


    public int countServicios() throws SQLException {
        String query = "SELECT COUNT(*) AS total FROM Servicio";

        try (Connection connection = databaseConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al contar los servicios", e);
        }

        return 0;
    }


}
