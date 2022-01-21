import status from '../../../../Database/data_for_app/Vacinas/datasSimulation.json'
import vacinasInfo from '../../../../Database/data_for_app/Vacinas/vacinas.json'

class VaccineController {
    getVaccines() {
       return vacinasInfo.vacinas
    }

    getVaccine(id: number) {
        return vacinasInfo.vacinas.find(vacina => vacina.id == id)
    }

    getAllVaccinesStatus() {
        return status
    }

    getVaccineStatus(id: number) {
        return status.filter(statusVacina => statusVacina.id == id)
    }
}

export = new VaccineController();