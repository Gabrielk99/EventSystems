# EventSystems
Projeto desafiador da disciplina Sistema baseado em regas e eventos

Iniciar Zookeeper:

```
zookeeper-server-start.sh config/zookeeper.properties
```

Iniciar Kafka:
```
kafka-server-start.sh config/server.properties
```

Compilar:

```  
javac -cp .:gson-2.8.2.jar:kafka-clients-3.0.0.jar:slf4j-api-1.7.25.jar:slf4j-simple-1.7.25.jar src/corekafka/produtor/vacina/*.java src/corekafka/simulacao/*.java src/types/*.java src/corekafka/produtor/gestor/*.java src/corekafka/produtor/*.java src/corekafka/consumidor/*.java src/main/*.java
```
Executar:
```
java -cp .:gson-2.8.2.jar:kafka-clients-3.0.0.jar:slf4j-api-1.7.25.jar:slf4j-simple-1.7.25.jar src.main.Main
```
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
