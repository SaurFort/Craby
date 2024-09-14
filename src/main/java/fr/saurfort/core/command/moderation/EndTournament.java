package fr.saurfort.core.command.moderation;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.core.database.query.register.MySQLTournament;
import fr.saurfort.core.utils.enums.TournamentStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class EndTournament implements CommandBuilder {
    @Override
    public String getName() {
        return "endtournament";
    }

    @Override
    public String getDescription() {
        return "Met fin au tournoi en cours";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_PERMISSIONS;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        TextChannel logChannel = event.getGuild().getTextChannelById(MySQLRegisterConfig.getLogChannel(event.getGuild()));
        logChannel.sendMessage("Le tournoi à été cloturé par " + event.getMember().getAsMention() + " !").queue();

        TextChannel registrationChannel = event.getGuild().getTextChannelById(MySQLRegisterConfig.getRegisterChannel(event.getGuild()));
        registrationChannel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), EnumSet.of(Permission.MESSAGE_SEND), null).queue();

        MySQLTournament.changeStatus(event.getGuild(), TournamentStatus.ENDED);

        MySQLRegisterConfig.deleteConfig(event.getGuild());
        MySQLRegistration.removeAllRegistered(event.getGuild());

        event.getGuild().loadMembers().onSuccess(members -> {
            System.out.println("Nombre de membres récupérés : " + members.size());
            for (Member member : members) {
                System.out.println("Traitement du membre : " + member.getUser().getName());
                if(!member.isOwner() && !member.getUser().isBot()) {
                    member.modifyNickname(member.getUser().getGlobalName()).queue();
                    member.getGuild().modifyMemberRoles(member).queue();
                }
            }
        });

        event.getHook().sendMessage("Votre tournoi **" + MySQLTournament.getName(event.getGuild()) + "** à bien été fermer ! Les configurations lié à ce dernier ont été réinitialisé !").queue();
    }
}
