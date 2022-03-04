package src.corekafka.consumidor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import com.google.gson.*;

import src.models.*;
import src.api.*;
import src.types.*;

/**
 *  Classe que consome informações de vacinas que estão em status warning e danger no tópico 'notification',
 *  que precisa notificar gestores.
 *
 * @author mikaella
 */
public class NotificationConsumer extends Consumer {
    public NotificationConsumer(String BootstrapServer, String consumerGroupName, String topicToConsume) {
        super(BootstrapServer,consumerGroupName,topicToConsume);
    }

    @Override
    public void consumeMessages(){
        ConsumerRecords<String, String> records= super.getConsumer().poll(Duration.ofMillis(100)); // Consome mensagens do tópico

        for (ConsumerRecord<String, String> record : records) {
            JsonObject notificationMessage = new JsonParser().parse(record.value()).getAsJsonObject(); //id_gestor, id_vacina, localizacao e status

            processMessage(notificationMessage);
        }
    }

    private void processMessage(JsonObject notificationMessage) {
        JsonObject vaccineInfoJson = ApiManagerVaccine.getVaccineInfo(notificationMessage.get("id_lote").getAsInt());
        VaccineInfo vaccineInfo = new VaccineInfo(vaccineInfoJson.get("id").getAsInt(), vaccineInfoJson.get("name").getAsString());

        JsonObject locationJson = notificationMessage.get("location").getAsJsonObject();
        Coordinates location = new Coordinates(
                locationJson
                        .get("longitude").getAsDouble(),
                locationJson
                        .get("latitude").getAsDouble()
        );

        int status = notificationMessage.get("status").getAsInt();

        if (status == VaccineStatus.WARNING.ordinal()) {
            JsonArray managersJsons = ApiManagerVaccine.getAllManagerInfo();

            for (JsonElement manager : managersJsons) {
                JsonObject managerJson = manager.getAsJsonObject();

                EmailMessage email = new EmailMessage(
                        managerJson.get("email").getAsString(),
                        "mikaellaferreira.s@gmail.com", // Isso vai ser retirado depois
                        vaccineInfo,
                        VaccineStatus.WARNING.ordinal(),
                        location,
                        managerJson.get("name").getAsString(),
                        "Rua Clara de Assis, 123", // Isso vai ser retirado depois, pois a api fará a conversao para localização descrita
                        0 // Isso também vai ser retirado
                );

                ApiEmail.postEmail(email);
            }
        } else if (status == VaccineStatus.DANGER.ordinal()) {
            JsonObject managerJson = ApiManagerVaccine.getManagerInfo(notificationMessage.get("id_gestor").getAsInt());

            EmailMessage email = new EmailMessage(
                    managerJson.get("email").getAsString(),
                    "mikaellaferreira.s@gmail.com", // Isso vai ser retirado depois
                    vaccineInfo,
                    VaccineStatus.DANGER.ordinal(),
                    location,
                    managerJson.get("name").getAsString(),
                    "Rua Clara de Assis, 123", // Isso vai ser retirado depois, pois a api fará a conversao para localização descrita
                    0 // Isso também vai ser retirado
            );

            ApiEmail.postEmail(email);
        } else {
            return;
        }
    }
}