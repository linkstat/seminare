package ar.com.hmu.ui;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.DatabaseConnector;
import ar.com.hmu.utils.AlertUtils;
import ar.com.hmu.utils.SessionUtils;
import static ar.com.hmu.utils.SessionUtils.handleLogout;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
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
    private DatabaseConnector databaseConnector; // Necesario para la verificación del estado del servidor.

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
        serviceLabel.setText(usuario.getServicio().getNombre());  // Si tiene el servicio asignado
        positionLabel.setText(usuario.getCargo().toString());
        setupConnectionInfo();  // Muestra la información de la conexión actual.
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
     * Maneja la opción "Modificar Contraseña" abriendo una nueva ventana para cambiar la contraseña.
     */
    private void handleChangePassword() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Modificar contraseña");
        dialog.setHeaderText("Ingrese su contraseña actual y la nueva contraseña.");

        PasswordField currentPassword = new PasswordField();
        currentPassword.setPromptText("Contraseña actual");

        PasswordField newPassword = new PasswordField();
        newPassword.setPromptText("Nueva contraseña");

        PasswordField confirmPassword = new PasswordField();
        confirmPassword.setPromptText("Repetir nueva contraseña");

        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.getDialogPane().setContent(new VBox(10, currentPassword, newPassword, confirmPassword));

        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                if (!usuarioActual.validatePassword(currentPassword.getText())) {
                    AlertUtils.showErr("¡CUIDADO! La contraseña actual no es correcta.");
                } else if (!newPassword.getText().equals(confirmPassword.getText())) {
                    AlertUtils.showErr("¡CUIDADO! Las contraseñas no coinciden. La nueva contraseña se debe repetir dos veces, sin errores.");
                } else {
                    usuarioActual.setPassword(newPassword.getText());
                    // Aquí se debería actualizar la base de datos con el nuevo valor de la contraseña.
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
        // Similar a LoginController
        if (databaseConnector != null) {
            String[] serverStatus = databaseConnector.checkServerStatus();
            connectionStatusLabel.setText(serverStatus[0]);
            connectionStatusIcon.setImage(new Image(getClass().getResourceAsStream(serverStatus[2])));
        }
    }

    /**
     * Obtiene la información de conexión actual y previa, como hora de inicio de sesión,
     * nombre del host, e IP.
     */
    public void setupConnectionInfo() {
        String currentConnectionTime = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss").format(new Date());
        String hostname = "Desconocido";
        String ipAddress = "Desconocida";

        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            hostname = inetAddress.getHostName();
            ipAddress = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            System.err.println("No se pudo obtener la información de la conexión actual: " + e.getMessage());
        }

        connectionStatusLabel.setText("Conexión: " + currentConnectionTime + " | " + hostname + " | " + ipAddress);
    }
}
