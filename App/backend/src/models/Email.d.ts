import { VaccineStatus } from "./Vaccine";
export interface EmailMessage {
    to: String;
    from: String | undefined;
    subject: String;
    text: String;
    html: String;
    vaccine: VaccineStatus;
    manager: string;
    address: string;
}
