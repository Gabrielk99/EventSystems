package src.main;
import src.corekafka.produtor.gestor.*;
import src.corekafka.produtor.vacina.*;
import src.corekafka.stream.*;

import java.util.ArrayList;
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
        // vaccines controllers
        ArrayList<VaccineConsumer> consumersToVaccine = new ArrayList<VaccineConsumer>();
        ArrayList<VaccineProducer> producersToVaccine = new ArrayList<VaccineProducer>();

        //managers controllers
        ArrayList<ManagerConsumer> consumersToManagers = new ArrayList<ManagerConsumer>();
        ArrayList<ManagerProducer> producersToManagers = new ArrayList<ManagerProducer>();

        createConsumersProducersVaccine(consumersToVaccine,producersToVaccine);
        createConsumersProducersManager(consumersToManagers,producersToManagers);

        SmartStream streamController = new SmartStream("localhost:9092","vacina");
        SmartStream streamController1 = new SmartStream("localhost:9092","vacina");
        SmartStream streamController2 = new SmartStream("localhost:9092","vacina");
        streamController.run();
        streamController1.run();
        streamController2.run();
        while(true){

            for(VaccineProducer producerVaccine:producersToVaccine){
                producerVaccine.sendMessage();
            }
            
            for(ManagerProducer producerManager:producersToManagers){
                producerManager.sendLocation();
            }
          
            // for(VaccineConsumer consumerVaccine:consumersToVaccine){
            //     // System.out.println("CONSUMIDOR VACINA X LENDO X");
            //     consumerVaccine.consumeMessages();
            // }
            for(ManagerConsumer consumerManager:consumersToManagers){
                // System.out.println("CONSUMIDOR GESTOR X LENDO X ");
                consumerManager.consumeMessages();
            }
            
            try{
                TimeUnit.SECONDS.sleep(1);
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

    }

    public static void createConsumersProducersVaccine(ArrayList<VaccineConsumer> consumersToVaccine,  ArrayList<VaccineProducer> producersToVaccine ){
 
        Path path_to_vaccine_json= Paths.get("../Database/data_for_kafka/Vacinas/vacinas.json");
        try{
             // lendo o conteudo json
             String content = String.join("",Files.readAllLines(path_to_vaccine_json));
            
             // pegando o json da string como um objeto json
             JsonObject vaccinesData = new JsonParser().parse(content).getAsJsonObject();
             JsonArray vaccinesDataList = vaccinesData.get("vacinas").getAsJsonArray();
         
             for(JsonElement vaccineData:vaccinesDataList){
                 producersToVaccine.add(new VaccineProducer("localhost:9092",vaccineData.getAsJsonObject()));
             }
             for(int i=0;i<3;i++){
                 consumersToVaccine.add( new VaccineConsumer("localhost:9092", "consumerVacina", "vacina", path_to_vaccine_json));
             }
 
        }
        catch(IOException err){
            System.out.printf("error %s ",err.getMessage());
            System.exit(err.hashCode());
        }
    }
    public static void createConsumersProducersManager(ArrayList<ManagerConsumer> consumersToManager,  ArrayList<ManagerProducer> producersToManager ){
 
        Path path_to_manager_json= Paths.get("../Database/data_for_kafka/Gestores/gestores.json");
        try{
             // lendo o conteudo json
             String content = String.join("",Files.readAllLines(path_to_manager_json));
            
             // pegando o json da string como um objeto json
             JsonObject managersData = new JsonParser().parse(content).getAsJsonObject();
             JsonArray managersDataList = managersData.get("gestores").getAsJsonArray();
         
             for(JsonElement managerData:managersDataList){
                 producersToManager.add(new ManagerProducer("localhost:9092",managerData.getAsJsonObject()));
             }
             for(int i=0;i<3;i++){
                 consumersToManager.add( new ManagerConsumer("localhost:9092", "consumerGestor", "gestor"));
             }
 
        }
        catch(IOException err){
            System.out.printf("error %s ",err.getMessage());
            System.exit(err.hashCode());
        }
    }


}