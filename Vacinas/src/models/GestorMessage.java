package src.models;
import src.types.Coordinates;

/**
 * description of class GestorMessage{ 
 * classe que representa a mensagem do produtor de gestor
 * @author Gabriel Xavier
 * @version 
 */

public class GestorMessage{
    private int id;
    private Coordinates localizacao;

    public GestorMessage(int id,Coordinates localizacao){
        this.id=id;
        this.localizacao=localizacao;
    }

    /**
     * @return int return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Coordinates return the localizacao
     */
    public Coordinates getLocalizacao() {
        return localizacao;
    }

    /**
     * @param localizacao the localizacao to set
     */
    public void setLocalizacao(Coordinates localizacao) {
        this.localizacao = localizacao;
    }


}