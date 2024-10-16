package ar.com.hmu.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private CheckBox showPasswordCheckBox;

    @FXML
    private CheckBox rememberMeCheckBox;

    @FXML
    private Button loginButton;

    @FXML
    public void initialize() {
        // Lógica de inicialización si es necesaria
    }

    @FXML
    private void handleLoginButtonClick() {
        // Aquí puedes agregar la lógica del botón "INICIAR SESIÓN"
        String username = usernameField.getText();
        String password = passwordField.getText();
        System.out.println("Intentando iniciar sesión con usuario: " + username + " y contraseña: " + password);
        // Implementar la lógica de autenticación...
    }
}
