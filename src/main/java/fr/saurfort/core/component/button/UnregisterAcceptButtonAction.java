package fr.saurfort.core.component.button;

import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.core.logger.LoggerBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public class UnregisterAcceptButtonAction {
    public UnregisterAcceptButtonAction(ButtonInteractionEvent event) {
        LoggerBuilder logger = new LoggerBuilder() {};
        Member member = event.getGuild().retrieveMemberById(MySQLRegistration.getDiscordId(event.getGuild(), event.getMessage().getId())).complete();

        event.deferEdit().queue();
        event.getHook().sendMessage("La désinscription de " + member.getAsMention() + " a été accepté par " + event.getMember().getAsMention() + " !").queue();
        event.getHook().deleteOriginal().queue();

        member.modifyNickname(member.getUser().getGlobalName()).queue();
        member.getGuild().modifyMemberRoles(member).queue();

        logger.sendPrivateMessage(member.getUser(), "Votre désinscription a été accepté par le staff, c'est dommage de nous quitter si vite. :face_with_diagonal_mouth:");
    }
}
