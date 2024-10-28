package ar.com.hmu.service;

import ar.com.hmu.model.*;
import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
     * Devuelve el nombre completo del usuario actual, en formato "Apellidos, Nombres".
     *
     * @return El/los apellido/s y nombre/s del usuario.
     */
    public String getAgenteFullName() {
        return usuarioActual.getApellidos() + ", " + usuarioActual.getNombres();
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
     * Obtiene la imagen de perfil del usuario.
     * <p>
     * Este método devuelve la imagen de perfil del usuario en forma de una instancia de {@link Image}.
     * Si el usuario no tiene una imagen de perfil almacenada, se devuelve una imagen predeterminada.
     * Esta lógica asegura que siempre haya una imagen visible en la interfaz, incluso si el usuario
     * no ha configurado una imagen personalizada.
     *
     * @return una instancia de {@link Image} con la imagen de perfil del usuario si existe, o una imagen por defecto si no la hay.
     */
    public Image getProfileImage() {
        // Solo actualizar la imagen si el usuario tiene una imagen de perfil personalizada
        byte[] profileImageBytes = usuarioActual.getProfileImage();
        if (profileImageBytes != null && profileImageBytes.length > 0) {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(profileImageBytes);
            Image profileImage = new Image(inputStream);
            return new Image(inputStream);
        }
        //return null; // Si no tiene imagen personalizada, retornamos null.
        return new Image(getClass().getResourceAsStream("/ar/com/hmu/ui/loginImage.png"));
    }

    // Métodos adicionales para obtener la información de la conexión
    /**
     * Obtiene la información de conexión actual, como la fecha y hora de inicio de sesión.
     */
    public String getCurrentConnDateTime() {
        return new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(new Date());
    }

    /**
     * Obtiene la información de conexión actual, como el nombre del host.
     */
    public String getCurrentConnHostname() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostName();
        } catch (UnknownHostException e) {
            System.err.println("No se pudo obtener el nombre del host: " + e.getMessage());
            return "Desconocido";
        }
    }

    /**
     * Obtiene la información de conexión actual, como la dirección IP.
     */
    public String getCurrentConnIPAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("No se pudo obtener la dirección IP: " + e.getMessage());
            return "Desconocida";
        }
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
        Cargo cargo = usuarioActual.getCargo();
        if (cargo == null) {
            return "0000";
        } else {
            return usuarioActual.getCargo().getNumero().toString();
        }
    }


    // Aquí podrías añadir más métodos para proporcionar datos adicionales
    // sobre los módulos a los que tiene acceso el usuario o información de la sesión actual.

}
