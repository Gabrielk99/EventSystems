package src.corekafka.gestor;

import src.corekafka.simulacao.PositionControlOnMap;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import src.types.*;
import com.google.gson.*;

import java.util.Properties;

/**
 * Classe que representa um produtor kafka do tipo gestor
 * @author Mikaella
 */
public class ManagerProducer {
    private Manager manager;
    KafkaProducer<String, String> producer;
   Logger logger;

    /**
     * Função que gera uma string a partir de um json gerado com a localização e id do gestor
     * @param currentLocation localização atual do gestor
     * @return string gerada a partir do json
     */
    private String generateJsonMessage(Coordinates currentLocation) {
        JsonObject jsonLocation = new JsonObject();
        jsonLocation.addProperty("latitude", currentLocation.getLatitude());
        jsonLocation.addProperty("longitude", currentLocation.getLongitude());

        JsonObject jsonMessage = new JsonObject();
        jsonMessage.addProperty("id", manager.getId());
        jsonMessage.add("localizacao", jsonLocation);

        return jsonMessage.toString();
    }

   /**
    * Função callback de log do envio de uma mensagem pelo produtor
    * @return callback que printa os metadaos recebidos
    */
   private Callback callBackLogger() {
       return new Callback() {
           @Override
           public void onCompletion(RecordMetadata recordMetadata, Exception e) {
               //executes a record if success or exception is thrown
               if (e == null) {
                   logger.info("Metadados recebidos \n " +
                           "Topic " + recordMetadata.topic() + "\n " +
                           "Partition: " + recordMetadata.partition() + "\n" +
                           "Offset: " + recordMetadata.offset() + "\n" +
                           "Timestamp: " + recordMetadata.timestamp());
               } else {
                   logger.error("Algo deu errado");
               }
               ;
           }
       };
   }

    /**
     * Construtor da classe de produtor kafka de gestor
     * @param BootstrapServer server onde o kafka está rodando
     * @param pathToJson      caminho até as informações do gestor contidas num json
     */
    public ManagerProducer(String BootstrapServer, String pathToJson) {
        this.manager = new Manager(pathToJson);
//        this.logger = LoggerFactory.getLogger(ProducerDemoCallBack.class);

        // Cria e define as propriedades
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Cria um produtor <chave, valor>
        producer = new KafkaProducer<String, String>(prop);
    }

    /**
     * Função que envia a localização atual do gestor para o tópico "gestor"
     */
    public void sendLocation() {
        // Pega localização atual e cria string do json
        Coordinates currentLocation = manager.getCurrentPosition();
        String message = generateJsonMessage(currentLocation);

        //Cria um producer record e envia
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("gestor", message);
        producer.send(record);
//                , this.callBackLogger());

        producer.flush();
    }

}