import { Location } from './Location';
export interface Vaccine {
    name: String;
    id: number;
}
export declare enum Status {
    ok = 0,
    warning = 1,
    danger = 2,
    gameover = 3
}
export interface VaccineStatus {
    status: number;
    temperature: number;
    date: String;
    location: Location;
}
export interface VaccineListStatus {
    id: number;
    size: number;
    dataSaved: [VaccineStatus];
}
