import status from '../../../../Database/data_for_app/Vacinas/datasSimulation.json'
import vacinasInfo from '../../../../Database/data_for_app/Vacinas/vacinas.json'

import { Vaccine, VaccineListStatus } from '../models/Vaccine'

const fs = require('fs');

const pathToStatus = __dirname.replace("App/backend/src/controllers","Database/data_for_app/Vacinas/datasSimulation.json");
const pathToVacinasInfo = __dirname.replace("App/backend/src/controllers", "Database/data_for_app/Vacinas/vacinas.json");

class VaccineController {
    getVaccines() {
        let data = fs.readFileSync(pathToVacinasInfo)
        let vacinasInfo : [Vaccine] = JSON.parse(data).vacinas
        return vacinasInfo
    }

    getVaccine(id: number) {
        let data = fs.readFileSync(pathToVacinasInfo)
        let vacinasInfo : [Vaccine] = JSON.parse(data).vacinas
        return vacinasInfo.find(vacina => vacina.id == id)
    }

    getAllVaccinesStatus() {
        let data = fs.readFileSync(pathToStatus)
        let status : [VaccineListStatus] = JSON.parse(data)
        return status
    }

    getVaccineStatus(id: number) {
        let data = fs.readFileSync(pathToStatus)
        let status : [VaccineListStatus] = JSON.parse(data)
        return status.find(statusVacina => statusVacina.id == id)
    }
}

export = new VaccineController();