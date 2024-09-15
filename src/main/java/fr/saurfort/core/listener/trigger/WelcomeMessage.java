package fr.saurfort.core.listener.trigger;

import fr.saurfort.core.database.query.welcome.MySQLWelcomeConfig;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.Objects;

public class WelcomeMessage {
    public WelcomeMessage(GuildMemberJoinEvent event) {
        TextChannel welcomeChannel = event.getGuild().getTextChannelById(MySQLWelcomeConfig.getWelcomeChannel(event.getGuild()));
        String message = MySQLWelcomeConfig.getWelcomeMessage(event.getGuild());

        System.out.println(message);

        String formattedMessage = message.replace("%guildname%", event.getGuild().getName());
        formattedMessage = formattedMessage.replace("%user%", Objects.requireNonNull(event.getUser().getGlobalName()));
        formattedMessage = formattedMessage.replace("%usermention%", event.getMember().getAsMention());

        welcomeChannel.sendMessageFormat(formattedMessage, event.getMember()).queue();
    }
}
