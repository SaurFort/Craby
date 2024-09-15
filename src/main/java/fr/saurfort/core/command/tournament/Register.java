package fr.saurfort.core.command.tournament;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.modal.creator.RegisterModalCreator;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.utils.enums.CommandCategory;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
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
    public CommandCategory getCategory() {
        return CommandCategory.TOURNAMENT;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Modal modal = RegisterModalCreator.registerModalCreator();
        TextChannel registerChannel = event.getGuild().getTextChannelById(MySQLRegisterConfig.getRegisterChannel(event.getGuild()));


        if(event.getChannelIdLong() != registerChannel.getIdLong()) {
            event.reply("Les inscriptions ne sont pas prise en charge dans ce channel, veuillez allez dans le channel " + registerChannel.getAsMention() + " !").setEphemeral(true).queue();
        } else if(MySQLRegistration.canRegister(event.getGuild()).equals("max")) {
            event.reply("Les inscriptions sont déjà complètes, désolé, ça sera pour une prochaine fois peut-être :wink:").setEphemeral(true).queue();
        } else if(MySQLRegistration.canRegister(event.getGuild()).equals("error")) {
            event.reply("Erreur lors de votre inscription, veuillez réessayer !").setEphemeral(true).queue();
        } else {
            event.replyModal(modal).queue();
        }
    }
}
