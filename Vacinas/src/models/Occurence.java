package src.models;

/**
 * description of class Occurence{ 
 *
 * Representa o lote e quantas ocorrencias x de problema
 * 
 * @author Gabriel xavier
 * @version 
 */
public class Occurence{

    private VaccineMessage vaccine;
    private long count;

    public Occurence(VaccineMessage vaccine,long count){
        
        this.vaccine = vaccine;
        this.count=count;

    }


    /**
     * @return VaccineMessage return the vaccine
     */
    public VaccineMessage getVaccine() {
        return vaccine;
    }

    /**
     * @param vaccine the vaccine to set
     */
    public void setVaccine(VaccineMessage vaccine) {
        this.vaccine = vaccine;
    }

    /**
     * @return long return the count
     */
    public long getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(long count) {
        this.count = count;
    }

}