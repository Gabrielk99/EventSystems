package src.models;

import src.types.*;

public class EmailMessage {
    String to;
    String from;
    VaccineInfo vaccine;
    int status;
    Coordinates location;
    String manager;
    String address;
    int key;
    boolean isFrequentAlert;

    public EmailMessage(String to, String from, VaccineInfo vaccine, int status,
                        Coordinates location, String manager, String address, int key, boolean isFrequentAlert) {
        this.to = to;
        this.from = from;
        this.vaccine = vaccine;
        this.status = status;
        this.location = location;
        this.manager = manager;
        this.address = address;
        this.key = key;
        this.isFrequentAlert = isFrequentAlert;
    }
}