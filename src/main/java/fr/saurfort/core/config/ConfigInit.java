package fr.saurfort.core.config;

import fr.saurfort.core.utils.Scanner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigInit {
    public static final String CONFIG_FILE = "config.properties";

    public ConfigInit(Properties configFile) {
        String botToken;
        String databaseAddress;
        String databasePort;
        String database;
        String databaseUsername;
        String databasePassword;

        System.out.println("What is your bot token?");
        botToken = Scanner.stringScanner();

        System.out.println("What is your database address? (without port)");
        databaseAddress = Scanner.stringScanner();

        System.out.println("What is your database port?");
        databasePort = Scanner.stringScanner();

        System.out.println("What is the name of your database?");
        database = Scanner.stringScanner();

        System.out.println("What is your database user?");
        databaseUsername = Scanner.stringScanner();

        System.out.println("What is your database password?");
        databasePassword = Scanner.stringScanner();

        configFile.setProperty("bot.token", botToken);
        configFile.setProperty("db.address", databaseAddress);
        configFile.setProperty("db.port", databasePort);
        configFile.setProperty("db.name", database);
        configFile.setProperty("db.username", databaseUsername);
        configFile.setProperty("db.password", databasePassword);

        try(FileOutputStream output = new FileOutputStream(CONFIG_FILE)) {
            configFile.store(output, null);
            System.out.println("Configuration updated successfully.");
        } catch (IOException e) {
            System.err.println("Failed to save configuration: " + e.getMessage());
        }
    }
}
