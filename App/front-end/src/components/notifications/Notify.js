import React, { useEffect, useState } from "react";
import { getAddressFromLatLong } from "../../logic/Geocoding";
import { useGlobalState } from "../../logic/GlobalHook";
import { Status } from "../../models/Vaccine";
import "./Notify.css"

function Notify (props){
    const [vaccineStatus,setVaccineStatus] = useGlobalState('status')
    const [vaccineMessages,setVaccineMessages] = useState(null);

    useEffect(()=>{
        async function sendAlert(){
            if(vaccineStatus!==null){
                var messages = []
                var vaccinesIds = []
                for(var i =0;i<vaccineStatus.length;i++){
                    const vacina = vaccineStatus[i]
                    console.log(vacina)
                    const temp_messages =  
                    `Vacina ${vacina.name}_${vacina.id}, localização ${await getAddressFromLatLong(vacina.location.latitude,vacina.location.longitude)},
                    ${
                    vacina.status===Status.warning?'está próximo de atingir sua temperatura limite, mantenha atenção neste lote':
                    vacina.status===Status.danger?`sua temperatura já atingiu o seu limite, e corre risco de ser descartada, um email foi enviado pro gestor mais proximo, entretanto se não houver resposta, contate observando o mapa o gestor mais proximo.`
                    :vacina.status===Status.ok?`está perfeitamente normal, vai tomar um cafézinho menina, se joga!`
                    :
                    'já passou da sua validade e precisa ser descartada!'
                    }`
                    messages.push({message:temp_messages,status:vacina.status,id:vacina.id})                    
                }

                if(messages.length>0) 
                    setVaccineMessages(messages)
            }
        }
        sendAlert();
    },[vaccineStatus])

    return (
        <div className="div-notify">   
            <div className="alert-message">
                <div className="body-alert">
                    {
                        vaccineMessages?.map(messageJson=>{
                            const message = messageJson.message
                            const imgSrc = messageJson.status===Status.danger?"dangerFlag.svg":messageJson.status===Status.warning?"warningFlag.svg":
                            messageJson.status===Status.ok?"ok.png":"GameOverFlag.svg"
                            return (<div className="message" key={messageJson.id}><img src={imgSrc}/>{message}</div>)
                        })
                    }
                </div>
            </div>     
        </div>
    )
}

export default Notify;