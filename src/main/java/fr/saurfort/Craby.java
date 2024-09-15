package fr.saurfort;

import fr.saurfort.core.command.CommandLister;
import fr.saurfort.config.ConfigLoader;
import fr.saurfort.core.database.init.MySQLDatabase;
import fr.saurfort.core.listener.EventListener;
import fr.saurfort.migrator.MigratorController;
import jcrapi2.JCrApi;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
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

        start();

        /*JDA jda = JDABuilder
                .createLight(config.getProperty("bot.token"), Arrays.asList(GatewayIntent.values()))
                .addEventListeners(new EventListener())
                .setActivity(Activity.playing("Clash Royale"))
                .setStatus(OnlineStatus.DO_NOT_DISTURB)
                .setAutoReconnect(true)
                .build();*/
    }

    public static JCrApi getJCrApi() {
        Connector connector = new StandardConnector();
        JCrApi jCrApi = new JCrApi("https://api.clashroyale.com/v1", config.getProperty("cr.token"), connector);

        return jCrApi;
    }

    private static void start() {
        if(checkVersion()) {
            DefaultShardManagerBuilder builder = DefaultShardManagerBuilder.createDefault(config.getProperty("bot.token"));
            builder.enableIntents(Arrays.asList(GatewayIntent.values()));
            builder.setMemberCachePolicy(MemberCachePolicy.NONE);
            builder.disableCache(Arrays.asList(CacheFlag.values()));
            builder.addEventListeners(new EventListener());
            builder.setStatus(OnlineStatus.DO_NOT_DISTURB);
            builder.setActivity(Activity.playing("Clash Royale"));
            builder.build();
        } else {
            new MigratorController(config.getProperty("bot.version"));
        }
    }

    public static void ready(JDA jda) {
        new MySQLDatabase(config.getProperty("db.username"), config.getProperty("db.password"), config.getProperty("db.address"), config.getProperty("db.port"), config.getProperty("db.name"));
        new CommandLister(jda);
        System.out.println("Craby v" + config.getProperty("bot.version") + " est prêt à l'action !");
    }

    /**
     * This function check if the config file is at the final version
     *
     * @return true if the config is at final version, false if the config isn't at the final version
     */
    public static boolean checkVersion() {
        return config.getProperty("bot.version").equals(config.getDefaultProperty("bot.version"));
    }

    public static ConfigLoader getConfig() {
        return config;
    }
}