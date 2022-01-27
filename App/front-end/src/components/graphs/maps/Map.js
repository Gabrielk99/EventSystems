import React, { useEffect, useState } from "react";
import GoogleMapReact from 'google-map-react';
import Symbol from '../../symbolMapLocation/Symbol';
import { getAllStatusVaccines,getAllVaccineInformation} from "../../../controllers/vacinas/vacinaController";
import "./Map.css"


function Map(props){
    
    const [locationsVaccines,setLocationsVaccines] = useState([]);
    const [vaccines,setVaccines] = useState({});
    const [vaccinesSymbols,setVaccinesSymbols] = useState([]);

    useEffect(()=>{
        getLocationAndVaccineInformation();
    },[])

    useEffect(()=>{
        var symbols = []
        locationsVaccines.map((locationVaccine)=>{
            const newSymbol = <Symbol key = {locationVaccine.id} lat={locationVaccine.latitude} lng={locationVaccine.longitude} 
                                img={"vaccine.svg"} color={props.colorsToVaccine[locationVaccine.id]} name={vaccines[locationVaccine.id]+"_"+locationVaccine.id}
                                />
            symbols = [...symbols,newSymbol];
        })
        setVaccinesSymbols(symbols);
    },[locationsVaccines,vaccines])

    const defaultProps = {
        center: {
          lat:-20.277325477765995,
          lng: -40.304120784856785
        },
        zoom: 13
    };
    
    const getLocationAndVaccineInformation = ()=>{
        getAllStatusVaccines().then(data=>{
            var locations = [];
            data.map((vaccineMessage)=>{
                const location = vaccineMessage.datasSaved[vaccineMessage.datasSaved.length-1].location;
                const id = vaccineMessage.id;
                
                locations = [...locations,{id:id,...location}]
            })
            setLocationsVaccines(locations);
        });
        getAllVaccineInformation().then(vaccinesInfo=>{
            vaccinesInfo.map(vaccine=>{
                const vaccineInfo = vaccines
                vaccineInfo[vaccine.id] = vaccine.name
                setVaccines({...vaccineInfo});
            })
        })
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
                </GoogleMapReact>
            </div>
        </div>
    )

}

export default Map;