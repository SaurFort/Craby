package fr.saurfort.core.modal.creator;

import fr.saurfort.core.database.query.register.MySQLRegistration;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class UnregisterModalCreator {
    public static Modal unregisterModalCreator(Guild guild, Member member) {
        TextInput reason = TextInput.create("reason", "Raison", TextInputStyle.PARAGRAPH)
                .setPlaceholder("Voulez-vous donner la raison de votre désinscription ?")
                .setRequired(false)
                .build();

        return Modal.create("unregister", "Se désinscrire")
                .addComponents(ActionRow.of(reason))
                .build();
    }
}
