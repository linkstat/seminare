package ar.com.hmu.auth;

import ar.com.hmu.model.JefaturaDeServicio;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.model.OficinaDePersonal;
import ar.com.hmu.model.Servicio;

/**
 * Servicio que gestiona la lógica del menú principal en mosaico.
 *
 * Esta clase maneja la lógica relacionada con la personalización del menú
 * según el tipo de usuario, proporcionando datos específicos como los módulos
 * habilitados o la información de la sesión actual.
 */
public class MainMenuMosaicoService {

    private Usuario usuarioActual;

    /**
     * Constructor que inicializa el servicio con el usuario actual.
     *
     * @param usuario El usuario que ha iniciado sesión.
     */
    public MainMenuMosaicoService(Usuario usuario) {
        this.usuarioActual = usuario;
    }

    /**
     * Devuelve el nombre del servicio al que pertenece el usuario actual.
     *
     * @return El nombre del servicio del usuario.
     */
    public String getServicioNombre() {
        Servicio servicio = usuarioActual.getServicio();
        return servicio != null ? servicio.getNombre() : "Sin servicio asignado";
    }

    /**
     * Determina si el usuario tiene acceso al módulo de Alta, Baja y Modificación de Agentes.
     *
     * @return true si el usuario tiene acceso, false de lo contrario.
     */
    public boolean puedeAccederAltaBajaAgentes() {
        return usuarioActual instanceof OficinaDePersonal; // Solo OficinaDePersonal tiene acceso a este módulo
    }

    /**
     * Determina si el usuario tiene acceso al módulo de aprobación de solicitudes.
     *
     * @return true si el usuario tiene acceso, false de lo contrario.
     */
    public boolean puedeAccederAprobacionSolicitudes() {
        return usuarioActual instanceof JefaturaDeServicio; // Solo JefaturaDeServicio tiene acceso a este módulo
    }

    /**
     * Proporciona el cargo del usuario actual.
     *
     * @return El cargo del usuario actual como String.
     */
    public String getCargoUsuario() {
        return usuarioActual.getCargo().getNumero().toString();
    }

    // Aquí podrías añadir más métodos para proporcionar datos adicionales
    // sobre los módulos a los que tiene acceso el usuario o información de la sesión actual.

}
