package fr.saurfort.core.command.moderation;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.core.utils.enums.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;

public class RegisteredList implements CommandBuilder {
    @Override
    public String getName() {
        return "registeredlist";
    }

    @Override
    public String getDescription() {
        return "Affiche la liste des joueurs inscrit";
    }

    @Override
    public Permission getPermission() {
        return Permission.MESSAGE_MANAGE;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.MODERATION;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(!event.getMember().hasPermission(getPermission())) {
            event.reply("Vous n'avez pas les permissions pour voir ça, petit coquin :smirk:").setEphemeral(true).queue();
        } else {
            event.deferReply().queue();

            String membersList = MySQLRegistration.listRegisteredMember(event.getGuild());
            String substituteList = MySQLRegistration.listSubstituteMember(event.getGuild());

            if(!membersList.isEmpty()) {
                int registeredMember = MySQLRegistration.getRegisteredMember(event.getGuild());

                event.getHook().sendMessage("Il y a actuellement " + registeredMember + " inscrits").queue();

                EmbedBuilder eb1 = new EmbedBuilder();

                eb1.setTitle("Liste des inscrits");
                eb1.setDescription(membersList);
                eb1.setColor(Color.GREEN);

                if(!substituteList.isEmpty()) {
                    int substituteMember = MySQLRegistration.getSubstituteMember(event.getGuild());

                    event.getHook().sendMessage("Il y a actuellement " + substituteMember + " remplaçant").queue();

                    EmbedBuilder eb2 = new EmbedBuilder();

                    eb2.setTitle("Liste des remplaçants");
                    eb2.setDescription(substituteList);
                    eb2.setColor(Color.YELLOW);

                    event.getHook().sendMessageEmbeds(eb1.build(), eb2.build()).queue();
                } else {
                    event.getHook().sendMessageEmbeds(eb1.build()).queue();
                }
            } else {
                event.getHook().sendMessage("Désolé, mais personne ne s'est inscrit pour le moment :face_with_diagonal_mouth:").queue();
            }
        }
    }
}
