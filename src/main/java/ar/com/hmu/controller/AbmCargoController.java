package ar.com.hmu.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import ar.com.hmu.model.Cargo;
import ar.com.hmu.service.CargoService;
import ar.com.hmu.exceptions.ServiceException;
import ar.com.hmu.util.AlertUtils;

public class AbmCargoController {

    @FXML
    private TableView<Cargo> cargosTableView;
    @FXML
    private TableColumn<Cargo, String> descripcionColumn;
    @FXML
    private TextField descripcionTextField;

    private CargoService cargoService;

    public void setCargoService(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @FXML
    public void initialize() {
        // Configurar la columna de descripción
        descripcionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescripcion()));

        // Manejar la selección en la tabla
        cargosTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> mostrarDetalleCargo(newValue));

        cargarCargos();
    }

    private void cargarCargos() {
        try {
            cargosTableView.getItems().setAll(cargoService.readAll());
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al cargar los cargos: " + e.getMessage());
        }
    }

    private void mostrarDetalleCargo(Cargo cargo) {
        if (cargo != null) {
            descripcionTextField.setText(cargo.getDescripcion());
        } else {
            descripcionTextField.clear();
        }
    }

    @FXML
    private void onAgregarCargo() {
        String descripcion = descripcionTextField.getText().trim();
        if (descripcion.isEmpty()) {
            AlertUtils.showWarn("La descripción no puede estar vacía.");
            return;
        }

        Cargo cargo = new Cargo();
        cargo.setDescripcion(descripcion);

        try {
            cargoService.create(cargo);
            cargarCargos();
            descripcionTextField.clear();
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al agregar el cargo: " + e.getMessage());
        }
    }

    @FXML
    private void onModificarCargo() {
        Cargo seleccionado = cargosTableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtils.showWarn("Debe seleccionar un cargo para modificar.");
            return;
        }

        String descripcion = descripcionTextField.getText().trim();
        if (descripcion.isEmpty()) {
            AlertUtils.showWarn("La descripción no puede estar vacía.");
            return;
        }

        seleccionado.setDescripcion(descripcion);

        try {
            cargoService.update(seleccionado);
            cargarCargos();
            descripcionTextField.clear();
        } catch (ServiceException e) {
            AlertUtils.showErr("Error al modificar el cargo: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminarCargo() {
        Cargo seleccionado = cargosTableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            AlertUtils.showWarn("Debe seleccionar un cargo para eliminar.");
            return;
        }

        boolean confirmar = AlertUtils.showConfirm("¿Está seguro de eliminar el cargo seleccionado?");
        if (confirmar) {
            try {
                cargoService.delete(seleccionado);
                cargarCargos();
            } catch (ServiceException e) {
                AlertUtils.showErr("Error al eliminar el cargo: " + e.getMessage());
            }
        }
    }
}
