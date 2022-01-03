package src.corekafka.simulacao;

import java.util.ArrayList;
import java.util.Random;
import java.util.Calendar;
import java.time.Duration;
import java.util.Date;

/**
 * Classe para simular a temperatura do lote
 * @author Gabriel Xavier
 */
public class TemperatureConfigurate {

    private float t_init; //Temperatura inicial
    private float t_midle_limit; //Temperatura limite media (avisar todos gestores)
    private float t_max_limite; //Temperatura maxima limite (avisar o gestor mais proximo)
    private float t_current; //Temperatura corrente

    private float time_wait_max=60; //tempo maximo em segundos que estamos aptos a esperar pela simulacao
    private float time_max_limit_T; //tempo maximo que a vacina que instancia a simulacao aguenta
                                    //fora do limite max de temperatura

    private ArrayList<Float> list_of_chuncks_times = new ArrayList<Float>();//Lista de pedaços de tempo
                                                    //usados para fazer o sorteio de forma a definir quanto 
                                                    //tempo a temperatura sera incrementada
    
    private float inc; //valor de incremento de temperatura
    private int chuncks=5; //pedaços de tempo que vamos sortear, usado para calcular
                        //cada temperatura na lista de pedaços de tempo pro sorteio  

    private long time_to_increment; //quanto tempo de incremento será realizado
    private Date current_date; //data corrente da ultima consulta de temperatura
    private long current_timestamp; //timestamp corrente da ultima consulta de temperatura
                                    // baseado na simulacao 
    private long initial_timestamp; //timestamp inicial que foi iniciado a simulacao
    
    private boolean reset = false;
    /**
     * 
     * @param t_i temperatura inicial
     * @param t_m_l temperatura mediana do limite (avisa todos os gestores)
     * @param t_max_l temperatura maxima do limite (avisa o gestor mais proximo)
     * @param time_max_lim tempo maximo que o lote aguenta apos o limite ser ultrapassado
     * @param time_w_max tempo maximo de espera da simulacao
     * @param chunks pedacos de tempo para realizar sorteio
     */
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim,float time_w_max,int chunks){
        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;

        this.time_wait_max = time_w_max;
        this.time_max_limit_T=time_max_lim;

        this.chuncks=chuncks;

        this.configureValues(); //configura valores primordiais da simulação
        this.start();//inicializa a simulacao
    }
    
    /**
     * 
     * @param t_i temperatura inicial
     * @param t_m_l temperatura mediana do limite
     * @param t_max_l temperatura maxima do limite
     * @param time_max_lim tempo max apos o limite
     * @param chuncks pedacos de tempo pro sorteio
     */
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim,int chuncks){

        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;

        this.time_max_limit_T=time_max_lim;

        this.chuncks=chuncks;

        this.configureValues();
        this.start();//inicializa a simulacao
    }

    /**
     * 
     * @param t_i temperatura inicial
     * @param t_m_l temperatura mediana do limite
     * @param t_max_l temperatura maxima do limite
     * @param time_max_lim tempo max apos o limite
     * @param time_w_max tempo maximo de espera da simulacao
     */
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim,float time_w_max){

        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;
        
        this.time_wait_max = time_w_max;
        this.time_max_limit_T=time_max_lim;


        this.configureValues();
        this.start();//inicializa a simulacao
    }
    /**
     * 
     * @param t_i temperatura inicial
     * @param t_m_l temperatura mediana do limite
     * @param t_max_l temperatura maxima do limite
     * @param time_max_lim tempo max apos o limite
     */
    public TemperatureConfigurate(float t_i,float t_m_l,float t_max_l,
                           float time_max_lim){

        this.t_init=t_i;
        this.t_midle_limit=t_m_l;
        this.t_max_limite=t_max_l;

        this.time_max_limit_T=time_max_lim;

        this.configureValues();
        this.start();//inicializa a simulacao
    }

    /**
     * calcula o tempo total max que a simulacao pode ocorrer
        // depende do tempo max de espera da simulacao e o tempo limite 
        // que a vacina aguenta depois do max de sua temperatura
        // e uma taxa de erro de 20%
     */
    public void configureValues(){

        float totalTime = this.time_wait_max+this.time_max_limit_T;
        totalTime+=0.2*totalTime;

        // adiciona cada pedaço de tempo baseado no numero de pedaços
        for(int i =0;i<=this.chuncks;i++){

            this.list_of_chuncks_times.add(totalTime/this.chuncks*(i));

        }

        //o incremento é a diferença da temperatura max menos a inicial
        // sobre o tempo total max de simulacao
        this.inc = (this.t_max_limite-this.t_init)/totalTime;
        
    }

    /**
     * 
     * @return retorna a temperatura atual do lote
     */
    public float getCurrentTemperature(){
        // pega a diferença de tempo entre essa consulta e a ultima
        long time_difference = this.computeDifferenceTime();

        // verifica se a simulacao deve resetar
        this.verifyRestartSimu();

        // se passou de 1 segundo deve atualizar a temperatura
        if(time_difference>1){
            this.t_current+=this.inc;
            this.current_timestamp+=1;
        }

        //atualizar a data em que a ultima consulta foi realizada
        this.current_date = Calendar.getInstance().getTime();
        return this.t_current;
    }
    
    /**
     * 
     * @return  a temperatura inicial do lote
     */
    public float getTemperatureInit(){
        return this.t_init;
    }

    /**
     * 
     * @return a temperatura limite mediana
     */
    public float getTemperatureMidleLimite(){
        return this.t_midle_limit;
    }
    /**
     * 
     * @return  a temperatura limite maxima
     */
    public float getTemperatureMaxLimite(){
        return this.t_max_limite;
    }
    /**
     * 
     * @return o tempo maximo de espera da simulacao
     */
    public float getTimeMaxWait(){
        return this.time_wait_max;
    }

    /**
     * 
     * @return o tempo maximo apos o limite de temperatura
     */
    public float getTimeMaxTemperatureLim(){
        return this.time_max_limit_T;
    }
    /**
     * 
     * @return a lista de pedacos de tempo para sortear
     */
    public ArrayList<Float> getListOfChuncks(){
        return this.list_of_chuncks_times;
    }
    /**
     * 
     * @return o incremento da temperatura
     */
    public float getIncTemperature(){
        return this.inc;
    }
    /**
     * 
     * @return o numero de pedacos de tempo para gerar 
     */
    public int getChuncks(){
        return this.chuncks;
    }
    /**
     * 
     * @return o tempo sorteado para acontecer o incremento
     */
    public long getTimeToIncrement(){
        return this.time_to_increment;
    }

    /**
     * 
     * @return o timestamp atual da simulacao
     */
    public long getCurrentTimeStamp(){
        return this.current_timestamp;
    }
    
    /**
     * 
     * @return a data atual da ultima medicao
     */
    public Date getCurrentDate(){
        return this.current_date;
    }
    /**
     * 
     * @return o timestamp inicial da simulacao
     */
    public long getInitialTimeStamp(){
        return this.initial_timestamp;
    }

    /**
     * gera o tempo de incremento aleatorio para a simulacao
     */
    public void generateTimeToIncrement(){
        Random random = new Random();

        // pega um indice aleatorio para escolher a quantidade de tempo aleatoria
        // definida pela lista de pedaços de tempo
        int index_random = random.nextInt(this.list_of_chuncks_times.size());

        this.time_to_increment = this.list_of_chuncks_times.get(index_random).longValue();
    }

    /**
     * Inicializa a simulacao 
     */
    public void start(){

        this.generateTimeToIncrement();
        
        this.t_current=this.t_init;
        this.current_date = Calendar.getInstance().getTime();
        this.initial_timestamp = current_date.getTime()/1000;
        this.current_timestamp = this.current_date.getTime()/1000;
    }
    
    /**
     * 
     * @return computa a diferenca do tempo atual com o timestamp da medicao anterior
     */
    public long computeDifferenceTime(){
        //computa a diferenca de tempo ente o tempo local da maquina
        // e o tempo atual do simulador

        long now = Calendar.getInstance().getTime().getTime()/1000;
        long difference = now-this.current_timestamp;

        return difference;
        
    }

    /**
     * Verifica se a simulacao precisa ser resetada
     */
    private void verifyRestartSimu(){
        if(this.getCurrentTimeStamp()>=this.getTimeToIncrement()+this.getInitialTimeStamp() && !reset){
           this.inc = -this.inc;
           this.reset=true;
           System.out.println(this.getCurrentTimeStamp());

        }
        if(this.reset && this.getCurrentTimeStamp()>=(2*this.getTimeToIncrement()+this.getInitialTimeStamp())){
            this.inc=-this.inc;
            this.start();
            this.reset = false;
            System.out.println(this.getCurrentTimeStamp());
        }
    }

}