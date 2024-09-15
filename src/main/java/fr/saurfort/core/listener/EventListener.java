package fr.saurfort.core.listener;

import fr.saurfort.Craby;
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
import fr.saurfort.core.component.button.TicketButtonAction;
import fr.saurfort.core.component.button.UnregisterAcceptButtonAction;
import fr.saurfort.core.component.button.UnregisterRefuseButtonAction;
import fr.saurfort.core.component.button.WelcomeMessageButtonAction;
import fr.saurfort.core.database.query.message.MySQLLastMessage;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.listener.trigger.RegisteredRejoin;
import fr.saurfort.core.listener.trigger.WelcomeMessage;
import fr.saurfort.core.modal.action.RegisterModalAction;
import fr.saurfort.core.modal.action.UnregisterModalAction;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class EventListener extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            // Config
            case "getconfig":
                new GetConfig().execute(event);
                break;
            case "registerconfig":
                new RegisterConfig().execute(event);
                break;
            case "ticketconfig":
                new TicketConfig().execute(event);
                break;
            case "welcomeconfig":
                new WelcomeConfig().execute(event);
                break;

            // Moderation
            case "clear":
                new Clear().execute(event);
                break;
            case "endtournament":
                new EndTournament().execute(event);
                break;
            case "forcedregister":
                new ForcedRegister().execute(event);
                break;
            case "forcedunregister":
                new ForcedUnregister().execute(event);
                break;
            case "getprofile":
                new GetProfile().execute(event);
                break;
            case "lastmessage":
                new LastMessage().execute(event);
                break;
            case "registeredlist":
                new RegisteredList().execute(event);
                break;
            case "starttournament":
                new StartTournament().execute(event);
                break;

            // Ticket
            case "addticketmember":
                new AddTicketMember().execute(event);
                break;
            case "closeticket":
                new CloseTicket().execute(event);
                break;
            case "removeticketmember":
                new RemoveTicketMember().execute(event);
                break;

            // Tournament
            case "register":
                new Register().execute(event);
                break;
            case "tournamentinfo":
                new TournamentInfo().execute(event);
                break;
            case "unregister":
                new Unregister().execute(event);
                break;

            // Utils
            case "help":
                new Help().execute(event);
                break;
            case "ping":
                new Ping().execute(event);
                break;
            default:
                System.out.printf("Unknown command %s used by %#s%n", event.getName(), event.getUser());
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.isFromType(ChannelType.PRIVATE)) {
            //event.getMessage().getChannel().sendMessage("Désolé, mais je n'aime pas beaucoup les messages privées des gens que je connais pas. :face_with_diagonal_mouth:").complete();
            //event.getChannel().sendMessage("Désolé, mais je n'aime pas beaucoup les messages privées des gens que je connais pas. :face_with_diagonal_mouth:").queue();
        } else {
            //System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(), event.getChannel().getName(), event.getMember().getEffectiveName(), event.getMessage().getContentDisplay());

            if(!event.getMessage().getAuthor().isBot()) {
                new MySQLLastMessage(event.getGuild(), event.getMember(), event.getChannel().asTextChannel(), event.getMessage());
            }
        }
    }

    @Override
    public void onMessageUpdate(@NotNull MessageUpdateEvent event) {
        if(event.isFromType(ChannelType.PRIVATE)) {

        } else {
            if(!event.getMessage().getAuthor().isBot()) {
                MySQLLastMessage.updateLastMessage(event.getGuild(), event.getMember(), event.getMessage());
            }
        }
    }

    @Override
    public void onModalInteraction(@NotNull ModalInteractionEvent event) {
        if(event.getModalId().equals("registration")) {
            new RegisterModalAction(event);
        } else if(event.getModalId().equals("unregister")) {
            new UnregisterModalAction(event);
        }
    }

    @Override
    public void onButtonInteraction(@NotNull ButtonInteractionEvent event) {
        if(event.getComponentId().equals("ticket:open")) {
            new TicketButtonAction(event);
        } else if(event.getComponentId().equals("welcome:modify_message")) {
            new WelcomeMessageButtonAction(event);
        } else if(event.getComponentId().equals("unregister:accept")) {
            new UnregisterAcceptButtonAction(event);
        } else if(event.getComponentId().equals("unregister:refuse")) {
            new UnregisterRefuseButtonAction(event);
        }
    }

    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent event) {
        new WelcomeMessage(event);
        if(MySQLRegisterConfig.configExist(event.getGuild())) {
            new RegisteredRejoin(event);
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        Craby.ready(event.getJDA());
    }
}
