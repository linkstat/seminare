package ar.com.hmu.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import java.io.InputStream;

public class ConfigReader {

    private DatabaseConfig dbConfig;

    public ConfigReader() {
        // Crear LoaderOptions para configurar el comportamiento de carga de YAML
        // LoaderOptions es una clase que permite configurar cómo SnakeYAML debe manejar ciertos aspectos del archivo YAML al deserializarlo.
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

    public DatabaseConfig getDbConfig() {
        return dbConfig;
    }

}
