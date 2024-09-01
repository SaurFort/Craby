package fr.saurfort.command.utils;

import fr.saurfort.command.CommandBuilder;
import fr.saurfort.modal.creator.RegisterModalCreator;
import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.ForcedUnregister;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Help implements CommandBuilder {
    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Affiche la liste des commandes";
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
        event.deferReply().queue();

        Ping ping = new Ping();
        LastMessage lastMessage = new LastMessage();
        RegisteredList registeredList = new RegisteredList();

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Liste des commandes");
        eb.setColor(Color.ORANGE);
        eb.setDescription("### Tournoi\n - `" + RegisterModalCreator.NAME + "` : " + RegisterModalCreator.DESCRIPTION + "\n\n### Utilitaire :tools:\n - `" + getName() + "` : " + getDescription() + "\n - `" + ping.getName() + "` : " + ping.getDescription() + "\n\n### Modération :cop:\n - `" + lastMessage.getName() + "` : " + lastMessage.getDescription() + "\n - `" + registeredList.getName() + "` : " + registeredList.getDescription() + "\n - `" + ForcedUnregister.NAME + "` : " + ForcedUnregister.DESCRIPTION);
        eb.setFooter(event.getMember().getNickname(), event.getMember().getAvatarUrl());

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
