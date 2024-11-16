package ar.com.hmu.utils;

import java.io.*;
import java.nio.file.*;
import java.util.Properties;
import java.util.UUID;

public class PreferencesManager {

    private static final String APP_DIR = System.getProperty("os.name").startsWith("Windows")
            ? System.getenv("LocalAppData") + File.separator + AppInfo.CFG_FOLDER_NAME
            : System.getProperty("user.home") + File.separator + ".config" + File.separator + AppInfo.CFG_FOLDER_NAME;

    private static final String PREFS_FILE = APP_DIR + File.separator + "preferences.properties";
    private Properties properties;

    public PreferencesManager() {
        properties = new Properties();
        loadProperties();
    }

    private void loadProperties() {
        try {
            Path path = Paths.get(PREFS_FILE);
            if (Files.exists(path)) {
                try (InputStream input = Files.newInputStream(path)) {
                    properties.load(input);
                }
            } else {
                // Crear el directorio si no existe
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al cargar las preferencias", e);
        }
    }

    public void savePreferences() {
        try (OutputStream output = Files.newOutputStream(Paths.get(PREFS_FILE))) {
            properties.store(output, "Aromito Preferences");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error al guardar las preferencias", e);
        }
    }

    public void put(String key, String value) {
        properties.setProperty(key, value);
        savePreferences();
    }

    public String get(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    // Métodos para manejar UUID
    public void putUUID(String key, UUID value) {
        if (value != null) {
            properties.setProperty(key, value.toString());
        } else {
            properties.remove(key);
        }
        savePreferences();
    }

    public UUID getUUID(String key, UUID defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return UUID.fromString(value);
        }
        return defaultValue;
    }

    // Métodos para otros tipos de datos (int, boolean) si es necesario
    public void putInt(String key, Integer value) {
        if (value != null) {
            properties.setProperty(key, value.toString());
        } else {
            properties.remove(key);
        }
        savePreferences();
    }

    public Integer getInt(String key, Integer defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return defaultValue;
    }

    public void putBoolean(String key, Boolean value) {
        if (value != null) {
            properties.setProperty(key, value.toString());
        } else {
            properties.remove(key);
        }
        savePreferences();
    }

    public Boolean getBoolean(String key, Boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }

}
