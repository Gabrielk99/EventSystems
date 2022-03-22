import { EmailMessage } from '../models/Email'
import dotenv from 'dotenv';
import fs from 'fs'
import { Status } from '../models/Vaccine';
import ManagerController from '../controllers/ManagerController';
import { getAddressFromLatLong } from './Geocoding'

dotenv.config({
  path: '.env'
});

const axios = require('axios')
const emailSendUrl = 'https://api.sendgrid.com/v3/mail/send'

let key = 0
let send = false;

const getTemplateId = (status: number, isFrequentAlert: boolean) => {
    let templateId = 'batata'

    //TODO: colocar id do template pra alerta frequente
    switch (status) {
        case Status.warning:
            if (key === 0) {
                templateId = isFrequentAlert? "d-607f444d07d34fc68af45d6016698a21" : "d-6463b62cb655495ebf3fff00eefc7ebf"
            } else {
                templateId =  isFrequentAlert? "d-d833e21be95b498a96d11e36a967d1b5" : "d-537ac74b7c2048d1ad122d08e96273dd"
            }
            break;
        case Status.danger:
            if (key === 0) {
                templateId = isFrequentAlert? "d-a289ccd851c940dc885cd1ab5e666ace" : "d-5e77f1ed29fd4e16a0f39e3b5853eb9e"
            } else {
                templateId =  isFrequentAlert? 'd-09b7ee5a44c148e9a667db506ec5ba52' : 'd-7896e840032c4c2b8e6c57c2119ced2c'
            }
            break;
        case Status.gameover:
            if(key===0){
                templateId="d-fb176167feb94e569aaf3934b6e3e84d";
            }
            else{
                templateId="d-5f90e91b9b4e40d294c7781f6087c296";
            }
            break;
        default:
            break;
    }

    return templateId
}

export const setEmailSender = (value: number) => {
    console.log("antes: " + key)
    key = value
    console.log("depois: " + key)
    console.log("===================")
}

export const setSendEmail = (value:boolean)=>{
    console.log("old: ", send);
    console.log(value)
    send = value;
    console.log("new: ", send);
}

export const sendEmail= async (email: EmailMessage) => {
    if(send){
        const sendgridApiKey = key===0? process.env.SENDGRID_API_KEY : process.env.SENDGRID_API_KEY2
        const sender = key ===0? process.env.EMAIL_SENDER : process.env.EMAIL_SENDER2
        try{
            const message = {
                                'personalizations': [
                                    {
                                        'to': [
                                            {
                                                'email': email.to
                                            }
                                        ],
                                        'subject': email.subject,
                                        "dynamic_template_data":{
                                            "name":email.vaccine.name,
                                            "lat":email.location.latitude,
                                            "long":email.location.longitude,
                                            "manager":email.manager,
                                            "address": await getAddressFromLatLong(email.location.latitude, email.location.longitude)
                                        }
                                    }
                                ],
                                'from': {
                                    'email': sender
                                },
                                "template_id": getTemplateId(email.status, email.isFrequentAlert)
                                // 'content': [
                                //     {
                                //         'type': 'text/html',
                                //         'value': email.text
                                //     }
                                
                                // ]

                            }
            
                const res = await axios({
                    method: 'post',
                    url: emailSendUrl,
                    headers: {
                        Authorization: `Bearer ${sendgridApiKey}`,
                    },
                    data: message,
                })
                return res
    }
        catch(err:any){
            console.log(err)
            return {status:'error',message:"limite de email batido"};
        }
    }else{
        return {status:"success",message:"nenhum email foi enviado, o envio está bloqueado."}
    }
}