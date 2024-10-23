package ar.com.hmu.repository;

import ar.com.hmu.config.AppConfigReader;
import ar.com.hmu.config.AppConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase responsable de gestionar la conexión a la base de datos.
 *
 * La clase `DatabaseConnector` utiliza los valores de configuración proporcionados por {@link AppConfigReader}
 * para establecer una conexión con la base de datos especificada. Actualmente, soporta la conexión
 * a bases de datos de tipo MariaDB (y desestima otros motores de BD, indicando que no están soportados).
 */
public class DatabaseConnector {

    private final String dbType;
    private final String hostname;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

    /**
     * Constructor que inicializa el conector de base de datos utilizando un objeto {@link AppConfigReader}.
     *
     * Este constructor recupera la configuración de la base de datos a través de un {@link AppConfigReader},
     * que lee los detalles de conexión desde un archivo de configuración (por ejemplo, YAML).
     *
     * @param appConfigReader una instancia de {@link AppConfigReader} que proporciona los valores de configuración necesarios.
     */
    public DatabaseConnector(AppConfigReader appConfigReader) {
        // Obtenemos la configuración desde ConfigReader
        AppConfig dbConfig = appConfigReader.getDbConfig();

        // Asignamos los valores de configuración
        this.dbType = dbConfig.getDbType();
        this.hostname = dbConfig.getDbHostname();
        this.port = dbConfig.getDbPort();
        this.database = dbConfig.getDbName();
        this.username = dbConfig.getDbUsername();
        this.password = dbConfig.getDbPassword();
    }

    /**
     * Establece y devuelve una conexión con la base de datos.
     *
     * Este método crea una conexión a la base de datos utilizando los valores de configuración previamente
     * obtenidos. Actualmente, solo se soporta la conexión a bases de datos de tipo MariaDB, por lo cual si
     * el motor de BD fuera otro distinto de MariaDB, se tira una excepción indicando que no hay implementación.
     *
     * @return una instancia de {@link Connection} que representa la conexión activa a la base de datos.
     * @throws SQLException si ocurre un error al intentar conectar a la base de datos.
     * @throws UnsupportedOperationException si el tipo de base de datos no es soportado.
     */
    public Connection getConnection() throws SQLException {
        if (!dbType.equalsIgnoreCase("mariadb")) {
            throw new UnsupportedOperationException("Motor de base de datos no implementado: " + dbType);
        }

        String url = String.format("jdbc:mariadb://%s:%d/%s", hostname, port, database);
        return DriverManager.getConnection(url, username, password);
    }

    // Métodos para verificar estado de conectividad contra el servidor //

    /**
     * Verifica si es posible conectarse a la base de datos utilizando las credenciales especificadas.
     *
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
     *
     * Este método intenta abrir una conexión TCP al servidor y puerto configurados para verificar
     * que el servicio de base de datos esté operativo. Utiliza un tiempo de espera de 2 seg.
     *
     * @return true si el puerto del servicio de BD está abierto y accesible; false si no lo está.
     */
    public boolean isDatabaseServiceAvailable() {
        String host = this.hostname;
        int port = this.port;
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 2000); // Timeout de 2 segundos
            return true; // Conexión exitosa
        } catch (IOException e) {
            return false; // Conexión fallida
        }
    }

    /**
     * Verifica si el servidor es alcanzable mediante un ping ICMP.
     *
     * Este método intenta enviar un ping ICMP al servidor configurado para determinar si el host está en línea.
     * Se adapta tanto para sistemas Windows como para sistemas Unix/Linux, ejecutando el comando adecuado.
     *
     * @return true si el servidor responde al ping ICMP; false en caso contrario.
     */
    public boolean isHostReachable() {
        String host = this.hostname;
        try {
            String command = System.getProperty("os.name").toLowerCase().contains("win") ?
                    "ping -n 1 " + host :
                    "ping -c 1 " + host;

            Process process = Runtime.getRuntime().exec(command);
            int returnVal = process.waitFor();
            return (returnVal == 0);
        } catch (IOException | InterruptedException e) {
            return false; // Ping fallido
        }
    }

    /**
     * Verifica el estado del servidor combinando múltiples niveles de verificación de conectividad.
     *
     * Este método verifica si el servidor de base de datos está disponible realizando tres niveles de pruebas:
     * 1. Intentar conectarse a la BD con las credenciales especificadas en el archivo de configuración YAML.
     * 2. Si falla, intenta una conexión TCP al puerto especificado. Si es exitoso, hay problemas de autenticación.
     * 3. Si falla, probar si el servidor está en línea mediante un ping ICMP. Si es exitoso, el servicio de BD no está en ejecución.
     *
     * @return una descripción del estado del servidor:
     *         - "Servidor en línea y funcional."
     *         - "Servidor en línea, pero error de validación para la conexión a la BD."
     *         - "Servidor parcialmente en línea: el servicio de BD no está en ejecución."
     *         - "Servidor completamente fuera de línea / apagado."
     */
    public String[] checkServerStatus() {
        // Nivel 1: Intentar conexión con las credenciales
        if (canConnectToDatabase()) {
            return new String[] {"Servidor en línea y funcional.", "green"};
        }

        // Nivel 2: Si la conexión con las credenciales falla, intentamos conectar al puerto directamente
        if (isDatabaseServiceAvailable()) {
            return new String[] {"Servidor en línea, pero error de validación para la conexión a la base de datos.", "orange"};
        }

        // Nivel 3: Si la conexión al puerto falla, intentamos un ping ICMP
        if (isHostReachable()) {
            return new String[] {"Servidor parcialmente en línea: el servicio de base de datos no está en ejecución.", "orange"};
        }

        // Si ninguna verificación tiene éxito, el servidor está completamente fuera de línea
        return new String[] {"Servidor completamente fuera de línea.", "red"};
    }

}
