package fr.saurfort.database.query;

import fr.saurfort.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLRegistration {
    private static Connection conn = MySQLDatabase.conn;

    public MySQLRegistration(Guild guild, Member member) {
        String query = "INSERT INTO registered (uuid, guild_id)" +
                "VALUES (?,?)";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, member.getId());
            stmt.setString(2, guild.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getRegisteredMember(Guild guild) {
        String query = "SELECT COUNT(id) AS total_registered FROM registered WHERE guild_id = ? LIMIT ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setInt(2, MySQLConfig.getRegisterLimit(guild));

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();
            return rs.getInt("total_registered");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getSubstituteMember(Guild guild) {
        String query = "SELECT COUNT(id) AS total_registered FROM registered WHERE guild_id = ? LIMIT ? OFFSET ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setInt(2, MySQLConfig.getSubstituteLimit(guild));
            stmt.setInt(3, MySQLConfig.getRegisterLimit(guild));

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();
            return rs.getInt("total_registered");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String listRegisteredMember(Guild guild) {
        String query = "SELECT * FROM registered WHERE guild_id = ? LIMIT ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setInt(2, MySQLConfig.getRegisterLimit(guild));

            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            StringBuilder membersList = new StringBuilder();
            while(rs.next()) {
                Member member = guild.retrieveMemberById(rs.getString("uuid")).complete();
                membersList.append(member.getAsMention()).append("\n");
            }

            return membersList.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String listSubstituteMember(Guild guild) {
        String query = "SELECT * FROM registered WHERE guild_id = ? LIMIT ? OFFSET ?";
        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setInt(2, MySQLConfig.getSubstituteLimit(guild));
            stmt.setInt(3, MySQLConfig.getRegisterLimit(guild));

            stmt.execute();
            ResultSet rs = stmt.getResultSet();

            StringBuilder membersList = new StringBuilder();
            while(rs.next()) {
                Member member = guild.retrieveMemberById(rs.getString("uuid")).complete();
                membersList.append("- ").append(member.getAsMention()).append("\n");
            }

            return membersList.toString();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unregister(Guild guild, Member member) {
        
    }
}
