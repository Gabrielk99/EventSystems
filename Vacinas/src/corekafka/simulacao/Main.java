package src.corekafka.simulacao;

import src.types.Coordinates;
import src.corekafka.gestor.ManagerProducer;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Main{
    public static void main(String args[]) {
        ArrayList<Coordinates> cords = new ArrayList<Coordinates>();
        for (int i = 0; i<5; i++) {
            cords.add(new Coordinates(i,i));
        }

        PositionControlOnMap controller = new PositionControlOnMap(cords);

        for (int i = 0; i<10; i++) {
            ManagerProducer gestor = new ManagerProducer("localhost:9092", "./src/Dados/Gestores/Gestor0/rota.json");
            gestor.sendLocation();

        }

    }

}