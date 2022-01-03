package src.corekafka.vacina;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.*;
import java.util.ArrayList;
import src.types.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import src.corekafka.simulacao.PositionControlOnMap;
import src.corekafka.simulacao.TemperatureConfigurate;
import java.util.Date;

/**
 * Classe criada para representar um lote de vacina
 * 
 * @author Gabriel Xavier
 */
public class Vaccine{

    private TemperatureConfigurate _temp_controller; //Simulador da temperatura
    private PositionControlOnMap _coord_controller; //Simulador de posicao

    private int _id; //identificador da vacina

    private String _name; //nome da empresa da vacina

    /**
     * Construtor da classe Vacina
     * @param path_to_json caminho para o arquivo de configuração da vacina
     * 
     */
    public Vaccine(String path_to_json){
        
        try{

            // lendo o conteudo json
            String content = String.join("",Files.readAllLines(Paths.get(path_to_json)));
            
            // pegando o json da string como um objeto json
            JsonObject vaccine = new JsonParser().parse(content).getAsJsonObject();

            // Pegando a temperatura inicial (default)
            float t_init=Float.parseFloat(vaccine.get("t_init").getAsString());
            // Pegando a temperatura media do limite
            float t_midle_limite=Float.parseFloat(vaccine.get("t_med").getAsString());
            // Pegando a temperatura maxima do limite
            float t_max_limite=Float.parseFloat(vaccine.get("t_max").getAsString());
            // Pegando o tempo maximo que o lote aguenta apos limite de temp
            float time_long=Float.parseFloat(vaccine.get("time_max").getAsString());

            // Instanciando o simulador de temperatura
            if(vaccine.has("time_increment")){
                this._temp_controller = new TemperatureConfigurate(t_init,t_midle_limite
                                                ,t_max_limite,time_long,vaccine.get("time_increment").getAsFloat());
            }
            else {
                this._temp_controller = new TemperatureConfigurate(t_init,t_midle_limite
                ,t_max_limite,time_long);
            }

            // Pegando o nome da vacina
            this._name=vaccine.get("name").getAsString();

            // Pegando o id do lote de vacina
            this._id=Integer.parseInt(vaccine.get("id").getAsString());

          

            
            // Pegando as coordenadas do arquivo json de configuração
            ArrayList<Coordinates> coordinates = Coordinates.parseListCoordinates(vaccine.get("coordinates").toString());
            // Instanciado o simulador de posicao
            this._coord_controller = new PositionControlOnMap(coordinates,2.5);

        }

        catch(IOException e){
            System.out.printf("error arquivo %s de configuração não encontrado.!",e.getMessage());
            System.exit(1);
        }

    }

    /**
     * 
     * @return a temperatura inicial da vacina (também conhecida como temperatura normal)
     */
    public float getInitialTemperature(){
        return this._temp_controller.getTemperatureInit();
    }

    /**
     * 
     * @return a temperatura mediana do limite (usada para avisar todos gestores)
     */
    public float getMidleTemperatureLim(){
        return this._temp_controller.getTemperatureMidleLimite();
    }

    /**
     * 
     * @return temperatura maxima do limite suportado
     */
    public float getMaxTemperatureLim(){
        return this._temp_controller.getTemperatureMaxLimite();
    }

    /**
     * 
     * @return o id do lote de vacina
     */
    public int getId(){
        return this._id;
    }

    /**
     * 
     * @return o tempo maximo de espera apos passar da temperatura limite
     */
    public float getTimeWaitMax(){
        return this._temp_controller.getTimeMaxTemperatureLim();
    }

    /**
     * 
     * @return o nome da empresa fabricante
     */
    public String getName(){
        return this._name;
    }

    /**
     * 
     * @return a posicao atual do lote
     */
    public Coordinates getCurrentLocation(){
        return this._coord_controller.getCurrentPosition();
    }
    /**
     * 
     * @return a temperatura atual do lote
     */
    public float getCurrentTemperature(){
       return this._temp_controller.getCurrentTemperature();
    }

    /**
     * 
     * @return data corrente da ultima atualizacao de temperatura
     */
    public Date getCurrentDate(){
        return this._temp_controller.getCurrentDate();
    }
}