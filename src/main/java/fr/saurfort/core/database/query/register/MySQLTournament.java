package fr.saurfort.core.database.query.register;

import fr.saurfort.core.database.init.MySQLDatabase;
import fr.saurfort.core.utils.enums.TournamentStatus;
import net.dv8tion.jda.api.entities.Guild;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLTournament {
    private static Connection conn = MySQLDatabase.conn;

    public MySQLTournament(Guild guild, TournamentStatus status) {
        String query = "INSERT INTO tournament_list (guild_id, status)" +
                "   VALUES (?,?)";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());
            stmt.setString(2, status.getStatus());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeStatus(Guild guild, String tournamentName, TournamentStatus status) {
        String query = "UPDATE tournament_list SET tournament_name = ?, status = ? WHERE guild_id = ? AND status != ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, tournamentName);
            stmt.setString(2, status.getStatus());
            stmt.setString(3, guild.getId());
            stmt.setString(4, TournamentStatus.ENDED.getStatus());

            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void changeStatus(Guild guild, TournamentStatus status) {
        String query = "UPDATE tournament_list SET status = ? WHERE guild_id = ? AND status != ?";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, status.getStatus());
            stmt.setString(2, guild.getId());
            stmt.setString(3, TournamentStatus.ENDED.getStatus());

            stmt.executeUpdate();
        } catch(SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static TournamentStatus getStatus(Guild guild) {
        String query = "SELECT status FROM tournament_list WHERE guild_id = ? ORDER BY id DESC LIMIT 1";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            try {
                if(rs.next()) {
                    return TournamentStatus.convertStatus(rs.getString("status"));
                }
            } catch (Exception ignored) {}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return TournamentStatus.ENDED;
    }

    public static String getName(Guild guild) {
        String query = "SELECT tournament_name FROM tournament_list WHERE guild_id = ? ORDER BY id DESC LIMIT 1";

        try {
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, guild.getId());

            stmt.executeQuery();
            ResultSet rs = stmt.getResultSet();

            try {
                if(rs.next()) {
                    return rs.getString("tournament_name");
                }
            } catch (Exception ignored) {}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return null;
    }
}
