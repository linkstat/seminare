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
    public void create(Servicio servicio) {
        String query = "INSERT INTO Servicio (id, nombre, agrupacion, direccionID) VALUES (UUID_TO_BIN(?), ?, ?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, servicio.getId().toString());
            stmt.setString(2, servicio.getNombre());
            stmt.setString(3, servicio.getAgrupacion().name());
            stmt.setString(4, servicio.getDireccionId().toString());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el servicio", e);
        }
    }

    @Override
    public Servicio readByUUID(UUID id) {
        String query = "SELECT * FROM Servicio WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());
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
    public List<Servicio> readAll() {
        List<Servicio> servicios = new ArrayList<>();
        String query = "SELECT * FROM Servicio";
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
    public void update(Servicio servicio) {
        String query = "INSERT INTO Servicio (id, nombre, agrupacion, direccionID) VALUES (UUID_TO_BIN(?), ?, ?, UUID_TO_BIN(?))";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, servicio.getId().toString());           // UUID del servicio
            stmt.setString(2, servicio.getNombre());                  // Nombre del servicio
            stmt.setString(3, servicio.getAgrupacion().name());       // Agrupación como String
            stmt.setString(4, servicio.getDireccionId().toString());  // UUID de la dirección

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el servicio", e);
        }
    }

    @Override
    public void delete(Servicio servicio) {
        String query = "DELETE FROM Servicio WHERE id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, servicio.getId().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el servicio", e);
        }
    }

    /**
     * Helper method to map a ResultSet row to a Servicio object.
     */
    private Servicio mapResultSetToServicio(ResultSet rs) throws SQLException {
        UUID id = UUID.fromString(rs.getString("UUID"));
        String nombre = rs.getString("nombre");
        Agrupacion agrupacion = Agrupacion.valueOf(rs.getString("agrupacion"));
        UUID direccionId = UUID.fromString("direccionId");

        return new Servicio(id, nombre, agrupacion, direccionId);
    }
}

