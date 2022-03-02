package src.corekafka.produtor;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {
    KafkaProducer<String, String> producer;

    /**
     * Construtor da classe de produtor kafka
     * @param BootstrapServer server onde o kafka está rodando
     */
    public Producer(String BootstrapServer) {
        // Cria e define as propriedades
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Cria um produtor <chave, valor>
        producer = new KafkaProducer<String, String>(prop);
    }

    public void sendMessage() {}

    public KafkaProducer<String, String> getProducer() { return this.producer; }
}