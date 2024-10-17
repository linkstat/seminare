package ar.com.hmu.config;

/*
 * Clase POJO que se utiliza para almacenar y acceder a los parámetros de conexión de la BD.
 * Contiene datos de configuración como motor de BD, host, puerto, nombre de la BD, y credenciales de conexión.
 * No realiza operaciones activas, solo almacena información.
 * Es utilizada por la clase ConfigReader para mapear los valores de YAML a un objeto Java.
 */
public class DatabaseConfig {

    private String type;
    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;

    // Getters y setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

