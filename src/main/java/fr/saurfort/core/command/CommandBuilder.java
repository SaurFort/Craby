package fr.saurfort.core.command;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public interface CommandBuilder {
    String getName();
    String getDescription();
    Permission getPermission();
    boolean getGuildOnly();
    void execute(SlashCommandInteractionEvent event);
}
