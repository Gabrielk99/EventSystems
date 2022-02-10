import React, { useEffect, useState } from "react";
import { getAddressFromLatLong } from "../../logic/Geocoding";
import { useGlobalState } from "../../logic/GlobalHook";
import { Status } from "../../models/Vaccine";
import "./Notify.css"

function Notify (props){
    const [vaccineStatus,setVaccineStatus] = useGlobalState('status')
    const [vaccineMessages,setVaccineMessages] = useState(null);
    const [vaccinesIds,setVaccinesIds] = useState(null);
    useEffect(()=>{
        async function sendAlert(){
            if(vaccineStatus){
                var messages = []
                var vaccinesIds = []
                for(var i =0;i<vaccineStatus.length;i++){
                    const element = vaccineStatus[i]
                    var alert = localStorage.getItem(`vaccineAlert${element.vacina.id}`)
                    // console.log(alert)
                    alert = alert==='true'?true:false
                    if(alert){
                        const vacina = element.vacina
                        const manager = element.manager
                        const temp_messages =  
                        `Vacina ${vacina.name}, localização ${await getAddressFromLatLong(vacina.location.latitude,vacina.location.longitude)},
                        ${element.status===Status.warning?'está próximo de atingir sua temperatura limite, mantenha atenção neste lote':
                        element.status===Status.danger?`sua temperatura já atingiu o seu limite, e corre risco de ser descartada, o gestor mais próximo é ${manager.name}, envie email para ${manager.email}, para avisa-lo(a) do ocorrido`:
                        'já passou da sua validade e precisa ser descartada!'
                        }.`
                        messages.push({message:temp_messages,status:element.status})
                        vaccinesIds.push(vacina.id)
                    }
                }
                if(messages.length>0) 
                    setVaccineMessages(messages)
                if(vaccinesIds.length>0)
                    setVaccinesIds(vaccinesIds);
            }
        }
        sendAlert();
    },[vaccineStatus])
    useEffect(()=>{
        // console.log(vaccineMessages)
    },[vaccineMessages])

    const handleClose = ()=>{
        setVaccineMessages(null)
        if(vaccinesIds){
            vaccinesIds.forEach((id)=>{
                localStorage.setItem(`vaccineAlert${id}`,'false');
            })
        }
        setVaccinesIds(null)
    }
    return (
        <div className="div-notify">
            {
                vaccineMessages?
                    <div className="alert-message">
                        <div className="body-alert">
                            {
                                vaccineMessages.map(messageJson=>{
                                    const message = messageJson.message
                                    const imgSrc = messageJson.status===Status.danger?"dangerFlag.svg":messageJson.status===Status.warning?"warningFlag.svg":"GameOverFlag.svg"
                                    return (<div className="message" key={message.length}><img src={imgSrc}/>{message}</div>)
                                })
                            }
                        </div>
                        <div className="button-confirm-notify" onClick={handleClose}>
                            Ok
                        </div>
                    </div>
                :<></>
            }
        </div>
    )
}

export default Notify;