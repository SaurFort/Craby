package fr.saurfort.migrator.v211.database.mysql;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.*;

public class MySQLUpdater {
    private final String databaseAddress;
    public static Connection conn;
    private static final String[] REQUIRED_TABLES = {"register_config", "users_messages", "registered", "ticket_config", "ticket", "welcome_config", "tournament_list"};

    public MySQLUpdater(String username, String password, String address, String port, String databaseName) {
        String encodedPassword = URLEncoder.encode(password, StandardCharsets.UTF_8);
        this.databaseAddress = "jdbc:mysql://" + username + ":" + encodedPassword + "@" + address + ":" + port + "/" + databaseName;

        try {
            conn = DriverManager.getConnection(databaseAddress);

            Statement stmt = conn.createStatement();

            stmt.executeUpdate("ALTER TABLE welcome_config" +
                    "   ADD COLUMN `goodbye_message` TEXT NULL AFTER `welcome_message`," +
                    "   ADD COLUMN `goodbye_channel` TEXT NULL AFTER `welcome_channel`," +
                    "   CHANGE COLUMN `channel_id` `welcome_channel` TEXT NULL," +
                    "   CHANGE COLUMN `message` `welcome_message` TEXT NULL ;");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
