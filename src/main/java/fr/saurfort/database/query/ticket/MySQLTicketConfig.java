package fr.saurfort.database.query.ticket;

import fr.saurfort.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLTicketConfig {
    private static Connection conn = MySQLDatabase.conn;

    public MySQLTicketConfig(Guild guild, Category category, TextChannel logs, Role support) {
        String query = "SELECT * FROM ticket_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            setTicketConfig(guild, category, logs, support);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    private static void setTicketConfig(Guild guild, Category category, TextChannel logs, Role support) {
        String query = "DELETE FROM ticket_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }

        query = "INSERT INTO ticket_config (guild_id, category_id, archive_id, support_id)" +
                "VALUES (?, ?, ?, ?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, category.getId());
            stmt.setString(3, logs.getId());
            stmt.setString(4, support.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
    }

    public static ResultSet getTicketConfig(Guild guild) {
        String query = "SELECT * FROM ticket_config WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            ResultSet rs = stmt.executeQuery();
            rs.next();

            if(rs.getString("id") == null) {
                return null;
            } else {
                return rs;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }

        return null;
    }
}
