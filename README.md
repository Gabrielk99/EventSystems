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
javac -cp .:gson-2.8.2.jar:kafka-clients-3.0.0.jar:slf4j-api-1.7.25.jar:slf4j-simple-1.7.25.jar src/corekafka/vacina/*.java src/corekafka/simulacao/*.java src/types/*.java src/corekafka/gestor/*.java src/corekafka/consumidor/*.java
```
Executar:
```
java -cp .:gson-2.8.2.jar:kafka-clients-3.0.0.jar:slf4j-api-1.7.25.jar:slf4j-simple-1.7.25.jar src.corekafka.vacina.MainTeste
```
