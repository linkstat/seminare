package ar.com.hmu.controller;

import java.io.IOException;

import ar.com.hmu.model.Domicilio;
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

import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.model.Cargo;
import ar.com.hmu.model.Servicio;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.*;
import ar.com.hmu.service.MainMenuMosaicoService;
import ar.com.hmu.service.UsuarioService;
import ar.com.hmu.util.AlertUtils;
import ar.com.hmu.util.AppInfo;
import ar.com.hmu.util.PasswordDialogUtils;
import ar.com.hmu.util.PreferencesManager;
import ar.com.hmu.util.SessionUtils;
import static ar.com.hmu.util.SessionUtils.handleLogout;
import static ar.com.hmu.util.ServerStatusUtils.*;

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

    // Inyección de dependencias en UsuarioRepository (implementación de patrón «Lazy Loading») / Ya no lo uso. Cambio de enfoque. Borrar después.
    private DomicilioRepository domicilioRepository;
    private CargoRepository cargoRepository;
    private ServicioRepository servicioRepository;

    private Usuario usuarioActual;
    private UsuarioService usuarioService;
    private Stage stage;  // Necesario para guardar las propiedades de ventana (asi lo llamo desde LoginController)

    // Nueva instancia para manejar preferencias
    private PreferencesManager preferencesManager;
    // Almacenamiento de las propiedades de la ventana (tamaño y posición)
    private static final String WINDOW_WIDTH_KEY = "window.width";
    private static final String WINDOW_HEIGHT_KEY = "window.height";
    private static final String WINDOW_X_KEY = "window.x";
    private static final String WINDOW_Y_KEY = "window.y";



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
     * @param databaseConnector   El conector de la base de datos.
     * @param stage               El Stage principal de la aplicación.
     */
    public void postInitialize(Usuario usuario, DatabaseConnector databaseConnector, RolRepository rolRepository, Stage stage) {
        this.mainMenuMosaicoService = new MainMenuMosaicoService(usuario);

        this.usuarioActual = usuario;
        this.databaseConnector = databaseConnector;
        //this.domicilioRepository = domicilioRepository;
        //this.cargoRepository = cargoRepository;
        //this.servicioRepository = servicioRepository;
        this.stage = stage;  // Guardar la referencia al Stage
        this.usuarioService = new UsuarioService(new UsuarioRepository(databaseConnector, rolRepository));
        this.mainMenuMosaicoService = new MainMenuMosaicoService(usuario);

        // Cargar Servicio, Cargo y Domicilio
        this.usuarioService = new UsuarioService(
                new UsuarioRepository(databaseConnector, rolRepository),
                new ServicioRepository(databaseConnector),
                new CargoRepository(databaseConnector),
                new DomicilioRepository(databaseConnector)
        );

        try {
            usuarioService.loadAdditionalUserData(usuarioActual);
        } catch (ServiceException e) {
            AlertUtils.showErr(e.getMessage());
        }

        // Inicializar PreferencesManager con el CUIL del usuario
        String userId = String.valueOf(usuarioActual.getCuil());
        this.preferencesManager = new PreferencesManager(userId);

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

        //Configurar visibilidad
        configurarVisibilidadMenus();

        // Actualizar el estado del servidor y comenzar el chequeo periódico
        if (databaseConnector == null) {
            throw new IllegalStateException("MainMenuMosaicoController: DatabaseConnector no está configurado. No se puede verificar el estado del servidor.");
        }
        updateServerStatusUI(databaseConnector, serverStatusLabel, serverStatusIcon);
        startPeriodicServerCheck(databaseConnector, serverStatusLabel, serverStatusIcon);

        // Configurar el evento de cierre de la ventana
        this.stage.setOnCloseRequest(event -> saveWindowPreferences());

        // Cargar preferencias de la ventana en este punto
        loadWindowPreferences();

    }


    /**
     * Método que configura el tamaño de la ventana al momento de cerrarse.
     */
    public void saveWindowPreferences() {
        preferencesManager.put(WINDOW_WIDTH_KEY, String.valueOf(stage.getWidth()));
        preferencesManager.put(WINDOW_HEIGHT_KEY, String.valueOf(stage.getHeight()));
        preferencesManager.put(WINDOW_X_KEY, String.valueOf(stage.getX()));
        preferencesManager.put(WINDOW_Y_KEY, String.valueOf(stage.getY()));
    }


    /**
     * Método que carga las preferencias de la ventana (dimensiones y posición).
     */
    private void loadWindowPreferences() {
        try {
            double width = Double.parseDouble(preferencesManager.get(WINDOW_WIDTH_KEY, "800"));
            double height = Double.parseDouble(preferencesManager.get(WINDOW_HEIGHT_KEY, "600"));
            double x = Double.parseDouble(preferencesManager.get(WINDOW_X_KEY, "100"));
            double y = Double.parseDouble(preferencesManager.get(WINDOW_Y_KEY, "100"));

            stage.setWidth(width);
            stage.setHeight(height);
            stage.setX(x);
            stage.setY(y);
        } catch (NumberFormatException e) {
            e.printStackTrace(); // Error en la carga de preferencias, se usan valores predeterminados
            // Valores predeterminados ya están establecidos en el método get de PreferencesManager
        }
    }


    /**
     * Configura la visibilidad de elementos según los roles del usuario
     */
    private void configurarVisibilidadMenus() {
        // Inicializar todos los menús como no visibles
        agenteMenu.setVisible(false);
        jefaturaDeServicioMenu.setVisible(false);
        oficinaDePersonalMenu.setVisible(false);
        direccionMenu.setVisible(false);

        // Configurar visibilidad de menús según roles
        agenteMenu.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        jefaturaDeServicioMenu.setVisible(usuarioActual.hasRole(TipoUsuario.JEFATURA_DE_SERVICIO));
        oficinaDePersonalMenu.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL));
        direccionMenu.setVisible(usuarioActual.hasRole(TipoUsuario.DIRECCION));

        // Configurar visibilidad de mosaicos (VBox) según roles
        aprobacionSolicitudesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.JEFATURA_DE_SERVICIO, TipoUsuario.OFICINA_DE_PERSONAL, TipoUsuario.DIRECCION));
        notasMemosVBox.setVisible(usuarioActual.hasAnyRole());
        partesDiariosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL));
        consultaDiagramasDeServicioVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL, TipoUsuario.DIRECCION));
        diagramacionDeServicioVBox.setVisible(usuarioActual.hasRole(TipoUsuario.JEFATURA_DE_SERVICIO));
        controlMarcacionesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        pasesDeSalidaVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        omisionesIngresEgresoVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        faltasJustificadasVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        faltasInjustificadasVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        faltasRazonFuerzaMayorVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        solicitudHorasExtraFCVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        francosCompensatoriosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        reportesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.EMPLEADO));
        listadoDeAgentesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL, TipoUsuario.DIRECCION));
        listadoDeServiciosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL, TipoUsuario.DIRECCION));
        abmAgentesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL));
        abmServiciosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINA_DE_PERSONAL));

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abmUsuarios.fxml"));
            Parent abmUsuariosRoot = loader.load();

            // Obtener el controlador de la nueva vista
            AbmUsuariosController abmUsuariosController = loader.getController();

            // TODO: pasar el usuario logueado para verificación adicional
            // Por ejemplo, si el usuario es de tipo empleado, no debería poder ingresar.
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/licenciasDeUso.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Licencias de uso" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal hasta que se cierre esta
            stage.setResizable(false);
            // Establecer el ícono de la aplicación
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppInfo.ICON_IMAGE)));

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/about.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Acerca de " + AppInfo.PRG_SHORT_TITLE + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal hasta que se cierre esta
            stage.setResizable(false);
            // Establecer el ícono de la aplicación
            stage.getIcons().add(new Image(getClass().getResourceAsStream(AppInfo.ICON_IMAGE)));

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
