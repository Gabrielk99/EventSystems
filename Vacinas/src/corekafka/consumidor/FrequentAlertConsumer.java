package src.corekafka.consumidor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import com.google.gson.*;

import src.api.*;
import src.models.*;
import src.types.*;

/**
 *  Classe que consome informações sobre vacinas que estão com alertas frequentes e
 *  precisam de ações de mitigação
 *
 * @author mikaella
 */
public class FrequentAlertConsumer extends Consumer {
    public FrequentAlertConsumer(String BootstrapServer, String consumerGroupName, String topicToConsume) {
        super(BootstrapServer,consumerGroupName,topicToConsume);
    }

    @Override
    public void consumeMessages(){
        ConsumerRecords<String, String> records= super.getConsumer().poll(Duration.ofMillis(100)); // Consome mensagens do tópico

        for (ConsumerRecord<String, String> record : records) {
            JsonObject message = new JsonParser().parse(record.value()).getAsJsonObject(); //id_gestor, id_vacina, localizacao e status

            processMessage(message);
        }
    }

    public static void processMessage(JsonObject message) {
        JsonObject vaccineInfoJson = ApiManagerVaccine.getVaccineInfo(message.get("id_lote").getAsInt());
        int status = message.get("status").getAsInt();

        if (status == VaccineStatus.WARNING.ordinal()) {
            System.out.println("[FREQUENT_ALERT_CONSUMER]: Vacina " + vaccineInfoJson.get("name").getAsString()
                    + " está tendo muitos WARNING");

        } else if (status == VaccineStatus.DANGER.ordinal()) {
            System.out.println("[FREQUENT_ALERT_CONSUMER]: Vacina " + vaccineInfoJson.get("name").getAsString()
                    + " está tendo muitos DANGER");

        } else if (status == VaccineStatus.GAMEOVER.ordinal()) {
            System.out.println("[FREQUENT_ALERT_CONSUMER]: Vacina " + vaccineInfoJson.get("name").getAsString()
                    + " está tendo muitos GAMEOVER");
        }

        VaccineInfo vaccineInfo = new VaccineInfo(vaccineInfoJson.get("id").getAsInt(), vaccineInfoJson.get("name").getAsString()+"_"+vaccineInfoJson.get("id").getAsString());

        JsonObject locationJson = message.get("location").getAsJsonObject();
        Coordinates location = new Coordinates(
                locationJson
                        .get("longitude").getAsDouble(),
                locationJson
                        .get("latitude").getAsDouble()
        );

        sendEmailToAllManagers(status, vaccineInfo, location);
    }

    static void sendEmailToAllManagers(int vaccineStatus, VaccineInfo vaccineInfo, Coordinates location) {
        JsonArray managersJsons = ApiManagerVaccine.getAllManagerInfo();

        for (JsonElement manager : managersJsons) {
            JsonObject managerJson = manager.getAsJsonObject();

            EmailMessage email = new EmailMessage(
                    managerJson.get("email").getAsString(),
                    vaccineInfo,
                    vaccineStatus,
                    location,
                    managerJson.get("name").getAsString(),
                    true
            );

            ApiEmail.postEmail(email);
        }
    }
}