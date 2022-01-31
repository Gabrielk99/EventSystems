import { VaccineStatus, Status } from '../models/Vaccine'
import { Location } from '../models/Location'
import { calculateDistance } from './Location'
import { getManagerInformationByID, getAllManagerInformation, getAllManagerLocation } from '../controllers/gestores/gestorController'
import { ManagerLocation, Manager } from '../models/Manager'
import { sendEmail } from '../controllers/email/emailController'

const subject = "Monitoramento de Vacinas"

export const checkAndSendNotification = async (vacina: VaccineStatus) => {
    switch (vacina.status) {
    case Status.ok:
        return;
    case Status.warning:
        processWarningStatus(vacina.location)
        break;
    case Status.danger:
        processDangerStatus(vacina.location)
        break;
    case Status.gameover:
        console.log("Game over, seu gerente de merda")
    }
}

const generateMessage = (manager: Manager, vaccineLocation: Location) => {
    return {
        to: manager.email,
        from: process.env.REACT_APP_EMAIL_SENDER,
        subject: subject,
        text: "Casa caiu cleitinho",
        html: generateEmailBody(manager.name, vaccineLocation), // passar as infos que precisa aqui, tipo vaccineLocation
    }
}

const processWarningStatus = async (vaccineLocation: Location) => {
    const managers: [Manager] = await getAllManagerInformation()

    managers.forEach( (manager) => {
        const message = generateMessage(manager, vaccineLocation)
        sendEmail(message)
    })
}

const processDangerStatus = async (vaccineLocation: Location) => {
    const manager: Manager = await getNearestManager(vaccineLocation)

    const message = generateMessage(manager, vaccineLocation)
    sendEmail(message)
}

export const getNearestManager = async (location: Location) => {
     const gestoresLocation: [ManagerLocation] = await getAllManagerLocation()

     let distNearest = calculateDistance(gestoresLocation[0].dataSaved, location)
     let nearestManagerLocation = gestoresLocation.reduce((nearest, current) => {
         let currentDist = calculateDistance(current.dataSaved, location)
         if (distNearest > currentDist) {
             distNearest = currentDist
             return current
         }
         return nearest
     }, gestoresLocation[0])

     return getManagerInformationByID(nearestManagerLocation.id)
 }

 // TODO: Gerar corpo da mensagem aqui (sugestao de local apenas, nao tenho certeza)
const generateEmailBody = (name: String, vaccineLocation: Location) => {
     return ""
 }