package fr.saurfort.command.config;

import fr.saurfort.command.CommandBuilder;
import fr.saurfort.database.query.ticket.MySQLTicketConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.Category;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;
import java.util.Collections;
import java.util.EnumSet;

public class TicketConfig implements CommandBuilder {
    @Override
    public String getName() {
        return "ticketconfig";
    }

    @Override
    public String getDescription() {
        return "Configuration des tickets";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_CHANNEL;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Role supportRole;
        Category ticketCategory;
        TextChannel ticketLog;

        event.deferReply(true).queue();

        if(event.getOption("support_role") != null) {
            supportRole = event.getOption("support_role", OptionMapping::getAsRole);
        } else {
            supportRole = event.getGuild().createRole().setName("Support").setColor(Color.GREEN).complete();
        }

        if(event.getOption("ticket_category") != null) {
            ticketCategory = event.getOption("ticket_category", OptionMapping::getAsChannel).asCategory();
        } else {
            ticketCategory = event.getGuild().createCategory("Tickets").addRolePermissionOverride(supportRole.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL, Permission.MANAGE_CHANNEL), null).addPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL)).complete();
        }

        if(event.getOption("ticket_log") != null) {
            ticketLog = event.getOption("ticket_log", OptionMapping::getAsChannel).asTextChannel();
        } else {
            ticketLog = event.getGuild().createTextChannel("ticket-log").addRolePermissionOverride(supportRole.getIdLong(), EnumSet.of(Permission.VIEW_CHANNEL), null).addPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singleton(Permission.VIEW_CHANNEL)).complete();
        }

        EmbedBuilder eb1 = new EmbedBuilder();

        eb1.setTitle("Ticket Config");
        eb1.setColor(Color.GREEN);
        eb1.setDescription("Les tickets ont été configuré avec succès.\nCatégorie des tickets: " + ticketCategory.getName() + "\nSalon des logs: " + ticketLog.getAsMention() + "\nRôle du support: " + supportRole.getAsMention());

        new MySQLTicketConfig(event.getGuild(), ticketCategory, ticketLog, supportRole);

        ticketLog.sendMessageEmbeds(eb1.build()).queue();

        Button button = Button.primary("ticket:open", "Ouvrir un ticket");

        event.getHook().sendMessage("Ticket configuré avec succès !").queue();

        event.getChannel().sendMessage("Vous avez besoin d'aide ? Ouvrez un ticket !").addActionRow(button).queue();
    }
}
