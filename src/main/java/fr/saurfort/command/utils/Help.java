package fr.saurfort.command.utils;

import fr.saurfort.modal.creator.RegistrationModal;
import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.ForcedUnregister;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class Help extends ListenerAdapter {
    public static final String NAME = "help";
    public static final String DESCRIPTION = "Affiche la liste des commandes";
    public static final long PERMISSION = Permission.ALL_PERMISSIONS;
    public static final boolean GUILD_ONLY = false;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals(NAME)) {
            event.deferReply().queue();

            EmbedBuilder eb = new EmbedBuilder();

            eb.setTitle("Liste des commandes");
            eb.setColor(Color.ORANGE);
            eb.setDescription("### Tournoi\n - `" + RegistrationModal.NAME + "` : " + RegistrationModal.DESCRIPTION + "\n\n### Utilitaire :tools:\n - `" + Help.NAME + "` : " + Help.DESCRIPTION + "\n - `" + Ping.NAME + "` : " + Ping.DESCRIPTION + "\n\n### Modération :cop:\n - `" + LastMessage.NAME + "` : " + LastMessage.DESCRIPTION + "\n - `" + RegisteredList.NAME + "` : " + RegisteredList.DESCRIPTION + "\n - `" + ForcedUnregister.NAME + "` : " + ForcedUnregister.DESCRIPTION);
            eb.setFooter(event.getMember().getNickname(), event.getMember().getAvatarUrl());

            event.getHook().sendMessageEmbeds(eb.build()).queue();
        }
    }
}
