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
import src.corekafka.produtor.vacina.*;
import java.util.HashMap;
import org.slf4j.spi.LoggerFactoryBinder;
import com.google.gson.*;

import java.util.Properties;
import java.util.ArrayList;
import java.util.Arrays;
import java.time.Duration;
import java.util.Calendar;
import java.util.stream.Stream;

import src.types.SavedMessageVaccine;
/**
 * Classe que representa o consumidor das informações que os produtores de vacina enviam
 */
public class VaccineConsumer extends Consumer {
    private HashMap<Integer, Vaccine> vacinas;
    private HashMap<Integer, Double> timeWhenReachedMaxTemp;

    /**
     * Construtor da classe
     * @param BootstrapServer   server onde o kafka está rodando
     * @param consumerGroupName  nome do grupo no qual o consumidor irá fazer parte
     * @param topicToConsume    tópico que vai consumir as mensagens
     * @param pathToVaccinesFolder  pasta onde se encontram os arquivos de configuração das vacinas
     */
    public VaccineConsumer (String BootstrapServer, String consumerGroupName, String topicToConsume, Path pathToVaccinesFolder) {
        super(BootstrapServer,consumerGroupName,topicToConsume);

        this.timeWhenReachedMaxTemp = new HashMap<Integer, Double>();
        this.vacinas = searchForVaccines(pathToVaccinesFolder);
    }

    /**
     *  Função responsável por buscar os arquivos de configurações das vacinas
     * @param path  caminho até a pasta onde se encontram os arquivos de configuração das vacinas
     * @return  Um hashmap com as informações das vacinas
     */
    private HashMap<Integer, Vaccine> searchForVaccines(Path path) {
        HashMap<Integer, Vaccine> vaccinesFound = new HashMap<Integer, Vaccine>();

        try {
            // lendo o conteudo json
            String content = String.join("",Files.readAllLines(path));
        
            // pegando o json da string como um objeto json
            JsonObject vaccinesData = new JsonParser().parse(content).getAsJsonObject();
            JsonArray vaccinesDataList = vaccinesData.get("vacinas").getAsJsonArray();

            for(JsonElement vaccine_json:vaccinesDataList){
                Vaccine vaccine = new Vaccine(vaccine_json.getAsJsonObject());
                vaccinesFound.put(vaccine.getId(),vaccine);
                this.timeWhenReachedMaxTemp.put(vaccine.getId(), (double) 0.0);
            }
           
        }catch (Exception e) {
            System.out.printf("error %s ",e.getMessage());
            System.exit(e.hashCode());
        }
        return vaccinesFound;
    }

    /**
     * Função responsável por gerar um JSON com as informações recebidas e concluídas de uma dada vacina
     * @param id    id da vacina
     * @param status    status da vacina
     * @param temperatura   temperatura atual da vacina
     * @param location  localização atual da vacina
     */
    private void generateAndSaveJSON(Integer id, Integer status, String temperatura, JsonElement location, String date) {
        // Gera objeto json com as informações a serem consumidas pela aplicação
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("status", status);
        jsonMessage.addProperty("temperature", temperatura);
        jsonMessage.addProperty("date", date);
        jsonMessage.add("location", location);

        String path = Paths.get("../Database/data_for_app/Vacinas").toString();

        // cria pasta pra salvar os dados
        if(!Files.exists(Paths.get(path))){
          File folder = new File(path);
          if(!folder.mkdir()){
            System.out.println("erro ao criar pasta "+path);
            System.exit(1);
          }
        }

        //check if json file exist
        if(!(new File(path+"/datasSimulation.json")).isFile()){
          try{
              FileWriter writer = new FileWriter(path+"/datasSimulation.json");
              writer.write("[ ]");
              writer.close();
          }
          catch(IOException err){
              System.out.printf("erro %s",err.getMessage());
              System.exit(err.hashCode());
          }
        }

        try{
            String content = String.join("",Files.readAllLines(Paths.get(path+"/datasSimulation.json")));
            JsonArray allMessages = new JsonParser().parse(content).getAsJsonArray();
            HashMap<Integer,SavedMessageVaccine> messages = new HashMap<Integer,SavedMessageVaccine>();
            
            for(JsonElement message:allMessages){
                JsonObject messageObj = message.getAsJsonObject();
                int idMessage = messageObj.get("id").getAsInt();
                messages.put(idMessage,new SavedMessageVaccine(idMessage,messageObj.get("datasSaved").getAsJsonArray()));
                
                if(idMessage==id){
                    messages.get(id).updateMessages(jsonMessage);
                }
            }
            if(!messages.containsKey(id)){
                messages.put(id,new SavedMessageVaccine(id,new JsonArray()));
                messages.get(id).updateMessages(jsonMessage);
            }

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            FileWriter writer = new FileWriter(path+"/datasSimulation.json");
    
            gson.toJson(messages.values(), writer);
            writer.flush();
            writer.close();
        }
        catch(IOException e){
            System.out.printf("erro %s",e.getMessage());
            System.exit(e.hashCode());
        }

    }

    /**
     * Função responsável por checar o status da vacina dada a temperatura atual e o tempo atual
     * @param id
     * @param currentTime
     * @param temperature
     * @return
     */
    private VaccineStatus checkVaccineStatus(Integer id, double currentTime, float temperature) {
        Vaccine vacina = vacinas.get(id);
        Double timeMaxTemp = timeWhenReachedMaxTemp.get(id);
        float maxTemperatureLim = vacina.getMaxTemperatureLim();

        // Se a temperatura está abaixo do limite, seta o marcador de quando a vacina
        // atingiu o max para o tempo atual para que currentTime - timeMaxTemp seja igual a 0.
        if (temperature <= maxTemperatureLim) {
            timeMaxTemp = currentTime;
            timeWhenReachedMaxTemp.put(id, timeMaxTemp);
        }

        // System.out.println("Time que passou: " + (currentTime - timeMaxTemp) + " -> " + vacina.getTimeWaitMax());
        return VaccineStatus.checkStatus(
            temperature,
            vacina.getMidleTemperatureLim(),
            maxTemperatureLim,
            currentTime - timeMaxTemp,  // tempo que passou acima da temperatura maxima limite
            vacina.getTimeWaitMax()
        );
    }

    /**
     * Função responsável por consumir mensagens enviadas no tópico em que está inscrito e tratá-las
     */
    @Override
    public void consumeMessages() {
        ConsumerRecords<String, String> records= super.getConsumer().poll(Duration.ofMillis(100)); // Consome mensagens dos produtores
        double now = Calendar.getInstance().getTime().getTime()/1000; // Pega tempo atual
        for (ConsumerRecord<String, String> record : records) {
            JsonObject vaccineMessage = new JsonParser().parse(record.value()).getAsJsonObject(); // Faz o parse do record recebido para objeto Json
            VaccineStatus status = this.checkVaccineStatus(   // Checa o status da vacina
                    Integer.parseInt(vaccineMessage.get("id").getAsString()),
                    now,
                    Float.parseFloat(vaccineMessage.get("temperatura").getAsString())
            );

            generateAndSaveJSON(
                    vaccineMessage.get("id").getAsInt(),
                    status.ordinal(),
                    vaccineMessage.get("temperatura").getAsString(),        // Gera o Json com, principalmente, id e status da vacina num arquivo
                    vaccineMessage.get("localizacao"),
                    vaccineMessage.get("data").getAsString()
                    );

            // System.out.println("[VaccineConsumer] Info received: " +
            //         " vaccine Id: " + vaccineMessage.get("id") +
            //         " temperature: " + vaccineMessage.get("temperatura") +
            //         " status: " + status.toString());
        }
    }
}