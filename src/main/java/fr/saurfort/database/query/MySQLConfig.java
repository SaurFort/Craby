package fr.saurfort.database.query;

import fr.saurfort.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLConfig {
    private static Connection conn = MySQLDatabase.conn;

    public static void registrationConfig(Guild guild, TextChannel logChannel, TextChannel registrationChannel, Role registrationRole) {
        String query = "INSERT INTO register_config (guild_id, log_channel, register_channel, register_role)" +
                "VALUES (?,?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, logChannel.getId());
            stmt.setString(3, registrationChannel.getId());
            stmt.setString(4, registrationRole.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRegisterLimit(Guild guild) {
        String query = "SELECT register_limit FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getInt("register_limit");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getSubstituteLimit(Guild guild) {
        String query = "SELECT substitute_limit FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getInt("substitute_limit");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
