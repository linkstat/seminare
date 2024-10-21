package ar.com.hmu.ui;

import ar.com.hmu.auth.LoginService;
import ar.com.hmu.utils.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLException;

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

    // LoginService para la autenticación del usuario
    private LoginService loginService;

    /**
     * Método para establecer el {@link LoginService} que se utilizará para la autenticación.
     *
     * @param loginService el servicio de autenticación que se va a utilizar.
     */
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    /**
     * Inicializa los componentes de la interfaz de usuario.
     *
     * Este método es llamado automáticamente por el framework JavaFX después de que se cargue el archivo FXML.
     */
    @FXML
    public void initialize() {
        configureCuilField();
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
                AlertUtils.showInfo("Inicio de sesión exitoso para el CUIL: " + cuil);
            } else {
                AlertUtils.showWarn("¡Contraseña incorrecta o usuario no encontrado! [CUIL ingresado: " + cuil + "]");
            }

        } catch (NumberFormatException e) {
            AlertUtils.showErr("El CUIL ingresado no es válido.\nDebe contener solo números.");
        } catch (IllegalStateException e) {
            AlertUtils.showErr("Error de configuración: " + e.getMessage());
        } catch (Exception e) {
            AlertUtils.showErr("Ocurrió un error inesperado: " + e.getMessage());
        }
    }
}
