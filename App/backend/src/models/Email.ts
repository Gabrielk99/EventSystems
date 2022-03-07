import { Vaccine } from "./Vaccine";
import { Location } from "./Location"

export interface EmailMessage {
    to: String,
    from: String | undefined,
    subject: String,
    vaccine: Vaccine,
    status: number,
    location: Location,
    manager:string,
    address:string,
    key:number,
    isFrequentAlert: boolean
}