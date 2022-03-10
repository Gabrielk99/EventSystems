package src.models;
import src.types.Coordinates;

/**
 * description of class VaccineMessage{ 
 * classe que representa a mensagem do produtor de vacina
 * @author Gabriel Xavier
 * @version 
 */
public class VaccineMessage{
    private int id;
    private float temperatura;
    private String data;
    private Coordinates localizacao;

    /**
     *
     * @param id id do lote
     * @param temperatura temperatura atual do lote
     * @param data data da medição
     * @param localizacao localização atual do lote
     */
    public VaccineMessage(int id,float temperatura,String data, Coordinates localizacao){
        this.id=id;
        this.temperatura=temperatura;
        this.data=data;
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
     * @return float return the temperatura
     */
    public float getTemperatura() {
        return temperatura;
    }

    /**
     * @param temperatura the temperatura to set
     */
    public void setTemperatura(float temperatura) {
        this.temperatura = temperatura;
    }

    /**
     * @return String return the data
     */
    public String getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(String data) {
        this.data = data;
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