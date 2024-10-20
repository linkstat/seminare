package ar.com.hmu.config;

/**
 * Clase POJO que se utiliza para almacenar y acceder a los parámetros de conexión de la base de datos.
 *
 * La clase `DatabaseConfig` contiene los datos de configuración necesarios para conectarse a la base de datos,
 * tales como el tipo de base de datos, el host, el puerto, el nombre de la base de datos, y las credenciales
 * de acceso. Esta clase no realiza operaciones activas, solo actúa como un contenedor de información.
 * Es utilizada por {@link ConfigReader} para mapear los valores de un archivo YAML a un objeto Java.
 */
public class DatabaseConfig {

    private String type;
    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;

    /**
     * Obtiene el tipo de base de datos.
     *
     * @return el tipo de base de datos (por ejemplo, "mariadb").
     */
    public String getType() {
        return type;
    }

    /**
     * Establece el tipo de base de datos.
     *
     * @param type el tipo de base de datos (por ejemplo, "mariadb").
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Obtiene el nombre del host de la base de datos.
     *
     * @return el nombre del host de la base de datos.
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Establece el nombre del host de la base de datos.
     *
     * @param hostname el nombre del host donde se encuentra la base de datos.
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    /**
     * Obtiene el número de puerto para la conexión a la base de datos.
     *
     * @return el número de puerto de la base de datos.
     */
    public int getPort() {
        return port;
    }

    /**
     * Establece el número de puerto para la conexión a la base de datos.
     *
     * @param port el número de puerto de la base de datos.
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Obtiene el nombre de la base de datos.
     *
     * @return el nombre de la base de datos.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Establece el nombre de la base de datos.
     *
     * @param database el nombre de la base de datos a la cual conectarse.
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * Obtiene el nombre de usuario para la conexión a la base de datos.
     *
     * @return el nombre de usuario utilizado para la conexión.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Establece el nombre de usuario para la conexión a la base de datos.
     *
     * @param username el nombre de usuario para conectarse a la base de datos.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Obtiene la contraseña para la conexión a la base de datos.
     *
     * @return la contraseña utilizada para la conexión.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Establece la contraseña para la conexión a la base de datos.
     *
     * @param password la contraseña para conectarse a la base de datos.
     */
    public void setPassword(String password) {
        this.password = password;
    }
}
