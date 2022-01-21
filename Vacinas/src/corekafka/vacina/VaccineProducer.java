package src.corekafka.vacina;

import src.types.*;
import com.google.gson.*;


import src.corekafka.simulacao.PositionControlOnMap;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.common.serialization.StringSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * Classe criada para representar de forma generica um produtor de lotes de vacina
 * 
 * @author Gabriel Xavier
 */
public class VaccineProducer{
    
    // instancia do lote
    private Vaccine _vaccine;
    // produtor kafka para as informações
    KafkaProducer<String, String> producer;

    /**
     * 
     * @param BootstrapServer servidor no qual o kafka vai rodar
     * @param pathToJson    caminho para o arquivo de configuração do lote
     */
    public VaccineProducer(String BootstrapServer, JsonObject vaccine){

        // Instanciando o lote de vacina
        this._vaccine = new Vaccine(vaccine);

        // Definindo as propriedades
        Properties prop = new Properties();
        prop.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BootstrapServer);
        prop.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        prop.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        // Criando o produtor <chave:String,valor:String>
        producer = new KafkaProducer<String, String>(prop);

    }

    /**
     * 
     * @param current_coordinate localizacao atual do lote
     * @param current_temperature temperatura atual do lote 
     * @return a mensagem do objeto json em string
     */
    public String generateMessage(Coordinates current_coordinate, float current_temperature){

        //Define o objeto json de localizacao
        JsonObject jsonLocation = new JsonObject();

        //adiciona suas propriedades
        jsonLocation.addProperty("latitude",current_coordinate.getLatitude());
        jsonLocation.addProperty("longitude", current_coordinate.getLongitude());

        //define o objeto json da propria mensagem
        JsonObject jsonMessage  = new JsonObject();

        //adiciona suas propriedades
        jsonMessage.addProperty("id", this._vaccine.getId());
        jsonMessage.addProperty("temperatura", current_temperature);
        jsonMessage.addProperty("data", this._vaccine.getCurrentDate().toString());
        jsonMessage.add("localizacao", jsonLocation);

        return jsonMessage.toString();
    }   

    /**
     * Envia mensagem para o topico vacina
     */
    public void sendMessage(){
        
        String message = this.generateMessage(this._vaccine.getCurrentLocation(),
                                             this._vaccine.getCurrentTemperature());
        
        // (topico,key,mensagem)
        ProducerRecord<String, String> record = new ProducerRecord <String,String>("vacina",""+this._vaccine.getId(),message);

        Logger logger = LoggerFactory.getLogger(VaccineProducer.class);

        this.producer.send(record, new Callback() {
            
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e){
                // if(e==null){
                //     logger.info("\n key:" + record.key()+"\n");
                //     logger.info("topic: "+recordMetadata.topic()+"\n"+
                //     "partition: "+recordMetadata.partition()+"\n"+
                //     "offset: "+recordMetadata.offset()+"\n"
                //     );

                // }
            }
            
        });

        this.producer.flush();


    }

}