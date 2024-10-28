package ar.com.hmu.config;

/**
 * Clase que representa la configuración completa de la aplicación.
 * <p>
 * La clase `AppConfigSettings` se utiliza para encapsular todas las configuraciones de la aplicación.
 * Incluye un objeto de tipo {@link AppConfig} que almacena los detalles de conexión a la base de datos.
 */
public class AppConfigSettings {

    private AppConfig db;

    /**
     * Obtiene la configuración de la base de datos.
     *
     * @return un objeto {@link AppConfig} que contiene todos los parámetros
     *         necesarios para establecer una conexión con la base de datos.
     */
    public AppConfig getDb() {
        return db;
    }

    /**
     * Establece la configuración de la base de datos.
     *
     * @param db el objeto {@link AppConfig} que contiene los detalles de conexión de la base de datos.
     */
    public void setDb(AppConfig db) {
        this.db = db;
    }

}
