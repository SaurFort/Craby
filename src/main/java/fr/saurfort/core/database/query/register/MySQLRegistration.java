package fr.saurfort.core.database.query.register;

import fr.saurfort.core.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import org.jetbrains.annotations.NotNull;

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
            stmt.setInt(2, MySQLRegisterConfig.getRegisterLimit(guild));

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
            stmt.setInt(2, MySQLRegisterConfig.getSubstituteLimit(guild));
            stmt.setInt(3, MySQLRegisterConfig.getRegisterLimit(guild));

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
            stmt.setInt(2, MySQLRegisterConfig.getRegisterLimit(guild));

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
            stmt.setInt(2, MySQLRegisterConfig.getSubstituteLimit(guild));
            stmt.setInt(3, MySQLRegisterConfig.getRegisterLimit(guild));

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

    public static void register(Guild guild, Member member, @NotNull String crId) {
        String query = "INSERT INTO registered (uuid, guild_id, cr_id)" +
                "VALUES (?,?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, member.getId());
            stmt.setString(2, guild.getId());
            stmt.setString(3, crId);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unregister(Guild guild, Member member) {
        String query = "DELETE FROM registered WHERE guild_id = ? AND uuid = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, member.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void unregister(Guild guild, String tag) {
        String query = "DELETE FROM registered WHERE guild_id = ? AND cr_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, tag);

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String canRegister(Guild guild) {
        String query = "SELECT COUNT(*) AS count FROM registered WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.execute();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            int count = rs.getInt("count");

            if(count >= MySQLRegisterConfig.getRegisterLimit(guild)) {
                if(count >= MySQLRegisterConfig.getRegisterLimit(guild) + MySQLRegisterConfig.getSubstituteLimit(guild)) {
                    return "max";
                } else {
                    return "substitute";
                }
            } else {
                return "can";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean haveBeenRegistered(Guild guild, Member member) {
        String query = "SELECT id FROM registered WHERE guild_id = ? AND uuid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, guild.getId());
            stmt.setString(2, member.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }


    public static String getId(Guild guild, Member member) {
        String query = "SELECT cr_id FROM registered WHERE guild_id = ? AND uuid = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, member.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getString("cr_id");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getDiscordId(Guild guild, String message) {
        String query = "SELECT uuid FROM registered WHERE guild_id = ? AND want_unregister = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, message);

            System.out.println(stmt);

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();
            rs.next();

            return rs.getString("uuid");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setWantUnregister(Guild guild, Member member, Message message) {
        String query = "UPDATE registered SET want_unregister = ? WHERE guild_id = ? AND uuid = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, message.getId());
            stmt.setString(2, guild.getId());
            stmt.setString(3, member.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void removeAllRegistered(Guild guild) {
        String query = "DELETE FROM registered WHERE guild_id = ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
