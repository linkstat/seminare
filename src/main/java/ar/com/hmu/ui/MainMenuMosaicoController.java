package ar.com.hmu.ui;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Controlador para gestionar el comportamiento del menú principal en forma de mosaico.
 * <p>
 * Esta clase gestiona la interacción del usuario con el menú principal de la aplicación,
 * incluyendo el manejo de mosaicos, opciones de menú, estado del servidor y la sesión del usuario.
 */
public class MainMenuMosaicoController {

    @FXML
    private Label agentNameLabel;
    @FXML
    private Label serviceLabel;
    @FXML
    private Label positionLabel;
    @FXML
    private MenuItem changePasswordMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private Button logoutButton;
    @FXML
    private Label connectionStatusLabel;
    @FXML
    private ImageView connectionStatusIcon;
    @FXML
    private VBox altaBajaVBox;
    // Agrega referencias para todos los mosaicos (VBox)
    // Similar al altaBajaVBox...

    private Usuario usuarioActual;

    /**
     * Inicializa los componentes después de que el archivo FXML ha sido cargado.
     */
    @FXML
    public void initialize() {
        setupEventHandlers();
    }

    /**
     * Establece el usuario actual y actualiza la interfaz gráfica con sus datos.
     *
     * @param usuario El usuario que ha iniciado sesión.
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        agentNameLabel.setText(usuario.getApellidos() + ", " + usuario.getNombres());
        serviceLabel.setText(usuario.getServicio());  // Si tiene el servicio asignado
        positionLabel.setText(String.valueOf(usuario.getCargo()));
    }

    /**
     * Configura los manejadores de eventos para los componentes del menú y mosaicos.
     */
    private void setupEventHandlers() {
        // Configura la funcionalidad del botón "Cerrar sesión"
        logoutButton.setOnAction(event -> handleLogout());

        // Configura la funcionalidad del menú: Archivo -> Salir
        exitMenuItem.setOnAction(event -> System.exit(0));

        // Configura la funcionalidad del menú: Archivo -> Modificar contraseña
        changePasswordMenuItem.setOnAction(event -> handleChangePassword());

        // Configura los mosaicos para mostrar una alerta de "Módulo en construcción"
        altaBajaVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        // Repite para los otros mosaicos...
    }

    /**
     * Muestra la ventana de inicio de sesión y cierra el menú principal.
     */
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/loginScreen.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            AlertUtils.showErr("Error al cargar la pantalla de inicio de sesión: " + e.getMessage());
        }
    }

    /**
     * Maneja la opción "Modificar Contraseña" abriendo una nueva ventana para cambiar la contraseña.
     */
    private void handleChangePassword() {
        // Crear una ventana emergente con tres campos para modificar la contraseña.
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modificar contraseña");
        dialog.setHeaderText("Ingrese su contraseña actual y la nueva contraseña.");

        // Campos de contraseña
        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Contraseña actual");

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Nueva contraseña");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Repetir nueva contraseña");

        // Añadir campos a la ventana
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(new VBox(10, currentPassword, newPassword, confirmPassword));

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Implementar lógica para modificar la contraseña
                if (!newPassword.getText().equals(confirmPassword.getText())) {
                    AlertUtils.showErr("¡CUIDADO! Las contraseñas no coinciden. La nueva contraseña se debe repetir dos veces, sin errores.");
                } else {
                    // Aquí iría la lógica para actualizar la contraseña en la base de datos
                    AlertUtils.showInfo("Contraseña modificada exitosamente.");
                }
            }
        });
    }

    /**
     * Muestra una alerta indicando que el módulo está en construcción.
     */
    private void showModuleUnderConstructionAlert() {
        AlertUtils.showInfo("Estamos trabajando para Usted\nMódulo en construcción");
    }

    /**
     * Configura la barra de estado para actualizar el estado del servidor.
     * <p>
     * Se puede reutilizar la lógica implementada en la pantalla de inicio de sesión para mantener
     * actualizada esta información.
     */
    public void updateServerStatus() {
        // Implementar la lógica de verificación del estado del servidor aquí.
        // Puedes reutilizar el código de LoginController para actualizar el connectionStatusLabel y connectionStatusIcon.
    }

    /**
     * Obtiene la información de conexión actual y previa, como hora de inicio de sesión,
     * nombre del host, e IP.
     */
    public void setupConnectionInfo() {
        // Datos de la conexión actual
        String currentConnectionTime = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(new Date());
        String hostname = "Desconocido";
        String ipAddress = "Desconocida";

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostname = inetAddress.getHostName();
            ipAddress = inetAddress.getHostAddress();s
        } catch (UnknownHostException e) {
            System.err.println("No se pudo obtener la información de la conexión actual: " + e.getMessage());
        }

        // Actualiza la UI con la información de conexión
        connectionStatusLabel.setText("vie " + currentConnectionTime + " " + hostname + " " + ipAddress);
    }

}
