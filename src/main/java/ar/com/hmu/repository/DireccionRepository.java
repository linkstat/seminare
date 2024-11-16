package ar.com.hmu.repository;

import java.util.UUID;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ar.com.hmu.constants.NombreServicio;
import ar.com.hmu.model.Direccion;
import ar.com.hmu.model.Servicio;
import ar.com.hmu.model.Usuario;


public class DireccionRepository {

    private DatabaseConnector databaseConnector;

    // Constructor que recibe el conector de la base de datos
    public DireccionRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Obtiene el objeto Direccion a partir de la base de datos con el servicio asociado.
     *
     * @return Una instancia de Direccion con el servicio correspondiente.
     */
    public Direccion findDireccion() {
        String query = "SELECT BIN_TO_UUID(id) AS id, nombre FROM Servicio WHERE nombre = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, NombreServicio.DIRECCION);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Servicio servicio = new Servicio();
                    servicio.setId(UUID.fromString(resultSet.getString("id")));
                    servicio.setNombre(resultSet.getString("nombre"));

                    // Crear el objeto Direccion y asignarle el servicio
                    Direccion direccion = new Direccion();
                    direccion.setServicio(servicio);
                    return direccion;
                } else {
                    throw new IllegalStateException("El servicio 'Dirección' no fue encontrado en la base de datos.");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar el servicio de la base de datos: " + e.getMessage(), e);
        }
    }
}

