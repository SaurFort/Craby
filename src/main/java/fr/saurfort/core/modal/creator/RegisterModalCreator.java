package fr.saurfort.core.modal.creator;

import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;

public class RegisterModalCreator {
    public static Modal registerModalCreator() {
        TextInput id = TextInput.create("id", "ID", TextInputStyle.SHORT)
                .setPlaceholder("Votre ID sur Clash Royale")
                .setMinLength(3)
                .setMaxLength(15)
                .build();

        return Modal.create("registration", "S'inscrire")
                .addComponents(ActionRow.of(id))
                .build();
    }
}
