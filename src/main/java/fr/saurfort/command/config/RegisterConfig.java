package fr.saurfort.command.config;

import fr.saurfort.command.CommandBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Collections;
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

        if(event.getOption("registration", OptionMapping::getAsChannel) != null) {
            registrationChannel = event.getOption("registration", OptionMapping::getAsChannel).asTextChannel();
        } else {
            registrationChannel = event.getGuild().createTextChannel("register").setSlowmode(5).complete();
        }

        if(event.getOption("registered_role", OptionMapping::getAsRole) != null) {
            registered = event.getOption("registered_role", OptionMapping::getAsRole);
        } else {
            registered = event.getGuild().createRole().setName("registered").complete();
        }
        registrationChannel.getManager().putRolePermissionOverride(registered.getGuild().getId(), null, EnumSet.of(Permission.VIEW_CHANNEL)).complete();
    }
}
