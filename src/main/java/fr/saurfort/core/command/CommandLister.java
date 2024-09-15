package fr.saurfort.core.command;

import fr.saurfort.core.command.config.GetConfig;
import fr.saurfort.core.command.config.RegisterConfig;
import fr.saurfort.core.command.config.TicketConfig;
import fr.saurfort.core.command.config.WelcomeConfig;
import fr.saurfort.core.command.moderation.*;
import fr.saurfort.core.command.ticket.AddTicketMember;
import fr.saurfort.core.command.ticket.CloseTicket;
import fr.saurfort.core.command.ticket.RemoveTicketMember;
import fr.saurfort.core.command.tournament.Register;
import fr.saurfort.core.command.tournament.TournamentInfo;
import fr.saurfort.core.command.tournament.Unregister;
import fr.saurfort.core.command.utils.Help;
import fr.saurfort.core.command.utils.Ping;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

public class CommandLister {
    // Config
    GetConfig getConfig = new GetConfig();
    RegisterConfig registerConfig = new RegisterConfig();
    TicketConfig ticketConfig = new TicketConfig();
    WelcomeConfig welcomeConfig = new WelcomeConfig();

    // Moderation
    Clear clear = new Clear();
    EndTournament endTournament = new EndTournament();
    ForcedRegister forcedRegister = new ForcedRegister();
    ForcedUnregister forcedUnregister = new ForcedUnregister();
    GetProfile getProfile = new GetProfile();
    LastMessage lastMessage = new LastMessage();
    RegisteredList registeredList = new RegisteredList();
    StartTournament startTournament = new StartTournament();

    // Ticket
    AddTicketMember addTicketMember = new AddTicketMember();
    CloseTicket closeTicket = new CloseTicket();
    RemoveTicketMember removeTicketMember = new RemoveTicketMember();

    // Tournament
    Register register = new Register();
    TournamentInfo tournamentInfo = new TournamentInfo();
    Unregister unregister = new Unregister();

    // Utils
    Help help = new Help();
    Ping ping = new Ping();

    public CommandLister(JDA jda) {
        jda.updateCommands().addCommands(
                // Config
                Commands.slash(getConfig.getName(), getConfig.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(getConfig.getPermission()))
                        .setGuildOnly(getConfig.getGuildOnly()),
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
                Commands.slash(welcomeConfig.getName(), welcomeConfig.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(welcomeConfig.getPermission()))
                        .setGuildOnly(welcomeConfig.getGuildOnly())
                        .addOption(OptionType.CHANNEL, "welcome_channel", "Salon de bienvenue"),

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

                // Tournament
                Commands.slash(register.getName(), register.getDescription())
                        .setGuildOnly(register.getGuildOnly()),
                Commands.slash(tournamentInfo.getName(), tournamentInfo.getDescription())
                        .setGuildOnly(tournamentInfo.getGuildOnly()),
                Commands.slash(unregister.getName(), unregister.getDescription())
                        .setGuildOnly(unregister.getGuildOnly()),

                // Moderation
                Commands.slash(clear.getName(), clear.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(clear.getPermission()))
                        .setGuildOnly(clear.getGuildOnly())
                        .addOption(OptionType.INTEGER, "amount", "Nombre de message à supprimé", true),
                Commands.slash(endTournament.getName(), endTournament.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(endTournament.getPermission()))
                        .setGuildOnly(endTournament.getGuildOnly()),
                Commands.slash(forcedRegister.getName(), forcedRegister.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(forcedRegister.getPermission()))
                        .setGuildOnly(forcedUnregister.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Membre à inscrire", true)
                        .addOption(OptionType.STRING, "id", "ID du joueur à inscrire", true),
                Commands.slash(forcedUnregister.getName(), forcedUnregister.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(forcedUnregister.getPermission()))
                        .setGuildOnly(forcedUnregister.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Utilisateur à désinscrire", true),
                Commands.slash(getProfile.getName(), getProfile.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(getProfile.getPermission()))
                        .setGuildOnly(getProfile.getGuildOnly())
                        .addOption(OptionType.MENTIONABLE, "player", "Joueur dont vous souhaitez obtenir le profile", true),
                Commands.slash(lastMessage.getName(), lastMessage.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(lastMessage.getPermission()))
                        .setGuildOnly(lastMessage.getGuildOnly())
                        .addOption(OptionType.USER, "user", "Utilisateur à vérifier", true),
                Commands.slash(registeredList.getName(), registeredList.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(registeredList.getPermission()))
                        .setGuildOnly(registeredList.getGuildOnly()),
                Commands.slash(startTournament.getName(), startTournament.getDescription())
                        .setDefaultPermissions(DefaultMemberPermissions.enabledFor(startTournament.getPermission()))
                        .setGuildOnly(startTournament.getGuildOnly())
                        .addOption(OptionType.STRING, "name", "Nom du tournoi", true)
                        .addOption(OptionType.BOOLEAN, "forced", "Force le début du tournoi")
        ).queue(success -> {
            System.out.println("Les commandes slash ont été enregistrées avec succès.");
        }, failure -> {
            System.err.println("Erreur lors de l'enregistrement des commandes slash: " + failure.getMessage());
        });
    }
}
