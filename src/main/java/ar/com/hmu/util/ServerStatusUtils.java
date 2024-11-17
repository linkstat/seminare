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
                boolean updatedServerStatus = updateServerStatusUI(databaseConnector, statusLabel, statusIcon);
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
            boolean serverIsFunctional = updateServerStatusUI(databaseConnector, statusLabel, statusIcon);
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
    public static boolean updateServerStatusUI(DatabaseConnector databaseConnector, Label statusLabel, ImageView statusIcon) {
        if (databaseConnector != null) {
            try {
                // Llamamos al método checkServerStatus() para obtener el estado del servidor
                String[] serverStatus = databaseConnector.checkServerStatus();

                // Almacenamos los valores retornados por el método para actualizar la interfaz gráfica
                String message = serverStatus[0];    // Mensaje de estado del servidor
                String textColor = serverStatus[1];  // Color del texto
                String iconPath = serverStatus[2];   // Ruta del ícono

                // Actualiza el estado del servidor en la interfaz gráfica
                statusLabel.setText(message);
                statusLabel.setStyle("-fx-text-fill: " + textColor + ";");

                try {
                    // Cargar el ícono usando la ruta proporcionada
                    Image icon = new Image(Objects.requireNonNull(
                            ServerStatusUtils.class.getResourceAsStream(serverStatus[2])
                    ));
                    statusIcon.setImage(icon);
                } catch (NullPointerException | IllegalArgumentException e) {
                    // Manejar errores al cargar el ícono
                    System.err.println(DatabaseConnectorStatus.ICON_LOAD_ERR + e.getMessage());
                    statusIcon.setImage(new Image(ServerStatusUtils.class.getResourceAsStream(DatabaseConnectorStatus.ICON_LOAD_ERR_ICON)));
                }
                // Retornar si el servidor está en línea y funcional
                return DatabaseConnectorStatus.FUNCTIONAL_MSG.equals(message);

            } catch (DatabaseAuthenticationException e) {
                // Manejo específico del error de autenticación
                statusLabel.setText(DatabaseConnectorStatus.AUTH_PROBLEM_MSG);
                statusLabel.setStyle("-fx-text-fill: " + DatabaseConnectorStatus.AUTH_PROBLEM_COLOR + ";");
                statusIcon.setImage(new Image(Objects.requireNonNull(ServerStatusUtils.class.getResourceAsStream(DatabaseConnectorStatus.AUTH_PROBLEM_ICON))));
                System.err.println(e.getMessage());

            } catch (DatabaseConnectionException e) {
                // Manejo específico del error de conexión
                statusLabel.setText(DatabaseConnectorStatus.DB_SERVICE_PROBLEM_MSG);
                statusLabel.setStyle("-fx-text-fill: " + DatabaseConnectorStatus.DB_SERVICE_PROBLEM_COLOR + ";");
                statusIcon.setImage(new Image(Objects.requireNonNull(ServerStatusUtils.class.getResourceAsStream(DatabaseConnectorStatus.DB_SERVICE_PROBLEM_ICON))));
                System.err.println(e.getMessage());

            } catch (Exception e) {
                // En caso de excepción, actualizar el estado a un error desconocido
                statusLabel.setText(DatabaseConnectorStatus.UNKNOWN_ERROR_MSG);
                statusLabel.setStyle(DatabaseConnectorStatus.UNKNOWN_ERROR_STYLE);
                statusIcon.setImage(new Image(Objects.requireNonNull(ServerStatusUtils.class.getResourceAsStream(DatabaseConnectorStatus.UNKNOWN_ERROR_ICON))));
                System.err.println(DatabaseConnectorStatus.UNKNOWN_ERROR_ERR + e.getMessage());
                return false;
            }
            return false;
        } else {
            // Caso cuando el `databaseConnector` es nulo
            statusLabel.setText(DatabaseConnectorStatus.NULL_DB_CONNECTOR_MSG);
            statusLabel.setStyle(DatabaseConnectorStatus.NULL_DB_CONNECTOR_STYLE);
            statusIcon.setImage(new Image(Objects.requireNonNull(ServerStatusUtils.class.getResourceAsStream(DatabaseConnectorStatus.NULL_DB_CONNECTOR_ICON))));
            return false;
        }
    }

}
