package fr.saurfort.core.command.utils;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.command.config.GetConfig;
import fr.saurfort.core.command.config.RegisterConfig;
import fr.saurfort.core.command.config.TicketConfig;
import fr.saurfort.core.command.config.WelcomeConfig;
import fr.saurfort.core.command.moderation.*;
import fr.saurfort.core.command.ticket.AddTicketMember;
import fr.saurfort.core.command.ticket.CloseTicket;
import fr.saurfort.core.command.ticket.RemoveTicketMember;
import fr.saurfort.core.command.tournament.Register;
import fr.saurfort.core.command.tournament.TournamentInfo;
import fr.saurfort.core.command.tournament.Unregister;
import fr.saurfort.utils.enums.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class Help implements CommandBuilder {

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Affiche la liste des commandes";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public boolean getGuildOnly() {
        return false;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Liste des commandes");
        eb.setColor(Color.ORANGE);
        eb.setDescription(getHelpContent());
        eb.setFooter(event.getGuild().getName(), event.getGuild().getIconUrl());

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }

    public String getHelpContent() {
        return "### " + CommandCategory.CONFIG.getFullName() + "\n" +
                "- `/" + new GetConfig().getName() + "` : " + new GetConfig().getDescription() + "\n" +
                "- `/" + new RegisterConfig().getName() + "` : " + new RegisterConfig().getDescription() + "\n" +
                "- `/" + new TicketConfig().getName() + "` : " + new TicketConfig().getDescription() + "\n" +
                "- `/" + new WelcomeConfig().getName() + "` : " + new WelcomeConfig().getDescription() + "\n" +
                "### " + CommandCategory.MODERATION.getFullName() + "\n" +
                "- `/" + new Clear().getName() + "` : " + new Clear().getDescription() + "\n" +
                "- `/" + new EndTournament().getName() + "` : " + new EndTournament().getDescription() + "\n" +
                "- `/" + new ForcedRegister().getName() + "` : " + new ForcedRegister().getDescription() + "\n" +
                "- `/" + new ForcedUnregister().getName() + "` : " + new ForcedUnregister().getDescription() + "\n" +
                "- `/" + new GetProfile().getName() + "` : " + new GetProfile().getDescription() + "\n" +
                "- `/" + new LastMessage().getName() + "` : " + new LastMessage().getDescription() + "\n" +
                "- `/" + new RegisteredList().getName() + "` : " + new RegisteredList().getDescription() + "\n" +
                "- `/" + new StartTournament().getName() + "` : " + new StartTournament().getDescription() + "\n" +
                "### " + CommandCategory.TICKET.getFullName() + "\n" +
                "- `/" + new AddTicketMember().getName() + "` : " + new AddTicketMember().getDescription() + "\n" +
                "- `/" + new CloseTicket().getName() + "` : " + new CloseTicket().getDescription() + "\n" +
                "- `/" + new RemoveTicketMember().getName() + "` : " + new RemoveTicketMember().getDescription() + "\n" +
                "### " + CommandCategory.TOURNAMENT.getFullName() + "\n" +
                "- `/" + new Register().getName() + "` : " + new Register().getDescription() + "\n" +
                "- `/" + new TournamentInfo().getName() + "` : " + new TournamentInfo().getDescription() + "\n" +
                "- `/" + new Unregister().getName() + "` : " + new Unregister().getDescription() + "\n" +
                "### " + CommandCategory.UTILS.getFullName() + "\n" +
                "- `/" + new Help().getName() + "` : " + new Help().getDescription() + "\n" +
                "- `/" + new Ping().getName() + "` : " + new Ping().getDescription() + "\n";
    }
}
