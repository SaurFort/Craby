package fr.saurfort.listener;

import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.ForcedUnregister;
import fr.saurfort.command.utils.Help;
import fr.saurfort.command.utils.Ping;
import fr.saurfort.database.query.MySQLLastMessage;
import fr.saurfort.modal.creator.RegisterModalCreator;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            // Utils
            case "help":
                new Help().onSlashCommandInteraction(event);
                break;
            case "ping":
                new Ping().execute(event);
                break;

            // Modal
            case "register":
                new RegisterModalCreator().onSlashCommandInteraction(event);
                break;

            // Moderation
            case "lastmessage":
                new LastMessage().execute(event);
                break;
            case "registeredlist":
                new RegisteredList().execute(event);
                break;
            case "unregister":
                new ForcedUnregister().onSlashCommandInteraction(event);
                break;
            default:
                System.out.printf("Unknown command %s used by %#s%n", event.getName(), event.getUser());
        }
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.isFromType(ChannelType.PRIVATE)) {
            event.getMessage().getChannel().sendMessage("Désolé, mais je n'aime pas beaucoup les messages privées des gens que je connais pas. :face_with_diagonal_mouth:");
        } else {
            //System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(), event.getChannel().getName(), event.getMember().getEffectiveName(), event.getMessage().getContentDisplay());

            new MySQLLastMessage(event.getGuild(), event.getMember(), event.getChannel().asTextChannel(), event.getMessage());
        }
    }
}
