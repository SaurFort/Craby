package fr.saurfort.command.ticket;

import fr.saurfort.command.CommandBuilder;
import fr.saurfort.database.query.ticket.MySQLTicket;
import fr.saurfort.logger.TicketLogger;
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

            MySQLTicket.deleteTicket(event.getGuild(), ticket);

            logger.makeConsoleLog(event.getGuild(), ticket.getName() + " a été fermé par " + event.getMember().getNickname());

            event.getGuild().getTextChannelById(ticket.getId()).delete().reason(reason).queue();

            User ticketOwner = event.getJDA().getUserById(MySQLTicket.getTicketOwner(event.getGuild(), ticket));

            if (ticketOwner == event.getUser()) {
                logger.sendPrivateMessage(ticketOwner, "Votre ticket " + ticket.getName() + " a été fermé avec succès");
            } else {
                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Ticket: " + ticket.getName());
                eb.setDescription("Votre ticket a été fermé par " + event.getMember().getAsMention() + "\nPour la raison: " + reason);
                eb.setColor(Color.GREEN);
                eb.setAuthor(event.getMember().getNickname(), event.getMember().getAvatarUrl());

                logger.sendPrivateEmbed(ticketOwner, eb);
            }
        } else {
            event.reply("Vous pouvez utiliser cette commande uniquement dans les tickets.").queue();
        }
    }
}
