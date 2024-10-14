package ar.com.hmu.ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class LoginScreen extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Inicio de sesión :: Sistema de Gestión de Ausentismo HMU");

        // GridPane Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20, 20, 20, 20));
        grid.setVgap(10);
        grid.setHgap(10);

        // User Icon
        Image userIcon = new Image("https://via.placeholder.com/32"); // Placeholder para el icono del usuario
        ImageView userIconView = new ImageView(userIcon);
        grid.add(userIconView, 0, 0);

        // Username Field
        TextField usernameField = new TextField();
        usernameField.setPromptText("Ingrese nombre de usuario");
        grid.add(usernameField, 1, 0);

        // Password Field
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Ingrese contraseña");
        grid.add(passwordField, 1, 1);

        // Show Password Toggle
        CheckBox showPasswordCheckBox = new CheckBox("Mostrar contraseña");
        TextField passwordTextField = new TextField();
        passwordTextField.setManaged(false);
        passwordTextField.setVisible(false);
        grid.add(showPasswordCheckBox, 1, 2);

        passwordField.managedProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordField.visibleProperty().bind(showPasswordCheckBox.selectedProperty().not());
        passwordTextField.managedProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordTextField.visibleProperty().bind(showPasswordCheckBox.selectedProperty());
        passwordTextField.textProperty().bindBidirectional(passwordField.textProperty());
        grid.add(passwordTextField, 1, 1);

        // Remember Me Checkbox
        CheckBox rememberMeCheckBox = new CheckBox("Recordarme");
        grid.add(rememberMeCheckBox, 1, 3);

        // Login Button
        Button loginButton = new Button("INICIAR SESIÓN");
        loginButton.setStyle("-fx-background-color: #2a73ff; -fx-text-fill: white;");
        grid.add(loginButton, 1, 4);

        // Server Status
        Label serverStatusLabel = new Label("Servidor en línea");
        serverStatusLabel.setTextFill(Color.GREEN);
        grid.add(serverStatusLabel, 1, 5);

        // Set Scene
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
