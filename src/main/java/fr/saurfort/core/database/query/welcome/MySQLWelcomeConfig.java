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

    public MySQLWelcomeConfig(Guild guild, TextChannel channel, String message) {
        String query = "DELETE FROM welcome_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        query = "INSERT INTO welcome_config (guild_id, channel_id, message)" +
                "   VALUES (?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, channel.getId());
            stmt.setString(3, message);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
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

    public static boolean configExist(Guild guild) {
        String query = "SELECT id FROM welcome_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getString("id") != null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
