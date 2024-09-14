package fr.saurfort.core.logger;

import net.dv8tion.jda.api.entities.Guild;

public class TicketLogger implements LoggerBuilder {
    @Override
    public void makeConsoleLog(Guild guild, String logMessage) {
        String log = "[" + guild.getName() + "]" + "[TicketLog]: " + logMessage;

        System.out.println(log);
    }
}
