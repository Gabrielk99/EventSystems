import { Vaccine, VaccineListStatus } from '../models/Vaccine';
declare class VaccineController {
    getVaccines(): [Vaccine];
    getVaccine(id: number): Vaccine | undefined;
    getAllVaccinesStatus(): [VaccineListStatus] | {
        status: string;
        message: any;
    };
    getVaccineStatus(id: number): VaccineListStatus | undefined;
}
declare const _default: VaccineController;
export = _default;
