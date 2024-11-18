package ar.com.hmu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import ar.com.hmu.model.Servicio;
import ar.com.hmu.service.ServicioService;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.util.AlertUtils;

public class AbmServicioController {

    @FXML
    private TableView<Servicio> serviciosTableView;
    @FXML
    private TableColumn<Servicio, String> nombreColumn;
    @FXML
    private TextField nombreTextField;

    private ServicioService servicioService;

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @FXML
    public void initialize() {
        //nombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        cargarServicios();
    }

    private void cargarServicios() {
        try {
            serviciosTableView.getItems().setAll(servicioService.readAll());
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar los servicios: " + e.getMessage());
        }
    }

    @FXML
    private void onAgregarServicio() {
        String nombre = nombreTextField.getText().trim();
        if (nombre.isEmpty()) {
            AlertUtils.showWarn("El nombre no puede estar vacío.");
            return;
        }

        Servicio servicio = new Servicio();
        servicio.setNombre(nombre);

        try {
            servicioService.create(servicio);
            cargarServicios();
            nombreTextField.clear();
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
        if (nombre.isEmpty()) {
            AlertUtils.showWarn("El nombre no puede estar vacío.");
            return;
        }

        seleccionado.setNombre(nombre);

        try {
            servicioService.update(seleccionado);
            cargarServicios();
            nombreTextField.clear();
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
