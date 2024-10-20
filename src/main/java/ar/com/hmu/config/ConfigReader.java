package ar.com.hmu.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import java.io.InputStream;

/**
 * Clase que se encarga de leer la configuración de la base de datos desde un archivo YAML.
 *
 * La clase `ConfigReader` utiliza SnakeYAML para deserializar un archivo de configuración
 * YAML (`config.yaml`) y cargar los valores necesarios para la configuración de la base de datos.
 * Proporciona un objeto de tipo {@link DatabaseConfig} que contiene todos los detalles
 * para la conexión a la base de datos.
 */
public class ConfigReader {

    private DatabaseConfig dbConfig;

    /**
     * Constructor que inicializa el objeto `ConfigReader` y carga la configuración de la base de datos.
     *
     * Este constructor lee un archivo YAML (`config.yaml`) que contiene la configuración necesaria
     * para establecer la conexión con la base de datos. Utiliza `LoaderOptions` para personalizar
     * cómo se debe deserializar el archivo YAML y asegurar la seguridad del proceso.
     *
     * @throws RuntimeException si el archivo de configuración no se encuentra o si ocurre un error durante la carga.
     */
    public ConfigReader() {
        // Crear LoaderOptions para configurar el comportamiento de carga de YAML
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(50); // Configuración opcional para limitar alias (por seguridad)
        loaderOptions.setAllowRecursiveKeys(false); // Opcional, evita llaves recursivas

        // Crear un Constructor que utilice los LoaderOptions
        Constructor constructor = new Constructor(DatabaseSettings.class, loaderOptions);

        // Inicializar Yaml con el constructor
        Yaml yaml = new Yaml(constructor);
        String filePath = "config.yaml";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Archivo " + filePath + " no encontrado");
            }
            DatabaseSettings settings = yaml.load(inputStream);
            this.dbConfig = settings.getDb();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el archivo YAML", e);
        }
    }

    /**
     * Obtiene la configuración de la base de datos.
     *
     * Este método devuelve un objeto {@link DatabaseConfig} que contiene los parámetros necesarios
     * para establecer la conexión con la base de datos.
     *
     * @return un objeto {@link DatabaseConfig} con la configuración de la base de datos.
     */
    public DatabaseConfig getDbConfig() {
        return dbConfig;
    }

}
