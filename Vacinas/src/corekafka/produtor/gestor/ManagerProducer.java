package src.corekafka.produtor.gestor;

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

import src.corekafka.produtor.Producer;

/**
 * Classe que representa um produtor kafka do tipo gestor
 * @author Mikaella
 */
public class ManagerProducer extends Producer {
    private Manager manager;

    /**
     * Construtor da classe de produtor kafka
     * @param BootstrapServer server onde o kafka está rodando
     * @param pathToJson      caminho até as informações do gestor contidas num json
     */
    public ManagerProducer(String BootstrapServer, JsonObject manager) {
        super(BootstrapServer);

        this.manager = new Manager(manager);
    }

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
     * Função que envia a localização atual do gestor para o tópico "gestor"
     */
    @Override
    public void sendMessage() {
        // Pega localização atual e cria string do json
        Coordinates currentLocation = manager.getCurrentPosition();
        String message = generateJsonMessage(currentLocation);
        String id = Integer.toString(manager.getId());

        //Cria um producer record e envia
        ProducerRecord<String, String> record = new ProducerRecord<String, String>("gestor",id, message);
        super.getProducer().send(record);

        super.getProducer().flush();
    }

}