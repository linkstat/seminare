package ar.com.hmu.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import ar.com.hmu.repository.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.text.Text;

import ar.com.hmu.model.Usuario;
import ar.com.hmu.service.MainMenuMosaicoService;
import ar.com.hmu.service.UsuarioService;
import ar.com.hmu.utils.AlertUtils;
import ar.com.hmu.utils.AppInfo;
import ar.com.hmu.utils.PasswordDialogUtils;
import ar.com.hmu.utils.SessionUtils;
import static ar.com.hmu.utils.SessionUtils.handleLogout;
import static ar.com.hmu.utils.ServerStatusUtils.*;

/**
 * Controlador para gestionar el comportamiento del menú principal en forma de mosaico.
 * <p>
 * Esta clase gestiona la interacción del usuario con el menú principal de la aplicación,
 * incluyendo el manejo de mosaicos, opciones de menú, estado del servidor y la sesión del usuario.
 */
public class MainMenuMosaicoController {

    // Elementos del Menú
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu agenteMenu;
    @FXML
    private Menu jefaturaDeServicioMenu;
    @FXML
    private Menu oficinaDePersonalMenu;
    @FXML
    private Menu direccionMenu;
    @FXML
    private MenuItem changeProfileImageMenuItem;
    @FXML
    private MenuItem changePasswordMenuItem;
    @FXML
    private MenuItem logoutMenuItem;
    @FXML
    private MenuItem exitMenuItem;
    @FXML
    private MenuItem licenciasDeUsoMenuItem;
    @FXML
    private MenuItem acercaDeMenuItem;

    // Elementos del Encabezado
    @FXML
    private ImageView agentProfileImage;
    @FXML
    private Text agentFullNameText;
    @FXML
    private Text agentServiceText;
    @FXML
    private Text agentCargoText;

    // Elementos del Mosaico
    @FXML
    private Text currentConnDateTimeText;
    @FXML
    private Text currentConnHostnameText;
    @FXML
    private Text currentConnIPAddressText;
    @FXML
    private VBox aprobacionSolicitudesVBox;
    @FXML
    private VBox notasMemosVBox;
    @FXML
    private VBox partesDiariosVBox;
    @FXML
    private VBox consultaDiagramasDeServicioVBox;
    @FXML
    private VBox diagramacionDeServicioVBox;
    @FXML
    private VBox controlMarcacionesVBox;
    @FXML
    private VBox pasesDeSalidaVBox;
    @FXML
    private VBox omisionesIngresEgresoVBox;
    @FXML
    private VBox faltasJustificadasVBox;
    @FXML
    private VBox faltasInjustificadasVBox;
    @FXML
    private VBox faltasRazonFuerzaMayorVBox;
    @FXML
    private VBox solicitudHorasExtraFCVBox;
    @FXML
    private VBox francosCompensatoriosVBox;
    @FXML
    private VBox reportesVBox;
    @FXML
    private VBox listadoDeAgentesVBox;
    @FXML
    private VBox listadoDeServiciosVBox;
    @FXML
    private VBox abmAgentesVBox;
    @FXML
    private VBox abmServiciosVBox;

    // Botón de cierre de sesión
    @FXML
    private Button logoutButton;

    // Elementos de la barra de estado
    @FXML
    private ImageView serverStatusIcon;
    @FXML
    private Label serverStatusLabel;

    private MainMenuMosaicoService mainMenuMosaicoService;  // Servicio para gestionar la lógica del menú principal
    private DatabaseConnector databaseConnector;  // Necesario para la verificación del estado del servidor.

    // Inyección de dependencias en UsuarioRepository (implementación de patrón «Lazy Loading»)
    private DomicilioRepository domicilioRepository;
    private CargoRepository cargoRepository;
    private ServicioRepository servicioRepository;

    private Usuario usuarioActual;
    private UsuarioService usuarioService;
    private Stage stage;  // Necesario para guardar las propiedades de ventana (asi lo llamo desde LoginController)

    private static final String CONFIG_FILE_NAME = "window.properties";  // Archivo de configuración


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
     * @param databaseConnector El conector de la base de datos.
     */
    public void postInitialize(Usuario usuario, DatabaseConnector databaseConnector, DomicilioRepository domicilioRepository, CargoRepository cargoRepository, ServicioRepository servicioRepository, Stage stage) {
        this.mainMenuMosaicoService = new MainMenuMosaicoService(usuario);

        this.usuarioActual = usuario;
        this.databaseConnector = databaseConnector;
        this.domicilioRepository = domicilioRepository;
        this.cargoRepository = cargoRepository;
        this.servicioRepository = servicioRepository;
        this.stage = stage;  // Guardar la referencia al Stage
        this.usuarioService = new UsuarioService(new UsuarioRepository(databaseConnector, domicilioRepository, cargoRepository, servicioRepository));
        this.mainMenuMosaicoService = new MainMenuMosaicoService(usuario);

        // Configurar el evento de cierre de la ventana
        this.stage.setOnCloseRequest(event -> saveWindowPreferences());

        // Actualizar la información del agente
        agentFullNameText.setText(mainMenuMosaicoService.getAgenteFullName());
        agentServiceText.setText(mainMenuMosaicoService.getServicioNombre());
        agentCargoText.setText(mainMenuMosaicoService.getCargoUsuario());
        agentProfileImage.setImage(mainMenuMosaicoService.getProfileImage());

        // Actualizar la información de conexión
        currentConnDateTimeText.setText(mainMenuMosaicoService.getCurrentConnDateTime());
        currentConnHostnameText.setText(mainMenuMosaicoService.getCurrentConnHostname());
        currentConnIPAddressText.setText(mainMenuMosaicoService.getCurrentConnIPAddress());

        // Configurar visibilidad del mosaico "Alta, Baja y Modificación de Agentes"
        abmAgentesVBox.setVisible(mainMenuMosaicoService.puedeAccederAltaBajaAgentes());
        // Configurar la visibilidad de otros mosaicos según el servicio
        //TODO: Agregar la visibilidad de elementos según tipo de usuario

        // Actualizar el estado del servidor y comenzar el chequeo periódico
        if (databaseConnector == null) {
            throw new IllegalStateException("MainMenuMosaicoController: DatabaseConnector no está configurado. No se puede verificar el estado del servidor.");
        }
        updateServerStatusUI(databaseConnector, serverStatusLabel, serverStatusIcon);
        startPeriodicServerCheck(databaseConnector, serverStatusLabel, serverStatusIcon);

        // Configurar el evento de cierre de la ventana
        this.stage.setOnCloseRequest(event -> saveWindowPreferences());

        // Otros códigos de inicialización
        loadWindowPreferences(); // Cargar preferencias de la ventana en este punto

    }

    /**
     * Método que define una ruta y nombre de archivo, según el sistema operativo en el cuals e ejecute la aplicación
     * <p>
     * Si el sistema es windows, la configuración se almacena dentro de LocalAppData del usuario.
     * Si el sistema es UNIX-Like, se almacena en la carpeta .config dentro del HOME de usuario.
     * @return Ruta y nombre de archivo (según sistema operativo)
     */
    private File getConfigFilePath() {
        String osName = System.getProperty("os.name").toLowerCase();
        String baseDir;

        if (osName.contains("win")) { // Determinar si Windows u otro OS
            // Windows: %LocalAppData%\CFG_FOLDER_NAME\UUID_usuario\window.properties
            baseDir = System.getenv("LocalAppData");
        } else {
            // Unix/Linux/FreeBSD/Mac: $HOME/.config/CFG_FOLDER_NAME/UUID_usuario/window.properties
            baseDir = System.getenv("HOME") + File.separator + ".config";
        }

        // Crear la ruta completa: baseDir + /CFG_FOLDER_NAME/UUID_usuario/window.properties
        //File userConfigDir = new File(baseDir, AppInfo.CFG_FOLDER_NAME + File.separator + usuarioActual.getId().toString());
        String folderId = String.valueOf(usuarioActual.getCuil());
        //String folderId = String.valueOf(usuarioActual.getId());
        File userConfigDir = new File(baseDir, AppInfo.CFG_FOLDER_NAME + File.separator + folderId);

        if (!userConfigDir.exists()) {
            userConfigDir.mkdirs(); // Crear la ruta si no existe
        }

        return new File(userConfigDir, CONFIG_FILE_NAME);
    }


    /**
     * Método que configura el tamaño de la ventana al momento de cerrarse.
     */
    public void saveWindowPreferences() {
        Properties properties = new Properties();
        properties.setProperty("width", String.valueOf(stage.getWidth()));
        properties.setProperty("height", String.valueOf(stage.getHeight()));
        properties.setProperty("x", String.valueOf(stage.getX()));
        properties.setProperty("y", String.valueOf(stage.getY()));

        String cuil = String.valueOf(usuarioActual.getCuil()); // Convierte el CUIL a String

        File configFile = getConfigFilePath();
        try (FileOutputStream out = new FileOutputStream(configFile)) {
            properties.store(out, "# Window Preferences for User CUIL " + usuarioActual.getCuil());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Método que carga las preferencias de la ventana (dimensiones y posición).
     */
    public void loadWindowPreferences() {
        Properties properties = new Properties();
        File configFile = getConfigFilePath();

        if (configFile.exists()) {
            try (FileInputStream in = new FileInputStream(configFile)) {
                properties.load(in);

                double width = Double.parseDouble(properties.getProperty("window.width", "800"));
                double height = Double.parseDouble(properties.getProperty("window.height", "600"));
                double x = Double.parseDouble(properties.getProperty("window.x", "100"));
                double y = Double.parseDouble(properties.getProperty("window.y", "100"));

                stage.setWidth(width);
                stage.setHeight(height);
                stage.setX(x);
                stage.setY(y);
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace(); // Error en la carga del archivo de configuración, se usan valores predeterminados
            }
        }
    }


    /**
     * Método para obtener la ruta y nombre del archivo
     * @param cuil Nro de CUIL de un usuario
     * @return la ruta y nombre del archivo
     */
    private String getUserPreferencesFilePath(String cuil) {
        String directoryPath = System.getProperty("user.home") + "/.app_config/";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return directoryPath + cuil + "_window.properties";
    }


    /**
     * Configura los manejadores de eventos para los componentes del menú y mosaicos.
     */
    private void setupEventHandlers() {
        // Configura la funcionalidad del menú: Archivo -> Modificar contraseña
        changePasswordMenuItem.setOnAction(event -> handleChangePassword());

        // Configura la funcionalidad del menú: Archivo -> Modificar imagen de perfil
        changeProfileImageMenuItem.setOnAction(event -> showModuleUnderConstructionAlert());

        // Configura la funcionalidad del menú: Archivo -> Cerrar sesión
        logoutMenuItem.setOnAction(event -> handleLogout((Stage) menuBar.getScene().getWindow()));

        // Otro método (no me gusta por contra intuitivo)
        /* Obtener el Scene desde cualquier nodo de la escena
        logoutMenuItem.setOnAction(event -> {
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            handleLogout(stage);
        });
        */

        // Configura la funcionalidad del menú: Archivo -> Salir
        exitMenuItem.setOnAction(event -> System.exit(0));

        // Configura la funcionalidad del menú: Ayuda -> Licencias de uso
        licenciasDeUsoMenuItem.setOnAction(event -> handleLicenciasDeUso());

        // Configura la funcionalidad del menú: Ayuda -> Acerca de
        acercaDeMenuItem.setOnAction(event -> handleAcercaDeAromito());

        // Configura los mosaicos para mostrar una alerta de "Módulo en construcción"
        aprobacionSolicitudesVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        notasMemosVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        partesDiariosVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        consultaDiagramasDeServicioVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        diagramacionDeServicioVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        controlMarcacionesVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        pasesDeSalidaVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        omisionesIngresEgresoVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        faltasJustificadasVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        faltasInjustificadasVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        faltasRazonFuerzaMayorVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        solicitudHorasExtraFCVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        francosCompensatoriosVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        reportesVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        listadoDeAgentesVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        listadoDeServiciosVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());
        abmAgentesVBox.setOnMouseClicked(event -> handleAbmAgentes());
        abmServiciosVBox.setOnMouseClicked(event -> showModuleUnderConstructionAlert());

        // Configura la funcionalidad del botón "Cerrar sesión"
        logoutButton.setOnAction(event -> handleLogout((Stage) logoutButton.getScene().getWindow()));

    }

    /**
     * Maneja la opción "Modificar Contraseña" abriendo una nueva ventana para cambiar la contraseña.
     */
    private void handleChangePassword() {
        PasswordDialogUtils.showChangePasswordDialog(usuarioActual, usuarioService,
                (message) -> {}, // Callback vacío para continuar después del cambio de contraseña exitoso
                () -> SessionUtils.handleLogout((Stage) logoutButton.getScene().getWindow()) // Callback para cerrar la sesión si se cancela
        );

    }

    /**
     * Muestra una alerta indicando que el módulo está en construcción.
     */
    private void showModuleUnderConstructionAlert() {
        AlertUtils.showInfo("Estamos trabajando para Usted\nMódulo en construcción");
    }


    /**
     * Método para el alta, baja, modificación de usuarios (agentes).
     * Este método carga la ventana de ABM de usuarios y la muestra al usuario.
     */
    private void handleAbmAgentes() {
        try {
            // Cargar el archivo FXML del ABM de Usuarios
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/abmUsuarios.fxml"));
            Parent abmUsuariosRoot = loader.load();

            // Obtener el controlador de la nueva vista
            AbmUsuariosController abmUsuariosController = loader.getController();

            // TODO: pasar el usuario logueado para verificación adicional
            // Por ejemplo, si el usuario es de tipo empleado, no debería poder ingresar, o bien permitirle modificar solo sus propios datos
            // abmUsuariosController.setUsuarioActual(usuarioActual);  // meditarlo...

            // Crear una nueva escena y un nuevo Stage (ventana)
            Stage stage = new Stage();
            Scene scene = new Scene(abmUsuariosRoot);
            stage.setScene(scene);
            stage.setTitle("Alta, Baja y Modificación de Agentes" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(abmAgentesVBox.getScene().getWindow()); // Con esto, establecemos la ventana actual como propietaria, evitando múltiples instancias

            // Mostrar la nueva ventana
            stage.show();
        } catch (IOException e) {
            // Mostrar un error si no se puede cargar la vista
            AlertUtils.showErr("Error al cargar la pantalla de ABM de Usuarios: " + e.getMessage());
            e.printStackTrace(); // Imprimir la excepción para más detalles en la consola
        }
    }


    /**
     * Muestra la ventana de "Licencias de uso".
     */
    private void handleLicenciasDeUso() {
        try {
            // Cargar el FXML de la ventana "Licencias de uso"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/licenciasDeUso.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Licencias de uso" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal hasta que se cierre esta
            stage.setResizable(false);
            // Establecer el ícono de la aplicación
            stage.getIcons().add(new Image(getClass().getResourceAsStream("app-icon.png")));

            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Mostrar la ventana
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErr("Error al cargar la ventana 'Licencias de uso':\n" + e.getMessage());
        }
    }

    /**
     * Muestra la ventana de "Acerca de...".
     */
    private void handleAcercaDeAromito() {
        try {
            // Cargar el FXML de la ventana "Acerca de"
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/about.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Acerca de " + AppInfo.PRG_SHORT_TITLE + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal hasta que se cierre esta
            stage.setResizable(false);
            // Establecer el ícono de la aplicación
            stage.getIcons().add(new Image(getClass().getResourceAsStream("app-icon.png")));

            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Mostrar la ventana
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErr("Error al cargar la ventana 'Acerca de':\n" + e.getMessage());
        }
    }

}
