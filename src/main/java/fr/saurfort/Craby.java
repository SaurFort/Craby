package fr.saurfort;

import fr.saurfort.command.CommandLister;
import fr.saurfort.config.ConfigLoader;
import fr.saurfort.database.init.MySQLDatabase;
import fr.saurfort.listener.EventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;

public class Craby {
    public static void main(String[] args) {
        ConfigLoader config = new ConfigLoader();

        JDA jda = JDABuilder
                .createLight(Config.TOKEN)
                .disableIntents(Arrays.asList(GatewayIntent.values()))
                .addEventListeners(new EventListener())
                .setActivity(Activity.playing("Clash Royale"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .build();

        new MySQLDatabase(config.getProperty("db.username"), config.getProperty("db.password"), config.getProperty("db.address"), config.getProperty("db.name"));

        new CommandLister(jda);
    }
}