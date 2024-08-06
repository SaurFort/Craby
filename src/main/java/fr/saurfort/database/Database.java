package fr.saurfort.database;

import fr.saurfort.Config;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.desktop.SystemSleepEvent;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Database {
    public static boolean checkConnection() {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER +"&password="  + Config.DB_PASSWORD);

            return conn.isValid(1);
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return false;
    }

    public static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER +"&password="  + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS users_messages (" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    uuid BIGINT NOT NULL," +
                    "    guild_id BIGINT NOT NULL," +
                    "    channel_id BIGINT NOT NULL," +
                    "    message TEXT NOT NULL," +
                    "    posted_timestamp DATETIME NOT NULL" +
                    ");");
            System.out.println("Table `users_messages` initialized successfully");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS registered (" +
                    "    id INT AUTO_INCREMENT PRIMARY KEY," +
                    "    uuid BIGINT UNIQUE NOT NULL," +
                    "    guild_id BIGINT NOT NULL" +
                    ");");
            System.out.println("Table `registered` initialized successfully");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static void addUserMessage(MessageReceivedEvent event) {
        Connection conn = null;
        PreparedStatement stmt = null;

        if(!event.getAuthor().isBot()) {
            String query = "INSERT INTO `users_messages` (uuid, guild_id, channel_id, message, posted_timestamp)" +
                    " VALUES (?, ?, ?, ?, ?)";

            try {
                conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER +"&password="  + Config.DB_PASSWORD);
                stmt = conn.prepareStatement(query);

                String isoDate = String.valueOf(event.getMessage().getTimeCreated());
                Instant instant = Instant.parse(isoDate);
                LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDate = dateTime.format(formatter);

                stmt.setString(1, event.getMember().getId());
                stmt.setString(2, event.getGuild().getId());
                stmt.setString(3, event.getChannel().getId());
                stmt.setString(4, event.getMessage().getContentDisplay());
                stmt.setString(5, formattedDate);

                stmt.executeUpdate();
            } catch (SQLException ex) {
                System.out.println("SQLException: " + ex.getMessage());
                System.out.println("SQLState: " + ex.getSQLState());
                System.out.println("VendorError: " + ex.getErrorCode());
            }
        }
    }

    public static List<Object> getLastTimeUserMessage(User target, Member member) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER +"&password="  + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            if(stmt.execute("SELECT * FROM `users_messages` WHERE uuid = '" + target.getId() + "' AND guild_id = '" + member.getGuild().getId() + "' ORDER BY posted_timestamp DESC")) {
                rs = stmt.getResultSet();
                rs.next();

                List<Object> result = new ArrayList<>();
                result.add(rs.getString("channel_id"));
                result.add(rs.getString("message"));
                result.add(rs.getString("posted_timestamp"));
                return result;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        return null;
    }

    public static void registerAMember(Member member) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER +"&password="  + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            stmt.executeUpdate("INSERT INTO `registered` (uuid, guild_id)" +
                    " VALUES ('" + member.getId() + "', '" + member.getGuild().getId() + "')");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static String canRegister() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER +"&password="  + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            if(stmt.execute("SELECT * FROM registered")) {
                rs = stmt.getResultSet();

                int count = 0;
                while (rs.next()) {
                    count++;
                }

                if(count >= Config.REGISTER_LIMIT) {
                    if(count >= Config.REGISTER_LIMIT + Config.SUBSTITUTE_LIMIT) {
                        return "max";
                    } else {
                        return "substitute";
                    }
                } else {
                    return "can";
                }
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return "error";
    }

    public static void unregisterAMember(Member member) {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER + "&password=" + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            stmt.executeUpdate("DELETE FROM `registered` WHERE uuid = '" + member.getId() + "'");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }

    public static String listRegisteredMember(Guild guild) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER + "&password=" + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            if(stmt.execute("SELECT uuid FROM `registered` WHERE guild_id = '" + guild.getId() + "' LIMIT " + Config.REGISTER_LIMIT + ";")) {
                rs = stmt.getResultSet();

                StringBuilder membersList = new StringBuilder();
                while(rs.next()) {
                    Member member = guild.retrieveMemberById(rs.getString("uuid")).complete();
                    membersList.append(member.getAsMention()).append("\n");
                }

                return membersList.toString();
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return null;
    }

    public static String listSubstituteMember(Guild guild) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/" + Config.DB_NAME + "?user=" + Config.DB_USER + "&password=" + Config.DB_PASSWORD);
            stmt = conn.createStatement();

            if(stmt.execute("SELECT uuid FROM `registered` WHERE guild_id = '" + guild.getId() + "' LIMIT " + Config.SUBSTITUTE_LIMIT + " OFFSET " + Config.REGISTER_LIMIT + ";")) {
                rs = stmt.getResultSet();

                StringBuilder membersList = new StringBuilder();
                while(rs.next()) {
                    Member member = guild.retrieveMemberById(rs.getString("uuid")).complete();
                    membersList.append(member.getAsMention()).append("\n");
                }

                return membersList.toString();
            } else {
                return null;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return null;
    }
}
