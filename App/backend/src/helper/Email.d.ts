import { EmailMessage } from '../models/Email';
export declare const setEmailSender: (value: number) => void;
export declare const setSendEmail: (value: boolean) => void;
export declare const sendEmail: (email: EmailMessage) => Promise<any>;
