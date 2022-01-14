package src.main;
import src.corekafka.gestor.*;
import src.corekafka.vacina.*;
import java.util.concurrent.TimeUnit;
import com.google.gson.*;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;

import src.corekafka.consumidor.*;

import java.io.File;
import java.nio.file.Files;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main{

    public static void main(String args[]) {

        Path path_to_vaccine_json= Paths.get("../Database/data_for_kafka/Vacinas/vacinas.json");
        Path path_to_manager_json= Paths.get("../Database/data_for_kafka/Gestores/gestores.json");
        try{
            // lendo o conteudo json
            String content = String.join("",Files.readAllLines(path_to_vaccine_json));
            
            // pegando o json da string como um objeto json
            JsonObject vaccinesData = new JsonParser().parse(content).getAsJsonObject();
            JsonArray vaccinesDataList = vaccinesData.get("vacinas").getAsJsonArray();
        
            String contentManagers = String.join("", Files.readAllLines(path_to_manager_json)); // le json e passa para string
            
            JsonObject managersData = new JsonParser().parse(contentManagers).getAsJsonObject();       // parse da string para um objeto json
            JsonArray managersDataList = managersData.get("gestores").getAsJsonArray();

            VaccineProducer producer = new VaccineProducer("localhost:9092",vaccinesDataList.get(0).getAsJsonObject());
            VaccineConsumer consumer = new VaccineConsumer("localhost:9092", "consumerVacina", "vacina", path_to_vaccine_json);

            ManagerProducer producerManager = new ManagerProducer("localhost:9092",managersDataList.get(0).getAsJsonObject());
            ManagerConsumer consumerManager = new ManagerConsumer("localhost:9092", "consumerManager", "gestor");

            while(true){
                producer.sendMessage();
                producerManager.sendLocation();
                consumer.consumeMessages();
                consumerManager.consumeMessages();
                
                try{
                    TimeUnit.SECONDS.sleep(1);
                }
                catch(InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        
        }
        catch(IOException err){
            System.out.printf("error %s ",err.getMessage());
            System.exit(err.hashCode());
        }

        // File f = new File(path.toString());
        // String [] names = f.list();

        // System.out.println(names[0]);
        
    //     VaccineProducer producer = new VaccineProducer("localhost:9092",path.toString()+"/data_for_kafka");
    //     VaccineConsumer consumer = new VaccineConsumer("localhost:9092", "consumerVacina", "vacina", "./src/Dados/Vacinas/");
    //     // // ManagerProducer producerManager = new ManagerProducer("localhost:9092","./src/Dados/Gestores/Gestor0/rota.json");
    //     // // ManagerConsumer consumerManager = new ManagerConsumer("localhost:9092", "consumerManager", "gestor");
    //     while(true){
    //         producer.sendMessage();
    //         // producerManager.sendLocation();
    //         consumer.consumeMessages();
    //         // consumerManager.consumeMessages();
            
    //         try{
    //             TimeUnit.SECONDS.sleep(1);
    //         }
    //         catch(InterruptedException e) {
    //             Thread.currentThread().interrupt();
    //         }
    //     }
    }


}