package fr.saurfort.core.command.tournament;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.core.database.query.register.MySQLTournament;
import fr.saurfort.core.utils.enums.TournamentStatus;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class TournamentInfo implements CommandBuilder {
    @Override
    public String getName() {
        return "tournamentinfo";
    }

    @Override
    public String getDescription() {
        return "Affiche les informations du tournoi";
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
        event.deferReply(true).queue();

        TournamentStatus status = MySQLTournament.getStatus(event.getGuild());
        String statusName = null;

        if(status.getStatusCode() == 0) {
            statusName = "Inscription ouverte";
        } else if(status.getStatusCode() == 1) {
            statusName = "Tournoi en cours";
        }

        String tournamentName = MySQLTournament.getName(event.getGuild());

        if(tournamentName == null) {
            tournamentName = "Information du tournoi";
        }

        int registered = MySQLRegistration.getRegisteredMember(event.getGuild());
        int registeredLimit = MySQLRegisterConfig.getRegisterLimit(event.getGuild());

        String embedDescription = "**Statut :** " + statusName + " !\n**Inscrits : **" + registered + "/" + registeredLimit;

        if(registered == registeredLimit) {
            int substituteLimit = MySQLRegisterConfig.getSubstituteLimit(event.getGuild());
            if(substituteLimit != 0) {
                embedDescription += "\nRemplaçant" + MySQLRegistration.getSubstituteMember(event.getGuild()) + "/" + substituteLimit;
            }
        }

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle(tournamentName);
        eb.setDescription(embedDescription);
        eb.setColor(Color.GREEN);

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
