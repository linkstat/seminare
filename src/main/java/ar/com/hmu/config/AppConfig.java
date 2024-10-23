package ar.com.hmu.config;

/**
 * Clase POJO que se utiliza para almacenar y acceder a los parámetros de configuración de la aplicación.
 *
 * La clase `AppConfig` contiene los datos de configuración generales de la aplicación, incluyendo los parámetros
 * de conexión a la BD (y otros valores de configuración que puedan ser necesarios en el futuro).
 * Actualmente, se enfoca principalmente en los detalles de la base de datos, como el motor de BD,
 * el hostname del servidor, el puerto TCP, el nombre de la BD, y las credenciales de acceso.
 *
 * Esta clase no realiza operaciones activas, solo actúa como un contenedor de información.
 * Es utilizada por {@link AppConfigReader} para mapear los valores de un archivo YAML a un objeto Java,
 * permitiendo que otras partes de la aplicación accedan fácilmente a la configuración.
 */
public class AppConfig {

    private String dbType;
    private String dbHostname;
    private int dbPort;
    private String dbName;
    private String dbUsername;
    private String dbPassword;

    /**
     * Obtiene el tipo de base de datos.
     *
     * @return el tipo de base de datos (por ejemplo, "mariadb").
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Establece el tipo de base de datos.
     *
     * @param dbType el tipo de base de datos (por ejemplo, "mariadb").
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Obtiene el nombre del host de la base de datos.
     *
     * @return el nombre del host de la base de datos.
     */
    public String getDbHostname() {
        return dbHostname;
    }

    /**
     * Establece el nombre del host de la base de datos.
     *
     * @param dbHostname el nombre del host donde se encuentra la base de datos.
     */
    public void setDbHostname(String dbHostname) {
        this.dbHostname = dbHostname;
    }

    /**
     * Obtiene el número de puerto para la conexión a la base de datos.
     *
     * @return el número de puerto de la base de datos.
     */
    public int getDbPort() {
        return dbPort;
    }

    /**
     * Establece el número de puerto para la conexión a la base de datos.
     *
     * @param dbPort el número de puerto de la base de datos.
     */
    public void setDbPort(int dbPort) {
        this.dbPort = dbPort;
    }

    /**
     * Obtiene el nombre de la base de datos.
     *
     * @return el nombre de la base de datos.
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * Establece el nombre de la base de datos.
     *
     * @param dbName el nombre de la base de datos a la cual conectarse.
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Obtiene el nombre de usuario para la conexión a la base de datos.
     *
     * @return el nombre de usuario utilizado para la conexión.
     */
    public String getDbUsername() {
        return dbUsername;
    }

    /**
     * Establece el nombre de usuario para la conexión a la base de datos.
     *
     * @param dbUsername el nombre de usuario para conectarse a la base de datos.
     */
    public void setDbUsername(String dbUsername) {
        this.dbUsername = dbUsername;
    }

    /**
     * Obtiene la contraseña para la conexión a la base de datos.
     *
     * @return la contraseña utilizada para la conexión.
     */
    public String getDbPassword() {
        return dbPassword;
    }

    /**
     * Establece la contraseña para la conexión a la base de datos.
     *
     * @param dbPassword la contraseña para conectarse a la base de datos.
     */
    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }
}
