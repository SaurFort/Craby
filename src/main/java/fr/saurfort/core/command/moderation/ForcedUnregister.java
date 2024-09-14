package fr.saurfort.core.command.moderation;

import fr.saurfort.core.command.CommandBuilder;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class ForcedUnregister implements CommandBuilder {
    @Override
    public String getName() {
        return "forcedunregister";
    }

    @Override
    public String getDescription() {
        return "Désinscrit un joueur de force";
    }

    @Override
    public Permission getPermission() {
        return Permission.MANAGE_ROLES;
    }

    @Override
    public boolean getGuildOnly() {
        return true;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        User target = event.getOption("user", OptionMapping::getAsUser);
        Member member = event.getOption("user", OptionMapping::getAsMember);

        if(!event.getMember().canInteract(member)) {
            event.reply("Étrangement, vous ne pouvez pas interagir avec ce membre :thinking:").setEphemeral(true).queue();
        } else {
            event.deferReply().queue();

            MySQLRegistration.unregister(event.getGuild(), member);
            member.modifyNickname(target.getGlobalName()).queue();
            member.getGuild().modifyMemberRoles(member).queue();

            target.openPrivateChannel()
                    .flatMap(channel -> channel.sendMessage("Vous avez été désinscrit par " + event.getMember().getAsMention()))
                    .queue();

            event.getHook().editOriginal("Le joueur " + target.getAsMention() + " a été désinscrit avec succès.").queue();
        }
    }
}
