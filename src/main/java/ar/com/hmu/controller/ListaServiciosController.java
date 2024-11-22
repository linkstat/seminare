package ar.com.hmu.controller;

import ar.com.hmu.service.*;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

import ar.com.hmu.model.Servicio;
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

    // Servicios
    private UsuarioService usuarioService;
    private CargoService cargoService;
    private ServicioService servicioService;
    private DomicilioService domicilioService;
    private RolService rolService;

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
            String JefeDeServicio = obtenerJefeDeServicio(cellData.getValue());
            return new SimpleStringProperty(JefeDeServicio);
        });

        cargarServicios();
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
            return servicioService.countUsuariosByServicio(servicio.getId());
        } catch (ServiceException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private String obtenerJefeDeServicio(Servicio servicio) {
        try {
            return servicioService.findJefeByServicio(servicio.getId());
        } catch (ServiceException e) {
            e.printStackTrace();
            return "";
        }
    }
}
