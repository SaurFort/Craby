package fr.saurfort.logger;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

/**
 * This class permit you tu build loggers for any commands.
 *
 * @author SaurFort
 */
public interface LoggerBuilder {
    /**
     * This function write logs in console.
     *
     * @param text Text to write in bot console.
     */
    default void makeConsoleLog(Guild guild, String text) {
        System.out.println("[" + guild.getName() + "]: " + text);
    }

    /**
     * This function send a message in a specific channel.
     *
     * @param channel Channel where the message going to be sent.
     * @param message Content of the message.
     */
    default void sendMessage(TextChannel channel, String message) {
        channel.sendMessage(message).queue();
    }

    /**
     * This function send an embed message in a specific channel.
     *
     * @param channel Channel where the message going to be sent.
     * @param embed Embed to post.
     */
    default void sendEmbedMessage(TextChannel channel, EmbedBuilder embed) {
        channel.sendMessageEmbeds(embed.build()).queue();
    }

    /**
     * This function send a private message to a specific user.
     *
     * @param user User to receive message.
     * @param message Content of the message.
     */
    default void sendPrivateMessage(User user, String message) {
        user.openPrivateChannel()
                .flatMap(mp -> mp.sendMessage(message))
                .queue();
    }

    /**
     * This function send a private embed message to a specific user.
     *
     * @param user User to receive the embed.
     * @param embed Embed to post.
     */
    default void sendPrivateEmbed(User user, EmbedBuilder embed) {
        user.openPrivateChannel()
                .flatMap(mp -> mp.sendMessageEmbeds(embed.build()))
                .queue();
    }
}
