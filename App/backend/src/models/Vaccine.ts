import { Location } from './Location'

export interface Vaccine {
    name: String;
    id: number;
}

export interface VaccineStatus {
    status: number;
    temperature: number;
    date: String;
    location: Location
}

export interface VaccineListStatus {
    id: number;
    size: number;
    dataSaved: [VaccineStatus]
}