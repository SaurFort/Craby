package fr.saurfort.command;

import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.ForcedUnregister;
import fr.saurfort.command.utils.Help;
import fr.saurfort.command.utils.Ping;
import fr.saurfort.modal.creator.RegisterModalCreator;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandLister {
    // Moderation
    LastMessage lastMessage = new LastMessage();
    RegisteredList registeredList = new RegisteredList();

    // Utils
    Ping ping = new Ping();

    public CommandLister(JDA jda) {
        jda.updateCommands().addCommands(
                // Utils
                Commands.slash(Help.NAME, Help.DESCRIPTION),
                Commands.slash(ping.getName(), ping.getDescription()),

                // Modal
                Commands.slash(RegisterModalCreator.NAME, RegisterModalCreator.DESCRIPTION)
                        .setGuildOnly(RegisterModalCreator.GUILD_ONLY),

                // Moderation
                Commands.slash(lastMessage.getName(), lastMessage.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(lastMessage.getPermission()))
                        .setGuildOnly(lastMessage.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Utilisateur à vérifier", true),
                Commands.slash(registeredList.getName(), registeredList.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(registeredList.getPermission()))
                        .setGuildOnly(registeredList.getGuildOnly()),
                Commands.slash(ForcedUnregister.NAME, ForcedUnregister.DESCRIPTION)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(ForcedUnregister.PERMISSION))
                        .setGuildOnly(ForcedUnregister.GUILD_ONLY)
                        .addOption(OptionType.USER, "user", "Utilisateur à désinscrire", true)
        ).queue();
    }
}
