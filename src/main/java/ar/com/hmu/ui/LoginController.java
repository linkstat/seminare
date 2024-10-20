package ar.com.hmu.ui;

import ar.com.hmu.repository.DatabaseConnector;
import ar.com.hmu.utils.PasswordUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
//import org.mariadb.jdbc.Connection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

    private DatabaseConnector databaseConnector;
    // Método para establecer el DatabaseConnector desde fuera
    public void setDatabaseConnector(DatabaseConnector databaseConnector) {
        this.databaseConnector = databaseConnector;
    }

    @FXML
    private void handleLoginButtonClick() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        try (Connection connection = databaseConnector.getConnection()) {
            String query = "SELECT passwd FROM usuario WHERE cuil = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, username);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        String hashedPassword = resultSet.getString("passwd");
                        if (PasswordUtils.validatePassword(password, hashedPassword)) {
                            System.out.println("Inicio de sesión exitoso para el usuario: " + username);
                        } else {
                            System.out.println("Contraseña incorrecta para el usuario: " + username);
                        }
                    } else {
                        System.out.println("Usuario no encontrado: " + username);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al intentar conectar a la base de datos.");
        }
    }
}