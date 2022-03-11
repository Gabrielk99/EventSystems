import React, { useEffect, useState } from "react";
import Plot from 'react-plotly.js';
import Plotly from 'plotly.js-dist';
import { randomColor } from "../../../utils/randomColor";
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    Title,
    Tooltip,
    Legend,
  } from 'chart.js';
import { Line } from 'react-chartjs-2';
import { getAllStatusVaccines, getAllVaccineInformation} from "../../../controllers/vacinas/vacinaController";
import "./Temperature.css"

  
function TemperatureGraph(props){
    const [datas,setDatas] = useState([]);
    const [listOfDatasValue,setListOfDatasValue] = useState({
        labels:[],
        datasets:[]
    });
    const [vaccines,setVaccines] = useState({})

    ChartJS.register(
        CategoryScale,
        LinearScale,
        PointElement,
        LineElement,
        Title,
        Tooltip,
        Legend
    );

    // useEffect(()=>{
    //     console.log(listOfDatasValue)
    // },[listOfDatasValue])
    

    const style = {width:"100%",height:"100%",position:"absolute",top:"5%"};
    useEffect( ()=>{
        setDatas(props.datas);
    },[props.datas])

    useEffect(()=>{
        setVaccines(props.vaccines)
    },[props.vaccines])
   
    useEffect(()=>{
        if(datas.length>0){
            let lotesInformation = [];
            const labels = datas[0].datasSaved.map(element => {
                return element.date.split(" ")[1];
            });
            const datasets = datas.map(loteVaccine=>{
                const y = loteVaccine.datasSaved.map(jsonData=>{
                    return  jsonData.temperature;
                })
                let color;
                if(localStorage.getItem(`color${loteVaccine.id}`)){
                    color = localStorage.getItem(`color${loteVaccine.id}`);
                }
                else {
                    color = randomColor(1);
                    localStorage.setItem(`color${loteVaccine.id}`,color);
                }

                const label = vaccines[loteVaccine.id]+"_"+loteVaccine.id;
                
                return {
                    label:label,
                    data:y,
                    borderColor:color,
                    backgroundColor:color,
                };
            })

            setListOfDatasValue({
                labels:labels,
                datasets:datasets
            });
        }  
    },[datas,vaccines])
    
    const options = {
        responsive: true,
        plugins: {
          legend: {
            position: 'top',
          },
        },
        animation: {
            enabled: true,
            easing: 'linear',
            dynamicAnimation: {
            speed: 1000
            }
        },
        elements: {
            point:{
                radius: 0
            }
        }
      };

    return (
        <div id="graph" className="graph-container">
            <h2>Temperatura dos lotes</h2>
            <div className="graph-line">
                <Line options={options} data={listOfDatasValue}/>
            </div>
        </div>
       
    )
}


export default TemperatureGraph;