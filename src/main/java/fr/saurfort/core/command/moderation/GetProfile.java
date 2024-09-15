package fr.saurfort.core.command.moderation;

import fr.saurfort.Craby;
import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.utils.enums.CommandCategory;
import jcrapi2.JCrApi;
import jcrapi2.api.intern.players.PlayerApi;
import jcrapi2.api.intern.players.info.Clan;
import jcrapi2.api.intern.players.info.PlayerRequest;
import jcrapi2.api.intern.players.info.PlayerResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class GetProfile implements CommandBuilder {
    @Override
    public String getName() {
        return "getprofile";
    }

    @Override
    public String  getDescription() {
        return "Donne le profile Clash Royale d'un joueur inscrit";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_ROLES;
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

        Member player = event.getOption("player", OptionMapping::getAsMember);

        if (player == null) {
            event.getHook().sendMessage("Le joueur spécifié est invalide ou n'existe pas ! :warning:").queue();
            return;
        }

        if (MySQLRegistration.haveBeenRegistered(event.getGuild(), player)) {
            JCrApi jCrApi = Craby.getJCrApi();
            PlayerApi api = jCrApi.getApi(PlayerApi.class);

            try {
                PlayerResponse response = api.findByTag(PlayerRequest.builder(MySQLRegistration.getId(event.getGuild(), player)).build()).get();

                String playerName = response.getName();
                String playerTag = response.getTag();

                String embedDescription = "Profil de " + player.getAsMention() + "\n## Joueur\n**Pseudo CR:** `" + playerName + "`\n**ID CR:** `" + playerTag + "`\n## Clan\n";

                if(response.getClan() != null) {
                    Clan clan = response.getClan();

                    embedDescription += "**Nom du clan:** `" + clan.getName() + "`\n**ID du clan:** `" + clan.getTag() + "`";
                } else {
                    embedDescription += "**Aucun clan**";
                }

                EmbedBuilder eb = new EmbedBuilder();

                eb.setTitle("Inscription");
                eb.setColor(Color.GREEN);
                eb.setDescription(embedDescription);

                event.getHook().sendMessageEmbeds(eb.build()).queue();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        } else {
            event.getHook().sendMessage("Le joueur " + player.getAsMention() + " n'est pas inscrit ! :pensive:").queue();
        }
    }
}
