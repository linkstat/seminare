package ar.com.hmu.config;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.LoaderOptions;
import java.io.InputStream;

public class ConfigReader {

    private DatabaseConfig dbConfig;

    public ConfigReader() {
        // Crear LoaderOptions para el constructor
        LoaderOptions loaderOptions = new LoaderOptions();
        Constructor constructor = new Constructor(DatabaseConfig.class, loaderOptions);
        String filePath = "config.yaml";

        // Inicializar Yaml con el constructor
        Yaml yaml = new Yaml(constructor);

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new RuntimeException("Archivo " + filePath + " no encontrado");
            }
            this.dbConfig = yaml.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al leer el archivo YAML", e);
        }
    }

    public DatabaseConfig getDbConfig() {
        return dbConfig;
    }

}
