import { Manager, ManagerLocation } from '../models/Manager'

const fs = require('fs');

const pathToLocations = __dirname.replace("App/backend/src/controllers","Database/data_for_app/Gestores/datasSimulation.json");
const pathToGestoresInfo = __dirname.replace("App/backend/src/controllers", "Database/data_for_app/Gestores/gestores.json");

class ManagerController {
    getManagers() {
        let data = fs.readFileSync(pathToGestoresInfo)
        let gestores : [Manager] = JSON.parse(data).gestores
        return gestores;
    }

    getManager(id: number) {
        let data = fs.readFileSync(pathToGestoresInfo)
        let gestores : [Manager] = JSON.parse(data).gestores
        return gestores.find(gestor => gestor.id == id);
    }

    getManagersLocation() {
        let data = fs.readFileSync(pathToLocations)
        let locations : [ManagerLocation] = JSON.parse(data)
        return locations;
    }

    getManagerLocation(id: number) {
        let data = fs.readFileSync(pathToLocations)
        let locations : [ManagerLocation] = JSON.parse(data)
        return locations.find(gestor => gestor.id == id);
    }
}

export = new ManagerController();