import axios from 'axios';

const baseUrl = '/api/email'

export const sendEmail = async (message) => {
    const request = axios.post(`${baseUrl}/send`, message);
    return request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}