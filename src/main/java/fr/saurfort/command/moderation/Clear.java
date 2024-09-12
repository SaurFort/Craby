package fr.saurfort.command.moderation;

import fr.saurfort.command.CommandBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class Clear implements CommandBuilder {
    @Override
    public String getName() {
        return "clear";
    }

    @Override
    public String getDescription() {
        return "Supprime les messages d'un salon";
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_MANAGE;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply(true).queue();

        int amount = event.getOption("amount", OptionMapping::getAsInt);

        event.getChannel().getHistory().retrievePast(amount).queue(messageHistory -> {
            for (Message message : messageHistory) {
                if (!message.isPinned()) message.delete().queue();
            }

            event.getHook().sendMessage(amount + " messages ont été supprimé ! Les messages épinglés ont été ignorer ! :sunglasses:").queue();
        });
    }
}
