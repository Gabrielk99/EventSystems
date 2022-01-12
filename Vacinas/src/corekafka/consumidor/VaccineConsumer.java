package src.corekafka.consumidor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import src.corekafka.vacina.*;
import java.util.HashMap;
import org.slf4j.spi.LoggerFactoryBinder;
import com.google.gson.*;

import java.util.Properties;
import java.util.Arrays;
import java.time.Duration;
import java.util.Calendar;
import java.util.stream.Stream;

/**
 * Classe que representa o consumidor das informações que os produtores de vacina enviam
 */
public class VaccineConsumer extends Consumer {
    private HashMap<Integer, Vaccine> vacinas;
    private HashMap<Integer, Float> timeWhenReachedMaxTemp;
    private KafkaConsumer<String, String> consumer;
    Logger logger;

    /**
     * Construtor da classe
     * @param BootstrapServer   server onde o kafka está rodando
     * @param consumerGroupName  nome do grupo no qual o consumidor irá fazer parte
     * @param topicToConsume    tópico que vai consumir as mensagens
     * @param pathToVaccinesFolder  pasta onde se encontram os arquivos de configuração das vacinas
     */
    public VaccineConsumer (String BootstrapServer, String consumerGroupName, String topicToConsume, String pathToVaccinesFolder) {
        this.timeWhenReachedMaxTemp = new HashMap<Integer, Float>();
        this.vacinas = searchForVaccines(pathToVaccinesFolder);
//        this.logger = LoggerFactory.getLogger(ProducerDemoCallBack.class);

        // Cria e define as propriedades
        Properties prop = new Properties();
        prop.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        prop.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        prop.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupName);
        prop.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        // Cria consumidor
        this.consumer = new KafkaConsumer<String, String>(prop);

        // Subscribe no tópico
        consumer.subscribe(Arrays.asList(topicToConsume));
    }

    /**
     *  Função responsável por buscar os arquivos de configurações das vacinas
     * @param path  caminho até a pasta onde se encontram os arquivos de configuração das vacinas
     * @return  Um hashmap com as informações das vacinas
     */
    private HashMap<Integer, Vaccine> searchForVaccines(String path) {
        HashMap<Integer, Vaccine> vaccinesFound = new HashMap<Integer, Vaccine>();

        Path allDirVaccines = Paths.get(path);
        try (Stream<Path> paths = Files.walk(allDirVaccines)) {
            paths.filter(file -> file.toString().endsWith(".json")).forEach( vaccineConfig -> {
                Vaccine vaccine = new Vaccine(vaccineConfig.toString());
                vaccinesFound.put(vaccine.getId(), vaccine);
                this.timeWhenReachedMaxTemp.put(vaccine.getId(), (float) 0.0);
            });
        }catch (Exception e) {
            System.out.println(e);
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
    private void generateAndSaveJSON(String id, Integer status, String temperatura, JsonElement location) {
        // Gera objeto json com as informações a serem consumidas pela aplicação
        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", id);
        jsonMessage.addProperty("status", status);
        jsonMessage.addProperty("temperature", temperatura);
        jsonMessage.add("location", location);

        // TODO: Salvar em arquivo

    }

    /**
     * Função responsável por checar o status da vacina dada a temperatura atual e o tempo atual
     * @param id
     * @param currentTime
     * @param temperature
     * @return
     */
    private VaccineStatus checkVaccineStatus(Integer id, float currentTime, float temperature) {
        Vaccine vacina = vacinas.get(id);
        Float timeMaxTemp = timeWhenReachedMaxTemp.get(id);
        float maxTemperatureLim = vacina.getMaxTemperatureLim();

        // Se a temperatura está abaixo do limite, seta o marcador de quando a vacina
        // atingiu o max para o tempo atual para que currentTime - timeMaxTemp seja igual a 0.
        if (temperature < maxTemperatureLim)
            timeMaxTemp = currentTime;

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
        ConsumerRecords<String, String> records= consumer.poll(Duration.ofMillis(100)); // Consome mensagens dos produtores
        float now = Calendar.getInstance().getTime().getTime()/1000; // Pega tempo atual

        for (ConsumerRecord<String, String> record : records) {
            JsonObject vaccineMessage = new JsonParser().parse(record.value()).getAsJsonObject(); // Faz o parse do record recebido para objeto Json
            VaccineStatus status = this.checkVaccineStatus(   // Checa o status da vacina
                    Integer.parseInt(vaccineMessage.get("id").getAsString()),
                    now,
                    Float.parseFloat(vaccineMessage.get("temperatura").getAsString())
            );

            generateAndSaveJSON(
                    vaccineMessage.get("id").getAsString(),
                    status.ordinal(),
                    vaccineMessage.get("temperatura").getAsString(),        // Gera o Json com, principalemente, id e status da vacina num arquivo
                    vaccineMessage.get("location")
                    );

            System.out.println("[VaccineConsumer] Info received: " +
                    " vaccine Id: " + vaccineMessage.get("id") +
                    " temperature: " + vaccineMessage.get("temperatura") +
                    " status: " + status.toString());
        }
    }
}