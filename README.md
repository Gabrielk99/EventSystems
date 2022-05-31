# EventSystems
Challenging project of the Rules and Events Based System discipline

# Dependencies

## Kafka 

### English Version

1. From the command line:
• Java -version (to check the Java version)
- If it's Java8, ok!
- Otherwise, it is necessary to install...


2. Kafka download, on google:
• Download the latest version (binaries)
• Unzip in some directory
• From the command line, go to the directory and try: bin/kafka-topics.sh
- If a list of options appears, installation is ok!
- Otherwise check Java version


3. Add Kafka to PATH (example)
• Open the “bashrc” file for editing
• Add at the end the line: export PATH=/your_path/Kafka_2XXXX/bin:$PATH


4. Open a new terminal
• Try: kafka-topics.sh
- If a list of options appears, installation is ok!


5. Create the directories “/your_path/data”, “/your_path/data/zookeeper” and “/your_path/data/kafka”


6. Edit Zookeeper and Kafka configs, using text editor
• zookeeper.properties: dataDir=/your_path /data/zookeeper
• server.properties: log.dirs=/your_path/data/kafka


7. Start Zookeeper in a terminal window: zookeeper-server-start.sh config/zookeeper.properties


8. Start Kafka in another window: kafka-server-start.sh config/server.properties


### Portuguese Version

1. Na linha de comando:
• Java -version (para verificar a versão de Java)
- Se for Java8, ok!
- Caso contrário, é necessário instalar...


2. Kafka download, no google:
• Faça download da última versão (binários)
• Descompactar em algum diretório
• Na linha de comando, vá para o diretório e tente: bin/kafka-topics.sh
- Se aparecer uma lista de opções, instalação está ok!
- Caso contrário, verificar versão do Java


3. Adicionar Kafka ao PATH (exemplo)
• Abrir o arquivo “bashrc” para edição
• Acrescentar ao final, a linha: export PATH=/seu_caminho/Kafka_2XXXX/bin:$PATH


4. Abra um novo terminal
• Tente: kafka-topics.sh
- Se aparecer uma lista de opções, instalação está ok! 


5. Criar os diretórios “/seu_caminho/data” , “/seu_caminho/data/zookeeper”  e “/seu_caminho/data/kafka”


6. Editar Zookeeper e Kafka configs, usando editor de texto
• zookeeper.properties: dataDir=/seu_caminho /data/zookeeper
• server.properties: log.dirs=/seu_caminho/data/kafka


7. Start Zookeeper em uma janela de terminal: zookeeper-server-start.sh config/zookeeper.properties


8. Start Kafka em outra janela: kafka-server-start.sh config/server.properties


## Application 
### back-end
go to App/backend and run
```
npm install
```
create a acount on google and get a google maps key and change de value on .env, the same thing have to be done to sengrid api, in this case two acounts is necessary and two keys, besides also replace emails senders to the email that correspond to the keys sendgrid.

Obs: in back end helper to send email, use a template id, that you can get when you create your only template to each acount sendgrid.

### front-end
go to App/front-end and run
```
npm install
```


# Run application

## kafka

Start Zookeeper:

```
zookeeper-server-start.sh config/zookeeper.properties
```

Start Kafka:
```
kafka-server-start.sh config/server.properties
```

Compile:

on Vacinas/
```  
make all
```
Execute:
```
make run
```

## Application

### back-end
go to App/backend and run 

```
npm start
```
### front-end
go to App/front-end and run
```
npm start
```
# A review over Application page

![image](https://user-images.githubusercontent.com/34044829/171076651-c8779c64-4a3a-4f03-9f37-78ad9d782f06.png)
<br><br>
Troca Key button change the sendgrid key to send emails and SendEmail active and disable the shipping of emails.
<br><br>

![image](https://user-images.githubusercontent.com/34044829/171076818-f9d1e6f5-4600-442a-9cd4-d27e1d41f1ba.png)
<br><br>

This view shows the current situation from vaccine batches on system (normal, warning, danger, game over)

* normal : the batch its ok (temperature is on intervall aceptable)
* warning : the batch its not totally ok (the temperature its growing and its next from limit)
* dange : the batch its really not ok (the temperature its above of limit, vaccine can be lost)
* game over: the batch is out date (the vaccine lost the validity after temperature stay above limit for mutch time)

<br><br>
![image](https://user-images.githubusercontent.com/34044829/171077275-68c1ace0-f1e9-4307-8832-47a4e833b44c.png)
<br><br>
The left graph show the history of temperature for each vaccine batches and the right graph show the maps and current position from batchs and managers.

Manages are the "managers" that manage the lots.
<br><br>

![2022-05-30 22-56-19](https://user-images.githubusercontent.com/34044829/171079554-e5c5129e-bb92-4a56-9fdf-56bcef657b05.gif)


# Kafka Infos
Info do topico:

```
kafka-topics.sh --bootstrap-server=localhost:9092 --describe --topic vacina
```
Info dos consumidores:
```
kafka-consumer-groups.sh --bootstrap-server localhost:9092 --group consumerGestor --describe
```
Limpa a aplicação stream (pode dar problema de metadados, importante usar)
Se começou dar erro **SEM MOTIVO** e é algo de metados, partições erradas, reinicie o pc e rode:
```
kafka-streams-application-reset.sh --application-id vaccine-monitoring
```
