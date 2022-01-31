import { EmailMessage } from '../models/Email'
import dotenv from 'dotenv';

dotenv.config({
  path: '.env'
});

const axios = require('axios')
const emailSendUrl = 'https://api.sendgrid.com/v3/mail/send'
const sendgridApiKey = process.env.SENDGRID_API_KEY

export const sendEmail= (email: EmailMessage ) => {
    const message = {
                        'personalizations': [
                            {
                                'to': [
                                    {
                                        'email': email.to
                                    }
                                ],
                                'subject': email.subject
                            }
                        ],
                        'from': {
                            'email': email.from
                        },
                        'content': [
                            {
                                'type': 'text/html',
                                'value': email.text
                            }
                        ]
                    }
    return axios({
        method: 'post',
        url: emailSendUrl,
        headers: {
            Authorization: `Bearer ${sendgridApiKey}`,
        },
        data: message,
    })
}