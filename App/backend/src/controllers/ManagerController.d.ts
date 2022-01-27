declare class ManagerController {
    getManagers(): {
        name: string;
        id: number;
        email: string;
        celular: string;
        img: string;
    }[];
    getManager(id: number): {
        name: string;
        id: number;
        email: string;
        celular: string;
        img: string;
    } | undefined;
    getManagersLocation(): {
        id: number;
        dataSaved: {
            location: {
                latitude: number;
                longitude: number;
            };
        };
    }[];
    getManagerLocation(id: number): {
        id: number;
        dataSaved: {
            location: {
                latitude: number;
                longitude: number;
            };
        };
    } | undefined;
}
declare const _default: ManagerController;
export = _default;
