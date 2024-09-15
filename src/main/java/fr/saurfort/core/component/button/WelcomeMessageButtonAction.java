package fr.saurfort.core.component.button;

import fr.saurfort.core.database.query.welcome.MySQLWelcomeConfig;
import fr.saurfort.utils.enums.TimeConverter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

import java.util.concurrent.CompletableFuture;

public class WelcomeMessageButtonAction {
    public WelcomeMessageButtonAction(ButtonInteractionEvent event) {
        event.deferEdit().queue();
        //event.getHook().deleteOriginal().queue();
        event.getHook().editOriginalEmbeds().queue();
        event.getHook().editOriginal("Écrivez votre message de bienvenue (*Pour information, vous avec 20 secondes*) !\nUtiliser les codes suivants pour le personnalisez : `%guildname%`: Nom du serveur discord, `%user%`: Nom de l'utilisateur, `%usermention%`: Mention de l'utilisateur").queue();
        event.getHook().editOriginalComponents().queue();

        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(20 * TimeConverter.SECONDS.getMultiplication());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            event.getChannel().getHistory().retrievePast(1).queue(messages -> {
                Message message = messages.getFirst();

                if(message.getAuthor().getId().equals(event.getUser().getId())) {
                    String welcomeMessage = message.getContentRaw();

                    MySQLWelcomeConfig.setWelcomeMessage(event.getGuild(), welcomeMessage);

                    message.delete().queue();

                    EmbedBuilder eb = new EmbedBuilder();

                    eb.setTitle("Welcome Config");
                    eb.setDescription("Le nouveau message de bienvenue à été configuré !\n\n" + welcomeMessage);

                    event.getHook().editOriginalEmbeds(eb.build()).queue();
                    event.getHook().editOriginal("").queue();
                } else {
                    event.getHook().editOriginal("Le message de bienvenue n'a pas été modifié !").queue();
                }
            });
        });
    }
}
