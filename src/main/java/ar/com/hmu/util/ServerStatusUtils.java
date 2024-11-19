package ar.com.hmu.util;

import java.util.Objects;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.util.Duration;

import ar.com.hmu.constants.DatabaseConnectorStatus;
import ar.com.hmu.exceptions.DatabaseAuthenticationException;
import ar.com.hmu.exceptions.DatabaseConnectionException;
import ar.com.hmu.repository.DatabaseConnector;

public class ServerStatusUtils {

    private Timeline serverCheckTimeline;
    private int checkIntervalInSeconds = 4;  // Intervalo inicial de 4 segundos
    private DatabaseConnector databaseConnector;
    private Label statusLabel;
    private ImageView statusIcon;


    public ServerStatusUtils(DatabaseConnector databaseConnector, Label statusLabel, ImageView statusIcon) {
        this.databaseConnector = databaseConnector;
        this.statusLabel = statusLabel;
        this.statusIcon = statusIcon;
    }


    /**
     * Inicia un chequeo periódico del estado del servidor con un intervalo dinámico.
     *
     */
    public void startPeriodicServerCheck() {
        serverCheckTimeline = new Timeline(new KeyFrame(Duration.seconds(checkIntervalInSeconds), event -> {
            boolean serverIsFunctional = updateServerStatusUI();
            adjustCheckInterval(serverIsFunctional);
        }));
        serverCheckTimeline.setCycleCount(Timeline.INDEFINITE); // Se ejecuta indefinidamente
        serverCheckTimeline.play(); // Inicia el Timeline
    }

    /**
     * Verifica el estado del servidor y actualiza el Label e ícono correspondientes.
     *
     * @return true si el servidor está en línea y funcional, false si hay algún problema.
     */
    public boolean updateServerStatusUI() {
        String[] serverStatus = databaseConnector.checkServerStatus();

        // Update UI components
        statusLabel.setText(serverStatus[0]);
        statusLabel.setStyle("-fx-text-fill: " + serverStatus[1] + ";");
        try {
            Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream(serverStatus[2])));
            statusIcon.setImage(icon);
        } catch (NullPointerException | IllegalArgumentException e) {
            System.err.println(DatabaseConnectorStatus.ICON_LOAD_ERR + e.getMessage());
            statusIcon.setImage(new Image(getClass().getResourceAsStream(DatabaseConnectorStatus.ICON_LOAD_ERR_ICON)));
        }

        return DatabaseConnectorStatus.FUNCTIONAL_MSG.equals(serverStatus[0]);
    }


    public void stop() {
        if (serverCheckTimeline != null) {
            serverCheckTimeline.stop();
        }
    }


    /**
     * Ajusta el intervalo de chequeo según el estado del servidor.
     *
     * @param serverIsFunctional true si el servidor está operativo; false si hay algún problema.
     */
    private void adjustCheckInterval(boolean serverIsFunctional) {
        if (serverIsFunctional) {
            checkIntervalInSeconds = 16; // Si el servidor está en línea y funcional, aumentar el intervalo de chequeo a 16 segundos
        } else {
            checkIntervalInSeconds = 4;  // Si el servidor tiene problemas, reducir el intervalo de chequeo a 4 segundos
        }

        if (serverCheckTimeline != null) {
            serverCheckTimeline.stop();
            serverCheckTimeline.getKeyFrames().setAll(new KeyFrame(Duration.seconds(checkIntervalInSeconds), event -> {
                boolean updatedServerStatus = updateServerStatusUI();
                adjustCheckInterval(updatedServerStatus);
            }));
            serverCheckTimeline.play();
        }
    }

}
