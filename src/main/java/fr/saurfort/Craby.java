package fr.saurfort;

import fr.saurfort.command.CommandLister;
import fr.saurfort.database.Database;
import fr.saurfort.listener.MessageListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.Arrays;

public class Craby {
    public static void main(String[] args) {
        JDA jda = JDABuilder
                .createLight(Config.TOKEN)
                .disableIntents(Arrays.asList(GatewayIntent.values()))
                .addEventListeners(new MessageListener())
                .setActivity(Activity.playing("Clash Royale"))
                .build();

        new CommandLister(jda);

        if(!Database.checkConnection()) {
            System.out.println("Database creation in progress");
            Database.initializeDatabaseFile();
        } else {
            System.out.println("Database is connected");
            Database.initializeDatabase();
        }
    }
}