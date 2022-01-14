package src.types;

import com.google.gson.JsonObject;
import java.util.ArrayList;

/**
 * Classe que representa a mensagem a ser salva da vacina
 * @author Gabriel Xavier
 */
public class SavedMessageVaccine extends SavedMessage {
    public int size;

    /**
     * Construtor de mensagem de vacina
     * @param id do lote da vacina
     * @param datas mensagens a ser salva
     */

    public SavedMessageVaccine(int id,ArrayList<JsonObject>datas){
        super(id,datas);
        this.size = datas.size();
    }

    /**
     * Atualiza a lista de mensagens salvas do lote de vacina
     * @param message mensagem a salvar
     */
    public void updateMessages(JsonObject message){
        super.updateMessages(message);
        this.size = super.datasSaved.size();
    }
}
