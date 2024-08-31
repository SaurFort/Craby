package fr.saurfort.config;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class ConfigLoader {
    private Properties properties = new Properties();
    private static final String CONFIG_FILE = "config.properties";
    private static final String DEFAULT_CONFIG_FILE = "config/config-default.properties";

    public ConfigLoader() {
        File configFile = new File(CONFIG_FILE);
        if(!configFile.exists()) {
            try(InputStream defaultConfigStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_FILE)) {
                //OutputStream configFileStream = new FileOutputStream(CONFIG_FILE);

                Files.copy(defaultConfigStream, Paths.get(CONFIG_FILE));
                System.out.println("Configuration file created successfully");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            try(InputStream input = new FileInputStream(CONFIG_FILE)) {
                properties.load(input);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            new ConfigInit(properties);
        }

        try(InputStream input = new FileInputStream(CONFIG_FILE)) {
            properties.load(input);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
