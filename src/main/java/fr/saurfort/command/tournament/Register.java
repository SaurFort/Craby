package fr.saurfort.command.tournament;

import fr.saurfort.command.CommandBuilder;
import fr.saurfort.database.query.MySQLRegistration;
import fr.saurfort.modal.creator.RegisterModalCreator;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class Register implements CommandBuilder {
    @Override
    public String getName() {
        return "register";
    }

    @Override
    public String getDescription() {
        return "Permet de s'inscrire dans le tournoi";
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
        Modal modal = RegisterModalCreator.registerModalCreator();

        if(event.getChannelIdLong() != 1269723436364857404L) {
            event.reply("Les inscriptions ne sont pas prise en charge dans ce channel, veuillez allez dans le channel <#1269723436364857404> !").setEphemeral(true).queue();
        } else if(MySQLRegistration.canRegister(event.getGuild()).equals("max")) {
            event.reply("Les inscriptions sont déjà complètes, désolé, ça sera pour une prochaine fois peut-être :wink:").setEphemeral(true).queue();
        } else if(MySQLRegistration.canRegister(event.getGuild()).equals("error")) {
            event.reply("Erreur lors de votre inscription, veuillez réessayer !").setEphemeral(true).queue();
        } else {
            event.replyModal(modal).queue();
        }
    }
}
