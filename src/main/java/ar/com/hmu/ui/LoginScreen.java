package ar.com.hmu.ui;

import ar.com.hmu.config.ConfigReader;
import ar.com.hmu.repository.DatabaseConnector;
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
        // Configurar la base de datos
        ConfigReader configReader = new ConfigReader();
        DatabaseConnector databaseConnector = new DatabaseConnector(configReader);

        // Carga del archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("loginScreen.fxml"));
        Parent root = loader.load();

        // Obtener el controlador y pasarle el DatabaseConnector
        LoginController controller = loader.getController();
        if (controller == null) {
            System.err.println("El controlador no fue inicializado correctamente.");
            return;
        }
        controller.setDatabaseConnector(databaseConnector);

        // Configurar la ventana principal
        Scene scene = new Scene(root);
        primaryStage.setTitle("Inicio de sesión :: Sistema de Gestión de Ausentismo HMU");
        primaryStage.setResizable(false);
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        //double windowWidth = screenBounds.getWidth() * 0.25; // 25% de la pantalla
        //double windowHeight = screenBounds.getHeight() * 0.25; // 25% de la pantalla
        //primaryStage.setWidth(windowWidth);
        //primaryStage.setHeight(windowHeight);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
