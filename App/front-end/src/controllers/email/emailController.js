import axios from 'axios';

const baseUrl = '/api/email'

export const sendEmail = async (message) => {
    const request = axios.post(`${baseUrl}/send`, {message:message});
    return await request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}

export const setEmailSender = async (key) => {
    const request = axios.post(`${baseUrl}/setKey/${key}`);
    return await request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}