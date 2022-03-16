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
import src.models.MessageToAlert;
import src.models.Occurence;
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
import org.apache.kafka.streams.kstream.SlidingWindows;
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
    
    // mapeadores para previnir aglomeracao de envio de email de notificacao
    // garantindo que os filtros considerem se ja houve o aconteciemnto
    private HashMap<Integer, Boolean> warningActive = new HashMap<Integer,Boolean>();
    private HashMap<Integer, Boolean> dangerActiveAlert = new HashMap<Integer,Boolean>();
    private HashMap<Integer, Boolean> dangerActiveNot = new HashMap<Integer,Boolean>();
    private HashMap<Integer, Boolean> gameoverActive = new HashMap<Integer,Boolean>();
    
    // mapeadoress para previnir agllomeracao de email de alerta
    // garantindo que o filtro de contador de ocorrencias verifique
    // se ja houve ocorrencia previa, cada ocorrencia deve ser contabilizada
    // apenas apos o tamanho da janela de lidar com ocorrencias
    private HashMap<Integer, Long> warningAlert = new HashMap<Integer,Long>();
    private HashMap<Integer, Long> dangerAlert = new HashMap<Integer,Long>();
    private HashMap<Integer, Long> gameoverAlert = new HashMap<Integer,Long>();
    
    private Properties config;
    
    // janela de tempo de situacoes de alerta e maximo de ocorrencias
    // propriedade global generica para todos lotes
    private int max_occurrences_warning = 3;
    private int max_occurrences_danger  = 2;
    private int max_occurrences_gameover = 1;
    
    private long window_size = 400; //segundos

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
        if(status==VaccineStatus.OK){
            warningActive.put(message.getId(),true);
        }
        else if(status==VaccineStatus.WARNING){
            if(warningActive.containsKey(message.getId()) && warningActive.get(message.getId())){
                warningActive.put(message.getId(),false);
                return true;
            }
        }
        else{
            return false;
        }
        return false;
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
        int id = message.getVaccines().getId();
        if(status==VaccineStatus.OK){
            dangerActiveNot.put(id,true);
        }
        else if(dangerActiveNot.containsKey(id) && status==VaccineStatus.DANGER){
            if(dangerActiveNot.get(id)){
                dangerActiveNot.put(id,false);
                return true;
            }
        }
        else{
            return false;
        }
        return false;
    }
    /**
     *
     * @param key chave da mensagem
     * @param message mensagem do produtor de vacina
     * @return boolean,true para vacinas que estao na situacao de danger
     *          false caso contrario.
     */
    public boolean filterDanger(String key, VaccineMessage message){
        VaccineStatus status = this.getStatus(message);
        if(status==VaccineStatus.OK){
            dangerActiveAlert.put(message.getId(),true);
        }
        else if(dangerActiveAlert.containsKey(message.getId()) && status==VaccineStatus.DANGER){
            if(dangerActiveAlert.get(message.getId())){
                dangerActiveAlert.put(message.getId(),false);
                return true;
            }
        }
        else{
            return false;
        }
        return false;
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
        if(status==VaccineStatus.OK){
            gameoverActive.put(message.getId(),true);
        }
        else if(gameoverActive.containsKey(message.getId()) && status==VaccineStatus.GAMEOVER){
            if(gameoverActive.get(message.getId())){
                gameoverActive.put(message.getId(),false);
                return true;
            }
        }
        else{
            return false;
        }
        return false;
    }

   
    /**
     * Esse é um metodo especial para construir a topologia que filtrar os gestores mais 
     * proximos das vacinas no status danger
     */
    private KTable <String,DangerJoinManagers> buildTopologyToDangersNotification(){
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
        return dangerWithManager;
    }

    /**
     *
     * @param vaccine VaccineMessage, mensagem da vacina
     * @param status VaccineStatus, status da vacina
     * 
     * @return String, mensagem final apra o topico do frontend
     */
    public String createMessageToFrontEndTopic(VaccineMessage vaccine){
        JsonObject message = new JsonObject();
        JsonObject localizacao = new JsonObject();
        localizacao.addProperty("latitude",vaccine.getLocalizacao().getLatitude());
        localizacao.addProperty("longitude",vaccine.getLocalizacao().getLongitude());

        VaccineStatus status = this.getStatus(vaccine);
        message.addProperty("status",status.ordinal());
        message.addProperty("id",vaccine.getId());
        message.addProperty("temperatura",vaccine.getTemperatura());
        message.add("location",localizacao);
        message.addProperty("data",vaccine.getData());

        return new Gson().toJson(message);
    } 

    /**
     * Cria a topologia que constroi e filtra as mensagens de alerta
     * 
     * 
     * @param stream
     * @param status
     */
    
    public KTable <String,MessageToAlert> buildTopologyToAlert(KStream <String,VaccineMessage> stream,VaccineStatus status){
        // cria uma tabela de ocorrencias de problemas
        // baseado numa janela deslizante de this.window_size segundos
        // o segundo parametro é uma taxa de aceitação
        KTable <Windowed<String>,Long> countOccorencia = stream.groupByKey().windowedBy(SlidingWindows.withTimeDifferenceAndGrace(Duration.ofSeconds(this.window_size),Duration.ofSeconds(1)))
                                                        .count();
                                                        
       
        //transforma a tabela de ocorrencias em uma tabela que possui
        //as informações de vacina e a quantidade de problemas que deu no lote 
        KTable <String,Occurence> vaccinesAndOcurrence = countOccorencia.toStream().selectKey((k,v)->k.key()).toTable().join(mainStream.toTable(),
                                                                                                    (counts,vaccine)->{
                                                                                                        return new Occurence(vaccine,counts);
                                                                                                    },
                                                                                                    Materialized.with(Serdes.String(),CustomSerdes.Occurence())
                                                                                                    );
        //filta as ocorrencias para aquelas que ocorreram a cima do limite
        //permitido para cada tipo de situacao 
        KTable <String,Occurence> lotesToAlert = vaccinesAndOcurrence.filter((key,value)->{
            switch(status){
                case WARNING:{
                    int id = value.getVaccine().getId();
                    VaccineStatus statusV = this.getStatus(value.getVaccine());
                    // ja teve mais ocorrencias que o permitido?
                    if(value.getCount()>=this.max_occurrences_warning){
                        long now = Calendar.getInstance().getTime().getTime()/1000;
                        // o tempo atual é maior que o tempo de espera de uma nova notificação?
                        if(warningAlert.containsKey(id) && now >= warningAlert.get(id) && statusV==VaccineStatus.WARNING){
                            warningAlert.put(id,now+this.window_size);
                            return true;
                        }
                        // não existe tempo guardado? primeira ocorrencia
                        else if(!warningAlert.containsKey(id)){
                            warningAlert.put(id,now+this.window_size);
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }   
                case DANGER:{
                    int id = value.getVaccine().getId();
                    VaccineStatus statusV = this.getStatus(value.getVaccine());
                    // ja teve mais ocorrencias que o permitido?
                    if(value.getCount()>=this.max_occurrences_danger){
                        long now = Calendar.getInstance().getTime().getTime()/1000;
                        // o tempo atual é maior que o tempo de espera de uma nova notificação?
                        if(dangerAlert.containsKey(id) && now >= dangerAlert.get(id) && statusV==VaccineStatus.DANGER){
                            dangerAlert.put(id,now+this.window_size);
                            return true;
                        }
                        // não existe tempo guardado? primeira ocorrencia
                        else if(!dangerAlert.containsKey(id)){
                            dangerAlert.put(id,now+this.window_size);
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                case GAMEOVER:{
                    int id = value.getVaccine().getId();
                    VaccineStatus statusV = this.getStatus(value.getVaccine());
                    // ja teve mais ocorrencias que o permitido?
                    if(value.getCount()>=this.max_occurrences_gameover){
                        long now = Calendar.getInstance().getTime().getTime()/1000;
                        // o tempo atual é maior que o tempo de espera de uma nova notificação?
                        if(gameoverAlert.containsKey(id) && now >= gameoverAlert.get(id) && statusV==VaccineStatus.GAMEOVER){
                            gameoverAlert.put(id,now+this.window_size);
                            return true;
                        }
                        // não existe tempo guardado? primeira ocorrencia
                        else if(!gameoverAlert.containsKey(id)){
                            gameoverAlert.put(id,now+this.window_size);
                            return true;
                        }
                        else{
                            return false;
                        }
                    }
                    else{
                        return false;
                    }
                }
                default:
                    return false;
            }
        });
        vaccinesAndOcurrence.toStream().mapValues((k,v)->status+" key: "+k+" valor : "+Long.toString(v.getCount()))
                                .to("debug");

        return lotesToAlert.mapValues((value)->new MessageToAlert(value.getVaccine().getId(),status.ordinal(),value.getVaccine().getLocalizacao()));
        // return messages;
        
    }
    /**
     *  aplica o processamento stream gerando as informações necessarias
     * @return nothing
     */
    public void run(){
       //Filtrando as situações dos lotes
        KStream <String,VaccineMessage> warningVaccines = this.mainStream.filter((key,message)->this.filterWarning(key,message));       
        KStream <String,VaccineMessage> gameOverVaccines = this.mainStream.filter((key,message)->this.filterGameOver(key,message));
        // apenas existe para contar as situações de cada lote
        // (esta mantem todos ocorrimentos, a outra descarta)
        KStream <String,VaccineMessage> dangerVaccines2Alert = this.mainStream.filter((key,message)->this.filterDanger(key,message));
        // constroi a topologia que filtra os gestores mais proximos dos lotes em danger
        KTable <String,DangerJoinManagers> dangerWithManager = buildTopologyToDangersNotification();
        // filtra as vacinas que estao no status danger
        KTable <String,DangerJoinManagers> dangerVaccines = dangerWithManager.filter((key,message)->this.filterDanger(key,message));
        
        // -------------- Envia as mensagens de email ----------------- \\
        // limpa as mensagens nulas, gera a mensagem final e envia para notificação
        dangerVaccines.toStream().filter((key,message)->message!=null).mapValues((value)->messageDangerToNotifications(value))
                    .to("notificacao",Produced.with(Serdes.String(),CustomSerdes.MessageToNotifications()));
        // manda as mensagens de warning para notificacao
        warningVaccines.mapValues(message->messageWarningToNotifications(message))
                       .to("notificacao",Produced.with(Serdes.String(),CustomSerdes.MessageToNotifications()));

        //-------------------- Envia as informações de vacina pro frontend --------------\\
            mainStream.mapValues((value)->createMessageToFrontEndTopic(value))
                      .to("frontend-vaccine",Produced.with(Serdes.String(),Serdes.String()));

        // ---------------------- construindo a topologia e mensagens de alerta ---------------\\
        buildTopologyToAlert(warningVaccines,VaccineStatus.WARNING).toStream().to("alertas",Produced.with(Serdes.String(),CustomSerdes.MessageToAlert()));
        buildTopologyToAlert(dangerVaccines2Alert,VaccineStatus.DANGER).toStream().to("alertas",Produced.with(Serdes.String(),CustomSerdes.MessageToAlert()));
        buildTopologyToAlert(gameOverVaccines,VaccineStatus.GAMEOVER).toStream().to("alertas",Produced.with(Serdes.String(),CustomSerdes.MessageToAlert()));
        // ----------------- inicia o KafkaStream ----------------------\\
        this.streamsController = new KafkaStreams(this.builder.build(),this.config);

        this.streamsController.cleanUp();
        this.streamsController.start();

        // shutdown hook to correctly close the streams application
        Runtime.getRuntime().addShutdownHook(new Thread(this.streamsController::close));
        // this.debugStream();
    }

}
