package ar.com.hmu.utils;

import ar.com.hmu.auth.LoginService;
import ar.com.hmu.config.AppConfigReader;
import ar.com.hmu.repository.*;
import ar.com.hmu.controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SessionUtils {

    /**
     * Muestra la ventana de inicio de sesión y cierra el menú principal.
     */
    public static void handleLogout(Stage currentStage) {
        try {
            // Cargar el archivo FXML de la pantalla de login
            FXMLLoader loader = new FXMLLoader(LoginController.class.getResource("/ar/com/hmu/ui/loginScreen.fxml"));
            Parent root = loader.load();

            // Obtener el controlador
            LoginController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El controlador de Login no fue inicializado correctamente.");
            }

            // Configurar las dependencias
            AppConfigReader appConfigReader = new AppConfigReader();
            DatabaseConnector databaseConnector = new DatabaseConnector(appConfigReader);
            DomicilioRepository domicilioRepository = new DomicilioRepository(databaseConnector);
            CargoRepository cargoRepository = new CargoRepository(databaseConnector);
            ServicioRepository servicioRepository = new ServicioRepository(databaseConnector);
            UsuarioRepository usuarioRepository = new UsuarioRepository(databaseConnector, domicilioRepository, cargoRepository, servicioRepository);
            LoginService loginService = new LoginService(usuarioRepository);

            controller.setLoginService(loginService);
            controller.setDatabaseConnector(databaseConnector);
            controller.postInitialize();

            // Configurar la nueva ventana
            Stage stage = new Stage();
            stage.setTitle("Inicio de sesión" + " :: " + AppInfo.PRG_LONG_TITLE);
            stage.setResizable(false);

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

}
