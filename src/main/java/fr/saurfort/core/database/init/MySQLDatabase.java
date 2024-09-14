package fr.saurfort.core.database.init;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MySQLDatabase {
    private final String databaseAddress;
    public static Connection conn;
    private static final String[] REQUIRED_TABLES = {"register_config", "users_messages", "registered", "ticket_config", "ticket", "welcome_config", "tournament_list"};

    public MySQLDatabase(String username, String password, String address, String port, String databaseName) {
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
        this.databaseAddress = "jdbc:mysql://" + username + ":" + encodedPassword + "@" + address + ":" + port + "/" + databaseName;

        try {
            conn = DriverManager.getConnection(databaseAddress);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (!checkDatabaseInstallation()) {
            initializeTables();
        } else {
            System.out.println("The database is already installed!");
        }
    }

    public static boolean checkDatabaseInstallation() {
        try {
            if (conn != null && conn.isValid(1)) {
                System.out.println("Database connection is valid.");

                DatabaseMetaData metaData = conn.getMetaData();

                for (String tableName : REQUIRED_TABLES) {
                    try (ResultSet rs = metaData.getTables(null, null, tableName, null)) {
                        if (!rs.next()) {
                            System.err.println("Table missing: " + tableName);
                            return false;
                        }
                    }
                }
                System.out.println("All required tables exist.");
                return true;
            } else {
                System.err.println("Database connection is not valid.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void initializeTables() {
        try {
            Statement stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS register_config (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   guild_id BIGINT NOT NULL UNIQUE," +
                    "   log_channel TEXT NOT NULL," +
                    "   register_channel TEXT NOT NULL," +
                    "   register_role TEXT NOT NULL," +
                    "   register_limit INT NOT NULL," +
                    "   substitute_limit INT NOT NULL" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users_messages (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   uuid TEXT NOT NULL," +
                    "   guild_id TEXT NOT NULL," +
                    "   channel_id TEXT NOT NULL," +
                    "   message_id BIGINT NOT NULL UNIQUE," +
                    "   message TEXT NOT NULL," +
                    "   posted_timestamp TEXT NOT NULL" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS registered (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   uuid TEXT NOT NULL," +
                    "   guild_id TEXT NOT NULL," +
                    "   cr_id TEXT NOT NULL," +
                    "   want_unregister TEXT," +
                    "   UNIQUE (uuid, guild_id)" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticket_config (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   guild_id BIGINT NOT NULL UNIQUE," +
                    "   category_id TEXT NOT NULL," +
                    "   archive_id TEXT NOT NULL," +
                    "   support_id TEXT NOT NULL" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ticket (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   guild_id TEXT NOT NULL," +
                    "   ticket_id TEXT NOT NULL," +
                    "   ticket_owner TEXT NOT NULL" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS welcome_config (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   guild_id BIGINT NOT NULL UNIQUE," +
                    "   channel_id TEXT NOT NULL," +
                    "   message TEXT NOT NULL" +
                    ");");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS tournament_list (" +
                    "   id INT PRIMARY KEY AUTO_INCREMENT," +
                    "   guild_id TEXT NOT NULL," +
                    "   tournament_name TEXT," +
                    "   status TEXT NOT NULL" +
                    ");");

            System.out.println("Tables initialized successfully.");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
