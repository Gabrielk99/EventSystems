import localizacao from '../../../../Database/data_for_app/Gestores/datasSimulation.json'
import gestoresInfo from '../../../../Database/data_for_app/Gestores/gestores.json'

class ManagerController {
      getManagers() {
        return gestoresInfo.gestores
      }

      getManager(id: number) {
        return gestoresInfo.gestores.find(gestor => gestor.id == id)
      }

      getManagersLocation() {
        return localizacao
      }

      getManagerLocation(id: number) {
        return localizacao.find(gestor => gestor.id == id)
      }
}

export = new ManagerController();