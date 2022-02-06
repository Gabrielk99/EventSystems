import axios from 'axios';

const baseUrl = '/api/vacina'

export const getAllVaccineInformation = async ()=>{
    const request = axios.get(baseUrl);
    return await request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}

export const getVaccineInformationByID = async (id)=>{
    const request = axios.get(`${baseUrl}/${id}`);
    return await request.then(response=>response.data).catch(error=>{return {status:"error",message:error.message}})
}

export const getAllStatusVaccines = async ()=>{
    const request = axios.get(`${baseUrl}/status`);
    return await request.then(response=>response.data).catch(error=>{return {status:"error", message:error.message}})
}

export const getStatusVaccineByID = async (id)=>{
    const request = axios.get(`${baseUrl}/status/${id}`);
    return await request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}

