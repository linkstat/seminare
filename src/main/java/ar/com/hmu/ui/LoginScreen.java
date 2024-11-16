package ar.com.hmu.ui;

import java.util.Objects;

import ar.com.hmu.controller.LoginController;
import ar.com.hmu.repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import ar.com.hmu.auth.LoginService;
import ar.com.hmu.config.AppConfigReader;
import ar.com.hmu.utils.AppInfo;

/**
 * Clase principal que representa la pantalla de inicio de sesión de la aplicación.
 * <p>
 * Esta clase extiende {@link Application} y es responsable de cargar la interfaz de usuario (<i>JavaFX</i>),
 * inicializar los componentes, y configurar la ventana principal para el inicio de sesión.
 * Utiliza {@link AppConfigReader} para obtener la configuración de la base de datos y {@link DatabaseConnector}
 * para establecer la conexión necesaria para validar las credenciales del usuario.
 */
public class LoginScreen extends Application {

    /**
     * Método principal de la aplicación JavaFX que configura la ventana de inicio de sesión.
     * <p>
     * Este método se invoca automáticamente cuando se inicia la aplicación con {@link #launch(String...)}.
     * Se encarga de configurar la base de datos, cargar el archivo FXML correspondiente a la pantalla de login,
     * y establecer las propiedades de la ventana.
     *
     * @param primaryStage el escenario principal de la aplicación, proporcionado por el framework JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Cargar las fuentes personalizadas
            Font.loadFont(getClass().getResourceAsStream("/fonts/pristina.ttf"), 10);
            Font.loadFont(getClass().getResourceAsStream("/fonts/BarlowCondensed-Regular.ttf"), 10);

            // Configurar servicios necesarios
            //LoginService loginService = initializeLoginService();
            AppConfigReader appConfigReader = new AppConfigReader();
            DatabaseConnector databaseConnector = new DatabaseConnector(appConfigReader);
            DomicilioRepository domicilioRepository = new DomicilioRepository(databaseConnector);
            CargoRepository cargoRepository = new CargoRepository(databaseConnector);
            ServicioRepository servicioRepository = new ServicioRepository(databaseConnector);
            UsuarioRepository usuarioRepository = new UsuarioRepository(databaseConnector, domicilioRepository, cargoRepository, servicioRepository);
            LoginService loginService = new LoginService(usuarioRepository);

            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/loginScreen.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el LoginService
            LoginController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El controlador no fue inicializado correctamente.");
            }
            controller.setLoginService(loginService);
            controller.setDatabaseConnector(databaseConnector);

            // Llamar al método que depende de las dependencias inicializadas
            controller.postInitialize();

            // Configurar la ventana principal
            Scene scene = new Scene(root);
            primaryStage.setTitle("Inicio de sesión" + " :: " + AppInfo.PRG_LONG_TITLE);
            primaryStage.setResizable(false);

            // Establecer el icono de la aplicación
            setStageIcon(primaryStage, "app-icon.png");

            // Mostrar la ventana principal
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace(); // Imprimir mensaje completo del error por consola
            System.err.println("Error durante la inicialización de la aplicación: " + e.getMessage());
        }
    }

    /**
     * Inicializa el servicio de autenticación configurando la base de datos y los repositorios necesarios.
     *
     * @return una instancia de {@link LoginService} para la validación de credenciales.
     */
    private LoginService initializeLoginService() {
        AppConfigReader appConfigReader = new AppConfigReader();
        DatabaseConnector databaseConnector = new DatabaseConnector(appConfigReader);
        DomicilioRepository domicilioRepository = new DomicilioRepository(databaseConnector);
        CargoRepository cargoRepository = new CargoRepository(databaseConnector);
        ServicioRepository servicioRepository = new ServicioRepository(databaseConnector);
        UsuarioRepository usuarioRepository = new UsuarioRepository(databaseConnector, domicilioRepository, cargoRepository, servicioRepository);
        return new LoginService(usuarioRepository);
    }

    /**
     * Establece el icono del {@link Stage} utilizando el recurso especificado.
     *
     * @param stage     el escenario al cual se le asignará el icono.
     * @param iconPath  la ruta del recurso del icono.
     */
    private void setStageIcon(Stage stage, String iconPath) {
        try {
            Image icon = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream(iconPath)
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

    /**
     * Método de entrada principal de la aplicación.
     * <p>
     * Este método es el punto de entrada cuando se ejecuta la aplicación <i>JavaFX</i>.
     * Llama al método {@link #launch(String...)} que se encarga de iniciar la interfaz gráfica.
     *
     * @param args los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
