package ar.com.hmu.service;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.Servicio;
import ar.com.hmu.repository.ServicioRepository;

public class ServicioService {

    private final ServicioRepository servicioRepository;

    public ServicioService(ServicioRepository servicioRepository) {
        this.servicioRepository = servicioRepository;
    }

    public void create(Servicio servicio) throws ServiceException {
        try {
            servicioRepository.create(servicio);
        } catch (SQLException e) {
            throw new ServiceException("Error al crear el servicio", e);
        }
    }

    public Servicio readByUUID(UUID id) throws ServiceException {
        try {
            return servicioRepository.readByUUID(id);
        } catch (SQLException e) {
            throw new ServiceException("Error al leer el servicio", e);
        }
    }

    public List<Servicio> readAll() throws ServiceException {
        try {
            return servicioRepository.readAll();
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todos los servicios", e);
        }
    }

    public void update(Servicio servicio) throws ServiceException {
        try {
            servicioRepository.update(servicio);
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar el servicio", e);
        }
    }

    public void delete(Servicio servicio) throws ServiceException {
        try {
            servicioRepository.delete(servicio);
        } catch (SQLException e) {
            throw new ServiceException("Error al eliminar el servicio", e);
        }
    }

    // Métodos adicionales si es necesario
    public UUID findIdByName(String name) throws ServiceException {
        try {
            return servicioRepository.findIdByName(name);
        } catch (SQLException e) {
            throw new ServiceException("Error al buscar el UUID de servicio por nombre", e);
        }
    }

    public int countServicios() throws ServiceException {
        try {
            return servicioRepository.countServicios();
        } catch (SQLException e) {
            throw new ServiceException("Error al contar los servicios", e);
        }
    }

}
