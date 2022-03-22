package src.models;
import src.types.Coordinates;

/**
 * description of class MessageToAlert{ 
 * Representa mensagem para enviar pro topico alerta
 * 
 * @author Gabriel Xavier
 * @version 1.0
 */
public class MessageToAlert{

    private int id_lote;
    private int status;
    private Coordinates location;

    public MessageToAlert(int id, int status, Coordinates localizacao){
        this.id_lote = id;
        this.status = status;
        this.location = localizacao;
    }

    /**
     * @return int return the id_lote
     */
    public int getId_lote() {
        return id_lote;
    }

    /**
     * @param id_lote the id_lote to set
     */
    public void setId_lote(int id_lote) {
        this.id_lote = id_lote;
    }

    /**
     * @return int return the status
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(int status) {
        this.status = status;
    }

    /**
     * @return Coordinates return the localizacao
     */
    public Coordinates getLocation() {
        return location;
    }

    /**
     * @param localizacao the localizacao to set
     */
    public void setLocation(Coordinates localizacao) {
        this.location = localizacao;
    }

}