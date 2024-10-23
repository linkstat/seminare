package ar.com.hmu.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import java.io.InputStream;

/**
 * Clase que se encarga de leer la configuración general de la aplicación desde un archivo YAML.
 *
 * La clase `AppConfigReader` utiliza la biblioteca SnakeYAML para deserializar un archivo de configuración
 * YAML (`config.yaml`) y cargar los valores necesarios para la configuración de la aplicación.
 * Proporciona un objeto de tipo {@link AppConfig} que contiene tanto los detalles de conexión a la BD
 * como cualquier otra configuración general de la aplicación (que se agregue en el futuro).
 *
 * Esta clase permite centralizar el acceso a las configuraciones, facilitando la lectura y utilización de parámetros
 * clave que necesitan ser accesibles en diferentes partes de la aplicación.
 */
public class AppConfigReader {

    private AppConfig appConfig;


    /**
     * Constructor que inicializa el objeto `AppConfigReader` y carga la configuración de la aplicación.
     *
     * Este constructor lee un archivo YAML (`config.yaml`) que contiene la configuración general de la aplicación,
     * incluyendo detalles de la base de datos y otros parámetros relevantes. Utiliza `LoaderOptions` para personalizar
     * el comportamiento de deserialización del archivo YAML, lo cual ayuda a asegurar la seguridad y control del proceso.
     * El resultado es un objeto de tipo {@link AppConfig} que almacena toda la configuración.
     *
     * @throws RuntimeException si el archivo de configuración no se encuentra o si ocurre un error durante la carga.
     */
    public AppConfigReader() {
        // Crear LoaderOptions para configurar el comportamiento de carga de YAML
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setMaxAliasesForCollections(50); // Configuración opcional para limitar alias (por seguridad)
        loaderOptions.setAllowRecursiveKeys(false); // Opcional, evita llaves recursivas

        // Crear un Constructor que utilice los LoaderOptions
        Constructor constructor = new Constructor(AppConfigSettings.class, loaderOptions);

        // Inicializar Yaml con el constructor
        Yaml yaml = new Yaml(constructor);
        String filePath = "config.yaml";

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Archivo " + filePath + " no encontrado");
            }
            AppConfigSettings settings = yaml.load(inputStream);
            this.appConfig = settings.getDb();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el archivo YAML", e);
        }
    }

    /**
     * Obtiene la configuración de la aplicación.
     *
     * @return un objeto {@link AppConfig} con la configuración general de la aplicación.
     */
    public AppConfig getAppConfig() {
        return appConfig;
    }

}
