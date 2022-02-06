import React, { useEffect, useState } from "react";
import GoogleMapReact from 'google-map-react';
import Symbol from '../../symbolMapLocation/Symbol';
import { getAllStatusVaccines,getAllVaccineInformation} from "../../../controllers/vacinas/vacinaController";
import "./Map.css"
import { randomColor } from "../../../utils/randomColor";


function Map(props){
    
    const [locationsVaccines,setLocationsVaccines] = useState([]);
    const [vaccines,setVaccines] = useState({});
    const [vaccinesSymbols,setVaccinesSymbols] = useState([]);
    const [managersSymbols,setManagersSymbols] = useState([]);
    useEffect(()=>{
        if(props.datas!=null)
            getLocationAndVaccineInformation(props.datas);
    },[props.datas])

    useEffect(()=>{
        setVaccines(props.vaccines)
    },[props.vaccinnes])  

  
    useEffect(()=>{
        if(props.datasToManagers.length>0 && Object.keys(props.managers).length>0){
            var symbols = []
            props.datasToManagers.map(dataManager=>{
                let color;
                if(localStorage.getItem(`gestor${dataManager.id}`)){
                    color = localStorage.getItem(`gestor${dataManager.id}`);
                }
                else {
                    color = randomColor(1);
                    localStorage.setItem(`gestor${dataManager.id}`,color);
                }
                const location = dataManager.dataSaved.location;
                const newSymbol = <Symbol key={dataManager.id} lat={location.latitude} lng={location.longitude}
                                        img={props.managers[dataManager.id].img} name={props.managers[dataManager.id].name}
                                        color={color}
                                    />
                symbols = [...symbols,newSymbol];
            })
            setManagersSymbols(symbols);
        }
    },[props.datasToManagers,props.managers])

    useEffect(()=>{
        if(locationsVaccines.length>0 && Object.keys(vaccines).length>0){
            var symbols = []
            locationsVaccines.map((locationVaccine)=>{
                const newSymbol = <Symbol key = {locationVaccine.id} lat={locationVaccine.latitude} lng={locationVaccine.longitude} 
                                    img={"vaccine.svg"} color={localStorage.getItem(`color${locationVaccine.id}`)} name={vaccines[locationVaccine.id]+"_"+locationVaccine.id}
                                    />
                symbols = [...symbols,newSymbol];
            })
            setVaccinesSymbols(symbols);
        }
    },[locationsVaccines,vaccines])

    const defaultProps = {
        center: {
          lat:-20.277325477765995,
          lng: -40.304120784856785
        },
        zoom: 13
    };
    
    const getLocationAndVaccineInformation = (data)=>{
    
        var locations = [];
        data.map((vaccineMessage)=>{
            const location = vaccineMessage.datasSaved[vaccineMessage.datasSaved.length-1].location;
            const id = vaccineMessage.id;
            
            locations = [...locations,{id:id,...location}]
        })
        setLocationsVaccines(locations);
    }


    return(
        <div className="map-container">
            <h2>Acompanhe as localizações</h2>
            <div className="map-row">
                <GoogleMapReact
                    bootstrapURLKeys={{ key:process.env.REACT_APP_KEY_MAPS}}
                    defaultCenter={defaultProps.center}
                    defaultZoom={defaultProps.zoom}
                >
                    {
                        vaccinesSymbols.map((symbol)=>{return symbol})
                    }
                    {
                        managersSymbols.map((symbol)=>{return symbol})
                    }
                </GoogleMapReact>
            </div>
        </div>
    )

}

export default Map;