package fr.saurfort.modal.action;

import fr.saurfort.database.query.MySQLRegistration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;

public class RegisterModalAction {
    public RegisterModalAction(ModalInteractionEvent event) {
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

            TextChannel logChannel = guild.getTextChannelById(1269718846080815159L);

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
        }
    }
}
