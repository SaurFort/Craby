package fr.saurfort.command.utils;

import fr.saurfort.command.CommandBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class Ping implements CommandBuilder {
    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getDescription() {
        return "Retourne le délai de réponse du bot";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public boolean getGuildOnly() {
        return false;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        long time = System.currentTimeMillis();
        event.reply("Pong!").setEphemeral(true)
                .flatMap(v ->
                        event.getHook().editOriginalFormat(":ping_pong: Pong: %d ms", System.currentTimeMillis() - time)
                ).queue();
    }
}
