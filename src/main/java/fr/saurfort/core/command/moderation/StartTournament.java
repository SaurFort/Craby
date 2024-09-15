package fr.saurfort.core.command.moderation;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.core.database.query.register.MySQLTournament;
import fr.saurfort.core.utils.enums.CommandCategory;
import fr.saurfort.core.utils.enums.TournamentStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.EnumSet;

public class StartTournament implements CommandBuilder {
    @Override
    public String getName() {
        return "starttournament";
    }

    @Override
    public String getDescription() {
        return "Lance le tournoi";
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
        return CommandCategory.MODERATION;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        int registerLimit = MySQLRegisterConfig.getRegisterLimit(event.getGuild());

        if(Boolean.TRUE.equals(event.getOption("forced", OptionMapping::getAsBoolean))) {
            startTournament(event);
        } else {
            if(MySQLRegistration.getRegisteredMember(event.getGuild()) == registerLimit) {
                startTournament(event);
            } else {
                event.getHook().sendMessage("Le tournoi ne peut pas être lancé si les " + registerLimit + " joueurs inscrit n'ont pas été atteint.\nSi vous souhaitez tout de même lancé le tournoi, écrivez `/starttournament name: " + event.getOption("name", OptionMapping::getAsString) + " forced:True` !").queue();
            }
        }
    }

    private void startTournament(SlashCommandInteractionEvent event) {
        event.getHook().sendMessage("Lancement du tournoi en cours ...").queue(e -> {
            TextChannel registrationChannel = event.getGuild().getTextChannelById(MySQLRegisterConfig.getRegisterChannel(event.getGuild()));
            registrationChannel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, EnumSet.of(Permission.MESSAGE_SEND)).queue();

            MySQLTournament.changeStatus(event.getGuild(), event.getOption("name", OptionMapping::getAsString), TournamentStatus.IN_PROGRESS);

            event.getHook().editOriginal("Le tournoi a été lancé avec succès !\nPar conséquent les inscriptions ont été fermer !").queue();
        });
    }
}
