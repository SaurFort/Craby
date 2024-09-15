package fr.saurfort.core.command.utils;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.utils.enums.CommandCategory;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

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
    public CommandCategory getCategory() {
        return CommandCategory.UTILS;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();
        event.getHook().sendMessage("Pong! :ping_pong:").queue();
        JDA jda = event.getJDA();

        jda.getRestPing().queue((ping) -> event.getHook().editOriginalFormat("Bot ping: `%sms`\nDiscord ping: `%sms`", ping, jda.getGatewayPing()).queue());
    }
}
