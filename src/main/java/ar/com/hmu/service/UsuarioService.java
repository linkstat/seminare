package ar.com.hmu.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import ar.com.hmu.constants.UsuarioCreationResult;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.Cargo;
import ar.com.hmu.model.Domicilio;
import ar.com.hmu.model.Servicio;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.*;
import ar.com.hmu.util.PasswordUtils;


public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final CargoRepository cargoRepository;
    private final DomicilioRepository domicilioRepository;


    public UsuarioService(UsuarioRepository usuarioRepository, ServicioRepository servicioRepository, CargoRepository cargoRepository, DomicilioRepository domicilioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
        this.cargoRepository = cargoRepository;
        this.domicilioRepository = domicilioRepository;
    }


    public UsuarioCreationResult create(Usuario usuario) throws ServiceException {
        try {
            boolean includeDisabled = true;
            Usuario existente = usuarioRepository.findUsuarioByCuil(usuario.getCuil(), includeDisabled);
            if (existente != null) {
                if (existente.getEstado()) {
                    return UsuarioCreationResult.USUARIO_ACTIVO_EXISTENTE;
                } else {
                    return UsuarioCreationResult.USUARIO_DESHABILITADO_EXISTENTE;
                }
            } else {
                // Crear nuevo usuario
                usuarioRepository.create(usuario);
                return UsuarioCreationResult.USUARIO_CREADO;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al crear el usuario", e);
        }
    }

    public void reactivarUsuario(Usuario usuario) throws ServiceException {
        try {
            boolean includeDisabled = true;
            Usuario existente = usuarioRepository.findUsuarioByCuil(usuario.getCuil(), includeDisabled);
            if (existente != null && !existente.getEstado()) {
                // Reactivar y actualizar datos
                existente.setEstado(true);
                existente.setApellidos(usuario.getApellidos());
                existente.setNombres(usuario.getNombres());
                existente.setMail(usuario.getMail());
                existente.setTel(usuario.getTel());
                existente.setSexo(usuario.getSexo());
                existente.setCargo(usuario.getCargo());
                existente.setServicio(usuario.getServicio());
                existente.setProfileImage(usuario.getProfileImage());
                existente.setDomicilio(usuario.getDomicilio());

                // Actualizar en la base de datos
                usuarioRepository.update(existente);
            } else {
                throw new ServiceException("No se puede reactivar el usuario. El usuario no existe o ya está activo.");
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al reactivar el usuario", e);
        }
    }



    public List<Usuario> readAll() throws ServiceException {
        try {
            return usuarioRepository.readAll();
        } catch (SQLException e) {
            throw new ServiceException("Error al leer todos los usuarios", e);
        }
    }

    public Usuario readByUUID(UUID id) throws ServiceException {
        try {
            Usuario usuario = usuarioRepository.readByUUID(id);
            if (usuario != null) {
                // Cargar el domicilio
                if (usuario.getDomicilioId() != null) {
                    Domicilio domicilio = domicilioRepository.readByUUID(usuario.getDomicilioId());
                    usuario.setDomicilio(domicilio);
                }

                // Cargar el cargo
                if (usuario.getCargoId() != null) {
                    Cargo cargo = cargoRepository.readByUUID(usuario.getCargoId());
                    usuario.setCargo(cargo);
                }

                // Cargar el servicio
                if (usuario.getServicioId() != null) {
                    Servicio servicio = servicioRepository.readByUUID(usuario.getServicioId());
                    usuario.setServicio(servicio);
                }

                // Asignación de roles (si es necesario)
                usuario.setRoles(usuarioRepository.findRolesByUsuarioId(usuario.getId()));

                return usuario;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al leer el usuario por UUID", e);
        }
    }


    public void update(Usuario usuario) throws ServiceException {
        try {
            usuarioRepository.update(usuario);
        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar el usuario", e);
        }
    }

    public void delete(Usuario usuario) throws ServiceException {
        try {
            usuario.setEstado(false); // O estado = 0
            usuarioRepository.update(usuario);
        } catch (SQLException e) {
            throw new ServiceException("Error al deshabilitar el usuario", e);
        }
    }


    /**
     * Restablece la contraseña sus valores por defecto y persiste el cambio en la base de datos.
     * @param usuario El usuario que cambiará la contraseña.
     * @throws ServiceException excepción personalizada
     */
    public void resetPassword(Usuario usuario) throws ServiceException {
        try {
            // Establecer la contraseña por defecto en el objeto Usuario
            usuario.setDefaultPassword();

            // Actualizar la contraseña en la base de datos
            usuarioRepository.updatePassword(usuario.getCuil(), usuario.getEncryptedPassword());
        } catch (SQLException e) {
            throw new ServiceException("Error al restablecer la contraseña del usuario", e);
        }
    }

    /**
     * Cambia la contraseña de un usuario y persiste el cambio en la base de datos.
     *
     * @param usuario           El usuario que cambiará la contraseña.
     * @param currentPassword   La contraseña actual ingresada por el usuario.
     * @param newPassword       La nueva contraseña.
     * @param confirmNewPassword   Confirmación de la nueva contraseña.
     */
    public boolean changePassword(Usuario usuario, char[] currentPassword, char[] newPassword, char[] confirmNewPassword) {
        try {
            // Paso 1: Validar la contraseña actual
            if (!PasswordUtils.validatePassword(currentPassword, usuario.getEncryptedPassword())) {
                throw new IllegalArgumentException("La contraseña actual no es correcta.");
            }

            // Paso 2: Validar que la nueva contraseña coincide con la confirmación
            if (!Arrays.equals(newPassword, confirmNewPassword)) {
                throw new IllegalArgumentException("Las nuevas contraseñas no coinciden.");
            }

            // Paso 3: Establecer la nueva contraseña cifrada en el objeto Usuario
            String hashedPassword = PasswordUtils.hashPassword(newPassword);
            usuario.setPasswordHash(hashedPassword);

            // Paso 4: Actualizar la contraseña en la base de datos
            usuarioRepository.updatePassword(usuario.getCuil(), hashedPassword);

            return true; // Si el cambio es exitoso, retorna true
        } catch (SQLException e) {
            // Manejar la excepción y lanzar una excepción de tiempo de ejecución
            throw new RuntimeException("Error al actualizar la contraseña en la base de datos", e);
        } finally {
            // Paso 5: Limpiar los arrays de contraseñas para evitar que permanezcan en memoria
            Arrays.fill(currentPassword, '\0');
            Arrays.fill(newPassword, '\0');
            Arrays.fill(confirmNewPassword, '\0');
        }
    }


    public void loadAdditionalUserData(Usuario usuario) throws ServiceException {
        try {
            if (usuario.getServicioId() != null) {
                Servicio servicio = servicioRepository.readByUUID(usuario.getServicioId());
                usuario.setServicio(servicio);
            }

            if (usuario.getCargoId() != null) {
                Cargo cargo = cargoRepository.readByUUID(usuario.getCargoId());
                usuario.setCargo(cargo);
            }

            if (usuario.getDomicilioId() != null) {
                Domicilio domicilio = domicilioRepository.readByUUID(usuario.getDomicilioId());
                usuario.setDomicilio(domicilio);
            }
        } catch (SQLException e) {
            throw new ServiceException("Error al cargar datos adicionales del usuario\nmétodo loadAdditionalUserData", e);
        }
    }


    public void updateProfileImage(Usuario usuario) {
        try {
            usuarioRepository.updateProfileImage(usuario.getCuil(), usuario.getProfileImage());
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar la imagen de perfil en la base de datos", e);
        }
    }

}
