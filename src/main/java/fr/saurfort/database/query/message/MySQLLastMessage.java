package fr.saurfort.database.query.message;

import fr.saurfort.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MySQLLastMessage {
    private static Connection conn = MySQLDatabase.conn;

    public MySQLLastMessage(Guild guild, Member member, TextChannel channel, Message message) {
        String query = "INSERT INTO users_messages (uuid, guild_id, channel_id, message, posted_timestamp)" +
                "VALUES (?,?,?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            String formattedDate = message.getTimeCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            stmt.setString(1, member.getId());
            stmt.setString(2, guild.getId());
            stmt.setString(3, channel.getId());
            stmt.setString(4, message.getContentDisplay());
            stmt.setString(5, formattedDate);

            stmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e.getMessage());
        }
    }

    public static List<Object> getLastTimeUserMessage(Member member) {
        String query = "SELECT * FROM users_messages WHERE uuid = ? AND guild_id = ? ORDER BY posted_timestamp DESC LIMIT 1";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, member.getId());
            stmt.setString(2, member.getGuild().getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            List<Object> result = new ArrayList<>();
            result.add(rs.getString("channel_id"));
            result.add(rs.getString("message"));
            result.add(rs.getString("posted_timestamp"));

            return result;
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
        }
        return null;
    }
}
