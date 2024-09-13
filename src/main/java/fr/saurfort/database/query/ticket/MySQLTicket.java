package fr.saurfort.database.query.ticket;

import fr.saurfort.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLTicket {
    private static Connection conn = MySQLDatabase.conn;

    public MySQLTicket(Guild guild, TextChannel ticket, Member ticketOwner) {
        createTicket(guild, ticket, ticketOwner);
    }

    private static void createTicket(Guild guild, TextChannel ticket, Member ticketOwner) {
        String query = "INSERT INTO ticket (guild_id, ticket_id, ticket_owner)" +
                "VALUES (?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, ticket.getId());
            stmt.setString(3, ticketOwner.getId());

            stmt.executeUpdate();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getTicketID(Guild guild, TextChannel ticket) {
        String query = "SELECT id FROM ticket WHERE guild_id = ? AND ticket_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, ticket.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            if(rs.getString("id") != null) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }

    public static String getTicketOwner(Guild guild, TextChannel ticket) {
        String query = "SELECT ticket_owner FROM ticket WHERE guild_id = ? AND ticket_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, ticket.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getString("ticket_owner");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isTicket(Guild guild, TextChannel channel) {
        String query = "SELECT id FROM ticket WHERE guild_id = ? AND ticket_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, guild.getId());
            stmt.setString(2, channel.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("id") != null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void deleteTicket(Guild guild, TextChannel ticket) {
        String query = "DELETE FROM ticket WHERE guild_id = ? AND ticket_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, ticket.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
