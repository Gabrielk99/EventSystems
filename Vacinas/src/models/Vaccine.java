package src.models;

import com.google.gson.JsonObject;
import com.google.gson.*;
import java.util.ArrayList;
/**
 * Classe que representa um lote
 * @author Gabriel Xavier
 */

 public class Vaccine {
    private float normal_temperature;//temperatura considerada normal
    private float warning_temperature;//temperatura considerada alarmante
    private float danger_temperature;//temperatura considerada perigosa
    private float game_over_time; //tempo máximo suportado pela vacina após atingir
                                //a temperatura máxima (danger_temperature)
                                  
    private int id;

    /**
     *
     * @param vaccine objeto com as informações de configuração
     */
    public Vaccine(JsonObject vaccine){
        // Pegando a temperatura inicial (default)
        this.normal_temperature=Float.parseFloat(vaccine.get("t_init").getAsString());
        // Pegando a temperatura media do limite
        this.warning_temperature=Float.parseFloat(vaccine.get("t_med").getAsString());
        // Pegando a temperatura maxima do limite
        this.danger_temperature=Float.parseFloat(vaccine.get("t_max").getAsString());
        // Pegando o tempo maximo que o lote aguenta apos limite de temp
        this.game_over_time=Float.parseFloat(vaccine.get("time_max").getAsString());
        // Pegando o id do lote de vacina
        this.id = Integer.parseInt(vaccine.get("id").getAsString());
    }

    
    public float getNormalTemperature(){
        return this.normal_temperature;
    }

    public float getWarningTemperature(){
        return this.warning_temperature;
    }

    public float getDangerTemperature(){
        return this.danger_temperature;
    }

    public float getGameOverTime(){
        return this.game_over_time;
    }

    public int getId(){
        return this.id;
    }

}
