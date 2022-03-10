package src.models;

import src.types.*;

public class EmailMessage {
    String to;
    VaccineInfo vaccine;
    int status;
    Coordinates location;
    String manager;
    boolean isFrequentAlert;

    public EmailMessage(String to, VaccineInfo vaccine, int status,
                        Coordinates location, String manager, boolean isFrequentAlert) {
        this.to = to;
        this.vaccine = vaccine;
        this.status = status;
        this.location = location;
        this.manager = manager;
        this.isFrequentAlert = isFrequentAlert;
    }
}