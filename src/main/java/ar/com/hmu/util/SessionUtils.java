package ar.com.hmu.util;

import ar.com.hmu.auth.LoginService;
import ar.com.hmu.config.AppConfigReader;
import ar.com.hmu.factory.UsuarioFactory;
import ar.com.hmu.repository.*;
import ar.com.hmu.controller.LoginController;
import ar.com.hmu.service.RoleService;
import ar.com.hmu.service.UsuarioService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

public class SessionUtils {

    /**
     * Muestra la ventana de inicio de sesión y cierra el menú principal.
     */
    public static void handleLogout(Stage currentStage) {
        try {
            // Cargar el archivo FXML de la pantalla de login
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/fxml/loginScreen.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            LoginController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El controlador de Login no fue inicializado correctamente.");
            }

            // Inicialización de la configuración y el conector de base de datos
            AppConfigReader appConfigReader = new AppConfigReader();
            DatabaseConnector databaseConnector = new DatabaseConnector(appConfigReader);

            // Inicialización de repositorios (excepto UsuarioRepository)
            RoleRepository roleRepository = new RoleRepository(databaseConnector);
            ServicioRepository servicioRepository = new ServicioRepository(databaseConnector);
            CargoRepository cargoRepository = new CargoRepository(databaseConnector);
            DomicilioRepository domicilioRepository = new DomicilioRepository(databaseConnector);

            // Inicialización de servicios que no dependen de UsuarioRepository
            RoleService roleService = new RoleService(roleRepository);

            // Inicialización de UsuarioFactory
            UsuarioFactory usuarioFactory = new UsuarioFactory(roleService);

            // Inicialización de UsuarioRepository
            UsuarioRepository usuarioRepository = new UsuarioRepository(databaseConnector, usuarioFactory);

            // Inicialización de UsuarioService
            UsuarioService usuarioService = new UsuarioService(
                    usuarioRepository, servicioRepository, cargoRepository, domicilioRepository, roleService
            );

            // Inicialización de otros servicios
            LoginService loginService = new LoginService(usuarioService);

            // Pasar los servicios al controlador
            controller.setLoginService(loginService);
            controller.setDatabaseConnector(databaseConnector);
            controller.setRolService(roleService);
            controller.setUsuarioService(usuarioService);
            controller.postInitialize();

            // Configurar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Inicio de sesión" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setResizable(false);

            // Establecer el ícono de la aplicación
            setStageIcon(stage, AppInfo.ICON_IMAGE);

            Scene scene = new Scene(root);
            stage.setScene(scene);

            // Cerrar la ventana actual
            currentStage.close();

            // Mostrar la ventana de inicio de sesión
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.showErr("Error al cargar la pantalla de inicio de sesión:\n" + e.getMessage());
        }
    }

    private static void setStageIcon(Stage stage, String iconPath) {
        try {
            Image icon = new Image(Objects.requireNonNull(
                    SessionUtils.class.getResourceAsStream(iconPath)
            ));
            if (icon.isError()) {
                throw new IllegalArgumentException("Error al cargar el icono: " + iconPath);
            }
            stage.getIcons().add(icon);
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir mensaje completo del error por consola
            System.err.println("No se pudo establecer el icono: " + e.getMessage());
        }
    }

}
