import React, { useEffect, useState } from "react";
import Plot from 'react-plotly.js';
import { randomColor } from "../../../utils/randomColor";
import { getAllStatusVaccines, getAllVaccineInformation} from "../../../controllers/vacinas/vacinaController";
import "./Temperature.css"

function TemperatureGraph(props){
    const [datas,setDatas] = useState([]);
    const [listOfDatasValue,setListOfDatasValue] = useState([]);
    const [vaccines,setVaccines] = useState({})
    const [timeOut,setTimeOut] = useState(false);
    const [colors,setColors] = useState({});

    useEffect( async ()=>{
       getTemperatureOfAllVaccineInformation();
       setTimeout(()=>{setTimeOut(true)},500);
    },[])

    useEffect(async ()=>{
        if(timeOut){
            console.log(datas)
            getTemperatureOfAllVaccineInformation();
            setTimeOut(false);
            // setTimeout(()=>{setTimeOut(true)},500);
        }
    },[timeOut])
    useEffect(()=>{
        console.log(colors);
        props.updateColorVaccine(colors);
    },[colors])
    useEffect(()=>{
        console.log(listOfDatasValue)
    },[listOfDatasValue])
    useEffect(()=>{
        let lotesInformation = [];
        datas.map(loteVaccine=>{
            const data = {}
            let x = []
            let y = []
            loteVaccine.datasSaved.map(jsonData=>{
                y = [...y,jsonData.temperature]
                x = [...x,jsonData.date.split(" ")[1]]
            })
            data.x = x;
            data.y = y;
            data.type = 'scatter';
            data.mode="lines";
            if(typeof colors[loteVaccine.id]!=='undefined'){
                data.marker = {color:colors[loteVaccine.id]};
            }
            else {
                data.marker = {color: randomColor(1)};
                const colorInfo = {}
                colorInfo[loteVaccine.id]=data.marker.color;
                setColors({...colors,...colorInfo});
            }
            data.name = vaccines[loteVaccine.id]+"_"+loteVaccine.id;
            lotesInformation = [...lotesInformation,data];
            
        })
        setListOfDatasValue(lotesInformation);  
    },[datas,vaccines])

    
    const getTemperatureOfAllVaccineInformation = ()=>{
        getAllStatusVaccines().then(data=>setDatas(data));
        getAllVaccineInformation().then(vaccinesInfo=>{
            vaccinesInfo.map(vaccine=>{
                const vaccineInfo = vaccines
                vaccineInfo[vaccine.id] = vaccine.name
                setVaccines({...vaccineInfo});
            })
        })
    }
    return (
        <div id="graph" className="graph-container">
            <h2>Temperatura dos lotes</h2>
            <Plot divId="graph"
                data={listOfDatasValue}
                useResizeHandler={true}
                layout={{showlegend:true,scrollZoom:true,paper_bgcolor:"transparent",
                        plot_bgcolor:"white",xaxis:{tickangle:45,tickfont:{size:13},automargin:true}}}
                config={{scrollZoom:true,staticPlot:true}}
                style={{width:"100%",height:"100%",position:"absolute",top:"5%"}}
            />
        </div>
    )
}


export default TemperatureGraph;