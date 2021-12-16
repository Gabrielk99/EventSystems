package corekafka.vacina;
import java.util.concurrent.TimeUnit;

public class MainTeste{

    public static void main(String args[]) {

        // TemperatureConfigurate teste = new TemperatureConfigurate(2,5,8,10,50,5);

        // System.out.println(teste.getListOfChuncks());
        // System.out.println(teste.getIncTemperature());

        // teste.start();

        // System.out.println(teste.getTimeToIncrement());
      
      
        Vaccine vacina = new Vaccine("/home/gabriel/Desktop/My_tasks/ufes/2021.2/sys.regras/EventSystems/Vacinas/src/Dados/Vacinas/Vacina_Pfizer/configure.json");
      
        // while(true){

        //     long time_to_inc = teste.getTimeToIncrement();
        //     long time_stamp = teste.getCurrentTimeStamp();
        //     long init_time_stamp = teste.getInitialTimeStamp();
        //     System.out.printf("tI:%d  tS:%d\n",time_to_inc+init_time_stamp,time_stamp);

        //     if(time_stamp>=time_to_inc+init_time_stamp){
        //         teste.start();
        //     }

        //     try{
        //         TimeUnit.SECONDS.sleep(1);
        //     }
        //     catch(InterruptedException e){
        //         Thread.currentThread().interrupt();
        //     }
        //     System.out.println(teste.getTemperatureCurrent());
        //     System.out.println(teste.getCurrentDate());
        // }
    }


}