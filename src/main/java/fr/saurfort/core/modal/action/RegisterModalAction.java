package fr.saurfort.core.modal.action;

import fr.saurfort.Craby;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import jcrapi2.JCrApi;
import jcrapi2.api.intern.players.PlayerApi;
import jcrapi2.api.intern.players.info.Clan;
import jcrapi2.api.intern.players.info.PlayerRequest;
import jcrapi2.api.intern.players.info.PlayerResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

import java.awt.*;
import java.util.concurrent.ExecutionException;

public class RegisterModalAction {
    public RegisterModalAction(ModalInteractionEvent event) {
        JCrApi jCrApi = Craby.getJCrApi();
        PlayerApi api = jCrApi.getApi(PlayerApi.class);

        String id = event.getValue("id").getAsString();

        if(!id.startsWith("#")) {
            id = "#" + id;
        }

        try {
            PlayerResponse response = api.findByTag(PlayerRequest.builder(id).build()).get();

            String playerName = response.getName();
            String playerTag = response.getTag();
            Role role = event.getGuild().getRoleById(MySQLRegisterConfig.getRegisteredRole(event.getGuild()));
            TextChannel logChannel = event.getGuild().getTextChannelById(MySQLRegisterConfig.getLogChannel(event.getGuild()));

            event.getMember().modifyNickname(playerName).queue();
            event.getGuild().modifyMemberRoles(event.getMember(), role).queue();

            String embedDescription = "Profil de " + event.getMember().getAsMention() + "\n## Joueur\n**Pseudo CR:** `" + playerName + "`\n**ID CR:** `" + playerTag + "`\n## Clan\n";

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
            if(MySQLRegistration.canRegister(event.getGuild()).equals("substitute")) {
                logChannel.sendMessage("Le joueur" + event.getMember().getAsMention() + " vient de s'inscrire à la liste de remplacement !").queue();
                logChannel.sendMessageEmbeds(eb.build()).queue();
                event.reply("Vous êtes sur la liste des remplaçants ! Peut-être que vous pourrez quand même participer. :wink:").setEphemeral(true).queue();
                //event.reply("").queue();
                event.getUser().openPrivateChannel().flatMap(v ->
                        v.sendMessage("Vous avez été placé dans la liste des remplaçants, vous aurez peut-être une chance. :wink:")).queue();
                MySQLRegistration.register(event.getGuild(), event.getMember(), playerTag);
            } else {
                logChannel.sendMessage("Le joueur " + event.getMember().getAsMention() + " vient de s'inscrire !").queue();
                logChannel.sendMessageEmbeds(eb.build()).queue();
                event.reply("Vous avez été inscrit avec succès, bonne chance pour la suite ! :sunglasses:").setEphemeral(true).queue();
                //event.reply("").queue();
                event.getUser().openPrivateChannel().flatMap(v ->
                        v.sendMessage("Vous venez de vous inscrire avec succès ! :sunglasses:")).queue();
                MySQLRegistration.register(event.getGuild(), event.getMember(), playerTag);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        /*if(event.getMember().isOwner()) {
            event.reply("Désolé maitre ! Mais je ne peux pas faire ça, je ne suis qu'un crabe. :face_with_diagonal_mouth:").setEphemeral(true).queue();
        } else {
            Guild guild = event.getGuild();

            Role role = guild.getRoleById(MySQLConfig.getRegisteredRole(event.getGuild()));

            TextChannel logChannel = guild.getTextChannelById(MySQLConfig.getLogChannel(event.getGuild()));

            event.getMember().modifyNickname(username + " (" + id + ")").queue();
            guild.modifyMemberRoles(event.getMember(), role).queue();

            if(MySQLRegistration.canRegister(event.getGuild()).equals("substitute")) {
                logChannel.sendMessage("Le joueur" + event.getMember().getAsMention() + " vient de s'inscrire à la liste de remplacement !").queue();
                event.reply("Vous êtes sur la liste des remplaçants ! Peut-être que vous pourrez quand même participer. :wink:").setEphemeral(true).queue();
                MySQLRegistration.register(event.getGuild(), event.getMember());
            } else {
                logChannel.sendMessage("Le joueur " + event.getMember().getAsMention() + " vient de s'inscrire !").queue();
                event.reply("Vous avez été inscrit avec succès, bonne chance pour la suite ! :sunglasses:").setEphemeral(true).queue();
                MySQLRegistration.register(event.getGuild(), event.getMember());
            }
        }*/
    }
}
