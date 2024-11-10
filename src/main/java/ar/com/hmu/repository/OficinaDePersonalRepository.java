package ar.com.hmu.repository;

import java.util.UUID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.hmu.constants.NombreServicio;
import ar.com.hmu.model.OficinaDePersonal;
import ar.com.hmu.model.Servicio;

public class OficinaDePersonalRepository {

    private DatabaseConnector databaseConnector;

    // Constructor que recibe el conector de la base de datos
    public OficinaDePersonalRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Obtiene el objeto OficinaDePersonal a partir de la base de datos con el servicio asociado.
     *
     * @return Una instancia de OficinaDePersonal con el servicio correspondiente.
     */
    public OficinaDePersonal findOficinaDePersonal() {
        String query = "SELECT BIN_TO_UUID2(id) AS id, nombre FROM Servicio WHERE nombre = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, NombreServicio.OFICINA_DE_PERSONAL);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Servicio servicio = new Servicio();
                    servicio.setId(UUID.fromString(resultSet.getString("id")));
                    servicio.setNombre(resultSet.getString("nombre"));

                    // Crear el objeto OficinaDePersonal y asignarle el servicio
                    OficinaDePersonal oficinaDePersonal = new OficinaDePersonal();
                    oficinaDePersonal.setServicio(servicio);
                    return oficinaDePersonal;
                } else {
                    throw new IllegalStateException("El servicio 'Personal' no fue encontrado en la base de datos.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el servicio de la base de datos: " + e.getMessage(), e);
        }
    }
}

