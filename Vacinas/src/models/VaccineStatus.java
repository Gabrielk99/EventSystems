package src.models;

/**
 * Status da vacina: Ok, warning e danger segundo os critérios estabelecidos
 */
public enum VaccineStatus {
    OK {
        public String toString() { return "OK"; }
    },
    WARNING {
        public String toString() { return "WARNING"; }
    },
    DANGER {
        public String toString() { return "DANGER"; }
    },
    GAMEOVER {
        public String toString() { return "GAME OVER"; }
    };

    /**
     * Função que checa o status da vacina de acordo com a temperatura atual e as informações específica da vacina
     * @param currentTemperature    temperatura atual da vacina
     * @param averageTemperature    temperatura média tolerável
     * @param maxTemperature temperatura    máxima limite tolerável
     * @param timePassedSinceMaxTemp    tempo que passou após a vacina atingir a temperatura máxima limite
     * @param maxTimeAboveMaxTemp   tempo máximo tolerável da vacina estar acima da temperatura máxima limite
     * @return  Status da vacina: OK, WARNING ou DANGER
     */
    public static VaccineStatus checkStatus(
            float currentTemperature,
            float averageTemperature,
            float maxTemperature,
            double timePassedSinceMaxTemp,
            float maxTimeAboveMaxTemp) {
        // Checa se a temperatura está abaixo da temperatura que começa a preocupar
        if (currentTemperature < averageTemperature) {
            return OK;
            // Checa se a temperatura está entre o valor preocupante e o valor máximo tolerável
        }else if (currentTemperature >= averageTemperature && currentTemperature < maxTemperature) {
            return WARNING;
        }else if (currentTemperature >= maxTemperature) { // Se a temperatura está acima do valor tolerável
            if (timePassedSinceMaxTemp < maxTimeAboveMaxTemp) { // E não passou o tempo máximo que ela pode ficar
                return DANGER;                                 // Então o status é de perigo
            } else {
                return GAMEOVER;                                  // Se já excedeu o tempo máximo, as vacinas estragaram
            }
        }
        return null;
    }
}