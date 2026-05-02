package ar.com.hmu.repository;

import java.sql.*;
import java.util.*;

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.model.RoleData;

public class RoleRepository {
    private DatabaseConnector databaseConnector;

    public RoleRepository(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public RoleData findByNombre(String nombre) throws SQLException {
        String query = "SELECT id, nombre, descripcion FROM Rol WHERE nombre = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    RoleData roleData = new RoleData();
                    roleData.setId(rs.getObject("id", UUID.class));
                    roleData.setNombre(rs.getString("nombre"));
                    roleData.setDescripcion(rs.getString("descripcion"));
                    roleData.setTipoUsuario(TipoUsuario.fromInternalName(roleData.getNombre()));

                    return roleData;
                }
            }
        }
        return null;
    }

    public RoleData findByTipoUsuario(TipoUsuario tipoUsuario) throws SQLException {
        String nombre = tipoUsuario.getInternalName();
        return findByNombre(nombre);
    }

    public Set<RoleData> findAll() throws SQLException {
        Set<RoleData> rolesSet = new HashSet<>();
        String query = "SELECT id, nombre, descripcion FROM Rol";

        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                RoleData roleData = new RoleData();
                roleData.setId(rs.getObject("id", UUID.class));
                roleData.setNombre(rs.getString("nombre"));
                roleData.setDescripcion(rs.getString("descripcion"));
                roleData.setTipoUsuario(TipoUsuario.fromInternalName(roleData.getNombre()));
                rolesSet.add(roleData);
            }
        }

        return rolesSet;
    }

    public void asignarRol(UUID usuarioId, UUID rolId) throws SQLException {
        String query = "INSERT INTO Usuario_Rol (usuario_id, rol_id) VALUES (?, ?)";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, usuarioId);
            stmt.setObject(2, rolId);
            stmt.executeUpdate();
        }
    }


    public void revocarTodosLosRoles(UUID usuarioId) throws SQLException {
        String query = "DELETE FROM Usuario_Rol WHERE usuario_id = ?";
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, usuarioId);
            stmt.executeUpdate();
        }
    }


    public Set<RoleData> findRolesByUsuarioId(UUID usuarioId) throws SQLException {
        String query = "SELECT r.id AS id, r.nombre, r.descripcion " +
                "FROM Rol r INNER JOIN Usuario_Rol ur ON r.id = ur.rol_id " +
                "WHERE ur.usuario_id = ?";
        Set<RoleData> roles = new HashSet<>();
        try (Connection connection = databaseConnector.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setObject(1, usuarioId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RoleData roleData = new RoleData();
                roleData.setId(rs.getObject("id", UUID.class));
                roleData.setNombre(rs.getString("nombre"));
                roleData.setDescripcion(rs.getString("descripcion"));
                roles.add(roleData);
            }
        }
        return roles;
    }



}
