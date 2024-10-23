package ar.com.hmu.utils;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Utilidad para mostrar alertas emergentes en una aplicación JavaFX.
 * <p></p>
 * La clase proporciona métodos para mostrar mensajes al usuario utilizando la clase {@link Alert} de JavaFX.
 * Incluye métodos para mostrar alertas de error, advertencia e información.
 */
public class AlertUtils {

    /**
     * Método para mostrar una alerta en la pantalla con el tipo, título, encabezado y contenido especificados.
     *
     * @param alertType el tipo de alerta a mostrar (ERROR, WARNING, INFORMATION, etc.).
     * @param title el título de la ventana de alerta.
     * @param header el texto del encabezado de la alerta (puede ser null si no se necesita un encabezado).
     * @param content el mensaje principal que se mostrará en la alerta.
     */
    public static void showAlert(AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait(); // Espera a que el usuario cierre la ventana
    }

    /**
     * Muestra una alerta de error con un mensaje específico.
     *
     * @param content el mensaje que se mostrará en la alerta de error.
     */
    public static void showErr(String content) {
        showAlert(AlertType.ERROR, "Error", null, content);
    }

    /**
     * Muestra una alerta de advertencia con un mensaje específico.
     *
     * @param content el mensaje que se mostrará en la alerta de advertencia.
     */
    public static void showWarn(String content) {
        showAlert(AlertType.WARNING, "Advertencia", null, content);
    }

    /**
     * Muestra una alerta de información con un mensaje específico.
     *
     * @param content el mensaje que se mostrará en la alerta de información.
     */
    public static void showInfo(String content) {
        showAlert(AlertType.INFORMATION, "Información", null, content);
    }
}
