package ar.com.hmu.controller;

import ar.com.hmu.model.Agrupacion;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import ar.com.hmu.model.Servicio;
import ar.com.hmu.service.ServicioService;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.service.UsuarioService;
import ar.com.hmu.util.AlertUtils;

import java.util.UUID;

public class AbmServicioController {

    @FXML
    private TableView<Servicio> serviciosTableView;
    @FXML
    private TableColumn<Servicio, String> nombreColumn;
    @FXML
    private TableColumn<Servicio, String> agrupacionColumn;
    @FXML
    private TextField nombreTextField;
    @FXML
    private ComboBox<Agrupacion> agrupacionComboBox;
    private ObservableList<Agrupacion> agrupacionList = FXCollections.observableArrayList(Agrupacion.values());

    private ServicioService servicioService;

    public void setServices(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @FXML
    public void initialize() {
        // Configurar la columna de nombre de servicio
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        // Configurar la columna de agrupación
        agrupacionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAgrupacion().toString()));
        // Manejar la selección en la tabla
        serviciosTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetalleServicio(newValue));
        // Inicializar ComboBox
        agrupacionComboBox.setItems(agrupacionList);
        cargarServicios();
    }

    private void cargarServicios() {
        try {
            serviciosTableView.getItems().setAll(servicioService.readAll());
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar los servicios: " + e.getMessage());
        }
    }

    private void mostrarDetalleServicio(Servicio servicio) {
        if (servicio != null) {
            nombreTextField.setText(servicio.getNombre());
            agrupacionComboBox.getSelectionModel().select(servicio.getAgrupacion());
        } else {
            nombreTextField.clear();
            agrupacionComboBox.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void onAgregarServicio() {
        String nombre = nombreTextField.getText().trim();
        Agrupacion agrupacion = (Agrupacion) agrupacionComboBox.getSelectionModel().getSelectedItem();
        if (nombre.isEmpty() || agrupacion == null) {
            AlertUtils.showWarn("Se deben especificar nombre de servicio y agrupación.");
            return;
        }

        Servicio servicio = new Servicio();
        servicio.setId(UUID.randomUUID());
        servicio.setNombre(nombre);
        servicio.setAgrupacion(agrupacion);

        try {
            servicioService.create(servicio);
            cargarServicios();
            nombreTextField.clear();
            agrupacionComboBox.getSelectionModel().clearSelection();
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al agregar el servicio: " + e.getMessage());
        }
    }

    @FXML
    private void onModificarServicio() {
        Servicio seleccionado = serviciosTableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtils.showWarn("Debe seleccionar un servicio para modificar.");
            return;
        }

        String nombre = nombreTextField.getText().trim();
        Agrupacion agrupacion = (Agrupacion) agrupacionComboBox.getSelectionModel().getSelectedItem();
        if (nombre.isEmpty() || agrupacion == null) {
            AlertUtils.showWarn("El nombre de servicio no puede estar vacío. Además, debe seleccionar una agrupación");
            return;
        }

        seleccionado.setNombre(nombre);
        seleccionado.setAgrupacion(agrupacion);

        try {
            servicioService.update(seleccionado);
            cargarServicios();
            nombreTextField.clear();
            agrupacionComboBox.getSelectionModel().clearSelection();
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al modificar el servicio: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarServicio() {
        Servicio seleccionado = serviciosTableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtils.showWarn("Debe seleccionar un servicio para eliminar.");
            return;
        }

        boolean confirmar = AlertUtils.showConfirm("¿Está seguro de eliminar el servicio seleccionado?");
        if (confirmar) {
            try {
                servicioService.delete(seleccionado);
                cargarServicios();
            } catch (ServiceException e) {
                AlertUtils.showErr("Error al eliminar el servicio: " + e.getMessage());
            }
        }
    }
}
