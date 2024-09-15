package fr.saurfort.utils.enums;

public enum CommandCategory {
    CONFIG("Configuration", ":gear:"),
    MODERATION("Modération", ":cop:"),
    TICKET("Ticket", ":ticket:"),
    TOURNAMENT("Tournoi", ":trophy:"),
    UTILS("Utilitaire", ":tools:");

    private String category;
    private String emote;

    CommandCategory(String category, String emote) {
        this.category = category;
        this.emote = emote;
    }

    public String getCategory() {
        return category;
    }

    public String getEmote() {
        return emote;
    }

    public String getFullName() {
        return category + " " + emote;
    }
}
