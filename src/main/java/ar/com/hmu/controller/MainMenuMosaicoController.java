package ar.com.hmu.controller;

import java.io.IOException;

import ar.com.hmu.service.*;
import ar.com.hmu.util.*;
import javafx.event.Event;
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
import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.*;

import static ar.com.hmu.util.SessionUtils.handleLogout;

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
    private Menu jefeDeServicioMenu;
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
    private MenuItem listadoAgentesMenuItem;
    @FXML
    private MenuItem listadoServiciosMenuItem;
    @FXML
    private MenuItem abmAgentesMenuItem;
    @FXML
    private MenuItem abmServiciosMenuItem;
    @FXML
    private MenuItem abmCargosMenuItem;
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

    private ServerStatusUtils serverStatusUtils;

    // Repositorios
    private DomicilioRepository domicilioRepository;
    private CargoRepository cargoRepository;
    private ServicioRepository servicioRepository;
    private UsuarioRepository usuarioRepository;
    private RoleRepository roleRepository;

    // Servicios
    private UsuarioService usuarioService;
    private CargoService cargoService;
    private ServicioService servicioService;
    private DomicilioService domicilioService;
    private RoleService roleService;

    private Usuario usuarioActual;

    // Referencia a la ventana principal
    private static Stage primaryStage;  // Necesario para guardar las propiedades de ventana (asi lo llamo desde LoginController)

    // Nueva instancia para manejar preferencias
    private PreferencesManager preferencesManager;

    // Almacenamiento de las propiedades de la ventana (tamaño y posición)
    private static final String WINDOW_WIDTH_KEY = "window.width";
    private static final String WINDOW_HEIGHT_KEY = "window.height";
    private static final String WINDOW_X_KEY = "window.x";
    private static final String WINDOW_Y_KEY = "window.y";


    public void setDatabaseConnector(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    public void setServices(UsuarioService usuarioService, CargoService cargoService, ServicioService servicioService, DomicilioService domicilioService, RoleService roleService) {
        this.usuarioService = usuarioService;
        this.cargoService = cargoService;
        this.servicioService = servicioService;
        this.domicilioService = domicilioService;
        this.roleService = roleService;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }


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
     * @param stage               El Stage principal de la aplicación.
     */
    public void postInitialize(Usuario usuario, Stage stage) {
        this.usuarioActual = usuario;
        this.primaryStage = stage;  // Guardo la referencia al Stage

        this.mainMenuMosaicoService = new MainMenuMosaicoService(usuario);

        try {
            usuarioService.loadAdditionalUserData(usuarioActual);
        } catch (ServiceException e) {
            AlertUtils.showErr(e.getMessage());
        }

        // Inicializar PreferencesManager con el CUIL del usuario
        String userId = String.valueOf(usuarioActual.getCuil());
        this.preferencesManager = new PreferencesManager(userId);

        // Configurar el evento de cierre de la ventana
        this.primaryStage.setOnCloseRequest(event -> saveWindowPreferences());

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
        serverStatusUtils = new ServerStatusUtils(databaseConnector, serverStatusLabel, serverStatusIcon);
        serverStatusUtils.updateServerStatusUI();
        serverStatusUtils.startPeriodicServerCheck();

        // Configurar el evento de cierre de la ventana
        this.primaryStage.setOnCloseRequest(event -> saveWindowPreferences());

        // Cargar preferencias de la ventana en este punto
        loadWindowPreferences();

    }

    /**
     * Método que detiene el verificador de estado del servidor.
     * La idea, es llamar a este método cuando se vaya a eliminar el controlador
     */
    public void cleanup() {
        if (serverStatusUtils != null) {
            serverStatusUtils.stop();
        }
    }

    /**
     * Método que configura el tamaño de la ventana al momento de cerrarse.
     */
    public void saveWindowPreferences() {
        preferencesManager.put(WINDOW_WIDTH_KEY, String.valueOf(primaryStage.getWidth()));
        preferencesManager.put(WINDOW_HEIGHT_KEY, String.valueOf(primaryStage.getHeight()));
        preferencesManager.put(WINDOW_X_KEY, String.valueOf(primaryStage.getX()));
        preferencesManager.put(WINDOW_Y_KEY, String.valueOf(primaryStage.getY()));
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

            primaryStage.setWidth(width);
            primaryStage.setHeight(height);
            primaryStage.setX(x);
            primaryStage.setY(y);
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
        jefeDeServicioMenu.setVisible(false);
        oficinaDePersonalMenu.setVisible(false);
        direccionMenu.setVisible(false);

        // Configurar visibilidad de menús según roles
        agenteMenu.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        jefeDeServicioMenu.setVisible(usuarioActual.hasRole(TipoUsuario.JEFEDESERVICIO));
        oficinaDePersonalMenu.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));
        direccionMenu.setVisible(usuarioActual.hasRole(TipoUsuario.DIRECCION));

        // Configurar visibilidad de mosaicos (VBox) según roles
        aprobacionSolicitudesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.JEFEDESERVICIO, TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        aprobacionSolicitudesVBox.setManaged(usuarioActual.hasRole(TipoUsuario.JEFEDESERVICIO, TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        notasMemosVBox.setVisible(usuarioActual.hasAnyRoleData());
        notasMemosVBox.setManaged(usuarioActual.hasAnyRoleData());
        partesDiariosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));
        partesDiariosVBox.setManaged(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));
        consultaDiagramasDeServicioVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        consultaDiagramasDeServicioVBox.setManaged(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        diagramacionDeServicioVBox.setVisible(usuarioActual.hasRole(TipoUsuario.JEFEDESERVICIO));
        diagramacionDeServicioVBox.setManaged(usuarioActual.hasRole(TipoUsuario.JEFEDESERVICIO));
        controlMarcacionesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        controlMarcacionesVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        pasesDeSalidaVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        pasesDeSalidaVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        omisionesIngresEgresoVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        omisionesIngresEgresoVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        faltasJustificadasVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        faltasJustificadasVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        faltasInjustificadasVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        faltasInjustificadasVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        faltasRazonFuerzaMayorVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        faltasRazonFuerzaMayorVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        solicitudHorasExtraFCVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        solicitudHorasExtraFCVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        francosCompensatoriosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        francosCompensatoriosVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        reportesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.AGENTE));
        reportesVBox.setManaged(usuarioActual.hasRole(TipoUsuario.AGENTE));
        listadoDeAgentesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        listadoDeAgentesVBox.setManaged(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        listadoDeServiciosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        listadoDeServiciosVBox.setManaged(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL, TipoUsuario.DIRECCION));
        abmAgentesVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));
        abmAgentesVBox.setManaged(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));
        abmServiciosVBox.setVisible(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));
        abmServiciosVBox.setManaged(usuarioActual.hasRole(TipoUsuario.OFICINADEPERSONAL));

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

        // Configura la funcionalidad del menú: Oficina de Personal -> Listado de Agentes
        listadoAgentesMenuItem.setOnAction(this::handleListadoDeAgentes);

        // Configura la funcionalidad del menú: Oficina de Personal -> Listado de Servicios
        listadoServiciosMenuItem.setOnAction(this::handleListadoDeServicios);

        // Configura la funcionalidad del menú: Oficina de Personal -> ABM de Agentes
        abmAgentesMenuItem.setOnAction(this::handleAbmAgentes);

        // Configura la funcionalidad del menú: Oficina de Personal -> ABM de Servicios
        abmServiciosMenuItem.setOnAction(this::handleAbmServicios);

        // Configura la funcionalidad del menú: Oficina de Personal -> ABM de Cargos
        abmCargosMenuItem.setOnAction(this::handleAbmCargos);

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
        listadoDeAgentesVBox.setOnMouseClicked(this::handleListadoDeAgentes);
        listadoDeServiciosVBox.setOnMouseClicked(this::handleListadoDeServicios);
        abmAgentesVBox.setOnMouseClicked(this::handleAbmAgentes);
        abmServiciosVBox.setOnMouseClicked(this::handleAbmServicios);

        // Configura la funcionalidad del botón "Cerrar sesión"
        logoutButton.setOnAction(event -> handleLogoutAction());

        // Configura acceso al controlador de pruebas
        francosCompensatoriosVBox.setOnMouseClicked(this::handleTestController);

    }


    private void handleLogoutAction() {
        //Guardar propiedades de ventana
        saveWindowPreferences();

        // Limpieza de recursos
        cleanup();

        // Cerrar la sesión
        handleLogout((Stage) logoutButton.getScene().getWindow());
    }


    /**
     * Maneja la opción "Modificar Contraseña" abriendo una nueva ventana para cambiar la contraseña.
     */
    @FXML
    private void handleChangePassword() {
        PasswordDialogUtils.showChangePasswordDialog(usuarioActual, usuarioService,
                (message) -> {}, // Callback vacío para continuar después del cambio de contraseña exitoso
                () -> SessionUtils.handleLogout((Stage) logoutButton.getScene().getWindow()) // Callback para cerrar la sesión si se cancela
        );

    }

    /**
     * Muestra una alerta indicando que el módulo está en construcción.
     */
    @FXML
    private void showModuleUnderConstructionAlert() {
        AlertUtils.showInfo("Estamos trabajando para Usted\nMódulo en construcción");
    }


    /**
     * Método para el alta, baja, modificación de usuarios (agentes).
     * Este método carga la ventana de ABM de usuarios y la muestra al usuario.
     */
    @FXML
    private void handleAbmAgentes(Event event) {
        try {
            // Usa los servicios ya inicializados
            UsuarioService usuarioService = this.usuarioService;
            CargoService cargoService = this.cargoService;
            ServicioService servicioService = this.servicioService;
            DomicilioService domicilioService = this.domicilioService;
            RoleService roleService = this.roleService;

            // Configurar la fábrica de controladores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abmUsuario.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == AbmUsuarioController.class) {
                    AbmUsuarioController controller = new AbmUsuarioController();
                    controller.setServices(usuarioService, cargoService, servicioService, domicilioService, roleService);
                    return controller;
                } else {
                    // Manejo predeterminado
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Carga del FXML después de configurada la Fábrica
            Parent abmUsuariosRoot = loader.load();

            // Crear una nueva escena y un nuevo Stage (ventana)
            Stage stage = new Stage();  // Alternativa, que es lo mismo:  stage.setScene(new Scene(root))
            Scene scene = new Scene(abmUsuariosRoot);
            stage.setScene(scene);
            stage.setTitle("Alta, Baja y Modificación de Agentes" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.initModality(Modality.WINDOW_MODAL);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow()); // Con esto, establecemos la ventana actual como propietaria, evitando múltiples instancias. Supuestamente, es mejor que lo que hacía antes:  stage.initOwner(abmAgentesVBox.getScene().getWindow());
            //stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.initOwner(abmAgentesVBox.getScene().getWindow());
            // Mostrar la nueva ventana
            stage.show();  // También se sugiere el uso de:  stage.showAndWait(), lo que tal vez por ahí estaría bueno en conjunto con:  stage.initModality(Modality.WINDOW_MODAL)
        } catch (IOException e) {
            // Mostrar un error si no se puede cargar la vista
            AlertUtils.showErr("Error al cargar la pantalla de ABM de Usuarios: " + e.getMessage());
            e.printStackTrace(); // Imprimir la excepción para más detalles en la consola
        }
    }


    @FXML
    private void handleAbmCargos(Event event) {
        // Usa los servicios ya inicializados
        CargoService cargoService = this.cargoService;

        try {
            // Configurar la fábrica de controladores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abmCargo.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == AbmCargoController.class) {
                    AbmCargoController controller = new AbmCargoController();
                    controller.setServices(cargoService);
                    return controller;
                } else {
                    // Manejo predeterminado
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // Carga del FXML después de configurada la Fábrica
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestión de Cargos" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErr("Error al cargar la ventana de gestión de cargos: " + e.getMessage());
        }
    }

    @FXML
    private void handleListadoDeAgentes(Event event) {
        try {
            // Usa los servicios ya inicializados
            UsuarioService usuarioService = this.usuarioService;
            CargoService cargoService = this.cargoService;
            ServicioService servicioService = this.servicioService;
            DomicilioService domicilioService = this.domicilioService;
            RoleService roleService = this.roleService;

            // Configurar la fábrica de controladores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listaUsuarios.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == ListaUsuariosController.class) {
                    ListaUsuariosController controller = new ListaUsuariosController();
                    controller.setServices(usuarioService, cargoService, servicioService, domicilioService, roleService);
                    return controller;
                } else {
                    // Manejo predeterminado
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            // Carga del FXML después de configurada la Fábrica
            Parent root = loader.load();

            ListaUsuariosController controller = loader.getController();
            controller.setServices(usuarioService, cargoService, servicioService, domicilioService, roleService);

            Stage stage = new Stage();
            stage.setTitle("Listado de Agentes" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setWidth(590.0);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
            // Manejo de errores
        }
    }

    @FXML
    private void handleListadoDeServicios(Event event) {
        try {
            // Usa los servicios ya inicializados
            UsuarioService usuarioService = this.usuarioService;
            CargoService cargoService = this.cargoService;
            ServicioService servicioService = this.servicioService;
            DomicilioService domicilioService = this.domicilioService;
            RoleService roleService = this.roleService;

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/listaServicios.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == ListaServiciosController.class) {
                    ListaServiciosController controller = new ListaServiciosController();
                    controller.setServices(usuarioService, cargoService, servicioService, domicilioService, roleService);
                    return controller;
                } else {
                    // Manejo predeterminado
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Carga del FXML después de configurada la Fábrica
            Parent root = loader.load();

            ListaServiciosController controller = loader.getController();
            controller.setServicioService(servicioService);

            Stage stage = new Stage();
            stage.setTitle("Lista de Servicios" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setWidth(500.0);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleAbmServicios(Event event) {
        // Usa los servicios ya inicializados
        ServicioService servicioService = this.servicioService;

        try {
            // Configurar la fábrica de controladores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abmServicio.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == AbmServicioController.class) {
                    AbmServicioController controller = new AbmServicioController();
                    controller.setServices(servicioService);
                    return controller;
                } else {
                    // Manejo predeterminado
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Carga del FXML después de configurada la Fábrica
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Gestión de Servicios" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            //stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErr("Error al cargar la ventana de gestión de servicios: " + e.getMessage());
        }
    }


    /**
     * Muestra la ventana de "Licencias de uso".
     */
    @FXML
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
    @FXML
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

    @FXML
    private void handleTestController(Event event) {
        try {
            // Usa los servicios ya inicializados
            UsuarioService usuarioService = this.usuarioService;
            CargoService cargoService = this.cargoService;
            ServicioService servicioService = this.servicioService;
            DomicilioService domicilioService = this.domicilioService;
            RoleService roleService = this.roleService;

            // Configurar la fábrica de controladores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/test.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == TestController.class) {
                    TestController controller = new TestController();
                    controller.setServices(usuarioService, cargoService, servicioService, domicilioService, roleService);
                    return controller;
                } else {
                    // Manejo predeterminado
                    try {
                        return controllerClass.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            // Carga del FXML después de configurada la Fábrica
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Test Controller" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErr("Error al cargar la ventana de gestión de cargos: " + e.getMessage());
        }
    }

}

