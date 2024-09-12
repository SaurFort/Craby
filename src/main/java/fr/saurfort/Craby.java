package fr.saurfort;

import fr.saurfort.command.CommandLister;
import fr.saurfort.config.ConfigLoader;
import fr.saurfort.database.init.MySQLDatabase;
import fr.saurfort.listener.EventListener;
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
    public static void main(String[] args) {
        ConfigLoader config = new ConfigLoader();

        JDA jda = JDABuilder
                .createLight(config.getProperty("bot.token"))
                .enableIntents(Arrays.asList(GatewayIntent.values()))
                .addEventListeners(new EventListener())
                .setActivity(Activity.playing("Clash Royale"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setAutoReconnect(true)
                .build();


        //System.out.println(jCrApi.listApis());

        new MySQLDatabase(config.getProperty("db.username"), config.getProperty("db.password"), config.getProperty("db.address"), config.getProperty("db.port"), config.getProperty("db.name"));

        new CommandLister(jda);
    }

    public static JCrApi getJCrApi() {
        ConfigLoader config = new ConfigLoader();
        Connector connector = new StandardConnector();
        JCrApi jCrApi = new JCrApi("https://api.clashroyale.com/v1", config.getProperty("cr.token"), connector);

        return jCrApi;
    }
}