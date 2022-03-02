package src.corekafka.consumidor;

/**
 *  Classe que consome informações de vacinas que estão em status warning e danger no tópico 'notification',
 *  que precisa notificar gestores.
 */
public class NotificationConsumer extends Consumer {
    public NotificationConsumer(String BootstrapServer, String consumerGroupName, String topicToConsume) {
        super(BootstrapServer,consumerGroupName,topicToConsume);
    }

    @Override
    void consumeMessages(){

    }

}