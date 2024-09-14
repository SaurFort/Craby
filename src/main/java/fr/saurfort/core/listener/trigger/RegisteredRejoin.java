package fr.saurfort.core.listener.trigger;

import fr.saurfort.Craby;
import fr.saurfort.core.database.query.register.MySQLRegisterConfig;
import fr.saurfort.core.database.query.register.MySQLRegistration;
import jcrapi2.JCrApi;
import jcrapi2.api.intern.players.PlayerApi;
import jcrapi2.api.intern.players.info.PlayerRequest;
import jcrapi2.api.intern.players.info.PlayerResponse;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;

import java.util.concurrent.ExecutionException;

public class RegisteredRejoin {
    public RegisteredRejoin(GuildMemberJoinEvent event) {
        if(MySQLRegistration.haveBeenRegistered(event.getGuild(), event.getMember())) {
            Member member = event.getMember();

            JCrApi jCrApi = Craby.getJCrApi();
            PlayerApi api = jCrApi.getApi(PlayerApi.class);
            PlayerResponse response = null;

            try {
                response = api.findByTag(PlayerRequest.builder(MySQLRegistration.getId(event.getGuild(), member)).build()).get();
                String playerName = response.getName();
                Role role = event.getGuild().getRoleById(MySQLRegisterConfig.getRegisteredRole(event.getGuild()));

                member.modifyNickname(playerName).queue();
                member.getGuild().modifyMemberRoles(member, role).queue();

                event.getUser().openPrivateChannel().flatMap(v ->
                        v.sendMessage("Vous venez de rejoindre un tournoi sur lequel vous étiez déjà inscrit, votre rôle a été remis ! :sunglasses:")).queue();
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
