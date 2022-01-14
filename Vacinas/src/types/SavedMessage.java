package src.types;

import com.google.gson.*;
import java.util.ArrayList;

public class SavedMessage {
    public int id;
    public int size;
    public ArrayList<JsonObject> datasSaved;

    public SavedMessage(int id, ArrayList<JsonObject> datas){
        this.id=id;
        this.size = datas.size();
        this.datasSaved=datas;
    }
    public void updateMessage(JsonObject message){
         //se ja tiver 100 elementos descarta o mais antigo para adicionar o novo 
         if(datasSaved.size()==100){
            datasSaved.remove(0);
        }
       
        datasSaved.add(message);
        size = datasSaved.size();
    }

}
