package fr.saurfort.command.moderation;

import fr.saurfort.database.Database;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.awt.*;

public class RegisteredList extends ListenerAdapter {
    public static final String NAME = "registeredlist";
    public static final String DESCRIPTION = "List des personnes inscrites";
    public static final Permission PERMISSION = Permission.MANAGE_ROLES;
    public static final boolean GUILD_ONLY = true;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals(NAME)) {
            if(!event.getMember().hasPermission(PERMISSION)) {
                event.reply("Vous n'avez pas les permissions pour voir ça, petit coquin :smirk:").setEphemeral(true).queue();
            } else {
                String membersList = Database.listRegisteredMember(event.getGuild());
                String substituteList = Database.listSubstituteMember(event.getGuild());

                event.deferReply().queue();

                if(!membersList.isEmpty()) {
                    EmbedBuilder eb1 = new EmbedBuilder();

                    eb1.setTitle("Liste des inscrits");
                    eb1.setDescription(membersList);
                    eb1.setColor(Color.GREEN);

                    if(!substituteList.isEmpty()) {
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
}
