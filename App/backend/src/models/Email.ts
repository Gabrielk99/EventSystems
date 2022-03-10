import { Vaccine } from "./Vaccine";
import { Location } from "./Location"

export interface EmailMessage {
    to: String,
    subject: String,
    vaccine: Vaccine,
    status: number,
    location: Location,
    manager:string,
    isFrequentAlert: boolean
}