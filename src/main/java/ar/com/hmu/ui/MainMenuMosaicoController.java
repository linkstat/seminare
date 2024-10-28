package ar.com.hmu.ui;

import ar.com.hmu.service.MainMenuMosaicoService;
import ar.com.hmu.auth.PasswordChangeHandler;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.DatabaseConnector;
import ar.com.hmu.utils.AlertUtils;
import static ar.com.hmu.utils.SessionUtils.handleLogout;
import static ar.com.hmu.utils.ServerStatusUtils.*;
import ar.com.hmu.utils.SessionUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.text.Text;

/**
 * Controlador para gestionar el comportamiento del menú principal en forma de mosaico.
 * <p>
 * Esta clase gestiona la interacción del usuario con el menú principal de la aplicación,
 * incluyendo el manejo de mosaicos, opciones de menú, estado del servidor y la sesión del usuario.
 */
public class MainMenuMosaicoController {

    @FXML
    private ImageView serverStatusIcon;
    @FXML
    private Label serverStatusLabel;
    @FXML
    private Text agentFullNameText;
    @FXML
    private Text agentServiceText;
    @FXML
    private Text agentCargoText;
    @FXML
    private ImageView agentProfileImage;
    @FXML
    private MenuItem changePasswordMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private Button logoutButton;
    @FXML
    private Text currentConnDateTimeText;
    @FXML
    private Text currentConnHostnameText;
    @FXML
    private Text currentConnIPAddressText;
    @FXML
    private VBox altaBajaVBox;

    private MainMenuMosaicoService mainMenuService; // Servicio para gestionar la lógica del menú principal
    private DatabaseConnector databaseConnector; // Necesario para la verificación del estado del servidor.
    private Usuario usuarioActual;

    /**
     * Inicializa los componentes después de que el archivo FXML ha sido cargado.
     */
    @FXML
    public void initialize() {
        updateServerStatus(databaseConnector, serverStatusLabel, serverStatusIcon);
        setupEventHandlers();
    }

    /**
     * Establece el usuario actual y actualiza la interfaz gráfica con sus datos.
     *
     * @param usuario El usuario que ha iniciado sesión.
     * @param databaseConnector El conector de la base de datos.
     */
    public void postInitialize(Usuario usuario, DatabaseConnector databaseConnector) {
        this.mainMenuService = new MainMenuMosaicoService(usuario);

        // Actualizar la información del agente
        agentFullNameText.setText(mainMenuService.getAgenteFullName());
        agentServiceText.setText(mainMenuService.getServicioNombre());
        agentCargoText.setText(mainMenuService.getCargoUsuario());
        agentProfileImage.setImage(mainMenuService.getProfileImage());

        // Actualizar la información de conexión
        currentConnDateTimeText.setText(mainMenuService.getCurrentConnDateTime());
        currentConnHostnameText.setText(mainMenuService.getCurrentConnHostname());
        currentConnIPAddressText.setText(mainMenuService.getCurrentConnIPAddress());

        // Configurar visibilidad del mosaico "Alta, Baja y Modificación de Agentes"
        altaBajaVBox.setVisible(mainMenuService.puedeAccederAltaBajaAgentes());
        // Configurar la visibilidad de otros mosaicos según el servicio

        // Actualizar el estado del servidor y comenzar el chequeo periódico
        if (databaseConnector == null) {
            throw new IllegalStateException("MainMenuMosaicoController: DatabaseConnector no está configurado. No se puede verificar el estado del servidor.");
        }
        updateServerStatus(databaseConnector, serverStatusLabel, serverStatusIcon);
        startPeriodicServerCheck(databaseConnector, serverStatusLabel, serverStatusIcon);

    }

    /**
     * Configura los manejadores de eventos para los componentes del menú y mosaicos.
     */
    private void setupEventHandlers() {
        // Configura la funcionalidad del botón "Cerrar sesión"
        logoutButton.setOnAction(event -> handleLogout((Stage) logoutButton.getScene().getWindow()));

        // Configura la funcionalidad del menú: Archivo -> Salir
        exitMenuItem.setOnAction(event -> System.exit(0));

        // Configura la funcionalidad del menú: Archivo -> Modificar contraseña
        changePasswordMenuItem.setOnAction(event -> handleChangePassword());

        // Configura los mosaicos para mostrar una alerta de "Módulo en construcción"
        altaBajaVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());

        // Repetir para los otros mosaicos...
    }

    /**
     * Maneja la opción "Modificar Contraseña" abriendo una nueva ventana para cambiar la contraseña.
     */
    private void handleChangePassword() {
        PasswordChangeHandler passwordChangeHandler = new PasswordChangeHandler();
        passwordChangeHandler.showChangePasswordDialog(usuarioActual,
                () -> {}, // Callback vacío para continuar después del cambio de contraseña exitoso
                () -> SessionUtils.handleLogout((Stage) logoutButton.getScene().getWindow()) // Callback para cerrar la sesión si se cancela
        );
    }

    /**
     * Muestra una alerta indicando que el módulo está en construcción.
     */
    private void showModuleUnderConstructionAlert() {
        AlertUtils.showInfo("Estamos trabajando para Usted\nMódulo en construcción");
    }

}
