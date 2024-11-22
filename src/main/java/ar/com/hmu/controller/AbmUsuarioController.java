package ar.com.hmu.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;

import ar.com.hmu.constants.UsuarioCreationResult;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.service.*;
import ar.com.hmu.util.AlertUtils;
import ar.com.hmu.util.AppInfo;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import ar.com.hmu.constants.NombreServicio;
import ar.com.hmu.constants.TipoUsuario;
import ar.com.hmu.model.*;
import ar.com.hmu.util.CuilUtils;
import ar.com.hmu.util.ImageUtils;

public class AbmUsuarioController implements Initializable {

    // Contenedor
    @FXML
    private BorderPane rootPane;

    // Sección de Búsqueda
    @FXML private ComboBox<Usuario> busquedaComboBox;
    @FXML private Button nuevoAgenteButton;

    // Imagen de Perfil
    @FXML private ImageView imagenPerfilImageView;
    @FXML private Button cargarImagenButton;
    @FXML private Button revertirImagenButton;
    @FXML private Button eliminarImagenButton;

    // Datos Personales
    @FXML private TextField cuilTextField;
    @FXML private TextField apellidosTextField;
    @FXML private TextField nombresTextField;
    @FXML private TextField mailTextField;
    @FXML private TextField telTextField;
    @FXML private ComboBox<Sexo> sexoComboBox;

    // Domicilio
    @FXML private ComboBox<String> domCalleComboBox;
    @FXML private TextField domNumeracionField;
    @FXML private CheckBox domSinNumeroCheckBox;
    @FXML private ComboBox<String> domBarrioComboBox;
    @FXML private ComboBox<String> domCiudadComboBox;
    @FXML private ComboBox<String> domLocalidadComboBox;
    @FXML private ComboBox<String> domProvinciaComboBox;

    // Tipo de Usuario y Asignación de Roles
    @FXML private ComboBox<TipoUsuario> tipoUsuarioComboBox;
    @FXML private CheckBox rolAgenteCheckBox;
    @FXML private CheckBox rolJefeServicioCheckBox;
    @FXML private CheckBox rolOficinaPersonalCheckBox;
    @FXML private CheckBox rolDireccionCheckBox;

    // Gestión de Cargo
    @FXML private ComboBox<Cargo> cargoComboBox;
    @FXML private Button gestionarCargosButton;

    // Gestión de Servicio
    @FXML private ComboBox<Servicio> servicioComboBox;
    @FXML private Button gestionarServiciosButton;

    // Sección de Acciones
    @FXML private Button resetPasswdButton;
    @FXML private Button altaModButton;
    @FXML private Button cancelarButton;
    @FXML private Button eliminarButton;

    // Variables auxiliares
    private ObservableList<Usuario> usuariosList = FXCollections.observableArrayList();
    private FilteredList<Usuario> filteredUsuariosList;
    private ObservableList<Sexo> sexosList = FXCollections.observableArrayList(Sexo.values());
    private ObservableList<TipoUsuario> tiposUsuarioList = FXCollections.observableArrayList(TipoUsuario.values());
    private ObservableList<Cargo> cargosList = FXCollections.observableArrayList();
    private ObservableList<Servicio> serviciosList = FXCollections.observableArrayList();
    private String storedNumeracion = null;  // Almaceno el valor anterior de numeración de calle para uso en el comportamiento de checkBoxSinNumero

    // Inicializar el conjunto de roles
    Set<Rol> rolesSeleccionados = new HashSet<>();

    // Imagen de perfil
    private File imagenPerfilFile;
    private Image imagenPerfilOriginal;

    // Banderas para saber cuando un campo de formulario cambió
    private boolean isLoading = false;  // Indica si estamos cargando datos en el formulario
    private boolean isFormModified = false;  // para cambios posteriores

    // Bandera que vamos a necesitar en filtrarUsuarios() para evitar una recursión infinita que se me daba
    private boolean isFiltering = false;

    // Banderas de modo
    private boolean isAltaMode = false;
    private boolean isCancelMode = false;
    private boolean isModificacionMode = false;

    // Servicios
    private UsuarioService usuarioService;
    private CargoService cargoService;
    private ServicioService servicioService;
    private DomicilioService domicilioService;
    private RolService rolService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Verificar que los servicios han sido inyectados
        if (usuarioService == null || cargoService == null || servicioService == null || domicilioService == null) {
            throw new IllegalStateException("Los servicios no han sido inicializados. Llama a setServices() antes de initialize().");
        }

        // Cargar datos desde las clases Service
        cargarUsuarios();
        cargarCargos();
        cargarServicios();

        // Configuración inicial y carga de datos
        addChangeListeners(); // Solo se llama aquí, antes de que cualquier dato se cargue
        setControlsEnabled(false);
        resetInterface();

        // Event handler para la tecla ESC
        rootPane.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                onCancelar(null);
                event.consume();
            }
        });

        // Configurar el campo de texto para el CUIL
        CuilUtils.configureCuilField(cuilTextField);

        // Inicializar ComboBoxes
        sexoComboBox.setItems(sexosList);
        tipoUsuarioComboBox.setItems(tiposUsuarioList);
        cargoComboBox.setItems(cargosList);
        servicioComboBox.setItems(serviciosList);

        // Create the FilteredList
        filteredUsuariosList = new FilteredList<>(usuariosList, p -> true);

        // Set the items of the ComboBox to the FilteredList
        busquedaComboBox.setItems(filteredUsuariosList);

        // Configurar busquedaComboBox
        configurarBusquedaComboBox();

        // Deshabilitar controles al inicio
        setControlsEnabled(false);

        // Estado inicial de los botones de acción
        altaModButton.setVisible(false);    // Oculto al comienzo
        eliminarButton.setVisible(false);   // Oculto al comienzo

        cancelarButton.setVisible(true);    // Visible al comienzo
        cancelarButton.setText("Salir");    // Pero con el texto "Salir"

        // Cargar imagen por defecto
        imagenPerfilOriginal = new Image(getClass().getResourceAsStream("/images/loginImage.png"));
        imagenPerfilImageView.setImage(imagenPerfilOriginal);

        // Eventos adicionales
        tipoUsuarioComboBox.setOnAction(this::onTipoUsuarioSelected);
        nuevoAgenteButton.setOnAction(this::onNuevoAgente);
        cancelarButton.setOnAction(this::onCancelar);
        altaModButton.setOnAction(event -> {
            try { onAltaMod(event); } catch (ServiceException e) { mostrarError("Error al realizar la operación: " + e.getMessage()); }
        });

        // Habilitar solo el ComboBox y el botón "Nuevo Agente"
        busquedaComboBox.setDisable(false);
        nuevoAgenteButton.setDisable(false);

    }


    /**
     * Método para cargar Usuarios desde la base de datos a través de UsuarioService
     */
    private void cargarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioService.readAll();
            //List<Usuario> usuariosPrimarios = usuarioService.readAllPrimarios();
            usuariosList.clear();
            usuariosList.addAll(usuarios);
        } catch (ServiceException e) {
            mostrarError("Error al cargar usuarios: " + e.getMessage());
        }
    }

    /**
     * Método para cargar cargos desde la base de datos a través de CargoService
     */
    private void cargarCargos() {
        try {
            List<Cargo> cargos = cargoService.readAll();
            cargosList.clear();
            cargosList.addAll(cargos);
        } catch (ServiceException e) {
            mostrarError("Error al cargar cargos: " + e.getMessage());
        }
    }

    /**
     * Método para cargar servicios desde la base de datos a través de ServicioService
     */
    private void cargarServicios() {
        try {
            List<Servicio> servicios = servicioService.readAll();
            serviciosList.clear();
            serviciosList.addAll(servicios);
        } catch (ServiceException e) {
            mostrarError("Error al cargar servicios: " + e.getMessage());
        }
    }

    /**
     * Método para Inyectar los Servicios
     * @param usuarioService para la gestión de Usuarios
     * @param cargoService para la gestión de Cargos
     * @param servicioService para la gestión de Servicios
     */
    public void setServices(UsuarioService usuarioService, CargoService cargoService, ServicioService servicioService, DomicilioService domicilioService, RolService rolService) {
        this.usuarioService = usuarioService;
        this.cargoService = cargoService;
        this.servicioService = servicioService;
        this.domicilioService = domicilioService;
        this.rolService = rolService;
    }


    /**
     * Método para inicializar el cuadro de búsqueda
     */
    private void configurarBusquedaComboBox() {

        // Inicializar
        busquedaComboBox.setEditable(true);
        busquedaComboBox.setConverter(new UsuarioStringConverter());

        // Establecer un Custom Cell Factory para mostrar información adicional
        busquedaComboBox.setCellFactory(param -> new ListCell<Usuario>() {
            @Override
            protected void updateItem(Usuario usuario, boolean empty) {
                super.updateItem(usuario, empty);

                if (empty || usuario == null) {
                    setText(null);
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append(usuario.getNombreCompleto());

                    if (usuario.getMail() != null && !usuario.getMail().isEmpty()) {
                        sb.append(" <").append(usuario.getMail()).append(">");
                    }

                    if (usuario.getCuil() != 0) {
                        sb.append("  CUIL: ").append(usuario.getCuil());
                    }

                    if (usuario.getTel() != 0) {
                        sb.append("  Tel: ").append(usuario.getTel());
                    }

                    setText(sb.toString());
                }
            }
        });

        // Establecer un button cell para mostrar solo el nombre cuando se selecciona el resultado
//        busquedaComboBox.setButtonCell(new ListCell<Usuario>() {
//            @Override
//            protected void updateItem(Usuario usuario, boolean empty) {
//                super.updateItem(usuario, empty);
//
//                if (empty || usuario == null) {
//                    setText(null);
//                } else {
//                    setText(usuario.getNombreCompleto());
//                }
//            }
//        });

        // Agregar un listener para cambios en la entrada de texto (búsqueda de coincidencias)
        busquedaComboBox.getEditor().textProperty().addListener((obs, oldText, newText) -> {
            if (isFiltering) {
                return;
            }

            filtrarUsuarios();

            // Habilitar o deshabilitar el botón "Nuevo Agente" según el texto
            if (newText == null || newText.isEmpty()) {
                nuevoAgenteButton.setDisable(false);

                // Limpiar la selección del ComboBox
                busquedaComboBox.getSelectionModel().clearSelection();

                // Limpiar el formulario y deshabilitar controles
                limpiarFormulario();
                setControlsEnabled(false);

                // Deshabilitar botones de acción
                altaModButton.setDisable(true);
                cancelarButton.setDisable(true);
                eliminarButton.setDisable(true);
                resetPasswdButton.setDisable(true);
            } else {
                nuevoAgenteButton.setDisable(true);
            }
        });


        // Manejar la confirmación de la selección mediante el ActionEvent
        busquedaComboBox.setOnAction(event -> {
            Usuario usuarioSeleccionado = busquedaComboBox.getSelectionModel().getSelectedItem();
            if (usuarioSeleccionado != null) {
                nuevoAgenteButton.setDisable(true);
                onSeleccionarUsuario();
            } else {
                // Si no hay selección, verificar el texto del editor
                String text = busquedaComboBox.getEditor().getText();
                if (text == null || text.isEmpty()) {
                    nuevoAgenteButton.setDisable(false);
                }
            }
        });

        // Agregar un listener para manejar eventos de teclas (flechas y TAB)
        busquedaComboBox.getEditor().addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN || event.getCode() == KeyCode.UP) {
                // Mostrar el menú desplegable si no está visible
                if (!busquedaComboBox.isShowing()) {
                    busquedaComboBox.show();
                }
                // Dejar que el ComboBox maneje el evento por defecto
            } else if (event.getCode() == KeyCode.TAB) {

                // cuando el ComboBox está vacío
                if (busquedaComboBox.getEditor().getText().isEmpty()) {
                    // Mover el foco al botón "Nuevo Agente"
                    nuevoAgenteButton.requestFocus();
                    event.consume();
                }

                // Si hay un elemento seleccionado, mantenerlo
                if (busquedaComboBox.getSelectionModel().getSelectedItem() == null) {
                    // No seleccionar automáticamente
                }

                // Mover el foco al siguiente campo
                cuilTextField.requestFocus();
                event.consume();

            }
        });


    }


    /**
     * Método para habilitar o deshabilitar los controles
     * @param enabled bandera de habilitación
     */
    private void setControlsEnabled(boolean enabled) {
        // Datos Personales
        cuilTextField.setDisable(!enabled);
        apellidosTextField.setDisable(!enabled);
        nombresTextField.setDisable(!enabled);
        mailTextField.setDisable(!enabled);
        telTextField.setDisable(!enabled);
        sexoComboBox.setDisable(!enabled);

        // Domicilio
        domCalleComboBox.setDisable(!enabled);
        domNumeracionField.setDisable(!enabled);
        domSinNumeroCheckBox.setDisable(!enabled);
        domBarrioComboBox.setDisable(!enabled);
        domCiudadComboBox.setDisable(!enabled);
        domLocalidadComboBox.setDisable(!enabled);
        domProvinciaComboBox.setDisable(!enabled);

        // Tipo de Usuario
        tipoUsuarioComboBox.setDisable(!enabled);

        // Cargos
        cargoComboBox.setDisable(!enabled);
        gestionarCargosButton.setDisable(!enabled);

        // Servicios
        servicioComboBox.setDisable(!enabled);
        gestionarServiciosButton.setDisable(!enabled);

        // Imagen de Perfil
        //imagenPerfilImageView.setDisable(!enabled); //en vez de deshabilitar, establecemos imagen por defecto:
        imagenPerfilImageView.setImage(imagenPerfilOriginal);
        cargarImagenButton.setDisable(!enabled);
        revertirImagenButton.setDisable(!enabled);
        eliminarImagenButton.setDisable(!enabled);

        // Acciones
        resetPasswdButton.setDisable(!enabled);

        // Botones de acción
        altaModButton.setDisable(!enabled);
        cancelarButton.setDisable(!enabled);
        eliminarButton.setDisable(!enabled);

    }


    /**
     * Método para habilitar o deshabilitar los controles
     * @param enabled bandera de habilitación
     */
    private void setControlsEnabled(boolean enabled, boolean includeRoles) {
        // Datos Personales
        cuilTextField.setDisable(!enabled);
        apellidosTextField.setDisable(!enabled);
        nombresTextField.setDisable(!enabled);
        mailTextField.setDisable(!enabled);
        telTextField.setDisable(!enabled);
        sexoComboBox.setDisable(!enabled);

        // Domicilio
        domCalleComboBox.setDisable(!enabled);
        domNumeracionField.setDisable(!enabled);
        domSinNumeroCheckBox.setDisable(!enabled);
        domBarrioComboBox.setDisable(!enabled);
        domCiudadComboBox.setDisable(!enabled);
        domLocalidadComboBox.setDisable(!enabled);
        domProvinciaComboBox.setDisable(!enabled);

        // Tipo de Usuario y Roles
        if(includeRoles) {
            //TODO: Verificar, que se habilite solo si es nuevo usuario. Sino, tomar los valores desde la carga del usuario
            tipoUsuarioComboBox.setDisable(!enabled);
            rolAgenteCheckBox.setDisable(!enabled);
            rolJefeServicioCheckBox.setDisable(!enabled);
            rolOficinaPersonalCheckBox.setDisable(!enabled);
            rolDireccionCheckBox.setDisable(!enabled);
        }

        // Cargos
        cargoComboBox.setDisable(!enabled);
        gestionarCargosButton.setDisable(!enabled);

        // Servicios
        servicioComboBox.setDisable(!enabled);
        gestionarServiciosButton.setDisable(!enabled);

        // Imagen de Perfil
        //imagenPerfilImageView.setDisable(!enabled); //en vez de deshabilitar, establecemos imagen por defecto:
        imagenPerfilImageView.setImage(imagenPerfilOriginal);
        cargarImagenButton.setDisable(!enabled);
        revertirImagenButton.setDisable(!enabled);
        eliminarImagenButton.setDisable(!enabled);

        // Acciones
        resetPasswdButton.setDisable(!enabled);

        // Botones de acción
        altaModButton.setDisable(!enabled);
        cancelarButton.setDisable(!enabled);
        eliminarButton.setDisable(!enabled);

    }


    /**
     * Método para filtrar usuarios en el ComboBox de búsqueda
     */
    private void filtrarUsuarios() {
        if (isFiltering) {
            return;
        }

        isFiltering = true;
        try {
            String textoIngresado = busquedaComboBox.getEditor().getText().toLowerCase();

            filteredUsuariosList.setPredicate(usuario -> {
                if (usuario == null) {
                    return false;
                }
                return usuario.getNombreCompleto().toLowerCase().contains(textoIngresado) ||
                        String.valueOf(usuario.getCuil()).contains(textoIngresado) ||
                        (usuario.getMail() != null && usuario.getMail().toLowerCase().contains(textoIngresado)) ||
                        String.valueOf(usuario.getTel()).contains(textoIngresado);
            });

            // Mostrar el menú desplegable si hay elementos y no se está mostrando
            if (!filteredUsuariosList.isEmpty() && !busquedaComboBox.isShowing()) {
                busquedaComboBox.show();
            }

        } finally {
            isFiltering = false;
        }

//        // Crear una lista limitada de los primeros N elementos
//        int maxResults = 9;
//        ObservableList<Usuario> limitedList = FXCollections.observableArrayList();
//        for (int i = 0; i < Math.min(filteredUsuariosList.size(), maxResults); i++) {
//            limitedList.add(filteredUsuariosList.get(i));
//        }
//
//        busquedaComboBox.setItems(limitedList);
//
//        // Mostrar el menú desplegable si hay elementos y no se está mostrando
//        if (!limitedList.isEmpty() && !busquedaComboBox.isShowing()) {
//            busquedaComboBox.show();
//        }

    }


    /**
     * Método para registrar listeners en los campos relevantes
     */
    private void addChangeListeners() {
        // Escuchar en elementos TextField
        cuilTextField.textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        apellidosTextField.textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        nombresTextField.textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        mailTextField.textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        telTextField.textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domCalleComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domNumeracionField.textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domBarrioComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domCiudadComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domLocalidadComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domProvinciaComboBox.getEditor().textProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        // Checkbox "Sin numero" correspondiente al domicilio
        domSinNumeroCheckBox.selectedProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        domSinNumeroCheckBox.setOnAction(event -> {
            if (domSinNumeroCheckBox.isSelected()) {
                try {
                    storedNumeracion = domNumeracionField.getText();
                } catch (NumberFormatException e) {
                    domNumeracionField.setText("0");
                    throw new RuntimeException(e);
                } finally {
                    domNumeracionField.setText("0");
                    domNumeracionField.setDisable(true);
                }
            } else {
                domNumeracionField.setText(storedNumeracion);
                domNumeracionField.setDisable(false);
            }
            onFormModified();
        });


        // Listeners para botones de imagen
        cargarImagenButton.setOnAction(event -> {
            onCargarImagen();
            onFormModified();
        });
        revertirImagenButton.setOnAction(event -> {
            onRevertirImagen();
            onFormModified();
        });
        eliminarImagenButton.setOnAction(event -> {
            onEliminarImagen();
            onFormModified();
        });

        // Escuchar en elementos ComboBox
        sexoComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        tipoUsuarioComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        cargoComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onFormModified());
        servicioComboBox.valueProperty().addListener((observable, oldValue, newValue) -> onFormModified());
    }


    /**
     * Este método se llama cada vez que un campo cambia.
     * pero solo habilitará el botón Dar de alta / Modificar (altaModButton) si no estamos en la fase de carga (isLoading)
     */
    private void checkForModifications() {
        if (!isLoading) {
            isFormModified = true;
            altaModButton.setDisable(false); // Habilitar el botón de modificación
        }
    }


    /**
     * Método para Activar el Botón "Modificar" usuario
     */
    private void onFormModified() {
        if (!isLoading) {  // Detecta cambios solo si no estamos en carga inicial
            isFormModified = true;
            altaModButton.setDisable(false);  // Habilitar botón cuando hay cambios
        }
    }


    /**
     * Evento al seleccionar un tipo de usuario
     * @param event Evento
     */
    @FXML
    private void onTipoUsuarioSelected(ActionEvent event) {
        TipoUsuario tipoUsuario = tipoUsuarioComboBox.getSelectionModel().getSelectedItem();
        if (tipoUsuario != null) {
            switch (tipoUsuario) {
                case DIRECCION:
                    // Establecer checkbox de rol automáticamente
                    rolDireccionCheckBox.setSelected(true);
                    // Deshabilitar rol seleccionado y habilitar otros
                    rolDireccionCheckBox.setDisable(true);
                    rolOficinaPersonalCheckBox.setDisable(false);
                    rolJefeServicioCheckBox.setDisable(false);
                    rolAgenteCheckBox.setDisable(false);

                    // Asignar servicio automáticamente
                    Servicio servicioDireccion = buscarServicioPorNombre(NombreServicio.DIRECCION);
                    servicioComboBox.getSelectionModel().select(servicioDireccion);
                    servicioComboBox.setDisable(true);
                    gestionarServiciosButton.setDisable(true);

                    // Asignar cargo automáticamente
                    Cargo cargoDireccion = buscarCargoPorNumero(0);
                    cargoComboBox.getSelectionModel().select(cargoDireccion);
                    cargoComboBox.setDisable(true);
                    gestionarCargosButton.setDisable(true);
                    break;
                case OFICINA_DE_PERSONAL:
                    // Establecer checkbox de rol automáticamente
                    rolOficinaPersonalCheckBox.setSelected(true);
                    // Deshabilitar rol seleccionado y habilitar otros
                    rolDireccionCheckBox.setSelected(false);
                    rolOficinaPersonalCheckBox.setDisable(true);
                    rolJefeServicioCheckBox.setDisable(false);
                    rolAgenteCheckBox.setDisable(false);

                    // Asignar servicio automáticamente
                    Servicio servicioPersonal = buscarServicioPorNombre(NombreServicio.OFICINA_DE_PERSONAL);
                    servicioComboBox.getSelectionModel().select(servicioPersonal);
                    servicioComboBox.setDisable(true);
                    gestionarServiciosButton.setDisable(true);

                    // Habilitar cargoComboBox y gestionarCargosButton si es necesario
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);
                    break;
                case JEFATURA_DE_SERVICIO:
                    // Establecer checkbox de rol automáticamente
                    rolJefeServicioCheckBox.setSelected(true);
                    // Deshabilitar rol seleccionado y habilitar otros
                    rolDireccionCheckBox.setDisable(false);
                    rolOficinaPersonalCheckBox.setDisable(false);
                    rolJefeServicioCheckBox.setDisable(true);
                    rolAgenteCheckBox.setDisable(false);

                    // Habilitar todos los servicios para selección normal
                    servicioComboBox.setDisable(false);
                    gestionarServiciosButton.setDisable(false);

                    // Habilitar todos los cargos para selección normal
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);

                    // Validar si el servicio ya tiene jefes asignados
                    validarJefesDeServicio();
                    break;
                case EMPLEADO:
                    // Establecer checkbox de rol automáticamente
                    rolAgenteCheckBox.setSelected(true);
                    // Deshabilitar rol seleccionado y habilitar otros
                    rolDireccionCheckBox.setDisable(false);
                    rolOficinaPersonalCheckBox.setDisable(false);
                    rolJefeServicioCheckBox.setDisable(false);
                    rolAgenteCheckBox.setDisable(true);

                    // Habilitar todos los servicios para selección normal
                    servicioComboBox.setDisable(false);
                    gestionarServiciosButton.setDisable(false);

                    // Habilitar todos los cargos para selección normal
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);
                    break;
                default:
                    // Habilitar selección de roles
                    rolDireccionCheckBox.setDisable(false);
                    rolOficinaPersonalCheckBox.setDisable(false);
                    rolJefeServicioCheckBox.setDisable(false);
                    rolAgenteCheckBox.setDisable(false);
                    // Habilitar selección de otros controles
                    cargoComboBox.setDisable(false);
                    servicioComboBox.setDisable(false);
                    break;
            }
        } else {
            // Si no hay un Tipo de Usuario seleccionado, deshabilitar todos los checkbox de roles
            rolAgenteCheckBox.setDisable(true);
            rolJefeServicioCheckBox.setDisable(true);
            rolOficinaPersonalCheckBox.setDisable(true);
            rolDireccionCheckBox.setDisable(true);
        }
    }

    // Método para validar si el servicio seleccionado ya tiene jefes asignados
    private void validarJefesDeServicio() {
        Servicio servicioSeleccionado = servicioComboBox.getSelectionModel().getSelectedItem();
        if (servicioSeleccionado != null) {
            int cantidadJefes = obtenerCantidadJefesEnServicio(servicioSeleccionado);
            if (cantidadJefes >= 2) {
                Alert alerta = new Alert(AlertType.CONFIRMATION);
                alerta.setTitle("Advertencia");
                alerta.setHeaderText("Existen " + cantidadJefes + " jefes asignados a este servicio.");
                alerta.setContentText("¿Desea continuar?");
                alerta.showAndWait();
            }
        }
    }

    // Simulación de método para obtener la cantidad de jefes en un servicio
    private int obtenerCantidadJefesEnServicio(Servicio servicio) {
        // Aquí se debería consultar la base de datos o fuente de datos
        return 2; // Valor simulado
    }


    /**
     * Método para buscar un servicio por nombre
     * @param nombre Nombre de servicio
     * @return objeto de tipo Servicio
     */
    private Servicio buscarServicioPorNombre(String nombre) {
        for (Servicio servicio : serviciosList) {
            if (servicio.getNombre().equalsIgnoreCase(nombre)) {
                return servicio;
            }
        }
        return null;
    }


    /**
     * Método para buscar un Cargo por número
     * @param numero Número de cargo
     * @return objeto de tipo Cargo
     */
    private Cargo buscarCargoPorNumero(int numero) {
        for (Cargo cargo : cargosList) {
            if (cargo.getNumero() == numero) {
                return cargo;
            }
        }
        return null;
    }


    /**
     * Evento al hacer clic en "Cargar Imagen"
     */
    @FXML
    private void onCargarImagen() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de Perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.jpe", "*.gif"));
        File archivoSeleccionado = fileChooser.showOpenDialog(getStage());

        if (archivoSeleccionado != null) {
            imagenPerfilFile = archivoSeleccionado;
            Image imagenNueva = new Image(archivoSeleccionado.toURI().toString());
            imagenPerfilImageView.setImage(imagenNueva);
            revertirImagenButton.setDisable(false);
        }
    }


    /**
     * Evento al hacer clic en "Revertir Imagen"
     */
    @FXML
    private void onRevertirImagen() {
        imagenPerfilImageView.setImage(imagenPerfilOriginal);
        imagenPerfilFile = null;
        revertirImagenButton.setDisable(true);
    }


    /**
     * Evento al hacer clic en "Eliminar Imagen"
     */
    @FXML
    private void onEliminarImagen() {
        imagenPerfilImageView.setImage(null);
        imagenPerfilFile = null;
    }


    /**
     * Evento al hacer clic en "Resetear Contraseña"
     * @param event Evento
     */
    @FXML
    private void onResetPassword(ActionEvent event) {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            Alert confirmacion = new Alert(AlertType.CONFIRMATION);
            confirmacion.setTitle("Resetear Contraseña");
            confirmacion.setHeaderText("¿Desea restablecer la contraseña por defecto para el usuario " + usuario.getNombreCompleto() + "?");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    usuario.setDefaultPassword();
                    try {
                        usuarioService.resetPassword(usuario);
                        mostrarMensaje("La contraseña por defecto ha sido establecida.");
                    } catch (ServiceException e) {
                        mostrarError("Error al restablecer la contraseña: " + e.getMessage());
                    }
                }
            });
        }
    }


    /**
     * Evento al hacer clic en "Alta"
     * @param event Evento
     */
    @FXML
    public void onAlta(ActionEvent event) throws ServiceException {
        if (isCancelMode) {
            // Confirmar si desea cancelar
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación");
            confirmacion.setHeaderText("¿Cancelar la carga actual?");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    resetInterface();
                }
            });
        } else {
            // Lógica para dar de alta un nuevo usuario
            if (validarCamposObligatorios()) {
                Usuario nuevoUsuario = crearOActualizarUsuarioDesdeFormulario(null);
                if (nuevoUsuario != null) {
                    usuariosList.add(nuevoUsuario);
                    resetInterface();
                    mostrarMensaje("Usuario agregado correctamente.");
                }
            }
        }
    }

    /**
     * Evento al hacer clic en "Dar de alta" (o "Modificar", según contexto)
     * @param event Evento
     */
    @FXML
    private void onAltaMod(ActionEvent event) throws ServiceException {
        if (!isFormModified) {
            mostrarMensaje("No se han realizado cambios para modificar.");
            return;
        }

        if (isAltaMode) {
            // "Dar de alta" mode
            if (validarCamposObligatorios()) {
                Usuario nuevoUsuario = crearOActualizarUsuarioDesdeFormulario(null);
                if (nuevoUsuario != null) {
                    try {
                        UsuarioCreationResult result = usuarioService.create(nuevoUsuario);
                        switch (result) {
                            case USUARIO_CREADO:
                                usuariosList.add(nuevoUsuario);
                                resetInterface();
                                mostrarMensaje("Usuario agregado correctamente.");
                                break;

                            case USUARIO_ACTIVO_EXISTENTE:
                                mostrarError("Ya existe un usuario activo con el CUIL proporcionado.");
                                break;

                            case USUARIO_DESHABILITADO_EXISTENTE:
                                boolean reactivar = mostrarConfirmacion("El usuario con este CUIL está deshabilitado. ¿Desea reactivarlo y actualizar sus datos?");
                                if (reactivar) {
                                    try {
                                        usuarioService.reactivarUsuario(nuevoUsuario);
                                        usuariosList.add(nuevoUsuario);
                                        resetInterface();
                                        mostrarMensaje("Usuario reactivado y actualizado correctamente.");
                                    } catch (ServiceException e) {
                                        mostrarError("Error al reactivar el usuario: " + e.getMessage());
                                    }
                                } else {
                                    mostrarMensaje("Operación cancelada por el usuario.");
                                }
                                break;
                        }
                    } catch (ServiceException e) {
                        mostrarError("Error al agregar el usuario: " + e.getMessage());
                    }
                }
            }
        } else if (isModificacionMode) {
            // "Modificar" mode
            Usuario usuarioSeleccionado = obtenerUsuarioActual();
            if (usuarioSeleccionado != null && validarCamposObligatorios()) {
                Usuario usuarioActualizado = crearOActualizarUsuarioDesdeFormulario(usuarioSeleccionado);
                if (usuarioActualizado != null) {
                    try {
                        usuarioService.update(usuarioActualizado);
                        int index = usuariosList.indexOf(usuarioSeleccionado);
                        usuariosList.set(index, usuarioActualizado);
                        resetInterface();
                        mostrarMensaje("Usuario modificado correctamente.");
                    } catch (ServiceException e) {
                        mostrarError("Error al modificar el usuario: " + e.getMessage());
                    }
                }
            }
        }
    }


    /**
     * Evento al hacer clic en Cancelar (o salir, según su estado)
     * @param event Evento
     */
    @FXML
    private void onCancelar(ActionEvent event) {
        if ((isAltaMode || isModificacionMode) && isFormModified) {
            // Operación "Cancelar" con confirmación sólo si hubo modificaciones
            Alert confirmacion = new Alert(AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmación");
            confirmacion.setHeaderText("¿Cancelar la operación actual?");
            confirmacion.setContentText("Si ha realizado modificaciones, al cancelar ahora los cambios no se guardarán y se perderán.");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    resetInterface();
                }
            });
        } else if (isAltaMode || isModificacionMode) {
            // Cancelar sin confirmación si no hubo modificaciones
            resetInterface();
        } else {
            // Operación "Salir" cuando no estamos en modo alta o modificación
            Stage stage = (Stage) cancelarButton.getScene().getWindow();
            stage.close();
        }
    }

    /**
     * Evento al hacer clic en "Modificar"
     */
    @FXML
    private void onModificar() throws ServiceException {
        Usuario usuarioSeleccionado = busquedaComboBox.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null && validarCamposObligatorios()) {
            Usuario usuarioActualizado = crearOActualizarUsuarioDesdeFormulario(usuarioSeleccionado);
            if (usuarioActualizado != null) {
                // Actualizar en la lista si es necesario
                int index = usuariosList.indexOf(usuarioSeleccionado);
                usuariosList.set(index, usuarioActualizado);
                resetInterface();
                mostrarMensaje("Usuario modificado correctamente.");
            }
        }
        resetInterface();
    }


    /**
     * Evento al hacer clic en "Eliminar"
     * @param event Evento
     */
    @FXML
    private void onEliminar(ActionEvent event) {
        Usuario usuarioSeleccionado = busquedaComboBox.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert confirmacion = new Alert(AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar Usuario");
            confirmacion.setHeaderText("¿Está seguro de eliminar al usuario " + usuarioSeleccionado.getNombreCompleto() + "?");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        usuarioService.delete(usuarioSeleccionado);
                        usuariosList.remove(usuarioSeleccionado);
                        resetInterface();
                        mostrarMensaje("Usuario eliminado correctamente.");
                    } catch (ServiceException e) {
                        mostrarError("Error al eliminar el usuario: " + e.getMessage());
                    }
                }
            });
        }
        resetInterface();
    }


    /**
     * Evento al hacer clic en "Nuevo Agente"
     */
    @FXML
    private void onNuevoAgente(ActionEvent event) {
        // Deshabilitar el ComboBox y el botón "Nuevo Agente"
        busquedaComboBox.setDisable(true);
        nuevoAgenteButton.setDisable(true);

        // Habilitar los controles del formulario
        setControlsEnabled(true, true);

        // Limpiar el formulario
        limpiarFormulario();

        // Enfocar el campo CUIL
        cuilTextField.requestFocus();

        // Update buttons
        altaModButton.setVisible(true);
        altaModButton.setText("Dar de alta");
        altaModButton.setDisable(false);

        cancelarButton.setVisible(true);
        cancelarButton.setText("Cancelar");
        cancelarButton.setDisable(false);

        eliminarButton.setVisible(false);  // Ocultar eliminarButton si fuera visible

        // Banderas de modo
        isAltaMode = true;
        isModificacionMode = false;

    }


    /**
     * Restablece los controles a un estado inicial cada vez que sea invocado
     */
    private void resetInterface() {
        // Restablece todos los controles como antes
        setControlsEnabled(false, true);

        // Limpiar el formulario
        limpiarFormulario();

        // Habilitar el ComboBox y el botón "Nuevo Agente"
        busquedaComboBox.setDisable(false);
        nuevoAgenteButton.setDisable(false);

        // Limpiar selección del ComboBox
        busquedaComboBox.getSelectionModel().clearSelection();
        busquedaComboBox.getEditor().clear();

        // Resetear botones al estado inicial
        altaModButton.setDisable(true);
        altaModButton.setVisible(false);

        cancelarButton.setVisible(true);
        cancelarButton.setText("Salir");
        cancelarButton.setDisable(false);

        eliminarButton.setVisible(false);

        resetPasswdButton.setDisable(true);

        // Resetear banderas...
        isAltaMode = false;
        isModificacionMode = false;
        isFormModified = false;

    }


    /**
     * Evento al seleccionar un usuario en el ComboBox de búsqueda
     */
    @FXML
    private void onSeleccionarUsuario() {
        Usuario usuarioSeleccionado = busquedaComboBox.getValue();
        if (usuarioSeleccionado != null) {
            cargarUsuarioEnFormulario(usuarioSeleccionado);
            setControlsEnabled(true);

            // Actualizar botones de acción
            altaModButton.setVisible(true);
            altaModButton.setText("Modificar");
            altaModButton.setDisable(true); // Deshabilitado hasta que se detecte una modificación real

            cancelarButton.setVisible(true);
            cancelarButton.setText("Cancelar");
            cancelarButton.setDisable(false);

            eliminarButton.setVisible(true);
            eliminarButton.setDisable(false);

            resetPasswdButton.setDisable(false);
            nuevoAgenteButton.setDisable(true);
            busquedaComboBox.setDisable(true);

            // Set mode flags
            isAltaMode = false;
            isModificacionMode = true;

            // Move focus to cuilTextField
            cuilTextField.requestFocus();
        }
    }


    /**
     * Método para cargar los datos del usuario en el formulario
     * @param usuario objeto de tipo Usuario
     */
    private void cargarUsuarioEnFormulario(Usuario usuario) {
        // Indicar que estamos en la fase de carga
        isLoading = true;

        // Actualizar la interfaz con los datos del usuario
        tipoUsuarioComboBox.getSelectionModel().select(obtenerTipoUsuario(usuario));
        onTipoUsuarioSelected(null); // Aquí actualiza la interfaz

        // Cargar los datos...
        cuilTextField.setText(String.valueOf(usuario.getCuil()));
        apellidosTextField.setText(usuario.getApellidos());
        nombresTextField.setText(usuario.getNombres());
        mailTextField.setText(usuario.getMail());
        telTextField.setText(String.valueOf(usuario.getTel()));
        sexoComboBox.getSelectionModel().select(usuario.getSexo());

        // Cargar imagen de perfil
        ImageUtils.setProfileImage(imagenPerfilImageView, usuario.getProfileImage(), imagenPerfilOriginal);

        //Cargar roles en los CheckBoxes
        rolAgenteCheckBox.setSelected(usuario.hasRole(TipoUsuario.EMPLEADO));
        rolJefeServicioCheckBox.setSelected(usuario.hasRole(TipoUsuario.JEFATURA_DE_SERVICIO));
        rolOficinaPersonalCheckBox.setSelected(usuario.hasRole(TipoUsuario.OFICINA_DE_PERSONAL));
        rolDireccionCheckBox.setSelected(usuario.hasRole(TipoUsuario.DIRECCION));
        // Deshabilitar checkBox del Rol correspondiente al Tipo de usuario
        rolAgenteCheckBox.setDisable(usuario.isDefaultRole(TipoUsuario.EMPLEADO));
        rolJefeServicioCheckBox.setDisable(usuario.isDefaultRole(TipoUsuario.JEFATURA_DE_SERVICIO));
        rolOficinaPersonalCheckBox.setDisable(usuario.isDefaultRole(TipoUsuario.OFICINA_DE_PERSONAL));
        rolDireccionCheckBox.setDisable(usuario.isDefaultRole(TipoUsuario.DIRECCION));

        // Datos del domicilio
        Domicilio domicilio = usuario.getDomicilio();
        if (domicilio != null) {
            domCalleComboBox.getEditor().setText(
                    (domicilio.getCalle() != null && !domicilio.getCalle().isEmpty()) ? domicilio.getCalle() : "");
            int numeracion = domicilio.getNumeracion();
            boolean sinNumero = numeracion == 0;
            domSinNumeroCheckBox.setSelected(sinNumero);
            if (sinNumero) {
                domNumeracionField.setText("0");
                domNumeracionField.setDisable(true);
            } else {
                domNumeracionField.setText(String.valueOf(numeracion));
                domNumeracionField.setDisable(false);
            }
            domBarrioComboBox.getEditor().setText(
                    (domicilio.getBarrio() != null && !domicilio.getBarrio().isEmpty()) ? domicilio.getBarrio() : "");
            domCiudadComboBox.getEditor().setText(
                    (domicilio.getCiudad() != null && !domicilio.getCiudad().isEmpty()) ? domicilio.getCiudad() : "");
            domLocalidadComboBox.getEditor().setText(
                    (domicilio.getLocalidad() != null && !domicilio.getLocalidad().isEmpty()) ? domicilio.getLocalidad() : "");
            domProvinciaComboBox.getEditor().setText(
                    (domicilio.getProvincia() != null && !domicilio.getProvincia().isEmpty()) ? domicilio.getProvincia() : "");
        } else {
            // Limpiar campos de domicilio si no hay datos
            domCalleComboBox.getEditor().clear();
            domNumeracionField.clear();
            domBarrioComboBox.getEditor().clear();
            domCiudadComboBox.getEditor().clear();
            domLocalidadComboBox.getEditor().clear();
            domProvinciaComboBox.getEditor().clear();
            domSinNumeroCheckBox.setSelected(false);
        }

        // Restablecer el estado del formulario a "sin modificaciones"
        isFormModified = false;
        altaModButton.setDisable(true);  // Solo se habilitará cuando haya una modificación

        // Ahora, desactivar la fase de carga para detectar modificaciones a partir de aquí
        isLoading = false;

    }


    /**
     * Método para obtener el tipo de usuario como String
     * @param usuario objeto tipo Usuario
     * @return cadena de texto que indicado el tipo de usuario
     */
    private TipoUsuario obtenerTipoUsuario(Usuario usuario) {
        if (usuario instanceof Empleado) {
            return TipoUsuario.EMPLEADO;
        } else if (usuario instanceof JefaturaDeServicio) {
            return TipoUsuario.JEFATURA_DE_SERVICIO;
        } else if (usuario instanceof OficinaDePersonal) {
            return TipoUsuario.OFICINA_DE_PERSONAL;
        } else if (usuario instanceof Direccion) {
            return TipoUsuario.DIRECCION;
        } else {
            return null;
        }
    }


    /**
     * Método para crear un nuevo usuario desde los datos del formulario
     * @param usuarioExistente un objeto de tipo Usuario
     * @return un objeto de tipo Usuario
     */
    private Usuario crearOActualizarUsuarioDesdeFormulario(Usuario usuarioExistente) throws ServiceException {
        TipoUsuario tipoUsuarioSeleccionado = tipoUsuarioComboBox.getSelectionModel().getSelectedItem();
        Usuario usuario;

        if (usuarioExistente == null) {
            // Crear una nueva instancia según el tipo de usuario
            switch (tipoUsuarioSeleccionado) {
                case EMPLEADO:
                    usuario = new Empleado();
                    break;
                case JEFATURA_DE_SERVICIO:
                    usuario = new JefaturaDeServicio();
                    break;
                case OFICINA_DE_PERSONAL:
                    usuario = new OficinaDePersonal();
                    break;
                case DIRECCION:
                    usuario = new Direccion();
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipoUsuarioSeleccionado);
            }
            usuario.setEstado(true);  // Nuevo usuario, estado activo por defecto
            usuario.setFechaAlta(LocalDate.now());  // Por defecto, fecha de alta = fecha de creación
        } else {
            // Actualizar el usuario existente
            usuario = usuarioExistente;
        }

        // Asignar atributos comunes
        usuario.setId(UUID.randomUUID());
        try {
            String cuilText = cuilTextField.getText().replaceAll("-", "");
            usuario.setCuil(Long.parseLong(cuilText));
        } catch (NumberFormatException e) {
            mostrarError("El CUIL debe ser un número válido.");
            return null;
        }
        usuario.setApellidos(apellidosTextField.getText());
        usuario.setNombres(nombresTextField.getText());
        usuario.setMail(mailTextField.getText());
        try {
            usuario.setTel(Long.parseLong(telTextField.getText()));
        } catch (NumberFormatException e) {
            mostrarError("El teléfono debe ser un número válido.");
            return null;
        }
        usuario.setSexo(sexoComboBox.getSelectionModel().getSelectedItem());

        // Asignar Cargo y Servicio
        usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
        usuario.setServicio(servicioComboBox.getSelectionModel().getSelectedItem());

        // Cargar imagen de perfil
        ImageUtils.setProfileImage(imagenPerfilImageView, usuario.getProfileImage(), imagenPerfilOriginal);

        // Asignar Tipo de usuario
        usuario.setTipoUsuario(tipoUsuarioSeleccionado);

        // Agregar roles basados en los CheckBoxes
        Set<Rol> rolesSeleccionados = new HashSet<>();
        if (rolAgenteCheckBox.isSelected()) {
            Rol rolAgente = rolService.findByTipoUsuario(TipoUsuario.EMPLEADO);
            rolesSeleccionados.add(rolAgente);
        }
        if (rolJefeServicioCheckBox.isSelected()) {
            Rol rolAgente = rolService.findByTipoUsuario(TipoUsuario.JEFATURA_DE_SERVICIO);
            rolesSeleccionados.add(rolAgente);
        }
        if (rolOficinaPersonalCheckBox.isSelected()) {
            Rol rolAgente = rolService.findByTipoUsuario(TipoUsuario.OFICINA_DE_PERSONAL);
            rolesSeleccionados.add(rolAgente);
        }
        if (rolDireccionCheckBox.isSelected()) {
            Rol rolAgente = rolService.findByTipoUsuario(TipoUsuario.DIRECCION);
            rolesSeleccionados.add(rolAgente);
        }
        usuario.setRoles(rolesSeleccionados);

        // Asignar domicilio
        String numeracionInput = domNumeracionField.getText().trim();
        int numeracion;
        if (domSinNumeroCheckBox.isSelected()) {
            numeracion = 0;
            domNumeracionField.setText("0");
            domNumeracionField.setDisable(true);
        } else {
            try {
                numeracion = Integer.parseInt(numeracionInput);
                domNumeracionField.setDisable(false);
            } catch (NumberFormatException e) {
                mostrarError("La numeración debe ser un número válido.");
                return null;
            }
        }
        String calle = domCalleComboBox.getEditor().getText().trim();
        String barrio = domBarrioComboBox.getEditor().getText().trim();
        String ciudad = domCiudadComboBox.getEditor().getText().trim();
        String localidad = domLocalidadComboBox.getEditor().getText().trim();
        String provincia = domProvinciaComboBox.getEditor().getText().trim();
        if (!barrio.isEmpty()) {
            if(ciudad.isEmpty()) {
                mostrarError("Si indica un barrio, debe indicar la ciudad.");
                return null;
            }
        }
        try {
            if (usuario.getDomicilio() == null || usuario.getDomicilio().getId() == null) {
                // Crear nuevo domicilio con un nuevo UUID
                Domicilio domicilioCreado = new Domicilio.Builder()
                        .id(UUID.randomUUID())
                        .calle(calle)
                        .numeracion(numeracion)
                        .barrio(barrio)
                        .ciudad(ciudad)
                        .localidad(localidad)
                        .provincia(provincia)
                        .build();

                domicilioService.create(domicilioCreado);
                usuario.setDomicilio(domicilioCreado);
            } else {
                // Actualizar domicilio existente usando toBuilder
                Domicilio domicilioActualizado = usuario.getDomicilio().toBuilder()
                        .calle(calle)
                        .numeracion(numeracion)
                        .barrio(barrio)
                        .ciudad(ciudad)
                        .localidad(localidad)
                        .provincia(provincia)
                        .build();

                domicilioService.update(domicilioActualizado);
                usuario.setDomicilio(domicilioActualizado);
            }
        } catch (ServiceException e) {
            mostrarError("Error al asignar el domicilio: " + e.getMessage());
            return null;
        }

        // Asignar IDs de otros datos
        usuario.setDomicilioId(usuario.getDomicilio().getId());
        usuario.setCargoId(usuario.getCargo().getId());
        usuario.setServicioId(usuario.getServicio().getId());
        //Asignar la contraseña por defecto
        usuario.setDefaultPassword();

        return usuario;
    }


    /**
     * Método para validar los campos obligatorios
     * @return validación de los campos obligatorios
     */
    private boolean validarCamposObligatorios() {
        if (cuilTextField.getText().isEmpty() || apellidosTextField.getText().isEmpty()
                || nombresTextField.getText().isEmpty() || mailTextField.getText().isEmpty()
                || sexoComboBox.getSelectionModel().isEmpty() || tipoUsuarioComboBox.getSelectionModel().isEmpty()) {
            mostrarError("Debe completar todos los campos obligatorios.");
            return false;
        }

        // Validar campos del domicilio
//        if (domCalleComboBox.getEditor().getText().isEmpty() || domNumeracionField.getText().isEmpty()
//                || domCiudadComboBox.getEditor().getText().isEmpty() || domProvinciaComboBox.getEditor().getText().isEmpty()) {
//            mostrarError("Debe completar todos los campos de domicilio obligatorios.");
//            return false;
//        }

        // Validar formatos, email válido, etc.
        return true;
    }


    /**
     * Método para limpiar el formulario
     */
    private void limpiarFormulario() {
        cuilTextField.clear();
        apellidosTextField.clear();
        nombresTextField.clear();
        mailTextField.clear();
        telTextField.clear();
        sexoComboBox.getSelectionModel().clearSelection();
        tipoUsuarioComboBox.getSelectionModel().clearSelection();
        cargoComboBox.getSelectionModel().clearSelection();
        servicioComboBox.getSelectionModel().clearSelection();
        imagenPerfilImageView.setImage(imagenPerfilOriginal);
        // Limpiar domicilio...
        domCalleComboBox.getEditor().clear();
        domNumeracionField.clear();
        domNumeracionField.setDisable(false);
        domSinNumeroCheckBox.setSelected(false);
        domBarrioComboBox.getEditor().clear();
        domCiudadComboBox.getEditor().clear();
        domBarrioComboBox.getEditor().setText("");
        domLocalidadComboBox.getEditor().setText("");
        domLocalidadComboBox.getEditor().clear();
        domProvinciaComboBox.getEditor().clear();
    }


    /**
     * Método para obtener el usuario actualmente cargado en el formulario
     * @return objeto de tipo Usuario
     */
    private Usuario obtenerUsuarioActual() {
        return busquedaComboBox.getSelectionModel().getSelectedItem();
    }


    /**
     * Método para mostrar mensajes de información
     * @param mensaje Mensaje informativo
     */
    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    /**
     * Método para mostrar mensajes de error
     * @param mensaje Mensaje de error
     */
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


    private boolean mostrarConfirmacion(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Obtener el Stage actual
     * @return Stage
     */
    private Stage getStage() {
        return (Stage) busquedaComboBox.getScene().getWindow();
    }


    /**
     * Clase interna para convertir Usuario a String en el ComboBox
     */
    private class UsuarioStringConverter extends StringConverter<Usuario> {
        @Override
        public String toString(Usuario usuario) {
            if (usuario == null) {
                return busquedaComboBox.getEditor().getText();
            }
            return usuario.getNombreCompleto();
        }

        @Override
        public Usuario fromString(String string) {
            return busquedaComboBox.getItems().stream()
                    .filter(usuario -> usuario.getNombreCompleto().equalsIgnoreCase(string))
                    .findFirst()
                    .orElse(null);
        }

    }


    /**
     * Eventos para gestionar cargos (en construcción)
     * @param event Evento
     */
    @FXML
    private void onAbmCargo(ActionEvent event) {
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
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            //stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErr("Error al cargar la ventana de gestión de cargos: " + e.getMessage());
        }
    }


    @FXML
    private void onAbmServicio(ActionEvent event) {
        // Usa los servicios ya inicializados
        UsuarioService usuarioService = this.usuarioService;
        ServicioService servicioService = this.servicioService;

        try {
            // Configurar la fábrica de controladores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/abmServicio.fxml"));
            loader.setControllerFactory(controllerClass -> {
                if (controllerClass == AbmServicioController.class) {
                    AbmServicioController controller = new AbmServicioController();
                    controller.setServices(usuarioService, servicioService);
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
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            //stage.initOwner(MainMenuMosaicoController.getPrimaryStage());
            stage.showAndWait();
        } catch (IOException e) {
            AlertUtils.showErr("Error al cargar la ventana de gestión de servicios: " + e.getMessage());
        }
    }

}
