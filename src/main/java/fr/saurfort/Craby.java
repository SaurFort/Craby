package fr.saurfort;

import fr.saurfort.command.CommandLister;
import fr.saurfort.database.Database;
import fr.saurfort.listener.CommandListener;
import fr.saurfort.listener.MessageListener;
import fr.saurfort.listener.ModalListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Craby {
    public Craby() throws Exception {
        JDA jda = JDABuilder
                .createLight(Config.TOKEN, GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_MEMBERS)
                .addEventListeners(new MessageListener())
                .addEventListeners(new CommandListener())
                .addEventListeners(new ModalListener())
                .setActivity(Activity.playing("Clash Royale"))
                .build();

        new CommandLister(jda);

        if(!Database.checkConnection()) {
            System.out.println("Database is not connected");
            System.exit(1);
        } else {
            System.out.println("Database is connected");
            Database.initializeDatabase();
        }
    }

    public static void main(String[] args) throws Exception {
        new Craby();
    }
}