package src.corekafka.simulacao;

import src.types.Coordinates;
import java.util.Random;
import java.util.Calendar;
import java.util.ArrayList;

/**
 * Classe responsável por controlar o caminhamento da entidade no mapa
 * @author Mikaella
 */
public class PositionControlOnMap {
    private ArrayList<Coordinates> coordinates;
    private int currentPosition = -1;
    private double lastTime = 0.0;
    private double timeToNextCoord;
    private double upperbound = 5.0;

    /**
     * Função que gera o tempo que a entidade deve esperar para passar para a próxima coordenada
     * @param factor fator que multiplica o tempo gerado aleatoriamente
     * @return um double aleatório num intervalo de 0 a 5*factor
     */
    private double generateTimeToNextCoord(double factor) {
        Random rand = new Random();
        return rand.nextDouble()*upperbound*factor;
    }

    /**
     * Construtor da classe que leva em consideração um fator de multiplicação do tempo
     * @param coordinates coordenadas em que a entidade irá caminhar
     * @param fator que será usado para gerar o tempo aleatório de espera para a próxima coordenada
     */
    public PositionControlOnMap(ArrayList<Coordinates> coordinates, double factor) {
        this.coordinates = coordinates;
        this.timeToNextCoord = generateTimeToNextCoord(factor);
    }

    /**
     * Construtor da classe básico, o fator é fixado em 1.0
     * @param coordinates coordenadas em que a entidade irá caminhar
     */
    public PositionControlOnMap(ArrayList<Coordinates> coordinates) {
        this.coordinates = coordinates;
        this.timeToNextCoord = generateTimeToNextCoord(1.0);
    }

    /**
     *  Função que calcula a posição atual da entidade baseada no tempo em que permaneceu na última
     *  posição.
     * @return coordenada atual
     */
    public Coordinates getCurrentPosition() {
        double now = Calendar.getInstance().getTime().getTime()/1000;
        double timePassed = now - lastTime;

        if (timePassed >= timeToNextCoord) {
            currentPosition++;
            if (currentPosition == coordinates.size())
                currentPosition = 0;
            lastTime = now;
        }

        return coordinates.get(currentPosition);
    }
}