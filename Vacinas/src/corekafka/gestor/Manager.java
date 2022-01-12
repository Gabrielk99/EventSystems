package src.corekafka.gestor;

import java.nio.file.Files;
import java.nio.file.Paths;
import com.google.gson.*;
import java.util.ArrayList;
import src.types.*;
import java.io.IOException;
import src.corekafka.simulacao.PositionControlOnMap;

/**
 * Classe que representa um gestor
 *
 * @author Mikaella
 */

class Manager {
    private int id;
    private String name;
    private PositionControlOnMap mapController;

    /**
     * Construtor da classe que obtém dados dos gestores (id, nome e coordenadas) a partir de um json
     *
     * @param path_to_json caminho até o arquivo json com as informações do gestor
     */
    public Manager(String path_to_json){
        try {
            String content = String.join("", Files.readAllLines(Paths.get(path_to_json))); // le json e passa para string
            JsonObject manager = new JsonParser().parse(content).getAsJsonObject();       // parse da string para um objeto json

            this.id = Integer.parseInt(manager.get("id").getAsString());
            this.name = manager.get("name").getAsString();
            ArrayList<Coordinates> cords = Coordinates.parseListCoordinates(manager.get("coordinates").toString());
            this.mapController = new PositionControlOnMap(cords);

        } catch (Exception e) {
            System.out.printf("Erro: Arquivo %s de configuração não encontrado!\n", e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Função que retorna posição atual do gestor
     *
     * @return a coordenada atual do gestor
     */
    public Coordinates getCurrentPosition() {
        return mapController.getCurrentPosition();
    }

    public int getId() {
        return this.id;
    }
}