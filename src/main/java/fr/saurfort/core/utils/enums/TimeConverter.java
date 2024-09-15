package fr.saurfort.core.utils.enums;

public enum TimeConverter {
    HOURS(3600000),
    MINUTES(60000),
    SECONDS(1000),
    MILLISECONDS(1);

    private int multiplication;

    TimeConverter(int multiplication) {
        this.multiplication = multiplication;
    }

    public int getMultiplication() {
        return this.multiplication;
    }
}
