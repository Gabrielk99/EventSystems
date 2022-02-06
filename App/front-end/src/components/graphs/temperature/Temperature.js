import React, { useEffect, useState } from "react";
import Plot from 'react-plotly.js';
import { randomColor } from "../../../utils/randomColor";
import { getAllStatusVaccines, getAllVaccineInformation} from "../../../controllers/vacinas/vacinaController";
import "./Temperature.css"

function TemperatureGraph(props){
    const [datas,setDatas] = useState([]);
    const [listOfDatasValue,setListOfDatasValue] = useState([]);
    const [vaccines,setVaccines] = useState({})

    const layout={showlegend:true,scrollZoom:true,paper_bgcolor:"transparent",
    plot_bgcolor:"white",xaxis:{tickangle:45,tickfont:{size:13},automargin:true,autorange: true},uirevision:true,
    yaxis: {autorange: true}}

    const style = {width:"100%",height:"100%",position:"absolute",top:"5%"};
    useEffect( ()=>{
        setDatas(props.datas);
    },[props.datas])

    useEffect(()=>{
        setVaccines(props.vaccines)
    },[props.vaccines])
   
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
            if(localStorage.getItem(`color${loteVaccine.id}`)){
                data.marker = {color:localStorage.getItem(`color${loteVaccine.id}`)};
            }
            else {
                data.marker = {color: randomColor(1)};
                localStorage.setItem(`color${loteVaccine.id}`,data.marker.color);
            }
            data.name = vaccines[loteVaccine.id]+"_"+loteVaccine.id;
            lotesInformation = [...lotesInformation,data];
            
        })
        setListOfDatasValue(lotesInformation);  
    },[datas,vaccines])
   
    return (
        <div id="graph" className="graph-container">
            <h2>Temperatura dos lotes</h2>
            <Plot divId="graph"
                data={listOfDatasValue}
                useResizeHandler={true}
                layout={{showlegend:true,scrollZoom:true,paper_bgcolor:"transparent",
                        plot_bgcolor:"white",xaxis:{tickangle:45,tickfont:{size:13},automargin:true,autorange: true},uirevision:true,
                        yaxis: {autorange: true}}}
                // config={{scrollZoom:true}}
               
                style={{width:"100%",height:"100%",position:"absolute",top:"5%"}}
                onUpdate={(fig,div)=>{
                    fig.data=[]
                    fig.layout.xaxis.autorange=true
                    
                    fig.layout.yaxis.autorange=true
                    fig.layout.uirevision=true
                }}
            />
        </div>
       
    )
}


export default TemperatureGraph;