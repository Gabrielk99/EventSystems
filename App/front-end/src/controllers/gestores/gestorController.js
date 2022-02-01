import axios from 'axios';

const baseUrl = '/api/gestor'

export const getAllManagerInformation = async ()=>{
    const request = axios.get(baseUrl);
    return request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}

export const getManagerInformationByID = async (id)=>{
    const request = axios.get(`${baseUrl}/${id}`);
    return request.then(response=>response.data).catch(error=>{return {status:"error",message:error.message}})
}

export const getAllManagerLocation = async ()=>{
    const request = axios.get(`${baseUrl}/localizacao`);
    return request.then(response=>response.data).catch(error=>{return {status:"error", message:error.message}})
}

export const getManagerLocationByID = async (id)=>{
    const request = axios.get(`${baseUrl}/localizacao/${id}`);
    return request.then(response=>response.data).catch(error=>{return {status:'error',message:error.message}})
}

