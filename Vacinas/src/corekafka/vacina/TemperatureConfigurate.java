package src.corekafka.vacina;

import java.util.ArrayList;
import java.util.Random;
import java.util.Calendar;
import java.time.Duration;
import java.util.Date;

// TODO::COMENTAR O CÓDIGO

public class TemperatureConfigurate {

    private float t_init;
    private float t_midle_limit;
    private float t_max_limite;
    private float t_current;

    private float time_wait_max=60; //segundos
    private float time_max_limit_T;
    private ArrayList<Float> list_of_chuncks_times = new ArrayList<Float>();
    
    private float inc;
    private int chuncks=5;

    private long time_to_increment;
    private Date current_date;
    private long current_timestamp;
    private long initial_timestamp;
    
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim,float time_w_max,int chunks){

        /*
            t_i: temperatura padrão da vacina
            t_m_l: temperatura limite media (aviso para todos os gestores)
            t_max_l: temperatura limite maxima (aviso para o gestor mais proximo)
            time_max_lim: tempo limite apos passar a temperatura limite maxima (sugerir descarte)
            time_w_max (opicional): tempo limite de espera para simular
            chunks(opicional): pedaços de tempo para sortear aleatoriamente os tempos de incremento 
        */


        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;

        this.time_wait_max = time_w_max;
        this.time_max_limit_T=time_max_lim;

        this.chuncks=chuncks;

        configureValues();
    }
     public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim,int chuncks){

        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;

        this.time_max_limit_T=time_max_lim;

        this.chuncks=chuncks;

        configureValues();
    }
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim,float time_w_max){

        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;
        
        this.time_wait_max = time_w_max;
        this.time_max_limit_T=time_max_lim;


        configureValues();
    }
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim){

        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;

        this.time_max_limit_T=time_max_lim;
        configureValues();
    }


    public void configureValues(){

        float totalTime = this.time_wait_max+this.time_max_limit_T;
        totalTime+=0.2*totalTime;

        for(int i =0;i<=this.chuncks;i++){

            this.list_of_chuncks_times.add(totalTime/this.chuncks*(i));

        }

        this.inc = (this.t_max_limite-this.t_init)/totalTime;
        
    }

    public float getTemperatureCurrent(){
        long time_difference = this.computeDifferenceTime();
        if(time_difference>1){
            this.t_current+=this.inc;
            this.current_timestamp+=1;
        }
        this.current_date = Calendar.getInstance().getTime();
        return this.t_current;
    }
    
    public float getTemperatureInit(){
        return this.t_init;
    }

    public float getTemperatureMidleLimite(){
        return this.t_midle_limit;
    }

    public float getTemperatureMaxLimite(){
        return this.t_max_limite;
    }

    public float getTimeMaxWait(){
        return this.time_wait_max;
    }

    public float getTimeMaxTemperatureLim(){
        return this.time_max_limit_T;
    }
    
    public ArrayList<Float> getListOfChuncks(){
        return this.list_of_chuncks_times;
    }

    public float getIncTemperature(){
        return this.inc;
    }

    public int getChuncks(){
        return this.chuncks;
    }

    public long getTimeToIncrement(){
        return this.time_to_increment;
    }


    public long getCurrentTimeStamp(){
        return this.current_timestamp;
    }
    

    public Date getCurrentDate(){
        return this.current_date;
    }

    public long getInitialTimeStamp(){
        return this.initial_timestamp;
    }

    public void generateTimeToIncrement(){
        Random random = new Random();

        int index_random = random.nextInt(this.list_of_chuncks_times.size());

        this.time_to_increment = this.list_of_chuncks_times.get(index_random).longValue();
    }

    public void start(){
        generateTimeToIncrement();
        this.t_current=this.t_init;
        this.current_date = Calendar.getInstance().getTime();
        this.initial_timestamp = current_date.getTime()/1000;
        this.current_timestamp = this.current_date.getTime()/1000;
    }
    
    public long computeDifferenceTime(){

        long now = Calendar.getInstance().getTime().getTime()/1000;
        long difference = now-this.current_timestamp;

        return difference;
        
    }

}