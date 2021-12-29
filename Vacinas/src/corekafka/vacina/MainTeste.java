package src.corekafka.vacina;
import java.util.concurrent.TimeUnit;

public class MainTeste{

    public static void main(String args[]) {

        VaccineProducer producer = new VaccineProducer("localhost:9092","./src/Dados/Vacinas/Vacina_Pfizer/configure.json");
        
        while(true){
            producer.sendMessage();
            try{
                TimeUnit.SECONDS.sleep(1);
            }
            catch(InterruptedException e){
                Thread.currentThread().interrupt();
            }
        }
    }


}