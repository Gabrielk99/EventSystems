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
import java.time.Duration;
import java.util.Calendar;
import java.util.stream.Stream;

public class ManagerConsumer extends Consumer {

    public ManagerConsumer(String BootstrapServer, String consumerGroupName,String topicToConsume){

        super(BootstrapServer,consumerGroupName,topicToConsume);

    }

    private void generateAndSaveJSON(Integer id, JsonElement location){
        JsonObject message = new JsonObject();

        message.addProperty("id", id);
        message.add("location",location);

        String path = Paths.get("./src/Dados/managerSavedData").toString();

        // cria pasta pra salvar os dados
        if(!Files.exists(Paths.get(path))){
          File folder = new File(path);
          if(!folder.mkdir()){
            System.out.println("erro ao criar pasta "+path);
            System.exit(1);
          }
        }

        try{
            FileWriter writer = new FileWriter(path+"/"+id.toString()+".json");
            writer.write(message.toString());
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

