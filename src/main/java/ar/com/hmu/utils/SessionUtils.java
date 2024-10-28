package ar.com.hmu.utils;

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
            FXMLLoader loader = new FXMLLoader(SessionUtils.class.getResource("/ar/com/hmu/ui/loginScreen.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            currentStage.setScene(scene);
        } catch (IOException e) {
            System.err.println("Error al cargar la pantalla de inicio de sesión: " + e.getMessage());
        }
    }

}
