package fr.saurfort.command.config;

import fr.saurfort.command.CommandBuilder;
import fr.saurfort.database.query.MySQLConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.util.EnumSet;

public class RegisterConfig implements CommandBuilder {
    @Override
    public String getName() {
        return "registerconfig";
    }

    @Override
    public String getDescription() {
        return "Configuration du système d'inscription";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_PERMISSIONS;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        TextChannel logChannel;
        TextChannel registrationChannel;
        Role registered;
        int registerLimit = event.getOption("register_limit", OptionMapping::getAsInt);
        int substituteLimit;

        if(event.getOption("log", OptionMapping::getAsChannel) != null) {
            logChannel = event.getOption("log", OptionMapping::getAsChannel).asTextChannel();
        } else {
            logChannel = event.getGuild().createTextChannel("register-logs").addPermissionOverride(event.getGuild().getPublicRole(), null, EnumSet.of(Permission.VIEW_CHANNEL)).complete();
        }

        if(event.getOption("registered_role", OptionMapping::getAsRole) != null) {
            registered = event.getOption("registered_role", OptionMapping::getAsRole);
        } else {
            registered = event.getGuild().createRole().setName("registered").complete();
        }

        if(event.getOption("registration", OptionMapping::getAsChannel) != null) {
            registrationChannel = event.getOption("registration", OptionMapping::getAsChannel).asTextChannel();
            registrationChannel.getManager().putRolePermissionOverride(registered.getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL));
        } else {
            registrationChannel = event.getGuild().createTextChannel("register").addRolePermissionOverride(registered.getIdLong(), null, EnumSet.of(Permission.VIEW_CHANNEL)).setSlowmode(5).complete();
        }

        if(event.getOption("substitute_limit", OptionMapping::getAsInt) != null) {
            substituteLimit = event.getOption("substitute_limit", OptionMapping::getAsInt);
        } else {
            substituteLimit = 0;
        }

        MySQLConfig.registrationConfig(event.getGuild(), logChannel, registrationChannel, registered, registerLimit, substituteLimit);

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Configuration inscription");
        eb.setColor(Color.BLUE);

        if(substituteLimit > 0) {
            eb.setDescription("Les inscriptions ont été configuré avec succès !\nSalon des logs : " + logChannel.getAsMention() + "\nSalon des inscriptions : " + registrationChannel.getAsMention() + "\nRôle des inscrits : " + registered.getAsMention() + "\nLimite d'inscription : " + registerLimit + "\nLimite de remplaçant : " + substituteLimit);
        } else {
            eb.setDescription("Les inscriptions ont été configuré avec succès !\nSalon des logs : " + logChannel.getAsMention() + "\nSalon des inscriptions : " + registrationChannel.getAsMention() + "\nRôle des inscrits : " + registered.getAsMention() + "\nLimite d'inscription : " + registerLimit + "\n\nAucune limite de remplaçants n'a été défini.");
        }

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
