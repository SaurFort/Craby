package fr.saurfort.core.command.moderation;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.message.MySQLLastMessage;
import fr.saurfort.core.utils.enums.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class LastMessage implements CommandBuilder {

    @Override
    public String getName() {
        return "lastmessage";
    }

    @Override
    public String getDescription() {
        return "Affiche le dernier message d'un joueur";
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_MANAGE;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(!event.getMember().hasPermission(getPermission())) {
            event.reply("Vous ne pouvez pas utilisez cette commande ! Dommage :wink:").setEphemeral(true).queue();
        } else {
            User target = event.getOption("user", OptionMapping::getAsUser);
            Member member = event.getOption("user", OptionMapping::getAsMember);

            if(target.isBot()) {
                event.reply("Bah, je vais pas espionner des machines, vous vous attendiez à quoi ? :joy:").setEphemeral(true).queue();
            } else {
                event.deferReply().queue();
                List<Object> result = MySQLLastMessage.getLastTimeUserMessage(member);

                if(result == null) {
                    event.getHook().editOriginal("Je ne sais pas si je suis un mauvais espion, mais je ne l'ai pas vu parlé ! :saluting_face:").queue();
                }

                String channelId = (String) result.get(0);
                String lastMessage = (String) result.get(1);
                String originalDateTime = (String) result.get(2);

                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH'h'mm 'le' dd/MM/yyyy");

                try {
                    LocalDateTime dateTime = LocalDateTime.parse(originalDateTime, inputFormatter);
                    String formattedDateTime = dateTime.format(outputFormatter);

                    EmbedBuilder eb = new EmbedBuilder();

                    eb.setTitle("Dernier message posté à " + formattedDateTime + " dans le channel <#" + channelId + ">");
                    eb.setColor(Color.RED);
                    eb.setDescription(lastMessage);
                    eb.setFooter(member.getNickname(), target.getAvatarUrl());

                    event.getHook().sendMessageEmbeds(eb.build()).queue();
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format: " + originalDateTime);
                    e.printStackTrace();

                    event.getHook().editOriginal("Vous m'excuserez, mais je suis atteint de Flemmingite aiguë :sleeping:").queue();
                }
            }
        }
    }
}
