package fr.saurfort.migrator;

public enum VersionHistory {
    v2_1_1("2.1.1"),
    v2_2_0("2.2.0");

    private String textVersion;

    VersionHistory(String textVersion) {
        this.textVersion = textVersion;
    }

    public String getTextVersion() {
        return textVersion;
    }
}
