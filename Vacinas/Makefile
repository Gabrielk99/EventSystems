JAVA=java
JAVAC=javac


KAFKA_PATH=kafka-clients-3.1.0.jar:lz4-java-1.8.0.jar:snappy-java-1.1.8.4.jar:zstd-jni-1.5.-4.jar
slf4jAPI_PATH=slf4j-api-1.7.25.jar
GSON_PATH=gson-2.8.2.jar
slf4jSIMPLE_PATH=slf4j-simple-1.7.25.jar
KAFKA_STREAMS=kafka-streams-3.1.0.jar
ROCKDB_PATH=rocksdbjni-6.28.2.jar
JACKSON=:jackson-annotations-2.13.1.jar:jackson-core-2.13.1.jar:jackson-databind-2.13.1.jar
CLASS_PATH_OPTION=-cp .:$(KAFKA_PATH):$(slf4jAPI_PATH):$(GSON_PATH):$(slf4jSIMPLE_PATH):$(KAFKA_STREAMS):$(ROCKDB_PATH):$(JACKSON)

MAIN_PATH= src/main
# Diretório para os arquivos .class
BIN_PATH=bin

# Diretório para os casos de teste


all: javac
	@echo "Done."

# Compila todos os subdiretórios e joga todos os .class em BIN_PATH pra organizar.
javac:
	rm -fr $(BIN_PATH)
	mkdir $(BIN_PATH)
	$(JAVAC) $(CLASS_PATH_OPTION) -d $(BIN_PATH) src/corekafka/produtor/vacina/*.java src/corekafka/simulacao/*.java src/types/*.java src/corekafka/produtor/gestor/*.java src/corekafka/produtor/*.java src/corekafka/consumidor/*.java src/models/*.java src/main/*.java

run:
	$(JAVA) $(CLASS_PATH_OPTION):$(BIN_PATH) $(MAIN_PATH)/Main $(FILE)

clean:
	@rm -rf $(BIN_PATH) 
