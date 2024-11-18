package ar.com.hmu.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import ar.com.hmu.model.Servicio;
import ar.com.hmu.service.ServicioService;
import ar.com.hmu.exceptions.ServiceException;

import java.util.List;

public class ListaServiciosController {

    @FXML
    private TableView<Servicio> serviciosTableView;
    @FXML
    private TableColumn<Servicio, String> nombreServicioColumn;
    @FXML
    private TableColumn<Servicio, Integer> cantidadUsuariosColumn;
    @FXML
    private TableColumn<Servicio, String> apellidoJefeColumn;

    private ServicioService servicioService;

    public void setServicioService(ServicioService servicioService) {
        this.servicioService = servicioService;
    }

    @FXML
    public void initialize() {
        nombreServicioColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        cantidadUsuariosColumn.setCellValueFactory(cellData -> {
            int cantidadUsuarios = obtenerCantidadUsuarios(cellData.getValue());
            return new SimpleIntegerProperty(cantidadUsuarios).asObject();
        });
        apellidoJefeColumn.setCellValueFactory(cellData -> {
            String apellidoJefe = obtenerApellidoJefe(cellData.getValue());
            return new SimpleStringProperty(apellidoJefe);
        });

        cargarServicios();
    }

    private void cargarServicios() {
        try {
            List<Servicio> servicios = servicioService.readAll();
            serviciosTableView.getItems().setAll(servicios);
        } catch (ServiceException e) {
            // Manejo de errores
            e.printStackTrace();
        }
    }

    private int obtenerCantidadUsuarios(Servicio servicio) {
        try {
            return servicioService.obtenerCantidadUsuariosPorServicio(servicio.getId());
        } catch (ServiceException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String obtenerApellidoJefe(Servicio servicio) {
        try {
            return servicioService.obtenerApellidoJefePorServicio(servicio.getId());
        } catch (ServiceException e) {
            e.printStackTrace();
            return "";
        }
    }
}
