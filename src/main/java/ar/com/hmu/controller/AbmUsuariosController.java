package ar.com.hmu.controller;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.service.DomicilioService;
import javafx.collections.transformation.FilteredList;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
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
import ar.com.hmu.service.CargoService;
import ar.com.hmu.service.ServicioService;
import ar.com.hmu.service.UsuarioService;
import ar.com.hmu.util.CuilUtils;
import ar.com.hmu.util.ImageUtils;

public class AbmUsuariosController implements Initializable {

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

    // Tipo de Usuario y Asignaciones
    @FXML private ComboBox<TipoUsuario> tipoUsuarioComboBox;
    @FXML private ComboBox<Cargo> cargoComboBox;
    @FXML private Button gestionarCargosButton;
    @FXML private ComboBox<Servicio> servicioComboBox;
    @FXML private Button gestionarServiciosButton;

    // Sección de Acciones
    @FXML private Button resetPasswdButton;
    @FXML private RadioButton usuarioHabilitadoCheckBox;
    @FXML private RadioButton usuarioDeshabilitadoCheckBox;
    @FXML private ToggleGroup estadoToggleGroup;
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

        // Configurar ToggleGroup
        estadoToggleGroup = new ToggleGroup();
        usuarioHabilitadoCheckBox.setToggleGroup(estadoToggleGroup);
        usuarioDeshabilitadoCheckBox.setToggleGroup(estadoToggleGroup);

        // Cargar imagen por defecto
        imagenPerfilOriginal = imagenPerfilImageView.getImage();

        // Eventos adicionales
        tipoUsuarioComboBox.setOnAction(this::onTipoUsuarioSelected);
        nuevoAgenteButton.setOnAction(this::onNuevoAgente);
        altaModButton.setOnAction(this::onAltaMod);
        cancelarButton.setOnAction(this::onCancelar);

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
    public void setServices(UsuarioService usuarioService, CargoService cargoService, ServicioService servicioService, DomicilioService domicilioService) {
        this.usuarioService = usuarioService;
        this.cargoService = cargoService;
        this.servicioService = servicioService;
        this.domicilioService = domicilioService;
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

        // Tipo de Usuario y Asignaciones
        tipoUsuarioComboBox.setDisable(!enabled);
        cargoComboBox.setDisable(!enabled);
        gestionarCargosButton.setDisable(!enabled);
        servicioComboBox.setDisable(!enabled);
        gestionarServiciosButton.setDisable(!enabled);

        // Imagen de Perfil
        imagenPerfilImageView.setDisable(!enabled);
        cargarImagenButton.setDisable(!enabled);
        revertirImagenButton.setDisable(!enabled);
        eliminarImagenButton.setDisable(!enabled);

        // Acciones
        resetPasswdButton.setDisable(!enabled);

        // CheckBoxes de Estado
        usuarioHabilitadoCheckBox.setDisable(!enabled);
        usuarioDeshabilitadoCheckBox.setDisable(!enabled);

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
                    // Asignar servicio y cargo automáticamente
                    Servicio servicioDireccion = buscarServicioPorNombre(NombreServicio.DIRECCION);
                    servicioComboBox.getSelectionModel().select(servicioDireccion);
                    servicioComboBox.setDisable(true);
                    gestionarServiciosButton.setDisable(true);

                    Cargo cargoDireccion = buscarCargoPorNumero(9999);
                    cargoComboBox.getSelectionModel().select(cargoDireccion);
                    cargoComboBox.setDisable(true);
                    gestionarCargosButton.setDisable(true);
                    break;
                case OFICINA_DE_PERSONAL:
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
                    // Habilitar todos los elementos para selección normal
                    servicioComboBox.setDisable(false);
                    gestionarServiciosButton.setDisable(false);
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);

                    // Validar si el servicio ya tiene jefes asignados
                    validarJefesDeServicio();
                    break;
                case EMPLEADO:
                    // Habilitar todos los elementos para selección normal
                    servicioComboBox.setDisable(false);
                    gestionarServiciosButton.setDisable(false);
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);
                    break;
                default:
                    cargoComboBox.setDisable(false);
                    servicioComboBox.setDisable(false);
                    break;
            }
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
     * @param event Evento
     */
    @FXML
    private void onCargarImagen(ActionEvent event) {
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
     * @param event Evento
     */
    @FXML
    private void onRevertirImagen(ActionEvent event) {
        imagenPerfilImageView.setImage(imagenPerfilOriginal);
        imagenPerfilFile = null;
        revertirImagenButton.setDisable(true);
    }


    /**
     * Evento al hacer clic en "Eliminar Imagen"
     * @param event Evento
     */
    @FXML
    private void onEliminarImagen(ActionEvent event) {
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
    public void onAlta(ActionEvent event) {
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
    private void onAltaMod(ActionEvent event) {
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
                        usuarioService.create(nuevoUsuario);
                        usuariosList.add(nuevoUsuario);
                        resetInterface();
                        mostrarMensaje("Usuario agregado correctamente.");
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
     * @param event Evento
     */
    @FXML
    private void onModificar(ActionEvent event) {
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
    }


    /**
     * Evento al hacer clic en "Nuevo Agente"
     * @param event Evento
     */
    @FXML
    private void onNuevoAgente(ActionEvent event) {
        // Deshabilitar el ComboBox y el botón "Nuevo Agente"
        busquedaComboBox.setDisable(true);
        nuevoAgenteButton.setDisable(true);

        // Habilitar los controles del formulario
        setControlsEnabled(true);

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
        setControlsEnabled(false);

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
        Image image = ImageUtils.byteArrayToImage(usuario.getProfileImage());
        if (image != null) {
            imagenPerfilImageView.setImage(image);
        } else {
            imagenPerfilImageView.setImage(imagenPerfilOriginal);
        }

        // Datos del domicilio
        Domicilio domicilio = usuario.getDomicilio();
        if (domicilio != null) {
            domCalleComboBox.getEditor().setText(domicilio.getCalle());
            domNumeracionField.setText(domicilio.getNumeracion());
            domBarrioComboBox.getEditor().setText(domicilio.getBarrio());
            domCiudadComboBox.getEditor().setText(domicilio.getCiudad());
            domLocalidadComboBox.getEditor().setText(domicilio.getLocalidad());
            domProvinciaComboBox.getEditor().setText(domicilio.getProvincia());
        } else {
            // Limpiar campos de domicilio si no hay datos
            domCalleComboBox.getEditor().clear();
            domNumeracionField.clear();
            domBarrioComboBox.getEditor().clear();
            domCiudadComboBox.getEditor().clear();
            domLocalidadComboBox.getEditor().clear();
            domProvinciaComboBox.getEditor().clear();
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
    private Usuario crearOActualizarUsuarioDesdeFormulario(Usuario usuarioExistente) {
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
        } else {
            // Actualizar el usuario existente
            usuario = usuarioExistente;
        }

        // Asignar atributos comunes
        try {
            usuario.setCuil(Long.parseLong(cuilTextField.getText()));
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
        usuario.setEstado(usuarioHabilitadoCheckBox.isSelected());

        // Asignar Cargo y Servicio
        usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
        usuario.setServicio(servicioComboBox.getSelectionModel().getSelectedItem());

        // Manejar casos específicos según el tipo de usuario
        if (tipoUsuarioSeleccionado == TipoUsuario.DIRECCION) {
            // Asignar cargo y servicio de dirección
            Cargo cargoDireccion = buscarCargoPorNumero(9999);
            Servicio servicioDireccion = buscarServicioPorNombre(NombreServicio.DIRECCION);
            usuario.setCargo(cargoDireccion);
            usuario.setServicio(servicioDireccion);
        } else if (tipoUsuarioSeleccionado == TipoUsuario.OFICINA_DE_PERSONAL) {
            // Asignar servicio de oficina de personal
            Servicio servicioPersonal = buscarServicioPorNombre(NombreServicio.OFICINA_DE_PERSONAL);
            usuario.setServicio(servicioPersonal);
        }

        // Cargar imagen de perfil
        Image image = ImageUtils.byteArrayToImage(usuario.getProfileImage());
        if (image != null && image != imagenPerfilOriginal) {
            byte[] imageBytes = ImageUtils.imageToByteArray(image);
            usuario.setProfileImage(imageBytes);
        } else {
            usuario.setProfileImage(null);
        }

        // Asignar domicilio
        Domicilio domicilio = new Domicilio.Builder()
                .setCalle(domCalleComboBox.getEditor().getText())
                .setNumeracion(domNumeracionField.getText())
                .setBarrio(domBarrioComboBox.getEditor().getText())
                .setCiudad(domCiudadComboBox.getEditor().getText())
                .setLocalidad(domLocalidadComboBox.getEditor().getText())
                .setProvincia(domProvinciaComboBox.getEditor().getText())
                .build();

        try {
            if (usuario.getDomicilio() == null || usuario.getDomicilio().getId() == null) {
                // Crear nuevo domicilio
                domicilioService.create(domicilio);
                usuario.setDomicilio(domicilio);
            } else {
                // Actualizar domicilio existente
                domicilio.setId(usuario.getDomicilio().getId());
                domicilioService.update(domicilio);
                usuario.setDomicilio(domicilio);
            }
        } catch (ServiceException e) {
            mostrarError("Error al asignar el domicilio: " + e.getMessage());
            return null;
        }

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
        if (domCalleComboBox.getEditor().getText().isEmpty() || domNumeracionField.getText().isEmpty()
                || domCiudadComboBox.getEditor().getText().isEmpty() || domProvinciaComboBox.getEditor().getText().isEmpty()) {
            mostrarError("Debe completar todos los campos de domicilio obligatorios.");
            return false;
        }

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
        mostrarMensaje("Módulo de gestión de cargos en construcción.");
    }


    /**
     * Eventos para gestionar servicios (en construcción)
     * @param event Evento
     */
    @FXML
    private void onAbmServicio(ActionEvent event) {
        mostrarMensaje("Módulo de gestión de servicios en construcción.");
    }

}
