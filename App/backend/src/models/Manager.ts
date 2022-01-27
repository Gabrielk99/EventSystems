import { Location } from './Location'

export interface Manager {
    name: String;
    id: number;
    email: String;
    celular: String;
    img: String;
}

export interface ManagerLocation {
    id: number;
    dataSaved: Location;
}