package ar.com.hmu.repository;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import ar.com.hmu.config.AppConfig;
import ar.com.hmu.config.AppConfigReader;
import ar.com.hmu.constants.DatabaseConnectorStatus;
import ar.com.hmu.exceptions.DatabaseAuthenticationException;
import ar.com.hmu.exceptions.DatabaseConnectionException;
import ar.com.hmu.exceptions.DatabaseErrorType;

/**
 * Clase responsable de gestionar la conexión a la base de datos.
 * <p>
 * La clase {@link DatabaseConnector} utiliza los valores de configuración proporcionados por {@link AppConfigReader}
 * para establecer una conexión con la base de datos especificada. Actualmente, soporta la conexión
 * a bases de datos de tipo <i>MariaDB</i> (y desestima otros motores de BD, indicando que no están soportados).
 */
public class DatabaseConnector {

    private final String dbType;
    private final String dbHost;
    private final int dbPort;
    private final String dbName;
    private final String dbUser;
    private final String dbPass;

    /**
     * Constructor que inicializa el conector de base de datos utilizando un objeto {@link AppConfigReader}.
     * <p>
     * Este constructor recupera la configuración de la base de datos a través de un {@link AppConfigReader},
     * que lee los detalles de conexión desde un archivo de configuración (por ejemplo, YAML).
     *
     * @param appConfigReader una instancia de {@link AppConfigReader} que proporciona los valores de configuración necesarios.
     */
    public DatabaseConnector(AppConfigReader appConfigReader) {
        // Obtenemos la configuración desde ConfigReader
        AppConfig dbConfig = appConfigReader.getAppConfig();

        // Asignamos los valores de configuración
        this.dbType = dbConfig.getDbType();
        this.dbHost = dbConfig.getDbHost();
        this.dbPort = dbConfig.getDbPort();
        this.dbName = dbConfig.getDbName();
        this.dbUser = dbConfig.getDbUser();
        this.dbPass = dbConfig.getDbPass();
    }

    /**
     * Establece y devuelve una conexión con la base de datos.
     * <p>
     * Este método crea una conexión a la base de datos utilizando los valores de configuración previamente
     * obtenidos. Actualmente, solo se soporta la conexión a bases de datos de tipo <i>MariaDB</i>, por lo cual si
     * el motor de BD fuera otro distinto de <i>MariaDB</i>, se tira una excepción indicando que no hay implementación.
     *
     * @return una instancia de {@link Connection} que representa la conexión activa a la base de datos.
     * @throws SQLException si ocurre un error al intentar conectar a la base de datos.
     * @throws UnsupportedOperationException si el tipo de base de datos no es soportado.
     */
    public Connection getConnection() throws SQLException, DatabaseAuthenticationException, DatabaseConnectionException {
        if (!dbType.equalsIgnoreCase("mariadb")) {
            throw new UnsupportedOperationException("Motor de base de datos no implementado: " + dbType);
        }

        String url = String.format("jdbc:mariadb://%s:%d/%s", dbHost, dbPort, dbName);
        try {
            return DriverManager.getConnection(url, dbUser, dbPass);
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            int errorCode = e.getErrorCode();

            if ("28000".equals(sqlState) && errorCode == 1045) {
                // Authentication error
                String exceptionMessage = "Error de autenticación: No se pudo autenticar con el servidor de base de datos "
                        + dbType + " en " + dbHost + ":" + dbPort + " con el usuario '" + dbUser
                        + "'. Verificar credenciales en el archivo de configuración YAML.";
                throw new DatabaseAuthenticationException(exceptionMessage, e, DatabaseErrorType.AUTHENTICATION_FAILURE);
            } else {
                // Other SQL exceptions
                String exceptionMessage = "Error al conectar a la base de datos '" + dbName + "' en " + dbHost + ":" + dbPort
                        + " para el usuario '" + dbUser + "'. Revisa el archivo de configuración 'config.yaml'";
                throw new DatabaseConnectionException(exceptionMessage, e, dbHost, dbPort, dbName, dbUser);
            }
        }
    }

    // Métodos para verificar estado de conectividad contra el servidor //

    /**
     * Verifica si es posible conectarse a la base de datos utilizando las credenciales especificadas.
     * <p>
     * Este método intenta obtener una conexión con la BD configurada y verifica si está abierta.
     * Si la conexión se establece exitosamente, retorna true; en caso contrario, retorna false.
     *
     * @return true si la conexión con la base de datos se establece exitosamente; false en caso contrario.
     */
    public boolean canConnectToDatabase() {
        try (Connection connection = getConnection()) {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false; // Si ocurre una excepción, no podemos conectarnos
        }
    }

    /**
     * Verifica si el servicio de BD está disponible realizando una conexión TCP al puerto especificado.
     * <p>
     * Este método intenta abrir una conexión TCP al servidor y puerto configurados para verificar
     * que el servicio de base de datos esté operativo. Utiliza un tiempo de espera de 2 seg.
     *
     * @return true si el puerto del servicio de BD está abierto y accesible; false si no lo está.
     */
    public boolean isDatabaseServiceAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(this.dbHost, this.dbPort), 2000); // Timeout of 2 seconds
            return true; // Conexión exitosa
        } catch (IOException e) {
            return false; // Conexión fallida
        }
    }

    /**
     * Verifica si el servidor es alcanzable mediante un ping ICMP.
     * <p>
     * Este método intenta enviar un ping ICMP al servidor configurado para determinar si el host está en línea.
     * Se adapta tanto para sistemas Windows como para sistemas Unix/Linux, ejecutando el comando adecuado.
     *
     * @return true si el servidor responde al ping ICMP; false en caso contrario.
     */
    public boolean isHostReachable() {
        String host = this.dbHost;
        try {
            String command = System.getProperty("os.name").toLowerCase().contains("win") ?
                    "ping -n 1 " + host :
                    "ping -c 1 " + host;

            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command(command.split(" ")); // Divide el comando en partes
            Process process = processBuilder.start(); // Inicia el proceso
            int returnVal = process.waitFor(); // Espera a que el proceso termine
            return (returnVal == 0); // Retorna true si el comando se ejecuta con éxito
        } catch (IOException | InterruptedException e) {
            return false; // Ping fallido
        }
    }

    /**
     * Verifica el estado del servidor combinando múltiples niveles de verificación de conectividad.
     * <p>
     * Este método verifica si el servidor de base de datos está disponible realizando tres niveles de pruebas:
     * <ol>
     *      <li>Intentar conectarse a la BD con las credenciales especificadas en el archivo de configuración YAML.</li>
     *      <li>Si falla, intenta una conexión TCP al puerto especificado. Si es exitoso, hay problemas de autenticación.</li>
     *      <li>Si falla, probar si el servidor está en línea mediante un ping ICMP. Si es exitoso, el servicio de BD no está en ejecución.</li>
     *</ol>
     * @return una descripción del estado del servidor:
     * <ul>
     *      <li>"Servidor en línea y funcional."</li>
     *      <li>"Servidor en línea, pero error de validación para la conexión a la BD."</li>
     *      <li>"Servidor parcialmente en línea: el servicio de BD no está en ejecución."</li>
     *      <li>"Servidor completamente fuera de línea / apagado."</li>
     * </ul>
     */
    public String[] checkServerStatus() {
        // Intentar conexión con las credenciales
        try (Connection connection = getConnection()) {
            // Conexión exitosa
            return new String[] {
                DatabaseConnectorStatus.FUNCTIONAL_MSG,
                DatabaseConnectorStatus.FUNCTIONAL_COLOR,
                DatabaseConnectorStatus.FUNCTIONAL_ICON
            };
        } catch (DatabaseAuthenticationException e) {
            // Error de autenticación
            return new String[] {
                    DatabaseConnectorStatus.AUTH_PROBLEM_MSG,
                    DatabaseConnectorStatus.AUTH_PROBLEM_COLOR,
                    DatabaseConnectorStatus.AUTH_PROBLEM_ICON
            };
        } catch (DatabaseConnectionException e) {
            // Error de conexión
            return new String[] {
                    DatabaseConnectorStatus.DB_SERVICE_PROBLEM_MSG,
                    DatabaseConnectorStatus.DB_SERVICE_PROBLEM_COLOR,
                    DatabaseConnectorStatus.DB_SERVICE_PROBLEM_ICON
            };
        } catch (Exception e) {
            // Error desconocido
            return new String[] {
                    DatabaseConnectorStatus.UNKNOWN_ERROR_MSG,
                    DatabaseConnectorStatus.UNKNOWN_ERROR_STYLE,
                    DatabaseConnectorStatus.UNKNOWN_ERROR_ICON
            };
        }
    }


}
