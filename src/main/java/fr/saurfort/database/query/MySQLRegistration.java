package fr.saurfort.database.query;

import fr.saurfort.database.init.MySQLDatabase;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.PreparedStatement;
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

    public static String listRegisteredMember(Guild guild) {

    }
}
