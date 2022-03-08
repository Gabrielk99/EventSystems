package src.types;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.Arrays;
import java.lang.Math;
/**
 * Classe para representar as coordenadas
 * @author Gabriel Xavier
 */
public class Coordinates{
    private final static double R = 6371e3;

    private double latitude;
    private double longitude;


    /**
     * 
     * @param lat latitude 
     * @param longi longitude
     */
    public Coordinates (double longi, double lat){

        this.latitude=lat;
        this.longitude=longi;

    }

    public static double calculateDistance(Coordinates position1, Coordinates position2){
        double fi1 = position1.getLatitude() * Math.PI/180; // φ, λ in radians
        double fi2 = position2.getLatitude() * Math.PI/180;
        double deltaFi = (position2.getLatitude()-position1.getLatitude()) * Math.PI/180;
        double deltaLambda = (position2.getLongitude()-position1.getLongitude()) * Math.PI/180;

        double a = Math.sin(deltaFi/2) * Math.sin(deltaFi/2) +
                    Math.cos(fi1) * Math.cos(fi2) *
                    Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return Coordinates.R * c; // in metres
    }

    /**
     * 
     * @return a latitude da coordenada
     */
    public double getLatitude(){
        return this.latitude;
    }
    /**
     * 
     * @return a longitude da coordenada
     */
    public double getLongitude(){
        return this.longitude;
    }
    public String toString(){
        return "(" + String.valueOf(this.latitude)+","+String.valueOf(this.longitude)+")"; 
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