package src.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

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
    public String toString(){
        return "(" + String.valueOf(this._latitude)+","+String.valueOf(this._longitude)+")"; 
    }

    public static ArrayList<Coordinates> parseListCoordinates(String brutal_cordinates){

        List<String> coordinates_with_errors = Arrays.asList(brutal_cordinates.split("\\s*\\],\\s*"));

        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();

        for (String coordinate:coordinates_with_errors){
            String coordinate_correct = coordinate.replaceAll("\\[|\\]","");
            String[] lat_long = coordinate_correct.split(",");

            Coordinates coord = new Coordinates(Double.parseDouble(lat_long[0]),Double.parseDouble(lat_long[1]));

            coordinates.add(coord);
        }
        
        return coordinates;
    }

}