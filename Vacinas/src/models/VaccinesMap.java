package src.models;
import java.util.HashMap;

public class VaccinesMap{
    private HashMap<Integer,VaccineMessage> mapVaccines = new HashMap<Integer,VaccineMessage>();

    public void addVaccine(VaccineMessage vaccine){
        this.mapVaccines.put(vaccine.getId(),vaccine);
    }

    

    /**
     * @return HashMap<Integer,VaccineMessage> return the mapVaccines
     */
    public HashMap<Integer,VaccineMessage> getMapVaccines() {
        return mapVaccines;
    }

    /**
     * @param mapVaccines the mapVaccines to set
     */
    public void setMapVaccines(HashMap<Integer,VaccineMessage> mapVaccines) {
        this.mapVaccines = mapVaccines;
    }

}