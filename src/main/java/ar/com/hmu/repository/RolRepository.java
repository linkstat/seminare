package ar.com.hmu.repository;

import ar.com.hmu.model.Rol;
import ar.com.hmu.repository.DatabaseConnector;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RolRepository {
    private DatabaseConnector databaseConnector;

    public RolRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public Rol findByNombre(String nombre) throws SQLException {
        String query = "SELECT *, BIN_TO_UUID(id) AS id_str FROM Rol WHERE nombre = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Rol rol = new Rol();
                    rol.setId(UUID.fromString(rs.getString("id_str")));
                    rol.setNombre(rs.getString("nombre"));
                    rol.setDescripcion(rs.getString("descripcion"));
                    return rol;
                }
            }
        }
        return null;
    }

    public List<Rol> findAll() throws SQLException {
        List<Rol> roles = new ArrayList<>();
        String query = "SELECT *, BIN_TO_UUID(id) AS id_str FROM Rol";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId(UUID.fromString(rs.getString("id_str")));
                rol.setNombre(rs.getString("nombre"));
                rol.setDescripcion(rs.getString("descripcion"));
                roles.add(rol);
            }
        }

        return roles;
    }

    // Métodos CRUD adicionales si es necesario
}
