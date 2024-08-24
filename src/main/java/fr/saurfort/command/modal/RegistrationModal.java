package fr.saurfort.command.modal;

import fr.saurfort.database.Database;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class RegistrationModal extends ListenerAdapter {
    public static final String NAME = "register";
    public static final String DESCRIPTION = "Affiche le formulaire d'inscription";
    public static final long PERMISSION = Permission.ALL_PERMISSIONS;
    public static final boolean GUILD_ONLY = true;

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        if(event.getName().equals(NAME)) {
            TextInput username = TextInput.create("username", "Username", TextInputStyle.SHORT)
                    .setPlaceholder("Votre nom d'utilisateur sur Clash Royale")
                    .build();
            TextInput id = TextInput.create("id", "ID", TextInputStyle.SHORT)
                    .setPlaceholder("Votre ID sur Clash Royale")
                    .setMinLength(2)
                    .setMaxLength(15)
                    .build();

            Modal modal = Modal.create("registration", "S'inscrire")
                    .addComponents(ActionRow.of(username), ActionRow.of(id))
                    .build();

            if(event.getChannelIdLong() != 1269723436364857404L) {
                event.reply("Les inscriptions ne sont pas prise en charge dans ce channel, veuillez allez dans le channel <#1269723436364857404> !").setEphemeral(true).queue();
            } else if(Database.canRegister().equals("max")) {
                event.reply("Les inscriptions sont déjà complètes, désolé, ça sera pour une prochaine fois peut-être :wink:").setEphemeral(true).queue();
            } else if(Database.canRegister().equals("error")) {
                event.reply("Erreur lors de votre inscription, veuillez réessayer !").setEphemeral(true).queue();
            } else {
                event.replyModal(modal).queue();
            }
        }
    }
}
