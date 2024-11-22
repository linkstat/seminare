package ar.com.hmu.service;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ar.com.hmu.constants.UsuarioCreationResult;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.*;
import ar.com.hmu.repository.*;
import ar.com.hmu.util.PasswordUtils;


public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final ServicioRepository servicioRepository;
    private final CargoRepository cargoRepository;
    private final DomicilioRepository domicilioRepository;
    private final RolService rolService;

    public UsuarioService(UsuarioRepository usuarioRepository, ServicioRepository servicioRepository, CargoRepository cargoRepository, DomicilioRepository domicilioRepository, RolService rolService) {
        this.usuarioRepository = usuarioRepository;
        this.servicioRepository = servicioRepository;
        this.cargoRepository = cargoRepository;
        this.domicilioRepository = domicilioRepository;
        this.rolService = rolService;
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

                // Assign roles using RolService
                for (Rol rol : usuario.getRoles()) {
                    rolService.asignarRol(usuario.getId(), rol.getId());
                }

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

    public String findPasswordByCuil(long cuil) throws ServiceException {
        try {
            return usuarioRepository.findPasswordByCuil(cuil);
        } catch (SQLException e) {
            throw new ServiceException("Error al recuperar la contraseña del usuario", e);
        }
    }

    public Usuario findUsuarioByCuil(long cuil) throws ServiceException {
        try {
            Usuario usuario = usuarioRepository.findUsuarioByCuil(cuil);
            if (usuario != null) {
                // Load additional user data if necessary
                loadAdditionalUserData(usuario);
            }
            return usuario;
        } catch (SQLException e) {
            throw new ServiceException("Error al recuperar usuario por CUIL", e);
        }
    }

    public List<Usuario> readAll() throws ServiceException {
        try {
            List<Usuario> usuarios = usuarioRepository.readAll();
            for (Usuario usuario : usuarios) {
                // Load Domicilio
                Domicilio domicilio = domicilioRepository.findByIdAndUserId(usuario.getDomicilioId(), usuario.getId());
                if (domicilio == null) {
                    System.out.println("Domicilio es null para Usuario ID: " + usuario.getId());
                    //TODO: Decidir cómo manejar este caso: lanzar una excepción o continuar.
                    continue; // Por ejemplo, continuar con el siguiente usuario
                }
                usuario.setDomicilio(domicilio);

                // Load Roles
                Set<Rol> roles = rolService.findRolesByUsuarioId(usuario.getId());
                usuario.setRoles(roles);

                // Load Cargo
                Cargo cargo = cargoRepository.findById(usuario.getCargoId());
                usuario.setCargo(cargo);

                // Load Servicio
                Servicio servicio = servicioRepository.findById(usuario.getServicioId());
                usuario.setServicio(servicio);
            }
            return usuarios;
        } catch (Exception e) {
            throw new ServiceException("Error al leer todos los usuarios", e);
        }
    }

    public List<Usuario> readAllPrimarios() throws ServiceException {
        try {
            List<Usuario> usuarios = usuarioRepository.readAll();
            for (Usuario usuario : usuarios) {
            }
            return usuarios;
        } catch (Exception e) {
            throw new ServiceException("Error al leer todos los usuarios (solo datos primarios)", e);
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
                usuario.setRoles(rolService.findRolesByUsuarioId(usuario.getId()));

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

            // Update roles
            // First, remove existing roles
            rolService.revocarTodosLosRoles(usuario.getId());

            // Then, assign new roles
            for (Rol rol : usuario.getRoles()) {
                rolService.asignarRol(usuario.getId(), rol.getId());
            }

        } catch (SQLException e) {
            throw new ServiceException("Error al actualizar el usuario", e);
        }
    }


    public void delete(Usuario usuario) throws ServiceException {
        try {
            // Remove roles
            rolService.revocarTodosLosRoles(usuario.getId());

            // Deactivate user
            usuario.setEstado(false);
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

            // Cargar roles usando RolService
            usuario.setRoles(rolService.findRolesByUsuarioId(usuario.getId()));


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
