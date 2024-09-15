package fr.saurfort;

import fr.saurfort.core.command.CommandLister;
import fr.saurfort.core.config.ConfigLoader;
import fr.saurfort.core.database.init.MySQLDatabase;
import fr.saurfort.core.listener.EventListener;
import jcrapi2.JCrApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import supercell.api.wrapper.essentials.connector.Connector;
import supercell.api.wrapper.essentials.connector.StandardConnector;

import java.util.Arrays;

public class Craby {
    private static ConfigLoader config = new ConfigLoader();

    public static void main(String[] args) {
        String crToken = config.getProperty("cr.token");

        if(crToken.isEmpty() || crToken.equals("CR_TOKEN")) {
            System.out.println("Clash Royale API token has not been set!");
            System.exit(1);
        }

        JDA jda = JDABuilder
                .createLight(config.getProperty("bot.token"), Arrays.asList(GatewayIntent.values()))
                .addEventListeners(new EventListener())
                .setActivity(Activity.playing("Clash Royale"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setAutoReconnect(true)
                .build();

        new MySQLDatabase(config.getProperty("db.username"), config.getProperty("db.password"), config.getProperty("db.address"), config.getProperty("db.port"), config.getProperty("db.name"));

        new CommandLister(jda);
    }

    public static JCrApi getJCrApi() {
        Connector connector = new StandardConnector();
        JCrApi jCrApi = new JCrApi("https://api.clashroyale.com/v1", config.getProperty("cr.token"), connector);

        return jCrApi;
    }

    public static ConfigLoader getConfig() {
        return config;
    }
}