package fr.saurfort.command.utils;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;

public class Ping {
    public static final String NAME = "ping";
    public static final String DESCRIPTION = "Obtenir le délai du bot";
    public static final long PERMISSION = Permission.ALL_PERMISSIONS;
    public static final boolean GUILD_ONLY = false;

    public Ping(SlashCommandInteraction event) {
        long time = System.currentTimeMillis();
        //event.getJDA().installAuxiliaryPort().queue();
        event.reply("Pong!").setEphemeral(true)
                .flatMap(v ->
                        event.getHook().editOriginalFormat(":ping_pong: Pong: %d ms", System.currentTimeMillis() - time)
                ).queue();
    }
}
