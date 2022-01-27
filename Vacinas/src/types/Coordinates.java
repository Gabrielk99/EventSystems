package src.types;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Arrays;

/**
 * Classe para representar as coordenadas
 * @author Gabriel Xavier
 */
public class Coordinates{
    
    private double _latitude;
    private double _longitude;

    /**
     * 
     * @param lat latitude 
     * @param longi longitude
     */
    public Coordinates (double longi, double lat){

        this._latitude=lat;
        this._longitude=longi;

    }

    /**
     * 
     * @return a latitude da coordenada
     */
    public double getLatitude(){
        return this._latitude;
    }
    /**
     * 
     * @return a longitude da coordenada
     */
    public double getLongitude(){
        return this._longitude;
    }
    public String toString(){
        return "(" + String.valueOf(this._latitude)+","+String.valueOf(this._longitude)+")"; 
    }
    
    /**
     * 
     * @param jsonCoordinates São as coordenadas no formato de string
     * @return um ArraList de Coordinates
     */
    public static ArrayList<Coordinates> parseListCoordinates(JsonArray jsonCoordinates){

        ArrayList<Coordinates> coordinates = new ArrayList<Coordinates>();

        for (JsonElement coordinate:jsonCoordinates){
            JsonArray latitude_longitude = coordinate.getAsJsonArray();

            Double longitude = latitude_longitude.get(0).getAsDouble();
            Double latitude = latitude_longitude.get(1).getAsDouble();

            Coordinates coord = new Coordinates(longitude,latitude);

            coordinates.add(coord);
        }
        
        return coordinates;
    }

}