declare class VaccineController {
    getVaccines(): {
        name: string;
        id: number;
    }[];
    getVaccine(id: number): {
        name: string;
        id: number;
    } | undefined;
    getAllVaccinesStatus(): {
        size: number;
        id: number;
        datasSaved: {
            status: number;
            temperature: string;
            date: string;
            location: {
                latitude: number;
                longitude: number;
            };
        }[];
    }[];
    getVaccineStatus(id: number): {
        size: number;
        id: number;
        datasSaved: {
            status: number;
            temperature: string;
            date: string;
            location: {
                latitude: number;
                longitude: number;
            };
        }[];
    }[];
}
declare const _default: VaccineController;
export = _default;
