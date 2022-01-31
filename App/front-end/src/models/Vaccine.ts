import { Location } from './Location'

export interface Vaccine {
    name: String;
    id: number;
}

export enum Status {
    ok,
    warning,
    danger,
    gameover,
}

export interface VaccineStatus {
    status: Status;
    temperature: number;
    date: String;
    location: Location
}

export interface VaccineListStatus {
    id: number;
    size: number;
    dataSaved: [VaccineStatus]
}