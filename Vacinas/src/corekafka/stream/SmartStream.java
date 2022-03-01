package src.corekafka.stream;

import java.util.Properties;
import com.google.gson.*;

import java.util.Arrays;
import java.util.HashMap;
import src.models.Vaccine;
import src.models.VaccineStatus;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import src.models.VaccineStatus.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Calendar;
/**
 * Classe que representa um processamento inteligente dos dados 
 * da vacina, capaz de interpretar as devidas situações e regras
 * e gerar saídas que correspondem cada evento.
 * @author Gabriel Xavier
 */
public class SmartStream {

    private KStream<String,String> mainStream;
    private KafkaStreams streamsController;
    private StreamsBuilder builder; 
    private HashMap<Integer,Vaccine> vaccinesDefaultInfos;
    private HashMap<Integer, Double> timeWhenReachedMaxTemp;
    private Properties config;
    
    /**
     * 
     * @param BootstrapServer string do servidor Bootstrap
     * @param initTopic nome do topico de entrada default dos dados
     */
    public SmartStream (String BootstrapServer,String initTopic){
        
        // definindo as propriedades do stream
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "vaccine-monitoring");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());

        StreamsBuilder StreamBuilder = new StreamsBuilder();

        this.mainStream =  StreamBuilder.stream(initTopic);
        this.config = config;
        this.builder = StreamBuilder;
    }

    /**
     * Atualiza a lista de vacinas 
     * para poder realizar o tratamento inteligente
     * sempre é chamado quando encontra um id não listado
     */
    private void updateListOfVaccinesInfos(){
        Path path_to_vaccines_json= Paths.get("../Database/data_for_kafka/Vacinas/vacinas.json");
        try{
            // lendo o conteudo json
            String content = String.join("",Files.readAllLines(path_to_vaccines_json));
           
            // pegando o json da string como um objeto json
            JsonObject vaccinesData = new JsonParser().parse(content).getAsJsonObject();
            JsonArray vaccinesDataList = vaccinesData.get("vacinas").getAsJsonArray();
        
            for(JsonElement vaccineData:vaccinesDataList){
                Vaccine vaccine = new Vaccine(vaccineData.getAsJsonObject());
                this.vaccinesDefaultInfos.put(vaccine.getId(),vaccine);
                this.timeWhenReachedMaxTemp.put(vaccine.getId(),(double) 0.0);
            }
       }
       catch(IOException err){
           System.out.printf("error %s ",err.getMessage());
           System.exit(err.hashCode());
       }
   
    }

    /**
     * printa a topologia do stream
     */
    public void debugStream (){
        //printando a topologia
        this.streamsController.metadataForLocalThreads().forEach(data -> System.out.println(data));
    }


    
    /**
     * Função responsável por checar o status da vacina dada a temperatura atual e o tempo atual
     * @param id
     * @param currentTime
     * @param temperature
     * @return
     */
    private VaccineStatus checkVaccineStatus(Integer id, double currentTime, float temperature) {
        // caso receba uma vacina de id diferente atualiza a lista de informacoes pessoais
        if(!vaccinesDefaultInfos.containsKey(id)){
            updateListOfVaccinesInfos();
        }

        Vaccine vacina = vaccinesDefaultInfos.get(id);
        Double timeMaxTemp = timeWhenReachedMaxTemp.get(id);
        float maxTemperatureLim = vacina.getDangerTemperature();

        // Se a temperatura está abaixo do limite, seta o marcador de quando a vacina
        // atingiu o max para o tempo atual para que currentTime - timeMaxTemp seja igual a 0.
        if (temperature <= maxTemperatureLim) {
            timeMaxTemp = currentTime;
            timeWhenReachedMaxTemp.put(id, timeMaxTemp);
        }

        // System.out.println("Time que passou: " + (currentTime - timeMaxTemp) + " -> " + vacina.getTimeWaitMax());
        return VaccineStatus.checkStatus(
            temperature,
            vacina.getWarningTemperature(),
            maxTemperatureLim,
            currentTime - timeMaxTemp,  // tempo que passou acima da temperatura maxima limite
            vacina.getGameOverTime()
        );
    }

  
    /**
     *
     * @param message mensagem do produtor de vacinas
     * @return status da vacina
     */
    public VaccineStatus getStatus(String message){
        JsonObject vaccineMessage = new JsonParser().parse(message).getAsJsonObject();
        double now = Calendar.getInstance().getTime().getTime()/1000; // Pega tempo atual
        VaccineStatus status = this.checkVaccineStatus(vaccineMessage.get("id").getAsInt(),
                                                        now,
                                                        vaccineMessage.get("temperatura").getAsFloat());
        return status;
    }

    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean, true para vacinas que estao na situacao de warning
     *         false caso contrario.
     */
    public boolean filterWarning(String key,String message){
        VaccineStatus status = this.getStatus(message);
        return status == VaccineStatus.WARNING;
    }


    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean,true para vacinas que estao na situacao de danger
     *          false caso contrario.
     */
    public boolean filterDanger(String key,String message){
        VaccineStatus status = this.getStatus(message);
        return status == VaccineStatus.DANGER;
    }

    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean, true para vacinas que estao na situacao de gameover
     *          false caso contrario.
     */
    public boolean filterGameOver(String key, String message){
        VaccineStatus status = this.getStatus(message);
        return status == VaccineStatus.GAMEOVER;
    }

    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean, true para vacinas que estao na situacao de normalizacao
     *          false caso contrario.
     */
    public boolean filterOk(String key, String message){
        VaccineStatus status = this.getStatus(message);
        return status == VaccineStatus.OK;
    }

    /**
     *  aplica o processamento stream gerando as informações necessarias
     * @return nothing
     */
    public void run(){
        System.out.println("STREAM PROCESS");
        this.mainStream.peek((key,value)->System.out.println("MENSAGEM : "+value));
        // KStream <String,String> warningVaccines = this.mainStream.filter((key,message)->this.filterWarning(key,message));
        // KStream <String,String> dangerVaccines = this.mainStream.filter((key,message)->this.filterDanger(key,message));
        // KStream <String,String> gameOverVaccines = this.mainStream.filter((key,message)->this.filterGameOver(key,message));
        // KStream <String,String> okVaccines = this.mainStream.filter((key,message)->this.filterOk(key,message));

        // warningVaccines.peek((key,value)->System.out.println("Filtro warning key: "+key+" valor: "+value));

        this.streamsController = new KafkaStreams(this.builder.build(),this.config);

        this.streamsController.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(this.streamsController::close));
        // this.debugStream();
    }

}