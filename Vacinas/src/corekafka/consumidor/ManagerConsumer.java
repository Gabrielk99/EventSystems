package src.corekafka.consumidor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import src.corekafka.vacina.*;
import java.util.HashMap;
import org.slf4j.spi.LoggerFactoryBinder;
import com.google.gson.*;



import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import src.types.SavedMessageManager;
import java.time.Duration;

public class ManagerConsumer extends Consumer {
    private HashMap<Integer,SavedMessageManager> dataOffAllManeger = new HashMap <Integer,SavedMessageManager>();
    public ManagerConsumer(String BootstrapServer, String consumerGroupName,String topicToConsume){

        super(BootstrapServer,consumerGroupName,topicToConsume);

    }

    private void generateAndSaveJSON(Integer id, JsonElement location){
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.add("location",location);

        // if(dataOffAllManeger.containsKey(id)){
        //     dataOffAllManeger.get(id).updateMessage(message);
        // }
        // else{
        //     SavedMessageManager savedMessage = new SavedMessageManager(id,message);
        //     dataOffAllManeger.put(id,savedMessage);
        // }

        String path = Paths.get("../Database/data_for_app/Gestores").toString();

        // cria pasta pra salvar os dados
        if(!Files.exists(Paths.get(path))){
          File folder = new File(path);
          if(!folder.mkdir()){
            System.out.println("erro ao criar pasta "+path);
            System.exit(1);
          }
        }

        try{
            String content = String.join("",Files.readAllLines(Paths.get(path+"/datasSimulation.json")));
            JsonArray allMessages = new JsonParser().parse(content).getAsJsonArray();
            HashMap<Integer,SavedMessageManager> messages = new HashMap<Integer,SavedMessageManager>();
            
            for(JsonElement message:allMessages){
                JsonObject messageObj = message.getAsJsonObject();
                int idMessage = messageObj.get("id").getAsInt();
                messages.put(idMessage,new SavedMessageManager(idMessage,messageObj.get("dataSaved").getAsJsonObject()));
                
                if(idMessage==id){
                    messages.get(id).updateMessage(jsonMessage);
                }
            }
            if(!messages.containsKey(id)){
                messages.put(id,new SavedMessageManager(id,jsonMessage));
            }

            FileWriter writer = new FileWriter(path+"/datasSimulation.json");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(messages.values(),writer);
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            System.out.printf("erro %s",e.getMessage());
            System.exit(e.hashCode());
        }

    }

    @Override
    public void consumeMessages(){
        ConsumerRecords<String, String> records= super.getConsumer().poll(Duration.ofMillis(100)); // Consome mensagens dos produtores
    
        for (ConsumerRecord<String, String> record : records) {
            JsonObject managerMessage = new JsonParser().parse(record.value()).getAsJsonObject(); // Faz o parse do record recebido para objeto Json
            
            // salva a mensagem lida
            generateAndSaveJSON(
                    managerMessage.get("id").getAsInt(),
                    managerMessage.get("localizacao")
            );

            System.out.println("[ManagerConsumer] Info received: " +
                    " Manager Id: " + managerMessage.get("id")+
                    " location: " + managerMessage.get("localizacao"));
        }
    }
    
}

