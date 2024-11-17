package ar.com.hmu.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ar.com.hmu.factory.DomicilioFactory;
import ar.com.hmu.model.Domicilio;
import ar.com.hmu.repository.dao.GenericDAO;


/**
 * Clase encargada de las operaciones relacionadas con la entidad Domicilio en la base de datos.
 */
public class DomicilioRepository implements GenericDAO<Domicilio> {

    private DatabaseConnector databaseConnector;

    /**
     * Constructor que recibe un conector de base de datos.
     *
     * @param databaseConnector el conector de base de datos a utilizar.
     */
    public DomicilioRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @Override
    public void create(Domicilio domicilio) throws SQLException {
        String query = "INSERT INTO Domicilio (id, calle, numeracion, barrio, ciudad, localidad, provincia) VALUES (UUID_TO_BIN(UUID()), ?, ?, ?, ?, ?, ?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumeracion());
            stmt.setString(3, domicilio.getBarrio());
            stmt.setString(4, domicilio.getCiudad());
            stmt.setString(5, domicilio.getLocalidad());
            stmt.setString(6, domicilio.getProvincia());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al crear el domicilio", e);
        }
    }

    @Override
    public Domicilio readByUUID(UUID id) throws SQLException {
        String query = "SELECT * FROM Domicilio WHERE id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return DomicilioFactory.createDomicilio(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el domicilio por UUID", e);
        }
        return null;

    }

    @Override
    public List<Domicilio> readAll() throws SQLException {
        List<Domicilio> domicilios = new ArrayList<>();
        String query = "SELECT * FROM Domicilio";

        try (Connection connection = databaseConnector.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                domicilios.add(new Domicilio.Builder()
                        .setCalle(rs.getString("calle"))
                        .setNumeracion(rs.getString("numeracion"))
                        .setBarrio(rs.getString("barrio"))
                        .setCiudad(rs.getString("ciudad"))
                        .setLocalidad(rs.getString("localidad"))
                        .setProvincia(rs.getString("provincia"))
                        .build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer todos los domicilios", e);
        }

        return domicilios;
    }

    @Override
    public void update(Domicilio domicilio) throws SQLException {
        String query = "UPDATE Domicilio SET calle = ?, numeracion = ?, barrio = ?, ciudad = ?, localidad = ?, provincia = ? WHERE id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, domicilio.getCalle());
            stmt.setString(2, domicilio.getNumeracion());
            stmt.setString(3, domicilio.getBarrio());
            stmt.setString(4, domicilio.getCiudad());
            stmt.setString(5, domicilio.getLocalidad());
            stmt.setString(6, domicilio.getProvincia());
            stmt.setString(7, domicilio.getId().toString());

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated == 0) {
                throw new SQLException("No se pudo actualizar el domicilio; el ID proporcionado no existe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al actualizar el domicilio", e);
        }
    }

    @Override
    public void delete(Domicilio domicilio) throws SQLException {
        String query = "DELETE FROM Domicilio WHERE id = UUID_TO_BIN(?)";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, domicilio.getId().toString());

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted == 0) {
                throw new SQLException("No se pudo eliminar el domicilio; el ID proporcionado no existe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al eliminar el domicilio", e);
        }
    }

}
