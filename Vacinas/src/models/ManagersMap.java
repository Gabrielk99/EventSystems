package src.models;
import java.util.HashMap;

public class ManagersMap{
    private HashMap<Integer,GestorMessage> mapManagers = new HashMap<Integer,GestorMessage>();

    public void addManager(GestorMessage manager){
        this.mapManagers.put(manager.getId(),manager);
    }

    /**
     * @return HashMap<Integer,GestorMessage> return the mapManagers
     */
    public HashMap<Integer,GestorMessage> getMapManagers() {
        return mapManagers;
    }

    /**
     * @param mapManagers the mapManagers to set
     */
    public void setMapManagers(HashMap<Integer,GestorMessage> mapManagers) {
        this.mapManagers = mapManagers;
    }

}