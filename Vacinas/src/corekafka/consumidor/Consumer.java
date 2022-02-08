package src.corekafka.consumidor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import com.google.gson.*;

import java.util.Properties;
import java.util.Arrays;
/**
 * Classe do consumidor kafka
 */
public class Consumer {
    private KafkaConsumer<String, String> consumer;

    public Consumer (String BootstrapServer, String consumerGroupName,String topicToConsume){
        
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

    
    public KafkaConsumer<String, String> getConsumer(){
        return this.consumer;
    }
    void consumeMessages(){}
}