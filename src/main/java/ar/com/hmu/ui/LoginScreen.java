package ar.com.hmu.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class LoginScreen extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Seteo de las propiedades de la "ventana" de login
        primaryStage.setTitle("Inicio de sesión :: Sistema de Gestión de Ausentismo HMU");
        primaryStage.setResizable(false);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        //double windowWidth = screenBounds.getWidth() * 0.25; // 25% de la pantalla
        //double windowHeight = screenBounds.getHeight() * 0.25; // 25% de la pantalla
        //primaryStage.setWidth(windowWidth);
        //primaryStage.setHeight(windowHeight);

        // Carga del archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScreen.fxml"));
        Parent root = loader.load();

        // Crear la escena a partir del FXML
        //Scene scene = new Scene(root, 400, 300);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
