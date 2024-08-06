package fr.saurfort.command;

import fr.saurfort.command.moderation.LastMessage;
import fr.saurfort.command.moderation.RegisteredList;
import fr.saurfort.command.moderation.Unregister;
import fr.saurfort.command.utils.Help;
import fr.saurfort.command.utils.Ping;
import fr.saurfort.command.modal.RegistrationModal;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandLister {
    public CommandLister(JDA jda) {
        jda.updateCommands().addCommands(
                // Utils
                Commands.slash(Help.NAME, Help.DESCRIPTION),
                Commands.slash(Ping.NAME, Ping.DESCRIPTION),

                // Modal
                Commands.slash(RegistrationModal.NAME, RegistrationModal.DESCRIPTION)
                        .setGuildOnly(RegistrationModal.GUILD_ONLY),

                // Moderation
                Commands.slash(LastMessage.NAME, LastMessage.DESCRIPTION)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(LastMessage.PERMISSION))
                        .setGuildOnly(LastMessage.GUILD_ONLY)
                        .addOption(OptionType.USER, "user", "Utilisateur à vérifier", true),
                Commands.slash(RegisteredList.NAME, RegisteredList.DESCRIPTION)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(RegisteredList.PERMISSION))
                        .setGuildOnly(RegisteredList.GUILD_ONLY),
                Commands.slash(Unregister.NAME, Unregister.DESCRIPTION)
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Unregister.PERMISSION))
                        .setGuildOnly(Unregister.GUILD_ONLY)
                        .addOption(OptionType.USER, "user", "Utilisateur à désinscrire", true)
        ).queue();
    }
}
