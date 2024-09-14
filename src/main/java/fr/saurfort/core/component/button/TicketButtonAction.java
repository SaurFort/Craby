package fr.saurfort.core.component.button;

import fr.saurfort.core.database.query.ticket.MySQLTicket;
import fr.saurfort.core.database.query.ticket.MySQLTicketConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

public class TicketButtonAction {
    public TicketButtonAction(ButtonInteractionEvent event) {
        event.deferReply(true).queue();

        ResultSet rs = MySQLTicketConfig.getTicketConfig(event.getGuild());

        try {
            Category ticketCategory = event.getGuild().getCategoryById(rs.getLong("category_id"));
            TextChannel ticket = ticketCategory.createTextChannel("ticket").addMemberPermissionOverride(event.getMember().getIdLong(), Collections.singleton(Permission.VIEW_CHANNEL), null).complete();

            new MySQLTicket(event.getGuild(), ticket, event.getMember());
            int ticketID = MySQLTicket.getTicketID(event.getGuild(), ticket);

            ticket.getManager().setName("ticket-" + ticketID).queue();

            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Ticket");
            eb.setColor(Color.RED);
            eb.setDescription("Ticket ouvert par " + event.getMember().getAsMention() + "\n\nMerci de décrire le sujet de ce ticket, afin d'aider le support à vous aidez !");
            eb.setAuthor(event.getMember().getNickname(), event.getMember().getAvatarUrl());

            Role support = event.getGuild().getRoleById(rs.getLong("support_id"));

            ticket.sendMessage(support.getAsMention()).queue();
            ticket.sendMessageEmbeds(eb.build()).queue();

            event.getHook().editOriginal("Votre ticket a bien été créer : " + ticket.getAsMention()).queue();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
