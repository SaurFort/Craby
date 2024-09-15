package fr.saurfort.core.database.query.welcome;

import fr.saurfort.core.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLWelcomeConfig {
    private static Connection conn = MySQLDatabase.conn;

    public MySQLWelcomeConfig(Guild guild) {
        if(!configExist(guild)) {
            String query = "INSERT INTO welcome_config (guild_id)" +
                    "   VALUES (?)";

            try {
                PreparedStatement stmt = conn.prepareStatement(query);

                stmt.setString(1, guild.getId());

                stmt.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static long getWelcomeChannel(Guild guild) {
        String query = "SELECT channel_id FROM welcome_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getLong("channel_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getWelcomeMessage(Guild guild) {
        String query = "SELECT message FROM welcome_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getString("message");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setWelcomeMessage(Guild guild, String message) {
        String query = "UPDATE welcome_config SET message = ? WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(2, guild.getId());
            stmt.setString(1, message);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the guild already have a configuration
     *
     * @param guild Guild that interact with the command
     * @return true if the guild already have a config, false if the guild haven't a config
     */
    public static boolean configExist(Guild guild) {
        String query = "SELECT id FROM welcome_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            try {
                return rs.next();
            } catch (Exception ignored) {}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
