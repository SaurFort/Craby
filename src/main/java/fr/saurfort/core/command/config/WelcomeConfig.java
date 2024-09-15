package fr.saurfort.core.command.config;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.welcome.MySQLWelcomeConfig;
import fr.saurfort.utils.enums.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.buttons.Button;

import java.util.Collections;

public class WelcomeConfig implements CommandBuilder {
    @Override
    public String getName() {
        return "welcomeconfig";
    }

    @Override
    public String getDescription() {
        return "Configuration des messages de bienvenue et de départ";
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
        return CommandCategory.CONFIG;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        TextChannel welcomeChannel;

        /*if(event.getOption("welcome_channel", OptionMapping::getAsChannel) != null) {
            welcomeChannel = event.getOption("welcome_channel", OptionMapping::getAsChannel).asTextChannel();
        } else {
            welcomeChannel = event.getGuild().createTextChannel("welcome").complete();
        }*/

        //welcomeChannel.getManager().putPermissionOverride(event.getGuild().getPublicRole(), null, Collections.singleton(Permission.MESSAGE_SEND)).complete();

        //String welcomeMessage = "Bienvenue %usermention% sur le serveur **%guildname%** ! :wave:";

        //new MySQLWelcomeConfig(event.getGuild(), welcomeChannel, welcomeMessage);

        sendConfigMessage(event);
    }

    public static void sendConfigMessage(SlashCommandInteractionEvent event) {
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Welcome Config");
        //eb.setDescription("Configuration des messages de bienvenue\nLes messages de bienvenue seront posté dans le salon : " + welcomeChannel.getAsMention() + "\nLe format du message est :\n" + welcomeMessage);

        Button addJoinMessage = Button.primary("welcome:join_message", "Message de bienvenue");
        Button addLeftMessage = Button.primary("welcome:left_message", "Message d'au revoir");
        Button finishConfig = Button.success("welcome:finish", "Terminer la configuration");

        event.getHook().sendMessageEmbeds(eb.build()).addActionRow(addJoinMessage, addLeftMessage).addActionRow(finishConfig).queue();
    }
}
