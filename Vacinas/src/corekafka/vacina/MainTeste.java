package src.corekafka.vacina;
import java.util.concurrent.TimeUnit;
import src.corekafka.consumidor.*;

public class MainTeste{

    public static void main(String args[]) {

        VaccineProducer producer = new VaccineProducer("localhost:9092","./src/Dados/Vacinas/Vacina_Pfizer/configure.json");
        VaccineConsumer consumer = new VaccineConsumer("localhost:9092", "consumerVacina", "vacina", "./src/Dados/Vacinas/");

        while(true){
            producer.sendMessage();

            consumer.consumeMessages();
            try{
                TimeUnit.SECONDS.sleep(1);
            }
            catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }


}