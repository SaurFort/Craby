package fr.saurfort.core.command.utils;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.command.tournament.Register;
import fr.saurfort.core.command.moderation.LastMessage;
import fr.saurfort.core.command.moderation.RegisteredList;
import fr.saurfort.core.command.moderation.ForcedUnregister;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

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
        Register register = new Register();
        ForcedUnregister forcedUnregister = new ForcedUnregister();

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Liste des commandes");
        eb.setColor(Color.ORANGE);
        eb.setDescription("### Tournoi :trophy:\n - `" + register.getName() + "` : " + register.getDescription() + "\n\n### Utilitaire :tools:\n - `" + getName() + "` : " + getDescription() + "\n - `" + ping.getName() + "` : " + ping.getDescription() + "\n\n### Modération :cop:\n - `" + lastMessage.getName() + "` : " + lastMessage.getDescription() + "\n - `" + registeredList.getName() + "` : " + registeredList.getDescription() + "\n - `" + forcedUnregister.getName() + "` : " + forcedUnregister.getDescription());
        eb.setFooter(event.getMember().getNickname(), event.getMember().getAvatarUrl());

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
