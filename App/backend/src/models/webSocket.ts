import websocket from 'ws';
import VaccineController from '../controllers/VaccineController';

function onError(ws:any, err:Error) {
    console.error(`onError: ${err.message}`);
}
 
function onMessage(ws:any, data:any) {
    console.log(`onMessage: ${data}`);
    ws.send(`recebido!`);
}
 
function onConnection(ws:any, req:Request) {
    const url = req.url;
    const method = req.method;
    console.log(url,method)
    if(url==='/api/vacina/status' && method==='GET'){
        whileSend(ws,1);
    }
    ws.on('message', (data:any) => onMessage(ws, data));
    ws.on('error', (error:Error) => onError(ws, error));
    console.log(`onConnection`);
}
 
export const webSocketConnection = (server:any) => {
    const wss = new websocket.Server({
        server
    });
 
    wss.on('connection', onConnection);
 
    console.log(`App Web Socket Server is running!`);
    return wss;
}

const whileSend = async (ws:any,caseRequest:Number) =>{
    let controller;
    switch(caseRequest){
        case 1:
            controller = VaccineController;
    }

    while(true){

        const message:any = controller?.getAllVaccinesStatus()
        if(message.status!=='error'){
            ws.send(JSON.stringify(message));
        }
        else {
            continue;
        }
        await delay(1500);
    }
}
const delay = (time:number)=>{
    return new Promise (resolve=>setTimeout(resolve,time));
}