package fr.saurfort.command;

import fr.saurfort.command.config.RegisterConfig;
import fr.saurfort.command.config.TicketConfig;
import fr.saurfort.command.moderation.*;
import fr.saurfort.command.ticket.AddTicketMember;
import fr.saurfort.command.ticket.CloseTicket;
import fr.saurfort.command.ticket.RemoveTicketMember;
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
    TicketConfig ticketConfig = new TicketConfig();

    // Moderation
    Clear clear = new Clear();
    ForcedRegister forcedRegister = new ForcedRegister();
    ForcedUnregister forcedUnregister = new ForcedUnregister();
    LastMessage lastMessage = new LastMessage();
    RegisteredList registeredList = new RegisteredList();

    // Ticket
    AddTicketMember addTicketMember = new AddTicketMember();
    CloseTicket closeTicket = new CloseTicket();
    RemoveTicketMember removeTicketMember = new RemoveTicketMember();

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
                Commands.slash(ticketConfig.getName(), ticketConfig.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(ticketConfig.getPermission()))
                        .setGuildOnly(ticketConfig.getGuildOnly())
                        .addOption(OptionType.CHANNEL, "ticket_category", "Catégorie des tickets")
                        .addOption(OptionType.CHANNEL, "ticket_log", "Salon de log des tickets")
                        .addOption(OptionType.ROLE, "support_role", "Role du support"),

                // Ticket
                Commands.slash(addTicketMember.getName(), addTicketMember.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(addTicketMember.getPermission()))
                        .setGuildOnly(addTicketMember.getGuildOnly())
                        .addOption(OptionType.USER, "member", "Membre à ajouter au ticket", true),
                Commands.slash(closeTicket.getName(), closeTicket.getDescription())
                        .setGuildOnly(closeTicket.getGuildOnly())
                        .addOption(OptionType.STRING, "reason", "Raison de la fermeture du ticket", true),
                Commands.slash(removeTicketMember.getName(), removeTicketMember.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(removeTicketMember.getPermission()))
                        .setGuildOnly(removeTicketMember.getGuildOnly())
                        .addOption(OptionType.USER, "member", "Membre à ajouter au ticket", true),

                // Utils
                Commands.slash(help.getName(), help.getDescription()),
                Commands.slash(ping.getName(), ping.getDescription()),

                // Modal
                Commands.slash(register.getName(), register.getDescription())
                        .setGuildOnly(register.getGuildOnly()),

                // Moderation
                Commands.slash(clear.getName(), clear.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(clear.getPermission()))
                        .setGuildOnly(clear.getGuildOnly())
                        .addOption(OptionType.INTEGER, "amount", "Nombre de message à supprimé", true),
                Commands.slash(forcedRegister.getName(), forcedRegister.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(forcedRegister.getPermission()))
                        .setGuildOnly(forcedUnregister.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Membre à inscrire", true)
                        .addOption(OptionType.STRING, "id", "ID du joueur à inscrire", true),
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
