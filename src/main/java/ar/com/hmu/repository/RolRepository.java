package ar.com.hmu.repository;

import java.sql.*;
import java.util.*;

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.model.Rol;
import ar.com.hmu.repository.DatabaseConnector;

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
                    // Cuando creamos objetos de tipo Rol, debemos configurar el campo tipoUsuario, que podría ser necesario en otro lugar.
                    rol.setTipoUsuario(TipoUsuario.fromInternalName(rol.getNombre()));

                    return rol;
                }
            }
        }
        return null;
    }

    public Rol findByTipoUsuario(TipoUsuario tipoUsuario) throws SQLException {
        String nombre = tipoUsuario.getInternalName();
        return findByNombre(nombre);
    }

    public Set<Rol> findAll() throws SQLException {
        Set<Rol> rolesSet = new HashSet<>();
        String query = "SELECT *, BIN_TO_UUID(id) AS id_str FROM Rol";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Rol rol = new Rol();
                rol.setId(UUID.fromString(rs.getString("id_str")));
                rol.setNombre(rs.getString("nombre"));
                rol.setDescripcion(rs.getString("descripcion"));
                // Se debe configurar tipoUsuario en función del nombre
                rol.setTipoUsuario(TipoUsuario.fromInternalName(rol.getNombre()));
                rolesSet.add(rol);
            }
        }

        return rolesSet;
    }

    public void asignarRol(UUID usuarioId, UUID rolId) throws SQLException {
        String query = "INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (UUID_TO_BIN(?), UUID_TO_BIN(?))";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuarioId.toString());
            stmt.setString(2, rolId.toString());
            stmt.executeUpdate();
        }
    }


    public void revocarTodosLosRoles(UUID usuarioId) throws SQLException {
        String query = "DELETE FROM Usuario_Rol WHERE usuario_id = UUID_TO_BIN(?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuarioId.toString());
            stmt.executeUpdate();
        }
    }


    public Set<Rol> findRolesByUsuarioId(UUID usuarioId) throws SQLException {
        String query = "SELECT BIN_TO_UUID(r.id) AS id, r.nombre, r.descripcion  " +
                "FROM Rol r INNER JOIN Usuario_Rol ur ON r.id = ur.rol_id " +
                "WHERE ur.usuario_id = UUID_TO_BIN(?)";
        Set<Rol> roles = new HashSet<>();
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, usuarioId.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Rol rol = new Rol();
                // Set properties of rol
                rol.setId(UUID.fromString(rs.getString("id")));
                rol.setNombre(rs.getString("nombre"));
                rol.setDescripcion(rs.getString("descripcion"));
                roles.add(rol);
            }
        }
        return roles;
    }



}
