package src.types;

import java.util.ArrayList;
import java.util.List;
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
    public Coordinates (double lat, double longi){

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
     * @param brutal_cordinates São as coordenadas no formato de string
     * @return um ArraList de Coordinates
     */
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