package vacina;
import java.util.concurrent.TimeUnit;

public class MainTeste{

    public static void main(String args[]) {

        TemperatureConfigurate teste = new TemperatureConfigurate(2,5,8,10,50,5);

        System.out.println(teste.getListOfChuncks());
        System.out.println(teste.getIncTemperature());

        teste.start();

        System.out.println(teste.getTimeToIncrement());
        System.out.println(teste.getCurrentTimeStamp());
        
        try{
            TimeUnit.SECONDS.sleep(5);
        }
        catch(InterruptedException e){
            Thread.currentThread().interrupt();
        }

        System.out.println(teste.computeDifferenceTime());
    }


}