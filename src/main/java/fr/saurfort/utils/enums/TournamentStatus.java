package fr.saurfort.utils.enums;

public enum TournamentStatus {
    REGISTRATION("registration", 0),
    IN_PROGRESS("in_progress", 1),
    ENDED("ended", 2);

    private String status;
    private int statusCode;

    TournamentStatus(String status, int statusCode) {
        this.status = status;
        this.statusCode = statusCode;
    }

    public final String getStatus() {
        return status;
    }

    public final int getStatusCode() {
        return statusCode;
    }

    public static TournamentStatus convertStatus(String status) {
        if(status.equals(REGISTRATION.status)) {
            return REGISTRATION;
        } else if(status.equals(IN_PROGRESS.status)) {
            return IN_PROGRESS;
        } else {
            return ENDED;
        }
    }

    public static TournamentStatus convertStatus(int statusCode) {
        if(statusCode == REGISTRATION.statusCode) {
            return REGISTRATION;
        } else if(statusCode == IN_PROGRESS.statusCode) {
            return IN_PROGRESS;
        } else {
            return ENDED;
        }
    }
}
