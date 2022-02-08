import { Manager, ManagerLocation } from '../models/Manager';
declare class ManagerController {
    getManagers(): [Manager];
    getManager(id: number): Manager | undefined;
    getManagersLocation(): [ManagerLocation];
    getManagerLocation(id: number): ManagerLocation | undefined;
}
declare const _default: ManagerController;
export = _default;
