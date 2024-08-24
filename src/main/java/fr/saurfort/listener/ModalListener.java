package fr.saurfort.listener;

import fr.saurfort.database.Database;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.UserSnowflake;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class ModalListener extends ListenerAdapter {
    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if(event.getModalId().equals("registration")) {
            String username = event.getValue("username").getAsString();
            String id = event.getValue("id").getAsString();

            if(!id.startsWith("#")) {
                id = "#" + id;
            }

            if(event.getMember().isOwner()) {
                event.reply("Désolé maitre ! Mais je ne peux pas faire ça, je ne suis qu'un crabe. :face_with_diagonal_mouth:").setEphemeral(true).queue();
            } else {
                Guild guild = event.getGuild();

                Role role = guild.getRoleById(1270066037131575408L);

                //System.out.println(role);

                TextChannel logChannel = guild.getTextChannelById(1269718846080815159L);
                //TextChannel chatChannel = guild.getTextChannelById(1269718729701462129L);

                event.getMember().modifyNickname(username + " (" + id + ")").queue();
                guild.modifyMemberRoles(event.getMember(), role).queue();

                if(Database.canRegister().equals("substitute")) {
                    logChannel.sendMessage("Le joueur" + event.getMember().getAsMention() + " vient de s'inscrire à la liste de remplacement !").queue();
                    event.reply("Vous êtes sur la liste des remplaçants ! Peut-être que vous pourrez quand même participer. :wink:").setEphemeral(true).queue();
                    Database.registerAMember(event.getMember());
                } else {
                    logChannel.sendMessage("Le joueur " + event.getMember().getAsMention() + " vient de s'inscrire !").queue();
                    event.reply("Vous avez été inscrit avec succès, bonne chance pour la suite ! :sunglasses:").setEphemeral(true).queue();
                    Database.registerAMember(event.getMember());
                }
            }
        }
    }
}
