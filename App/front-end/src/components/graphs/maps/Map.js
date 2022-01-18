import React from "react";
import GoogleMapReact from 'google-map-react';
import Symbol from '../../symbolMapLocation/Symbol';
import "./Map.css"


function Map(){

    const defaultProps = {
        center: {
          lat:-20.277325477765995,
          lng: -40.304120784856785
        },
        zoom: 13
      };

    return(
        <div className="map-container">
            <h2>Acompanhe as localizações</h2>
            <div className="map-row">
                <GoogleMapReact
                    bootstrapURLKeys={{ key:process.env.REACT_APP_KEY_MAPS}}
                    defaultCenter={defaultProps.center}
                    defaultZoom={defaultProps.zoom}
                >
                    <Symbol
                        lat={-20.277325477765995}
                        lng={-40.304120784856785}
                        img={"mikagay.jpeg"}
                        name={"mikagay"}
                    />
                </GoogleMapReact>
            </div>
        </div>
    )

}

export default Map;