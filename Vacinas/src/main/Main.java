package src.main;
import src.corekafka.gestor.*;
import src.corekafka.vacina.*;
import java.util.concurrent.TimeUnit;
import src.corekafka.consumidor.*;

public class Main{

    public static void main(String args[]) {

        VaccineProducer producer = new VaccineProducer("localhost:9092","./src/Dados/Vacinas/Vacina_Pfizer/configure.json");
        VaccineConsumer consumer = new VaccineConsumer("localhost:9092", "consumerVacina", "vacina", "./src/Dados/Vacinas/");
        ManagerProducer producerManager = new ManagerProducer("localhost:9092","./src/Dados/Gestores/Gestor0/rota.json");
        ManagerConsumer consumerManager = new ManagerConsumer("localhost:9092", "consumerManager", "gestor");
        while(true){
            producer.sendMessage();
            producerManager.sendLocation();
            consumer.consumeMessages();
            consumerManager.consumeMessages();
            
            try{
                TimeUnit.SECONDS.sleep(1);
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


}