package src.types;

public class Coordinates{
    
    private double _latitude;
    private double _longitude;

    public Coordinates (double lat, double longi){

        this._latitude=lat;
        this._longitude=longi;

    }

    public double getLatitude(){
        return this._latitude;
    }
    public double getLongitude(){
        return this._longitude;
    }

}