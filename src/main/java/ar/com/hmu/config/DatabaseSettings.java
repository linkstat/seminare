package ar.com.hmu.config;

/**
 * Clase que representa la configuración de la base de datos completa.
 *
 * La clase `DatabaseSettings` se utiliza para encapsular la configuración de la base de datos.
 * Esta clase contiene un objeto de tipo {@link AppConfig}, el cual almacena todos los detalles
 * de conexión, como el tipo de base de datos, el host, el puerto, el nombre de la base de datos,
 * y las credenciales.
 */
public class DatabaseSettings {

    private AppConfig db;

    /**
     * Obtiene la configuración de la base de datos.
     *
     * Este método devuelve un objeto {@link AppConfig} que contiene todos los parámetros
     * necesarios para establecer una conexión con la base de datos.
     *
     * @return la configuración de la base de datos encapsulada en un {@link AppConfig}.
     */
    public AppConfig getDb() {
        return db;
    }

    /**
     * Establece la configuración de la base de datos.
     *
     * Este método permite definir el objeto {@link AppConfig} que contiene toda la información
     * necesaria para conectarse a la base de datos, como tipo, host, puerto, nombre de base de datos,
     * y credenciales.
     *
     * @param db el objeto {@link AppConfig} que contiene los detalles de conexión de la base de datos.
     */
    public void setDb(AppConfig db) {
        this.db = db;
    }

}
