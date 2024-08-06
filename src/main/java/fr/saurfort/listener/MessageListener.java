package fr.saurfort.listener;

import fr.saurfort.database.Database;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.isFromType(ChannelType.PRIVATE)) {
            event.getMessage().getChannel().sendMessage("Désolé, mais je n'aime pas beaucoup les messages privées des gens que je connais pas. :face_with_diagonal_mouth:");
        } else {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(), event.getChannel().getName(), event.getMember().getEffectiveName(), event.getMessage().getContentDisplay());

            Database.addUserMessage(event);
        }
    }
}
