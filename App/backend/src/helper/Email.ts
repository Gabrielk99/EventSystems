import { EmailMessage } from '../models/Email'
import dotenv from 'dotenv';
import fs from 'fs'
import { Status } from '../models/Vaccine';
import ManagerController from '../controllers/ManagerController';
dotenv.config({
  path: '.env'
});


const axios = require('axios')
const emailSendUrl = 'https://api.sendgrid.com/v3/mail/send'


export const sendEmail= async (email: EmailMessage) => {
    const sendgridApiKey = email.key===0?process.env.SENDGRID_API_KEY:process.env.SENDGRID_API_KEY2
    const dangerId = email.key===0?"d-5e77f1ed29fd4e16a0f39e3b5853eb9e" :'d-7896e840032c4c2b8e6c57c2119ced2c'
    const warningId = email.key===0?"d-6463b62cb655495ebf3fff00eefc7ebf":"d-537ac74b7c2048d1ad122d08e96273dd"
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
                                        "address":email.address
                                    }
                                }
                            ],
                            'from': {
                                'email': email.from
                            },
                            "template_id":email.status===Status.warning?warningId
                                                                                :dangerId
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
}