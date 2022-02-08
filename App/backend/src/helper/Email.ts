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
const sendgridApiKey = process.env.SENDGRID_API_KEY

export const sendEmail= async (email: EmailMessage) => {

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
                                        "lat":email.vaccine.location.latitude,
                                        "long":email.vaccine.location.longitude,
                                        "manager":email.manager,                                  
                                        "address":email.address
                                    }
                                }
                            ],
                            'from': {
                                'email': email.from
                            },
                            "template_id":email.vaccine.status===Status.warning?"d-6463b62cb655495ebf3fff00eefc7ebf"
                                                                                :"d-5e77f1ed29fd4e16a0f39e3b5853eb9e" 
                            // 'content': [
                            //     {
                            //         'type': 'text/html',
                            //         'value': email.html
                            //     },
                            
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