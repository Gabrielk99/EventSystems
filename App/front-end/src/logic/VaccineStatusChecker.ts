import { VaccineStatus, Status } from '../models/Vaccine'
import { Location } from '../models/Location'
import { calculateDistance } from './Location'
import { getManagerInformationByID, getAllManagerInformation, getAllManagerLocation } from '../controllers/gestores/gestorController'
import { ManagerLocation, Manager } from '../models/Manager'
import { sendEmail } from '../controllers/email/emailController'
import {getAddressFromLatLong} from './Geocoding'
const subject = "Monitoramento de Vacinas"

export const checkAndSendNotification = async (vacina: VaccineStatus) => {
    switch (vacina.status) {
        case Status.ok:
            localStorage.setItem(`vaccineWarning${vacina.id}`,'true');
            localStorage.setItem(`vaccineDanger${vacina.id}`,'true');
            localStorage.setItem(`vaccineGameOver${vacina.id}`,'true');
            return {status:Status.ok,alert:false, message:"lote em estado normal",vacina:vacina};
        case Status.warning:
            var itsSend:any = localStorage.getItem(`vaccineWarning${vacina.id}`)
            itsSend = itsSend===null?true:itsSend==='false'?false:true;
            if(itsSend){
                await processWarningStatus(vacina)
                localStorage.setItem(`vaccineWarning${vacina.id}`,'false')
                localStorage.setItem(`vaccineAlert${vacina.id}`,'true')
            }
            return {status:Status.warning,message:`lote em perigo, todos gestores avisados`,vacina:vacina};
        case Status.danger:
            var itsSendDanger:any = (localStorage.getItem(`vaccineDanger${vacina.id}`))
            itsSendDanger = itsSendDanger===null?true:itsSendDanger==='false'?false:true;
            if(itsSendDanger){
                localStorage.setItem(`vaccineDanger${vacina.id}`,'false')
                localStorage.setItem(`vaccineAlert${vacina.id}`,'true')
                return await processDangerStatus(vacina)
            }
            const manager: Manager = await getNearestManager(vacina.location)
            return {status:Status.danger, message:"lote em perigo o gestor mais próximo ja foi avisado.",manager:manager,vacina:vacina}
        case Status.gameover:
            var itsSendGameOver:any=(localStorage.getItem(`vaccineGameOver${vacina.id}`))
            itsSendGameOver = itsSendGameOver===null?true:itsSendGameOver==='false'?false:true;
            if(itsSendGameOver){
                localStorage.setItem(`vaccineGameOver${vacina.id}`,'false')
                localStorage.setItem(`vaccineAlert${vacina.id}`,'true')
            }
            return {status:Status.gameover,message:'descarte do lote de vacina urgente.',vacina:vacina}

    }
}

const generateMessage = async (manager: Manager, vaccine:VaccineStatus) => {
    const key =Number(localStorage.getItem('keySendGrid'));
    return {
        to: manager.email,
        from: key===0?process.env.REACT_APP_EMAIL_SENDER:process.env.REACT_APP_EMAIL_SENDER2,
        subject: subject,
        text: "Casa caiu cleitinho",
        html: generateEmailBody(manager.name, vaccine), // passar as infos que precisa aqui, tipo vaccineLocation
        vaccine:vaccine,
        address:await getAddressFromLatLong(vaccine.location.latitude,vaccine.location.longitude),
        manager:manager.name,
        key:key
    }
}

const processWarningStatus = async (vaccine:VaccineStatus) => {
    const managers: [Manager] = await getAllManagerInformation()

    managers.forEach( async (manager) => {
        const message = await generateMessage(manager, vaccine)
        var send:any = localStorage.getItem('sendEmail')==='true'?true:false;
        if(send){
            var res = await sendEmail(message);
        }
    })
}

const processDangerStatus = async (vaccine:VaccineStatus) => {
    const manager: Manager = await getNearestManager(vaccine.location)

    const message = await generateMessage(manager, vaccine)
    const send = localStorage.getItem("sendEmail")==='true'?true:false
    if(send)
        var res = await sendEmail(message);
    return {status:Status.danger,message:"lote em perigo o gestor mais perto foi avisado", manager:manager,vacina:vaccine}
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
const generateEmailBody = (name: String, vaccine:VaccineStatus) => {
     return ""
 }