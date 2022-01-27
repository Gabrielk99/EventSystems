package src.types;

import com.google.gson.*;
import java.util.ArrayList;

/**
 * Classe pra representar mensagens que vão ser salvas
 * essa classe é uma classe pai e os filhos alternam 
 * na necessidade do dado, vacinas salvam listas 
 * gestores salvam apenas um objeto 
 * quando os valores são nulos não entra pro json
 * 
 * @author Gabriel Xavier
 */
public class SavedMessage {
    public int id;
    public ArrayList<JsonObject> datasSaved;
    public JsonObject dataSaved;
  
    /**
     * Construtor de mensagens para vacina
     * @param id id do lote
     * @param datas lista de mensagens da vacina
     */
    public SavedMessage(int id, JsonArray datas){
        this.id=id;

        ArrayList<JsonObject> datasSaved= new ArrayList<JsonObject>();
        for(JsonElement data:datas){
            datasSaved.add(data.getAsJsonObject());
        }
        this.datasSaved = datasSaved;
    }
    /**
     * Construtor de mensagens pra gestor
     * @param id id do gestor
     * @param data mensagem pra ser salva
     */
    public SavedMessage(int id, JsonObject data){
        this.id=id;
        this.dataSaved=data;
    }

    /**
     * Atualiza o vetor de mensagens caso vacina
     * @param message mensagem a ser salva
     */ 
    public void updateMessages(JsonObject message){
         //se ja tiver 100 elementos descarta o mais antigo para adicionar o novo 
         if(datasSaved.size()==100){
            datasSaved.remove(0);
        }
       
        datasSaved.add(message);
    }

    /**
     * Atualiza a mensagem individual caso gestor
     * @param message mensagem a ser salva
     */
    public void updateMessage(JsonObject message){
       this.dataSaved = message;
   }


}
