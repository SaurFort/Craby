package fr.saurfort.command.moderation;

import fr.saurfort.database.Database;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class Unregister extends ListenerAdapter {
    public static final String NAME = "unregister";
    public static final String DESCRIPTION = "Désinscrire un joueur";
    public static final Permission PERMISSION = Permission.MANAGE_ROLES;
    public static final boolean GUILD_ONLY = true;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals(NAME)) {
            if(!event.getMember().hasPermission(PERMISSION)) {
                event.reply("Vous ne pouvez pas désinscrire des gens sans avoir les permissions voyons, c'est pas gentil :sad:").setEphemeral(true).queue();
            } else {
                User target = event.getOption("user", OptionMapping::getAsUser);
                Member member = event.getOption("user", OptionMapping::getAsMember);
                Role role = event.getGuild().getRoleById(1270066037131575408L);

                if(!event.getMember().canInteract(member)) {
                    event.reply("Étrangement, vous ne pouvez pas interagir avec ce membre :thinking:").setEphemeral(true).queue();
                } else {
                    event.deferReply().queue();

                    Database.unregisterAMember(member);
                    member.modifyNickname(target.getGlobalName()).queue();
                    member.getGuild().modifyMemberRoles(member).queue();

                    target.openPrivateChannel()
                            .flatMap(channel -> channel.sendMessage("Vous avez été désinscrit par " + event.getMember().getAsMention()))
                            .queue();

                    event.getHook().editOriginal("Le joueur " + target.getAsMention() + " a été désinscrit avec succès.").queue();
                }
            }
        }
    }
}
