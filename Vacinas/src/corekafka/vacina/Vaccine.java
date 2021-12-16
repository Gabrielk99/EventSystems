package corekafka.vacina;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.*;
import java.util.ArrayList;
import types.*;
import java.io.IOException;

public class Vaccine{

    private float _t_init;
    private float _t_midle_limite;
    private float _t_max_limite;

    private ArrayList<Coordinates> _coordinates = new  ArrayList<Coordinates>();


    public Vaccine(String path_to_json){
        
        try{
            String content = String.join("",Files.readAllLines(Paths.get(path_to_json)));

            Gson g = new Gson();
            
            JsonObject vaccine = new JsonParser().parse(content).getAsJsonObject();

            System.out.println(vaccine.get("id").getAsString());
        }
        catch(IOException e){
            System.out.println(e);
        }

    }

}