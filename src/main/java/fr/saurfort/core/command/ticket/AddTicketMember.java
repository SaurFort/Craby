package fr.saurfort.core.command.ticket;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.ticket.MySQLTicket;
import fr.saurfort.utils.enums.CommandCategory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Collections;

public class AddTicketMember implements CommandBuilder {
    @Override
    public String getName() {
        return "addticketmember";
    }

    @Override
    public String getDescription() {
        return "Ajoute un membre au ticket";
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
    public CommandCategory getCategory() {
        return CommandCategory.TICKET;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        TextChannel ticket = event.getChannel().asTextChannel();

        if(MySQLTicket.isTicket(event.getGuild(), ticket)) {
            Member member = event.getOption("member", OptionMapping::getAsMember);

            System.out.println(ticket);

            ticket.getManager().putPermissionOverride(member, Collections.singleton(Permission.VIEW_CHANNEL), null).queue();

            event.getHook().editOriginal(member.getAsMention() + " a été ajouté au ticket !").queue();
        } else {
            event.getHook().editOriginal("Ce salon n'est pas un ticket !").queue();
        }
    }
}
