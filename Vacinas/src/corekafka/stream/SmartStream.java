package src.corekafka.stream;

import java.util.Properties;
import com.google.gson.*;

import java.util.Arrays;
import java.util.HashMap;
import src.models.Vaccine;
import src.models.VaccineStatus;
import src.models.MessageToNotifications;
import src.models.CustomSerdes;
import src.models.VaccineMessage;
import src.models.GestorMessage;
import src.models.DangerJoinManagers;
import src.models.ManagersMap;
import src.models.VaccinesMap;
import src.types.Coordinates;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.state.KeyValueStore;
import src.models.VaccineStatus.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Calendar;
import java.util.ArrayList;


/**
 * Classe que representa um processamento inteligente dos dados 
 * da vacina, capaz de interpretar as devidas situações e regras
 * e gerar saídas que correspondem cada evento.
 * @author Gabriel Xavier
 */
public class SmartStream {

    private KStream<String,VaccineMessage> mainStream;
    private KStream<String,GestorMessage> managersInfo;
    private KafkaStreams streamsController;
    private StreamsBuilder builder; 
    private HashMap<Integer,Vaccine> vaccinesDefaultInfos = new HashMap<Integer,Vaccine>();
    private HashMap<Integer, Double> timeWhenReachedMaxTemp  = new HashMap<Integer,Double>();
    private Properties config;
    
    
    int i = 0;
    /**
     * 
     * @param BootstrapServer string do servidor Bootstrap
     * @param initTopic nome do topico de entrada default dos dados
     */
    public SmartStream (String BootstrapServer,String initTopic,String secondaryTopic){
        
        // definindo as propriedades do stream
        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "vaccine-monitoring");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        config.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, "0");

        StreamsBuilder StreamBuilder = new StreamsBuilder();

        this.mainStream =  StreamBuilder.stream(initTopic,Consumed.with(Serdes.String(),CustomSerdes.VaccineMessage()));
        this.managersInfo = StreamBuilder.stream(secondaryTopic,Consumed.with(Serdes.String(),CustomSerdes.GestorMessage()));
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
    public VaccineStatus getStatus(VaccineMessage message){
        double now = Calendar.getInstance().getTime().getTime()/1000; // Pega tempo atual
        VaccineStatus status = this.checkVaccineStatus(message.getId(),
                                                        now,
                                                        message.getTemperatura());
        return status;
    }

    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean, true para vacinas que estao na situacao de warning
     *         false caso contrario.
     */
    public boolean filterWarning(String key,VaccineMessage message){
        VaccineStatus status = this.getStatus(message);
        return status == VaccineStatus.WARNING;
    }
    /**
     *
     * @param message mensagem do KSTREAM que apenas pega os WARNINGS
     * @return mensagem formatada para situações de warning pro topico notificacao
     * 
     */
    public MessageToNotifications messageWarningToNotifications(VaccineMessage message){
        // JsonObject messageJson = new JsonParser().parse(message).getAsJsonObject();
        // JsonObject locationJson = messageJson.get("localizacao").getAsJsonObject();
        // Coordinates location = new Coordinates(locationJson.get("longitude").getAsDouble(),locationJson.get("latitude").getAsDouble());
        MessageToNotifications messageToSend = new MessageToNotifications(message.getLocalizacao(),message.getId(),VaccineStatus.WARNING.ordinal(),-1);
        return  messageToSend;
    }

    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean,true para vacinas que estao na situacao de danger
     *          false caso contrario.
     */
    public boolean filterDanger(String key, DangerJoinManagers message){
        VaccineStatus status = this.getStatus(message.getVaccines());
        return status == VaccineStatus.DANGER;
    }
    public Double calculateDistance (GestorMessage managerData,Coordinates locationVaccine){
        Coordinates locationManager = managerData.getLocalizacao();
        return Coordinates.calculateDistance(locationManager,locationVaccine);
    }

    public MessageToNotifications messageDangerToNotifications(DangerJoinManagers message){
        Coordinates locationVaccine = message.getVaccines().getLocalizacao();

        int id = 0;
        Double min_dist = Double.MAX_VALUE;
        for(GestorMessage manager:message.getManagers().getMapManagers().values()){
            Double distance = calculateDistance(manager,locationVaccine);
            if(distance< min_dist){
                id=manager.getId();
                min_dist = distance;
            }
        }
        
        MessageToNotifications messageToSend = new MessageToNotifications(locationVaccine,message.getVaccines().getId(),VaccineStatus.DANGER.ordinal(),id);
        return messageToSend;
    }
    

    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean, true para vacinas que estao na situacao de gameover
     *          false caso contrario.
     */
    public boolean filterGameOver(String key, VaccineMessage message){
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
    public boolean filterOk(String key, VaccineMessage message){
        VaccineStatus status = this.getStatus(message);
        return status == VaccineStatus.OK;
    }

    /**
     *  aplica o processamento stream gerando as informações necessarias
     * @return nothing
     */
    public void run(){
       //Filtrando as situações dos lotes
        KStream <String,VaccineMessage> warningVaccines = this.mainStream.filter((key,message)->this.filterWarning(key,message));       
        KStream <String,VaccineMessage> gameOverVaccines = this.mainStream.filter((key,message)->this.filterGameOver(key,message));
        KStream <String,VaccineMessage> okVaccines = this.mainStream.filter((key,message)->this.filterOk(key,message));

        // manda as mensagens de warning para notificacao
        warningVaccines.mapValues(message->messageWarningToNotifications(message))
                       .to("notificacao",Produced.with(Serdes.String(),CustomSerdes.MessageToNotifications()));

        /*
         * Processo de gerar as mensagens do status danger 
         * 
         */
        //Aglomerando todos os gestores em um map para manter a ultima mensagem
        //Forçando a chave deles ser 0 e iguais para poder bater com as vacinas 
        Aggregator<String, GestorMessage, ManagersMap> gestorAgg = (id, gestor,  gestores) -> {
                                                                    gestores.addManager(gestor);
                                                                    return gestores;
                                                                };

        KTable <String,ManagersMap> newGestorMessages = managersInfo.selectKey((key,value)->"0")
                                                                    .groupByKey()
                                                                    .aggregate(ManagersMap::new
                                                                    ,gestorAgg,
                                                                    Materialized.with(Serdes.String(),CustomSerdes.ManagersMap())
                                                                    );

        // força a chave ser 0, vai sempre pegar a informacao mais atual da vacina X e mapeia a todos os gestores
        KTable <String,DangerJoinManagers> dangerWithManager = mainStream.selectKey((key,value)->"0").toTable().mapValues((v)->new DangerJoinManagers(v,null),Materialized.with(Serdes.String(),CustomSerdes.DangerJoinManagers()));
        // realiza o join antes do filtro (join depois do filtro gera lixo)
        dangerWithManager = dangerWithManager.join(newGestorMessages,
                                            (dangersManagers,mapGestor)->{
                                                dangersManagers.setManagers(mapGestor);
                                                return dangersManagers;
                                            },Materialized.with(Serdes.String(),CustomSerdes.DangerJoinManagers()));
        // filtra as vacinas que estao no status danger
        KTable <String,DangerJoinManagers> dangerVaccines = dangerWithManager.filter((key,message)->this.filterDanger(key,message));
        // limpa as mensagens nulas, gera a mensagem final e envia para notificação
        dangerVaccines.toStream().filter((key,message)->message!=null).mapValues((value)->messageDangerToNotifications(value))
                    .to("notificacao",Produced.with(Serdes.String(),CustomSerdes.MessageToNotifications()));
        
        this.streamsController = new KafkaStreams(this.builder.build(),this.config);

        this.streamsController.cleanUp();
        this.streamsController.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(this.streamsController::close));
        // this.debugStream();
    }

}