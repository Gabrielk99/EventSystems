import { Location } from '../models/Location'

 const R: number = 6371e3; //raio da terra em metros

 export const calculateDistance = (position1: Location, position2: Location) => {
     let fi1 = position1.latitude * Math.PI/180; // φ, λ in radians
     let fi2 = position2.latitude * Math.PI/180;
     let deltaFi = (position2.latitude-position1.latitude) * Math.PI/180;
     let deltaLambda = (position2.longitude-position1.longitude) * Math.PI/180;

     let a = Math.sin(deltaFi/2) * Math.sin(deltaFi/2) +
                Math.cos(fi1) * Math.cos(fi2) *
                Math.sin(deltaLambda/2) * Math.sin(deltaLambda/2);
     let c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

     return R * c; // in metres
 }