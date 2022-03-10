package src.models;
import src.types.Coordinates;
/**
 * description of class MessageToNotifications{ 
 * classe que representa a mensagem que vai ser enviada para o topico
 * de notificacoes
 * @author Gabriel Xavier
 * @version 
 */

public class MessageToNotifications{
    public Coordinates location;
    public int id_lote; //id do lote de vacina
    public int status; //status do lote
    public int id_gestor;//-1 se é warning, id do gestor se é danger

    /**
     *
     * @param location localização da vacina
     * @param id_lote id do lote de vacina
     * @param status status do lote
     * @param id_gestor id do gestor mais proximo
     */
    public MessageToNotifications(Coordinates location, int id_lote, int status,int id_gestor){
        this.location=location;
        this.id_lote=id_lote;
        this.status=status;
        this.id_gestor=id_gestor;
    }
}