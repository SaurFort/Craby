package fr.saurfort.core.command.moderation;

import fr.saurfort.Craby;
import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import jcrapi2.JCrApi;
import jcrapi2.api.intern.players.PlayerApi;
import jcrapi2.api.intern.players.info.Clan;
import jcrapi2.api.intern.players.info.PlayerRequest;
import jcrapi2.api.intern.players.info.PlayerResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class ForcedRegister implements CommandBuilder {
    @Override
    public String getName() {
        return "forcedregister";
    }

    @Override
    public String getDescription() {
        return "Force l'inscription d'un joueur";
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
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        JCrApi jCrApi = Craby.getJCrApi();
        PlayerApi api = jCrApi.getApi(PlayerApi.class);

        String id = event.getOption("id", OptionMapping::getAsString);
        Member member = event.getOption("user", OptionMapping::getAsMember);
        User user = event.getOption("user", OptionMapping::getAsUser);

        if(!id.startsWith("#")) {
            id = "#" + id;
        }

        PlayerResponse response = null;
        try {
            response = api.findByTag(PlayerRequest.builder(id).build()).get();

            String playerName = response.getName();
            String playerTag = response.getTag();
            Clan clan = response.getClan();
            Role role = event.getGuild().getRoleById(MySQLRegisterConfig.getRegisteredRole(event.getGuild()));
            TextChannel logChannel = event.getGuild().getTextChannelById(MySQLRegisterConfig.getLogChannel(event.getGuild()));

            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Inscription");
            eb.setColor(Color.GREEN);
            eb.setDescription("Inscription de " + event.getUser().getAsMention() + "\n# Joueur\n**Pseudo CR:** `" + playerName + "`\n**ID CR:** `" + playerTag + "`\n# Clan\n**Nom du clan:** `" + clan.getName() + "`\n**ID du clan:** `" + clan.getTag() + "`");

            if(!event.getMember().canInteract(event.getMember())) {
                event.getHook().sendMessage("Étrangement, vous ne pouvez pas interagir avec ce membre :thinking:").setEphemeral(true).queue();
            } else {
                event.getHook().setEphemeral(false);

                MySQLRegistration.register(event.getGuild(), member, playerTag);
                member.modifyNickname(playerName).queue();
                member.getGuild().modifyMemberRoles(member, role).queue();

                if(MySQLRegistration.canRegister(event.getGuild()).equals("substitute")) {
                    logChannel.sendMessage("Le joueur " + member.getAsMention() + " a été inscrit à la liste de remplacement !").queue();
                    logChannel.sendMessageEmbeds(eb.build()).queue();
                    event.getHook().editOriginal("Le joueur " + member.getAsMention() + " a été inscrit avec succès.").queue();
                    user.openPrivateChannel().flatMap(v ->
                            v.sendMessage("Vous venez d'être inscrit par " + event.getMember().getAsMention() + " mais vous êtes dans la liste des remplaçants, vous aurez peut-être une chance ! :wink:")).queue();
                } else {
                    logChannel.sendMessage("Le joueur " + member.getAsMention() + " vient de s'inscrire !").queue();
                    logChannel.sendMessageEmbeds(eb.build()).queue();
                    event.getHook().editOriginal("Le joueur " + member.getAsMention() + " a été inscrit avec succès.").queue();
                    user.openPrivateChannel().flatMap(v ->
                            v.sendMessage("Vous venez d'être inscrit par " + event.getMember().getAsMention() + " ! :sunglasses:")).queue();
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
