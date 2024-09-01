package fr.saurfort.modal.creator;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class RegisterModalCreator {
    public static Modal registerModalCreator() {
        TextInput username = TextInput.create("username", "Username", TextInputStyle.SHORT)
                .setPlaceholder("Votre nom d'utilisateur sur Clash Royale")
                .build();
        TextInput id = TextInput.create("id", "ID", TextInputStyle.SHORT)
                .setPlaceholder("Votre ID sur Clash Royale")
                .setMinLength(2)
                .setMaxLength(15)
                .build();

        return Modal.create("registration", "S'inscrire")
                .addComponents(ActionRow.of(username), ActionRow.of(id))
                .build();
    }
}
