package ar.com.hmu.ui;

import ar.com.hmu.auth.MainMenuService;
import ar.com.hmu.model.Usuario;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import java.util.List;

/**
 * Controlador para la pantalla del menú principal de la aplicación.
 *
 * Este controlador gestiona la vista del menú principal según los módulos accesibles para el usuario actual.
 */
public class MainMenuController {

    private MainMenuService mainMenuService;
    private Usuario usuarioActual;

    @FXML
    private ListView<String> moduleListView;

    @FXML
    private Button selectModuleButton;

    @FXML
    private Button handleLogoutButton;

    @FXML
    private Label agentLabel;

    @FXML
    private Label serviceLabel;

    @FXML
    private Label lastLoginLabel;

    @FXML
    private Label loginFromLabel;

    @FXML
    private Label userInfoLabel;

    @FXML
    private Label serverStatusLabel;

    /**
     * Establece el {@link MainMenuService} que se utilizará para determinar qué módulos mostrar.
     *
     * @param mainMenuService el servicio que gestiona la lógica de acceso a los módulos.
     */
    public void setMainMenuService(MainMenuService mainMenuService) {
        this.mainMenuService = mainMenuService;
    }

    /**
     * Establece el usuario actual para el cual se personalizará el menú principal.
     *
     * @param usuario el usuario actualmente autenticado.
     */
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        initializeModules();
    }

    /**
     * Inicializa los módulos de la vista según el tipo de usuario.
     */
    private void initializeModules() {
        if (usuarioActual == null) {
            throw new IllegalStateException("El usuario actual no ha sido configurado.");
        }

        // Obtener los módulos permitidos para el usuario específico
        List<String> availableModules = mainMenuService.getAvailableModules(usuarioActual);

        // Poblar la lista de módulos en la UI
        moduleListView.getItems().setAll(availableModules);
        //userInfoLabel.setText("Bienvenido, " + usuarioActual.getNombreCompleto());
    }

    @FXML
    public void initialize() {
        // Deshabilitar el botón de selección inicialmente hasta que se seleccione un módulo
        selectModuleButton.setDisable(true);
        moduleListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            selectModuleButton.setDisable(newSelection == null);
        });
    }

    /**
     * Maneja el evento del botón de selección de módulo.
     *
     * Cuando el usuario selecciona un módulo y hace clic en "Seleccionar", se lleva a cabo la lógica
     * correspondiente para abrir dicho módulo.
     */
    @FXML
    private void handleSelectModule() {
        String selectedModule = moduleListView.getSelectionModel().getSelectedItem();
        if (selectedModule != null) {
            // Aquí es donde se puede definir la lógica para redirigir al módulo seleccionado.
            System.out.println("Seleccionaste el módulo: " + selectedModule);
        }
    }

    /**
     * Maneja el evento de clic en el botón "Cerrar Sesión".
     *
     * Este método es invocado cuando el usuario hace clic en el botón "Cerrar Sesión" del menú principal.
     * Redirige al usuario a la pantalla de login.
     *
     * @param event el evento de acción generado al hacer clic en el botón.
     */
    @FXML
    public void handleLogout(ActionEvent event) {
        // Aquí se puede definir la lógica para volver a la pantalla de login.
        System.out.println("Cerrando sesión...");

        // Lógica de navegación a la pantalla de login, si es necesario
    }

}
