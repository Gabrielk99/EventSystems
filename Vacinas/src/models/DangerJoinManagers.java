package src.models;

import java.util.ArrayList;
import java.util.HashMap;
/**
 * description of class DangerJoinManagers{ 
 * classe que representa o join entre uma mensagem de vacina
 * e todas mensagens de gestores
 * @author Gabriel Xavier
 * @version 
 */
public class DangerJoinManagers{
    private VaccineMessage vaccines;
    private ManagersMap managers;
   
    /**
         *
     * @param vaccines mensagem da vacina 
     * @param managers todas as mensagens dos gestores
     */

    public DangerJoinManagers(VaccineMessage vaccines, ManagersMap managers){
        this.vaccines=vaccines;
        this.managers=managers;
    }
    

  

    /**
     * @return VaccineMessage return the vaccines
     */
    public VaccineMessage getVaccines() {
        return vaccines;
    }

    /**
     * @param vaccines the vaccines to set
     */
    public void setVaccines(VaccineMessage vaccines) {
        this.vaccines = vaccines;
    }

    /**
     * @return ManagersMap return the managers
     */
    public ManagersMap getManagers() {
        return managers;
    }

    /**
     * @param managers the managers to set
     */
    public void setManagers(ManagersMap managers) {
        this.managers = managers;
    }

}