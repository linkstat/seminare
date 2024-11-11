package ar.com.hmu.ui;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

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
import ar.com.hmu.utils.CuilUtils;
import ar.com.hmu.utils.ImageUtils;

public class AbmUsuariosController implements Initializable {

    // Sección de Búsqueda
    @FXML private ComboBox<Usuario> busquedaComboBox;
    @FXML private Button buscarButton;

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
    @FXML private ComboBox<String> tipoUsuarioComboBox;
    @FXML private ComboBox<Cargo> cargoComboBox;
    @FXML private Button gestionarCargosButton;
    @FXML private ComboBox<Servicio> servicioComboBox;
    @FXML private Button gestionarServiciosButton;

    // Sección de Acciones
    @FXML private Button resetPasswdButton;
    @FXML private RadioButton usuarioHabilitadoCheckBox;
    @FXML private RadioButton usuariosDeshabilitadoCheckBox;
    @FXML private ToggleGroup estadoToggleGroup;
    @FXML private Button altaButton;
    @FXML private Button modificarButton;
    @FXML private Button eliminarButton;

    // Variables auxiliares
    private ObservableList<Usuario> usuariosList = FXCollections.observableArrayList();
    private ObservableList<Sexo> sexosList = FXCollections.observableArrayList(Sexo.values());
    private ObservableList<String> tiposUsuarioList = FXCollections.observableArrayList(TipoUsuario.EMPLEADO, TipoUsuario.JEFATURA_DE_SERVICIO, TipoUsuario.OFICINA_DE_PERSONAL, TipoUsuario.DIRECCION);
    private ObservableList<Cargo> cargosList = FXCollections.observableArrayList();
    private ObservableList<Servicio> serviciosList = FXCollections.observableArrayList();

    private File imagenPerfilFile;
    private Image imagenPerfilOriginal;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // Cargar datos necesarios
        cargarCargos();
        cargarServicios();

        // Configurar el campo de texto para el CUIL
        CuilUtils.configureCuilField(cuilTextField);

        // Inicializar ComboBoxes
        sexoComboBox.setItems(sexosList);
        tipoUsuarioComboBox.setItems(tiposUsuarioList);
        cargoComboBox.setItems(cargosList);
        servicioComboBox.setItems(serviciosList);
        // Configurar busquedaComboBox
        busquedaComboBox.setItems(usuariosList);
        busquedaComboBox.setEditable(true);
        busquedaComboBox.setOnKeyTyped(event -> filtrarUsuarios());
        busquedaComboBox.setConverter(new UsuarioStringConverter());

        // Inicializar estado de ComboBoxes y botones
        servicioComboBox.setDisable(false);
        gestionarServiciosButton.setDisable(false);
        cargoComboBox.setDisable(false);
        gestionarCargosButton.setDisable(false);
        altaButton.setDisable(true);
        modificarButton.setDisable(true);
        eliminarButton.setDisable(true);
        resetPasswdButton.setDisable(true);

        // Configurar ToggleGroup
        estadoToggleGroup = new ToggleGroup();
        usuarioHabilitadoCheckBox.setToggleGroup(estadoToggleGroup);
        usuariosDeshabilitadoCheckBox.setToggleGroup(estadoToggleGroup);

        // Cargar imagen por defecto
        imagenPerfilOriginal = imagenPerfilImageView.getImage();

        // Eventos adicionales
        tipoUsuarioComboBox.setOnAction(this::onTipoUsuarioSelected);

    }

    // Método para cargar cargos (simulado)
    private void cargarCargos() {
        // Aquí se deberían cargar los cargos desde la base de datos o fuente de datos
        cargosList.add(new Cargo(UUID.randomUUID(), 1200, "Agrupamiento profesional", Agrupacion.SERVICIO));
        cargosList.add(new Cargo(UUID.randomUUID(), 513, "Administrativo Nivel 1", Agrupacion.ADMINISTRATIVO));
    }

    // Método para cargar servicios (simulado)
    private void cargarServicios() {
        // Aquí se deberían cargar los servicios desde la base de datos o fuente de datos
        serviciosList.add(new Servicio(UUID.randomUUID(), "Servicio médico 1", Agrupacion.MEDICO));
        serviciosList.add(new Servicio(UUID.randomUUID(), "Servicio médico 2", Agrupacion.MEDICO));
        serviciosList.add(new Servicio(UUID.randomUUID(), "Oficina administrativa 1", Agrupacion.ADMINISTRATIVO));
        serviciosList.add(new Servicio(UUID.randomUUID(), "Oficina administrativa 2", Agrupacion.ADMINISTRATIVO));
        serviciosList.add(new Servicio(UUID.randomUUID(), "Informática", Agrupacion.TECNICO));
        serviciosList.add(new Servicio(UUID.randomUUID(), NombreServicio.DIRECCION, Agrupacion.PLANTAPOLITICA));
    }

    // Método para filtrar usuarios en el ComboBox de búsqueda
    private void filtrarUsuarios() {
        String textoIngresado = busquedaComboBox.getEditor().getText().toLowerCase();
        ObservableList<Usuario> filtrados = FXCollections.observableArrayList();

        for (Usuario usuario : usuariosList) {
            if (usuario.coincideCon(textoIngresado)) {
                filtrados.add(usuario);
            }
        }

        busquedaComboBox.setItems(filtrados);
        busquedaComboBox.show();
    }

    // Evento al seleccionar un tipo de usuario
    @FXML
    private void onTipoUsuarioSelected(ActionEvent event) {
        String tipoUsuario = tipoUsuarioComboBox.getSelectionModel().getSelectedItem();

        if (tipoUsuario != null) {
            switch (tipoUsuario) {
                case TipoUsuario.DIRECCION:
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
                case TipoUsuario.OFICINA_DE_PERSONAL:
                    // Asignar servicio automáticamente
                    Servicio servicioPersonal = buscarServicioPorNombre(NombreServicio.OFICINA_DE_PERSONAL);
                    servicioComboBox.getSelectionModel().select(servicioPersonal);
                    servicioComboBox.setDisable(true);
                    gestionarServiciosButton.setDisable(true);

                    // Habilitar cargoComboBox y gestionarCargosButton si es necesario
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);
                    break;
                case TipoUsuario.JEFATURA_DE_SERVICIO:
                    // Habilitar todos los elementos para selección normal
                    servicioComboBox.setDisable(false);
                    gestionarServiciosButton.setDisable(false);
                    cargoComboBox.setDisable(false);
                    gestionarCargosButton.setDisable(false);

                    // Validar si el servicio ya tiene jefes asignados
                    validarJefesDeServicio();
                    break;
                case TipoUsuario.EMPLEADO:
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

    // Método para buscar un servicio por nombre
    private Servicio buscarServicioPorNombre(String nombre) {
        for (Servicio servicio : serviciosList) {
            if (servicio.getNombre().equalsIgnoreCase(nombre)) {
                return servicio;
            }
        }
        return null;
    }

    // Método para buscar un Cargo por número
    private Cargo buscarCargoPorNumero(int numero) {
        for (Cargo cargo : cargosList) {
            if (cargo.getNumero() == numero) {
                return cargo;
            }
        }
        return null;
    }

    // Evento al hacer clic en "Cargar Imagen"
    @FXML
    private void onCargarImagen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen de Perfil");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File archivoSeleccionado = fileChooser.showOpenDialog(getStage());

        if (archivoSeleccionado != null) {
            imagenPerfilFile = archivoSeleccionado;
            Image imagenNueva = new Image(archivoSeleccionado.toURI().toString());
            imagenPerfilImageView.setImage(imagenNueva);
            revertirImagenButton.setDisable(false);
        }
    }

    // Evento al hacer clic en "Revertir Imagen"
    @FXML
    private void onRevertirImagen(ActionEvent event) {
        imagenPerfilImageView.setImage(imagenPerfilOriginal);
        imagenPerfilFile = null;
        revertirImagenButton.setDisable(true);
    }

    // Evento al hacer clic en "Eliminar Imagen"
    @FXML
    private void onEliminarImagen(ActionEvent event) {
        imagenPerfilImageView.setImage(null);
        imagenPerfilFile = null;
    }

    // Evento al hacer clic en "Resetear Contraseña"
    @FXML
    private void onResetearContrasena(ActionEvent event) {
        Usuario usuario = obtenerUsuarioActual();
        if (usuario != null) {
            Alert confirmacion = new Alert(AlertType.CONFIRMATION);
            confirmacion.setTitle("Resetear Contraseña");
            confirmacion.setHeaderText("¿Desea restablecer la contraseña por defecto para el usuario " + usuario.getNombreCompleto() + "?");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    usuario.setDefaultPassword();
                    Alert info = new Alert(AlertType.INFORMATION);
                    info.setTitle("Contraseña Restablecida");
                    info.setHeaderText("La contraseña por defecto ha sido establecida.");
                    info.showAndWait();
                }
            });
        }
    }

    // Evento al hacer clic en "Alta"
    @FXML
    private void onAlta(ActionEvent event) {
        if (validarCamposObligatorios()) {
            Usuario nuevoUsuario = crearOActualizarUsuarioDesdeFormulario(null);
            if (nuevoUsuario != null) {
                usuariosList.add(nuevoUsuario);
                limpiarFormulario();
                mostrarMensaje("Usuario agregado correctamente.");
            }
        }
    }

    // Evento al hacer clic en "Modificar"
    @FXML
    private void onModificar(ActionEvent event) {
        Usuario usuarioSeleccionado = busquedaComboBox.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null && validarCamposObligatorios()) {
            Usuario usuarioActualizado = crearOActualizarUsuarioDesdeFormulario(usuarioSeleccionado);
            if (usuarioActualizado != null) {
                // Actualizar en la lista si es necesario
                int index = usuariosList.indexOf(usuarioSeleccionado);
                usuariosList.set(index, usuarioActualizado);
                limpiarFormulario();
                mostrarMensaje("Usuario modificado correctamente.");
            }
        }
    }

    // Evento al hacer clic en "Eliminar"
    @FXML
    private void onEliminar(ActionEvent event) {
        Usuario usuarioSeleccionado = busquedaComboBox.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            Alert confirmacion = new Alert(AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar Usuario");
            confirmacion.setHeaderText("¿Está seguro de eliminar al usuario " + usuarioSeleccionado.getNombreCompleto() + "?");
            confirmacion.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    usuariosList.remove(usuarioSeleccionado);
                    limpiarFormulario();
                    mostrarMensaje("Usuario eliminado correctamente.");
                }
            });
        }
    }

    // Evento al seleccionar un usuario en el ComboBox de búsqueda
    @FXML
    private void onSeleccionarUsuario(ActionEvent event) {
        Usuario usuarioSeleccionado = busquedaComboBox.getSelectionModel().getSelectedItem();
        if (usuarioSeleccionado != null) {
            cargarUsuarioEnFormulario(usuarioSeleccionado);
            modificarButton.setDisable(false);
            eliminarButton.setDisable(false);
            resetPasswdButton.setDisable(false);
            altaButton.setDisable(true);
        } else {
            limpiarFormulario();
            modificarButton.setDisable(true);
            eliminarButton.setDisable(true);
            resetPasswdButton.setDisable(true);
            altaButton.setDisable(false);
        }
    }

    // Método para cargar los datos del usuario en el formulario
    private void cargarUsuarioEnFormulario(Usuario usuario) {
        // Actualizar la interfaz
        tipoUsuarioComboBox.getSelectionModel().select(obtenerTipoUsuario(usuario));
        onTipoUsuarioSelected(null); // Aquí actualiza la interfaz

        // Cargar datos...
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

    }

    // Método para obtener el tipo de usuario como String
    private String obtenerTipoUsuario(Usuario usuario) {
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

    // Método para crear un nuevo usuario desde los datos del formulario
    private Usuario crearOActualizarUsuarioDesdeFormulario(Usuario usuarioExistente) {
        String tipoUsuarioSeleccionado = tipoUsuarioComboBox.getSelectionModel().getSelectedItem();
        Usuario usuario;

        if (usuarioExistente == null) {
            // Crear una nueva instancia según el tipo de usuario
            switch (tipoUsuarioSeleccionado) {
                case TipoUsuario.EMPLEADO:
                    usuario = new Empleado();
                    usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
                    usuario.setServicio(servicioComboBox.getSelectionModel().getSelectedItem());
                    break;
                case TipoUsuario.JEFATURA_DE_SERVICIO:
                    usuario = new JefaturaDeServicio();
                    usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
                    usuario.setServicio(servicioComboBox.getSelectionModel().getSelectedItem());
                    break;
                case TipoUsuario.OFICINA_DE_PERSONAL:
                    usuario = new OficinaDePersonal();
                    // Asignar servicio automáticamente
                    Servicio servicioPersonal = buscarServicioPorNombre(NombreServicio.OFICINA_DE_PERSONAL);
                    usuario.setServicio(servicioPersonal);
                    // Cargo puede ser nulo o asignarse automáticamente si es necesario
                    usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
                    break;
                case TipoUsuario.DIRECCION:
                    usuario = new Direccion();
                    // Asignar cargo y servicio automáticamente
                    Cargo cargoDireccion = buscarCargoPorNumero(9999);
                    Servicio servicioDireccion = buscarServicioPorNombre(NombreServicio.DIRECCION);
                    usuario.setCargo(cargoDireccion);
                    usuario.setServicio(servicioDireccion);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de usuario desconocido: " + tipoUsuarioSeleccionado);
            }

        } else {
            // Actualizar el usuario existente
            usuario = usuarioExistente;

            // Verificar si el tipo de usuario ha cambiado
            if (!usuario.getClass().getSimpleName().equals(tipoUsuarioSeleccionado)) {
                // Manejar el cambio de tipo de usuario si es necesario
                // Por simplicidad, podríamos lanzar una excepción o crear una nueva instancia
                throw new UnsupportedOperationException("No se puede cambiar el tipo de usuario existente.");
            }
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

        // Cargar imagen de perfil
        Image image = ImageUtils.byteArrayToImage(usuario.getProfileImage());
        if (image != null && image != imagenPerfilOriginal) {
            byte[] imageBytes = ImageUtils.imageToByteArray(image);
            usuario.setProfileImage(imageBytes);
        } else {
            usuario.setProfileImage(null);
        }

        // Asignar cargo y servicio según el tipo de usuario
        switch (tipoUsuarioSeleccionado) {
            case TipoUsuario.EMPLEADO:
            case TipoUsuario.JEFATURA_DE_SERVICIO:
                usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
                usuario.setServicio(servicioComboBox.getSelectionModel().getSelectedItem());
                break;
            case TipoUsuario.OFICINA_DE_PERSONAL:
                Servicio servicioPersonal = buscarServicioPorNombre(NombreServicio.OFICINA_DE_PERSONAL);
                usuario.setServicio(servicioPersonal);
                usuario.setCargo(cargoComboBox.getSelectionModel().getSelectedItem());
                break;
            case TipoUsuario.DIRECCION:
                Cargo cargoDireccion = buscarCargoPorNumero(9999);
                Servicio servicioDireccion = buscarServicioPorNombre(NombreServicio.DIRECCION);
                usuario.setCargo(cargoDireccion);
                usuario.setServicio(servicioDireccion);
                break;
        }

        // Asignar domicilio...
        // Implementar lógica de asignación de domicilio
        return usuario;
    }

    // Método para validar los campos obligatorios
    private boolean validarCamposObligatorios() {
        if (cuilTextField.getText().isEmpty() || apellidosTextField.getText().isEmpty()
                || nombresTextField.getText().isEmpty() || mailTextField.getText().isEmpty()
                || sexoComboBox.getSelectionModel().isEmpty() || tipoUsuarioComboBox.getSelectionModel().isEmpty()) {
            mostrarError("Debe completar todos los campos obligatorios.");
            return false;
        }
        // Validar formatos, email válido, etc.
        return true;
    }

    // Método para limpiar el formulario
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

    // Método para obtener el usuario actualmente cargado en el formulario
    private Usuario obtenerUsuarioActual() {
        return busquedaComboBox.getSelectionModel().getSelectedItem();
    }

    // Método para mostrar mensajes de información
    private void mostrarMensaje(String mensaje) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Método para mostrar mensajes de error
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Obtener el Stage actual
    private Stage getStage() {
        return (Stage) busquedaComboBox.getScene().getWindow();
    }

    // Clase interna para convertir Usuario a String en el ComboBox
    private class UsuarioStringConverter extends StringConverter<Usuario> {
        @Override
        public String toString(Usuario usuario) {
            if (usuario == null) {
                return "";
            }
            return usuario.getNombreCompleto();
        }

        @Override
        public Usuario fromString(String string) {
            return null;
        }
    }

    // Eventos para gestionar cargos y servicios (en construcción)
    @FXML
    private void onAbmCargo(ActionEvent event) {
        mostrarMensaje("Módulo de gestión de cargos en construcción.");
    }

    @FXML
    private void onAbmServicio(ActionEvent event) {
        mostrarMensaje("Módulo de gestión de servicios en construcción.");
    }
}
