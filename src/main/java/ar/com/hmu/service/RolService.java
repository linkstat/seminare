package ar.com.hmu.service;

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.Rol;
import ar.com.hmu.repository.RolRepository;

import java.sql.SQLException;
import java.util.Set;
import java.util.UUID;

public class RolService {

    private RolRepository rolRepository;

    public RolService(RolRepository rolRepository) {
        this.rolRepository = rolRepository;
    }

    public Rol findByTipoUsuario(TipoUsuario tipoUsuario) throws ServiceException {
        try {
            return rolRepository.findByTipoUsuario(tipoUsuario);
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener el rol: " + tipoUsuario.getInternalName(), e);
        }
    }

    public Set<Rol> findAll() throws ServiceException {
        try {
            return rolRepository.findAll();
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener todos los roles", e);
        }
    }

    public void asignarRol(UUID usuarioId, UUID rolId) throws ServiceException {
        try {
            rolRepository.asignarRol(usuarioId, rolId);
        } catch (SQLException e) {
            throw new ServiceException("Error al asignar rol", e);
        }
    }

    public void revocarTodosLosRoles(UUID usuarioId) throws ServiceException {
        try {
            rolRepository.revocarTodosLosRoles(usuarioId);
        } catch (SQLException e) {
            throw new ServiceException("Error al revocar todos los roles", e);
        }
    }

    public Set<Rol> findRolesByUsuarioId(UUID usuarioId) throws ServiceException {
        try {
            return rolRepository.findRolesByUsuarioId(usuarioId);
        } catch (SQLException e) {
            throw new ServiceException("Error al obtener roles del usuario", e);
        }
    }

}
