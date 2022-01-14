## vacinas.json

-  Para incluir na simulação de temperatura o tempo que você quer esperar no máximo de incremento vá até o arquivo **vacinas.json** e inclua a propriedade "time_increment": valor inteiro.
- Para incluir na simulação a quantidade de pedaços de tempo para sortear na simulação vá até o arquivo **vacinas.json** e inclua "chunks":valor inteiro


OBs: chunks é pedaços, os pedaços de tempo faz parte da simulação, quando o simulador vai incrementar o lote na sua temperatura ele decide quanto tempo esse incremento acontece por meio de um sorteio, o sorteio é em cima de uma lista de pedaços de tempo, a quantidade de pedaços é definida por chunks, o tempo de incremento é definido por time_increment + o tempo maximo que a vacina aguenta depois da temperatura limite ser atingida + erro 20%.
