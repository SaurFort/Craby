package fr.saurfort.listener;

import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.Unregister;
import fr.saurfort.command.utils.Help;
import fr.saurfort.command.utils.Ping;
import fr.saurfort.command.modal.RegistrationModal;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CommandListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            // Utils
            case "help":
                new Help().onSlashCommandInteraction(event);
                break;
            case "ping":
                new Ping(event);
                break;

            // Modal
            case "register":
                new RegistrationModal().onSlashCommandInteraction(event);
                break;

            // Moderation
            case "lastmessage":
                new LastMessage(event);
                break;
            case "registeredlist":
                new RegisteredList().onSlashCommandInteraction(event);
                break;
            case "unregister":
                new Unregister().onSlashCommandInteraction(event);
                break;
            default:
                System.out.printf("Unknown command %s used by %#s%n", event.getName(), event.getUser());
        }
    }
}
