package ar.com.hmu.repository;

import ar.com.hmu.config.ConfigReader;
import ar.com.hmu.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase responsable de gestionar la conexión a la base de datos.
 *
 * La clase `DatabaseConnector` utiliza los valores de configuración proporcionados por {@link ConfigReader}
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
     * Constructor que inicializa el conector de base de datos utilizando un objeto {@link ConfigReader}.
     *
     * Este constructor recupera la configuración de la base de datos a través de un {@link ConfigReader},
     * que lee los detalles de conexión desde un archivo de configuración (por ejemplo, YAML).
     *
     * @param configReader una instancia de {@link ConfigReader} que proporciona los valores de configuración necesarios.
     */
    public DatabaseConnector(ConfigReader configReader) {
        // Obtenemos la configuración desde ConfigReader
        DatabaseConfig dbConfig = configReader.getDbConfig();

        // Asignamos los valores de configuración
        this.dbType = dbConfig.getType();
        this.hostname = dbConfig.getHostname();
        this.port = dbConfig.getPort();
        this.database = dbConfig.getDatabase();
        this.username = dbConfig.getUsername();
        this.password = dbConfig.getPassword();
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
}
