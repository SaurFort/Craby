package fr.saurfort.core.database.query.register;

import fr.saurfort.core.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLRegisterConfig {
    private static Connection conn = MySQLDatabase.conn;

    // Actually depreciated because it's a breaking function
    /*public static boolean configExist(Guild guild) {
        String query = "SELECT id FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            if(rs.getString("guild_id") == null) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }*/

    public static void registrationConfig(Guild guild, TextChannel logChannel, TextChannel registrationChannel, Role registrationRole, int registerLimit, int substituteLimit) {
        deleteConfig(guild);

        String query = "INSERT INTO register_config (guild_id, log_channel, register_channel, register_role, register_limit, substitute_limit)" +
                "VALUES (?,?,?,?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, logChannel.getId());
            stmt.setString(3, registrationChannel.getId());
            stmt.setString(4, registrationRole.getId());
            stmt.setInt(5, registerLimit);
            stmt.setInt(6, substituteLimit);

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

            try {
                if(rs.next()) {
                    return rs.getInt("register_limit");
                }
            } catch (Exception ignored) {}

            return 0;
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

            try {
                if(rs.next()) {
                    return rs.getInt("substitute_limit");
                }
            } catch (Exception ignored) {}

            return 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getRegisterChannel(Guild guild) {
        String query = "SELECT register_channel FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getLong("register_channel");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getLogChannel(Guild guild) {
        String query = "SELECT log_channel FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getLong("log_channel");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static long getRegisteredRole(Guild guild) {
        String query = "SELECT register_role FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getLong("register_role");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean configExist(Guild guild) {
        String query = "SELECT id FROM register_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();


            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteConfig(Guild guild) {
        String query = "DELETE FROM register_config WHERE guild_id = ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
