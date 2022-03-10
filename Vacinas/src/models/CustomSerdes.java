package src.models;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;

/**
 * description of class {
 * classe que representa Serdes Kafkas customizados para classes genericas
 * @author Gabriel Xavier
 * @version 
 */
public final class CustomSerdes {
    
    private CustomSerdes() {}
    
    public static Serde<VaccineMessage> VaccineMessage() {
        JsonSerializer<VaccineMessage> serializer = new JsonSerializer<>();
        JsonDeserializer<VaccineMessage> deserializer = new JsonDeserializer<>(VaccineMessage.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<GestorMessage> GestorMessage() {
        JsonSerializer<GestorMessage> serializer = new JsonSerializer<>();
        JsonDeserializer<GestorMessage> deserializer = new JsonDeserializer<>(GestorMessage.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<DangerJoinManagers> DangerJoinManagers() {
        JsonSerializer<DangerJoinManagers> serializer = new JsonSerializer<>();
        JsonDeserializer<DangerJoinManagers> deserializer = new JsonDeserializer<>(DangerJoinManagers.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<MessageToNotifications> MessageToNotifications(){
        JsonSerializer<MessageToNotifications> serializer = new JsonSerializer<>();
        JsonDeserializer<MessageToNotifications> deserializer = new JsonDeserializer<>(MessageToNotifications.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
  
    public static Serde<ManagersMap> ManagersMap(){
        JsonSerializer<ManagersMap> serializer = new JsonSerializer<>();
        JsonDeserializer<ManagersMap> deserializer = new JsonDeserializer<>(ManagersMap.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<VaccinesMap> VaccinesMap(){
        JsonSerializer<VaccinesMap> serializer = new JsonSerializer<>();
        JsonDeserializer<VaccinesMap> deserializer = new JsonDeserializer<>(VaccinesMap.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
    public static Serde<MessageToAlert> MessageToAlert(){
        JsonSerializer<MessageToAlert> serializer = new JsonSerializer<>();
        JsonDeserializer<MessageToAlert> deserializer = new JsonDeserializer<>(MessageToAlert.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }

    public static Serde<Occurence> Occurence(){
        JsonSerializer<Occurence> serializer = new JsonSerializer<>();
        JsonDeserializer<Occurence> deserializer = new JsonDeserializer<>(Occurence.class);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}