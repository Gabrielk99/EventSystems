JAVA=java
JAVAC=javac


KAFKA_PATH=kafka-clients-3.0.0.jar
slf4jAPI_PATH=slf4j-api-1.7.25.jar
GSON_PATH=gson-2.8.2.jar
slf4jSIMPLE_PATH=slf4j-simple-1.7.25.jar
CLASS_PATH_OPTION=-cp .:$(KAFKA_PATH):$(slf4jAPI_PATH):$(GSON_PATH):$(slf4jSIMPLE_PATH)

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
	$(JAVAC) $(CLASS_PATH_OPTION) -d $(BIN_PATH) src/corekafka/produtor/vacina/*.java src/corekafka/simulacao/*.java src/types/*.java src/corekafka/produtor/gestor/*.java src/corekafka/produtor/*.java src/corekafka/consumidor/*.java src/main/*.java 

run:
	$(JAVA) $(CLASS_PATH_OPTION):$(BIN_PATH) $(MAIN_PATH)/Main $(FILE)

clean:
	@rm -rf $(BIN_PATH) 
