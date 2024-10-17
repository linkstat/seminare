package ar.com.hmu.repository;

import ar.com.hmu.config.ConfigReader;
import ar.com.hmu.config.DatabaseConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnector {

    private final String dbType;
    private final String hostname;
    private final int port;
    private final String database;
    private final String username;
    private final String password;

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

    public Connection getConnection() throws SQLException {
        if (!dbType.equalsIgnoreCase("mariadb")) {
            throw new UnsupportedOperationException("Motor de base de datos no implementado: " + dbType);
        }

        String url = String.format("jdbc:mariadb://%s:%d/%s", hostname, port, database);
        return DriverManager.getConnection(url, username, password);
    }
}
