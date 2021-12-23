package src.corekafka.vacina;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.*;
import java.util.ArrayList;
import src.types.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
public class Vaccine{

    private float _t_init;
    private float _t_midle_limite;
    private float _t_max_limite;

    private ArrayList<Coordinates> _coordinates = new  ArrayList<Coordinates>();

    private int _id;
    
    private float _time_long;

    private String _name;

    public Vaccine(String path_to_json){
        
        try{

            // lendo o conteudo json
            String content = String.join("",Files.readAllLines(Paths.get(path_to_json)));
            
            // pegando o json da string como um objeto json
            JsonObject vaccine = new JsonParser().parse(content).getAsJsonObject();

            // Pegando a temperatura inicial (default)
            this._t_init=Float.parseFloat(vaccine.get("t_init").getAsString());

            // Pegando a temperatura media do limite
            this._t_midle_limite=Float.parseFloat(vaccine.get("t_med").getAsString());

            // Pegando a temperatura maxima do limite
            this._t_init=Float.parseFloat(vaccine.get("t_max").getAsString());

            // Pegando o nome da vacina
            this._name=vaccine.get("name").getAsString();

            // Pegando o id do lote de vacina
            this._id=Integer.parseInt(vaccine.get("id").getAsString());

            // Pegando o tempo maximo que o lote aguenta apos limite de temp
            this._time_long=Float.parseFloat(vaccine.get("time_max").getAsString());

            //TODO finalizar conversão da string de coordenadas para lista de coordenadas
            String[] coordinates = vaccine.get("cordinates").toString().split("\\s*\\],\\s*");

            System.out.println(coordinates[0]);

        }

        catch(IOException e){
            System.out.println(e);
        }

    }

}