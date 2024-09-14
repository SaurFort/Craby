package fr.saurfort.core.command.ticket;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.ticket.MySQLTicket;
import fr.saurfort.core.logger.TicketLogger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;

public class CloseTicket implements CommandBuilder {
    private TicketLogger logger = new TicketLogger();

    @Override
    public String getName() {
        return "closeticket";
    }

    @Override
    public String getDescription() {
        return "Ferme le ticket";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        TextChannel ticket = event.getChannel().asTextChannel();

        if (MySQLTicket.isTicket(event.getGuild(), ticket)) {
            String reason = event.getOption("reason", OptionMapping::getAsString);

            User ticketOwner = event.getJDA().retrieveUserById(MySQLTicket.getTicketOwner(event.getGuild(), ticket)).complete();

            if (ticketOwner == event.getUser()) {
                logger.sendPrivateMessage(ticketOwner, "Votre ticket **" + ticket.getName() + "** a été fermé avec succès !");
            } else {
                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Ticket: " + ticket.getName());
                eb.setDescription("Votre ticket a été fermé par " + event.getMember().getAsMention() + "\nPour la raison: `" + reason + "`");
                eb.setColor(Color.GREEN);
                eb.setAuthor(event.getMember().getNickname(), event.getMember().getAvatarUrl());

                logger.sendPrivateEmbed(ticketOwner, eb);
            }

            MySQLTicket.deleteTicket(event.getGuild(), ticket);
            event.getGuild().getTextChannelById(ticket.getId()).delete().reason(reason).queue();
        } else {
            event.reply("Vous pouvez utiliser cette commande uniquement dans les tickets.").queue();
        }
    }
}
