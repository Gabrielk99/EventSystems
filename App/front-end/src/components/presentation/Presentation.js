import React from "react";
import "./Presentation.css";
import { useEffect } from 'react'

import { checkAndSendNotification } from '../../logic/VaccineStatusChecker';
import { getStatusVaccineByID } from '../../controllers/vacinas/vacinaController';

function Presentation (){
    return (
        <div className="pst-container">
            <h1>Bem vindo a sua mesa de comando</h1>
            <div className="img-container">
                <img src='control-panel.svg'/>
            </div>
            <h3>Nesta janela você vai monitorar as temperaturas de cada lote de vacina, a localização destas e dos gestores.</h3>
        </div>
    )


}

export default Presentation;