package fr.saurfort.core.command.config;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.ticket.MySQLTicketConfig;
import fr.saurfort.core.database.query.welcome.MySQLWelcomeConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class GetConfig implements CommandBuilder {
    @Override
    public String getName() {
        return "getconfig";
    }

    @Override
    public String getDescription() {
        return "Affiche la liste des configurations actuel du bot";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_CHANNEL;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.deferReply().queue();

        String config = "";

        if(MySQLRegisterConfig.configExist(event.getGuild())) {
            config += ":white_check_mark:";
        } else {
            config += ":x:";
        }
        config += " Configuration des inscriptions\n";

        if(MySQLTicketConfig.getTicketConfig(event.getGuild()) != null) {
            config += ":white_check_mark:";
        } else {
            config += ":x:";
        }
        config += " Configuration des tickets\n";

        if(MySQLWelcomeConfig.configExist(event.getGuild())) {
            config += ":white_check_mark:";
        } else {
            config += ":x:";
        }
        config += " Configuration des messages de bienvenue";

        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("Liste des configurations");
        eb.setDescription(config);
        eb.setColor(Color.orange);

        event.getHook().sendMessageEmbeds(eb.build()).queue();
    }
}
