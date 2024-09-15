package fr.saurfort.core.command.moderation;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import fr.saurfort.utils.enums.CommandCategory;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.awt.*;
import java.util.ArrayList;

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

            int registeredMember = MySQLRegistration.getRegisteredMember(event.getGuild());
            int substituteMember = MySQLRegistration.getSubstituteMember(event.getGuild());

            if(registeredMember > 0) {
                ArrayList membersList = MySQLRegistration.listRegisteredMember(event.getGuild());

                String memberDescription = "";

                for(int i = 0; i < membersList.size(); i++) {
                    memberDescription += membersList.get(i);
                }

                EmbedBuilder eb1 = new EmbedBuilder();

                eb1.setTitle("Liste des inscrits");
                eb1.setDescription(memberDescription);
                eb1.setColor(Color.GREEN);

                if(substituteMember > 0) {
                    ArrayList substituteList = MySQLRegistration.listSubstituteMember(event.getGuild());

                    EmbedBuilder eb2 = new EmbedBuilder();

                    String substituteDescription = "";

                    for(int i = 0; i < substituteList.size(); i++) {
                        substituteDescription += substituteList.get(i);
                    }

                    eb2.setTitle("Liste des remplaçants");
                    eb2.setDescription(substituteDescription);
                    eb2.setColor(Color.YELLOW);

                    event.getHook().sendMessage("Il y a actuellement " + registeredMember + " inscrits et " + substituteMember + " remplaçants.").addEmbeds(eb1.build(), eb2.build()).queue();
                } else {
                    event.getHook().sendMessage("Il y a actuellement " + membersList.size() + " inscrits").addEmbeds(eb1.build()).queue();
                }
            } else {
                if(MySQLRegisterConfig.configExist(event.getGuild())) {
                    event.getHook().sendMessage("Désolé, mais personne ne s'est inscrit pour le moment. :face_with_diagonal_mouth:").queue();
                } else {
                    event.getHook().sendMessage("Désolé, mais vous n'avez pas configuré les inscriptions, écrivez `/registerconfig` pour lancer la configuration !").queue();
                }
            }
        }
    }
}
