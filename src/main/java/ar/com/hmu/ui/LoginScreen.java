package ar.com.hmu.ui;

import ar.com.hmu.auth.LoginService;
import ar.com.hmu.config.ConfigReader;
import ar.com.hmu.repository.DatabaseConnector;
import ar.com.hmu.repository.UsuarioRepository;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Clase principal que representa la pantalla de inicio de sesión de la aplicación.
 *
 * Esta clase extiende {@link Application} y es responsable de cargar la interfaz de usuario (JavaFX),
 * inicializar los componentes, y configurar la ventana principal para el inicio de sesión.
 * Utiliza {@link ConfigReader} para obtener la configuración de la base de datos y {@link DatabaseConnector}
 * para establecer la conexión necesaria para validar las credenciales del usuario.
 */
public class LoginScreen extends Application {

    /**
     * Método principal de la aplicación JavaFX que configura la ventana de inicio de sesión.
     *
     * Este método se invoca automáticamente cuando se inicia la aplicación con {@link #launch(String...)}.
     * Se encarga de configurar la base de datos, cargar el archivo FXML correspondiente a la pantalla de login,
     * y establecer las propiedades de la ventana.
     *
     * @param primaryStage el escenario principal de la aplicación, proporcionado por el framework JavaFX.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            // Configurar servicios necesarios
            LoginService loginService = initializeLoginService();

            // Cargar el archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/loginScreen.fxml"));
            Parent root = loader.load();

            // Obtener el controlador y pasarle el LoginService
            LoginController controller = loader.getController();
            if (controller == null) {
                throw new IllegalStateException("El controlador no fue inicializado correctamente.");
            }
            controller.setLoginService(loginService);

            // Configurar la ventana principal
            Scene scene = new Scene(root);
            primaryStage.setTitle("Inicio de sesión :: Sistema de Gestión de Ausentismo HMU");
            primaryStage.setResizable(false);

            // Establecer el icono de la aplicación
            setStageIcon(primaryStage, "/ar/com/hmu/images/app-icon.png");

            // Mostrar la ventana principal
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error durante la inicialización de la aplicación: " + e.getMessage());
        }
    }

    /**
     * Inicializa el servicio de autenticación configurando la base de datos y los repositorios necesarios.
     *
     * @return una instancia de {@link LoginService} para la validación de credenciales.
     */
    private LoginService initializeLoginService() {
        ConfigReader configReader = new ConfigReader();
        DatabaseConnector databaseConnector = new DatabaseConnector(configReader);
        UsuarioRepository usuarioRepository = new UsuarioRepository(databaseConnector);
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
            Image icon = new Image(getClass().getResourceAsStream(iconPath));
            if (icon.isError()) {
                throw new IllegalArgumentException("Error al cargar el icono: " + iconPath);
            }
            stage.getIcons().add(icon);
        } catch (Exception e) {
            System.err.println("No se pudo establecer el icono: " + e.getMessage());
        }
    }

    /**
     * Método de entrada principal de la aplicación.
     *
     * Este método es el punto de entrada cuando se ejecuta la aplicación JavaFX.
     * Llama al método {@link #launch(String...)} que se encarga de iniciar la interfaz gráfica.
     *
     * @param args los argumentos de la línea de comandos (no se utilizan en este caso).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
