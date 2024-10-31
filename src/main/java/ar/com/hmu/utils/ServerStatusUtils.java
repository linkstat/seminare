package ar.com.hmu.utils;

import ar.com.hmu.repository.DatabaseConnector;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.util.Duration;
import javafx.scene.image.Image;

import java.util.Objects;

public class ServerStatusUtils {

    private static Timeline serverCheckTimeline;
    private static int checkIntervalInSeconds = 4;  // Intervalo inicial de 4 segundos

    /**
     * Ajusta el intervalo de chequeo según el estado del servidor.
     *
     * @param serverIsFunctional true si el servidor está operativo; false si hay algún problema.
     * @param databaseConnector  el conector de base de datos para verificar la conectividad.
     * @param statusLabel        el Label donde se muestra el estado del servidor.
     * @param statusIcon         el ImageView donde se muestra el ícono del estado del servidor.
     */
    public static void adjustCheckInterval(boolean serverIsFunctional, DatabaseConnector databaseConnector, Label statusLabel, ImageView statusIcon) {
        if (serverIsFunctional) {
            checkIntervalInSeconds = 16; // Si el servidor está en línea y funcional, aumentar el intervalo de chequeo a 16 segundos
        } else {
            checkIntervalInSeconds = 4;  // Si el servidor tiene problemas, reducir el intervalo de chequeo a 4 segundos
        }

        if (serverCheckTimeline != null) {
            serverCheckTimeline.stop();
            serverCheckTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(checkIntervalInSeconds), event -> {
                boolean updatedServerStatus = updateServerStatus(databaseConnector, statusLabel, statusIcon);
                adjustCheckInterval(updatedServerStatus, databaseConnector, statusLabel, statusIcon);
            }));
            serverCheckTimeline.play();
        }
    }

    /**
     * Inicia un chequeo periódico del estado del servidor con un intervalo dinámico.
     *
     * @param databaseConnector conector de base de datos para verificar la conectividad.
     * @param statusLabel       el Label donde se muestra el estado del servidor.
     * @param statusIcon        el ImageView donde se muestra el ícono del estado del servidor.
     */
    public static void startPeriodicServerCheck(DatabaseConnector databaseConnector, Label statusLabel, ImageView statusIcon) {
        serverCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(checkIntervalInSeconds), event -> {
            boolean serverIsFunctional = updateServerStatus(databaseConnector, statusLabel, statusIcon);
            adjustCheckInterval(serverIsFunctional, databaseConnector, statusLabel, statusIcon);
        }));
        serverCheckTimeline.setCycleCount(Timeline.INDEFINITE); // Se ejecuta indefinidamente
        serverCheckTimeline.play(); // Inicia el Timeline
    }

    /**
     * Verifica el estado del servidor y actualiza el Label e ícono correspondientes.
     *
     * @param databaseConnector conector de base de datos para verificar la conectividad.
     * @param statusLabel       el Label donde se muestra el estado del servidor.
     * @param statusIcon        el ImageView donde se muestra el ícono del estado del servidor.
     * @return true si el servidor está en línea y funcional, false si hay algún problema.
     */
    public static boolean updateServerStatus(DatabaseConnector databaseConnector, Label statusLabel, ImageView statusIcon) {
        if (databaseConnector != null) {
            String[] serverStatus = databaseConnector.checkServerStatus();
            statusLabel.setText(serverStatus[0]);
            statusLabel.setStyle("-fx-text-fill: " + serverStatus[1] + ";");
            try {
                Image icon = new Image(Objects.requireNonNull(
                        ServerStatusUtils.class.getResourceAsStream(serverStatus[2])
                ));
                statusIcon.setImage(icon);
            } catch (NullPointerException | IllegalArgumentException e) {
                System.err.println("Error al cargar el icono de estado del servidor: " + e.getMessage());
                statusIcon.setImage(new Image(ServerStatusUtils.class.getResourceAsStream("/ar/com/hmu/images/icon_circle_blue_question_52x52.png")));
            }
            return serverStatus[0].equals("Servidor en línea y funcional.");
        } else {
            statusLabel.setText("Error al inicializar la conexión al servidor");
            statusLabel.setStyle("-fx-text-fill: red;");
            statusIcon.setImage(new Image(Objects.requireNonNull(ServerStatusUtils.class.getResourceAsStream("/ar/com/hmu/images/icon_circle_blue_question_52x52.png"))));
            return false;
        }
    }
}
