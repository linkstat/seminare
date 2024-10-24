package ar.com.hmu.ui;

import ar.com.hmu.auth.LoginService;
import ar.com.hmu.auth.MainMenuService;
import ar.com.hmu.model.Usuario;
import ar.com.hmu.repository.DatabaseConnector;
import ar.com.hmu.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

/**
 * Controlador encargado de gestionar la interfaz de usuario de la pantalla de login.
 *
 * Esta clase se encarga de manejar la lógica de presentación del formulario de inicio de sesión,
 * incluyendo la validación de entradas del usuario y la coordinación con {@link LoginService}
 * para la autenticación de las credenciales.
 * Su responsabilidad principal es actualizar la interfaz de usuario en función de los resultados
 * obtenidos del servicio de autenticación, mostrando mensajes adecuados para informar al usuario.
 */
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Label serverStatusLabel;

    private Timeline serverCheckTimeline;
    private int checkIntervalInSeconds = 4;  // Intervalo inicial de 4 segundos

    private LoginService loginService;  // LoginService para la autenticación del usuario
    private DatabaseConnector databaseConnector;  // DatabaseConnector para la verificación del estado del servidor de BD

    /**
     * Método para establecer el {@link LoginService} que se utilizará para la autenticación.
     *
     * @param loginService el servicio de autenticación que se va a utilizar.
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }


    /**
     * Inicializa los componentes de la interfaz de usuario al cargar la pantalla de inicio de sesión.
     * <p>
     * Este método es invocado automáticamente por el framework JavaFX después de que se haya cargado
     * el archivo FXML correspondiente. Se encarga de realizar las siguientes tareas:
     * * Verifica el estado de conexión con el server y actualiza la GUI en consecuencia.
     * * Configura el TextBox del CUIL, limitando la entrada solo a números y además, le da un formato
     *   de fácil lectura para humanos (NN-UUVVVWWW-M en vez de NNUUVVVWWWM).
     */
    @FXML
    public void initialize() {
        updateServerStatus(); // Llamar a la función para verificar el estado del servidor al iniciar la ventana.
        configureCuilField(); // Configurar el campo de CUIL

        // Añadir el evento de presionar "Enter" para usernameField
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLoginButtonClick();
            }
        });

        // Añadir el evento de presionar "Enter" para passwordField
        passwordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleLoginButtonClick();
            }
        });

    }

    /**
     * Método para establecer el {@link DatabaseConnector} que se utilizará para verificar el estado del servidor.
     *
     * @param databaseConnector el conector de base de datos a utilizar.
     */
    public void setDatabaseConnector(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    /**
     * Método que se debe llamar después de establecer todas las dependencias necesarias.
     *
     * Este método se encarga de realizar todas las inicializaciones de la interfaz de usuario y de las
     * verificaciones necesarias para el estado del servidor.
     */
    public void postInitialize() {
        if (databaseConnector == null || loginService == null) {
            throw new IllegalStateException("Las dependencias no han sido configuradas correctamente.");
        }

        updateServerStatus();  // Actualizar el estado del servidor
        configureCuilField();  // Configurar el campo de CUIL después de configurar las dependencias
        startPeriodicServerCheck();  // Iniciar chequeo periódico del servidor
    }

    /**
     * Ajusta el intervalo de chequeo según el estado del servidor.
     *
     * @param serverIsFunctional true si el servidor está operativo; false si hay algún problema.
     */
    private void adjustCheckInterval(boolean serverIsFunctional) {
        if (serverIsFunctional) {
            // Si el servidor está en línea y funcional, aumentar el intervalo de chequeo a 16 segundos
            checkIntervalInSeconds = 16;
        } else {
            // Si el servidor tiene problemas, reducir el intervalo de chequeo a 4 segundos
            checkIntervalInSeconds = 4;
        }
        // Reiniciar el Timeline con el nuevo intervalo
        serverCheckTimeline.stop();
        serverCheckTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(checkIntervalInSeconds), event -> {
            boolean updatedServerStatus = updateServerStatus();
            adjustCheckInterval(updatedServerStatus);
        }));
        serverCheckTimeline.play();
    }

    /**
     * Inicia un chequeo periódico del estado del servidor con un intervalo dinámico.
     */
    private void startPeriodicServerCheck() {
        serverCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(checkIntervalInSeconds), event -> {
            boolean serverIsFunctional = updateServerStatus();
            adjustCheckInterval(serverIsFunctional);
        }));
        serverCheckTimeline.setCycleCount(Timeline.INDEFINITE); // Se ejecuta indefinidamente
        serverCheckTimeline.play(); // Inicia el Timeline
    }

    /**
     * Verifica el estado del servidor y actualiza el Label correspondiente.
     */
    private boolean updateServerStatus() {
        if (databaseConnector != null) {
            String[] serverStatus = databaseConnector.checkServerStatus();
            serverStatusLabel.setText(serverStatus[0]);
            serverStatusLabel.setStyle("-fx-text-fill: " + serverStatus[1] + ";");
            return serverStatus[0].equals("Servidor en línea y funcional.");
        } else {
            serverStatusLabel.setText("Error al inicializar la conexión al servidor");
            serverStatusLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
    }


    /**
     * Configura el campo de texto para el CUIL para que solo acepte números y formatee el texto automáticamente.
     */
    private void configureCuilField() {
        // Limitar el input a solo dígitos numéricos
        usernameField.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (!event.getCharacter().matches("\\d")) {
                event.consume();  // Ignora el input si no es un número
            }
        });

        // Añadir un listener para formatear el texto mientras se escribe
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Remover cualquier caracter que no sea un dígito
            String digitsOnly = newValue.replaceAll("[^\\d]", "");

            // Limitar la longitud máxima a 11 dígitos
            if (digitsOnly.length() > 11) {
                digitsOnly = digitsOnly.substring(0, 11);
            }

            // Aplicar el formato NN-NNNNNNNN-N
            String formattedText = formatCuil(digitsOnly);

            // Actualizar el campo de texto con el texto formateado
            usernameField.setText(formattedText);

            // Mover el cursor al final del texto
            usernameField.positionCaret(formattedText.length());
        });
    }

    /**
     * Aplica el formato NN-NNNNNNNN-N a una cadena de números.
     *
     * @param digits cadena de dígitos que representan el CUIL.
     * @return el CUIL formateado como NN-NNNNNNNN-N.
     */
    private String formatCuil(String digits) {
        StringBuilder formatted = new StringBuilder();

        if (digits.length() >= 2) {
            formatted.append(digits.substring(0, 2)).append("-");
        } else {
            formatted.append(digits);
            return formatted.toString();
        }

        if (digits.length() >= 10) {
            formatted.append(digits.substring(2, 10)).append("-");
            formatted.append(digits.substring(10));
        } else if (digits.length() > 2) {
            formatted.append(digits.substring(2));
        }

        return formatted.toString();
    }

    /**
     * Gestiona el evento del botón de inicio de sesión.
     *
     * Este método es invocado cuando el usuario hace clic en el botón de "Iniciar Sesión".
     * Valida las credenciales ingresadas por el usuario consultando la base de datos.
     * Si las credenciales son correctas, se muestra un mensaje de éxito; si no lo son, se muestra una advertencia.
     */
    @FXML
    private void handleLoginButtonClick() {
        try {
            // Obtener el CUIL y la contraseña ingresados
            String cuil = usernameField.getText().replaceAll("[^\\d]", "");  // Remover guiones para obtener solo números
            String password = passwordField.getText();

            if (loginService == null) {
                throw new IllegalStateException("LoginService no está configurado. No se puede validar la autenticación.");
            }

            // Validar el usuario con LoginService
            boolean isValidUser = loginService.validateUser(Long.parseLong(cuil), password);
            if (isValidUser) {
                // Obtener el usuario autenticado para pasarlo al menú principal
                Usuario usuario = loginService.getUsuarioByCuil(Long.parseLong(cuil));
                showMainMenu(usuario);
                //ANTES: AlertUtils.showInfo("Inicio de sesión exitoso para el CUIL: " + cuil);
            } else {
                AlertUtils.showWarn("¡Contraseña incorrecta o usuario no encontrado!\n[ CUIL ingresado: " + formatCuil(cuil) + " ]");
            }

        } catch (NumberFormatException e) {
            AlertUtils.showErr("El CUIL ingresado no es válido.\nDebe contener solo números.");
        } catch (IllegalStateException e) {
            AlertUtils.showErr("Error de configuración: " + e.getMessage());
        } catch (Exception e) {
            AlertUtils.showErr("Ocurrió un error inesperado: " + e.getMessage());
        }
    }

    /**
     * Muestra el menú principal de la aplicación después de un inicio de sesión exitoso.
     * <p>
     * Este método se encarga de cargar la vista del menú principal (mainMenu.fxml), configurar el controlador
     * del menú con el usuario autenticado y las opciones de menú correspondientes, y luego cambiar la escena
     * de la ventana principal de la aplicación para mostrar el menú principal.
     *
     * @param usuario El usuario autenticado que ha iniciado sesión.
     */
    private void showMainMenu(Usuario usuario) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ar/com/hmu/ui/mainMenu.fxml"));
            Parent root = loader.load();

            // Configurar el controlador del menú principal
            MainMenuController controller = loader.getController();
            controller.setMainMenuService(new MainMenuService());
            controller.setUsuarioActual(usuario);

            Stage stage = (Stage) loginButton.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace(); // Para imprimir todo el stack trace y facilitar el diagnóstico.
            AlertUtils.showErr("Error al cargar el menú principal:\n" + e.getMessage());
        }
    }

}
