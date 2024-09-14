package fr.saurfort.core.modal.action;

import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.awt.*;

public class UnregisterModalAction {
    public UnregisterModalAction(ModalInteractionEvent event) {
        event.deferReply(true).queue();

        String reason = event.getValue("reason").getAsString();
        TextChannel log = event.getGuild().getTextChannelById(MySQLRegisterConfig.getLogChannel(event.getGuild()));
        Button accept = Button.success("unregister:accept", "Accepter");
        Button refuse = Button.danger("unregister:refuse", "Refuser");

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Désinscription de " + event.getMember().getNickname());
        eb.setColor(Color.RED);

        if(!reason.isEmpty()) {
            eb.setDescription("**Raison :**\n```\n" + reason + "\n```");
        } else {
            eb.setDescription("**Aucune raison n'a été fourni !**");
        }

        log.sendMessageEmbeds(eb.build()).addActionRow(accept, refuse).queue(e -> {
            log.getHistory().retrievePast(1).queue(msgs -> {
                for(Message msg : msgs) {
                    MySQLRegistration.setWantUnregister(event.getGuild(), event.getMember(), msg);
                    event.getHook().sendMessage("Votre demande de désinscription a été envoyé au staff, vous recevrez un message lorsque le staff aura répondu.").queue();
                }
            });
        });

    }
}
