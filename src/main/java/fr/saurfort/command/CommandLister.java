package fr.saurfort.command;

import fr.saurfort.command.config.RegisterConfig;
import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.ForcedUnregister;
import fr.saurfort.command.tournament.Register;
import fr.saurfort.command.utils.Help;
import fr.saurfort.command.utils.Ping;
import fr.saurfort.modal.creator.RegisterModalCreator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandLister {
    // Config
    RegisterConfig registerConfig = new RegisterConfig();

    // Moderation
    ForcedUnregister forcedUnregister = new ForcedUnregister();
    LastMessage lastMessage = new LastMessage();
    RegisteredList registeredList = new RegisteredList();

    // Tournament
    Register register = new Register();

    // Utils
    Help help = new Help();
    Ping ping = new Ping();

    public CommandLister(JDA jda) {
        jda.updateCommands().addCommands(
                // Config
                Commands.slash(registerConfig.getName(), registerConfig.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(registerConfig.getPermission()))
                        .setGuildOnly(registerConfig.getGuildOnly())
                        .addOption(OptionType.INTEGER, "register_limit", "Limite d'inscription", true)
                        .addOption(OptionType.CHANNEL, "log", "Salon des logs")
                        .addOption(OptionType.CHANNEL, "registration", "Salon d'inscription")
                        .addOption(OptionType.ROLE, "registered_role", "Role des inscrits")
                        .addOption(OptionType.INTEGER, "substitute_limit", "Limite de remplaçant"),

                // Utils
                Commands.slash(help.getName(), help.getDescription()),
                Commands.slash(ping.getName(), ping.getDescription()),

                // Modal
                Commands.slash(register.getName(), register.getDescription())
                        .setGuildOnly(register.getGuildOnly()),

                // Moderation
                Commands.slash(lastMessage.getName(), lastMessage.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(lastMessage.getPermission()))
                        .setGuildOnly(lastMessage.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Utilisateur à vérifier", true),
                Commands.slash(registeredList.getName(), registeredList.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(registeredList.getPermission()))
                        .setGuildOnly(registeredList.getGuildOnly()),
                Commands.slash(forcedUnregister.getName(), forcedUnregister.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(forcedUnregister.getPermission()))
                        .setGuildOnly(forcedUnregister.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Utilisateur à désinscrire", true)
        ).queue();
    }
}
