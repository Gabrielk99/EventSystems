import React from "react";
import Geocode from 'react-geocode';

export const getAddressFromLatLong = (lat,long)=>{
    Geocode.setApiKey(process.env.REACT_APP_KEY_MAPS);
    Geocode.setLanguage("pt");
    Geocode.setRegion('br');
    Geocode.setLocationType("ROOFTOP");

    return Geocode.fromLatLng(lat, long).then(
        (response) => {
          const address = response.results[0].formatted_address;
          // console.log(address.toString());
          return address;
        },
        (error) => {
          console.error(error);
        }
    );

}