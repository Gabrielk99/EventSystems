package src.corekafka.consumidor;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.spi.LoggerFactoryBinder;
import com.google.gson.*;

import java.util.Properties;

/**
 * Classe abstrata do consumidor
 */
public abstract class Consumer {
    private KafkaConsumer<String, String> consumer;
    private Logger logger;

    abstract void consumeMessages();
}