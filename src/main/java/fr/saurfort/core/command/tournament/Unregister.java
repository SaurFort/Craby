package fr.saurfort.core.command.tournament;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.core.modal.creator.UnregisterModalCreator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Unregister implements CommandBuilder {
    @Override
    public String getName() {
        return "unregister";
    }

    @Override
    public String getDescription() {
        return "Permet de se désinscrire";
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(MySQLRegistration.haveBeenRegistered(event.getGuild(), event.getMember())) {
            event.replyModal(UnregisterModalCreator.unregisterModalCreator(event.getGuild(), event.getMember())).queue();
        } else {
            event.reply("Vous n'êtes pas inscrit, donc vous ne pouvez pas vous désinscrire. :face_with_diagonal_mouth:").setEphemeral(true).queue();
        }
    }
}
