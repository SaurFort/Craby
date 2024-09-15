package fr.saurfort.config;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUpdater {
    private Properties properties = new Properties();
    public static final String CONFIG_FILE = "config.properties";

    public ConfigUpdater() {
        try(InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateVersion(String version) {
        properties.setProperty("bot.version", version);

        try(FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            properties.store(output, null);
            System.out.println("Configuration updated successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }
}
