package src.types;

import com.google.gson.JsonObject;
/**
 * Classe responsavel por representar uma mensagem salva do gestor
 * @author Gabriel Xavier
 */
public class SavedMessageManager extends SavedMessage {
    /**
     * Construtor de mensagem de gestor
     * @param id id do gestor
     * @param data mensagem pra salvar
     */
    public SavedMessageManager(int id, JsonObject data){
        super(id,data);
    }

    /**
     * 
     * @param message atualiza a mensagem de gestor que deve ser salva
     */
    public void updateMessage(JsonObject message){
        super.updateMessage(message);
    }

}
