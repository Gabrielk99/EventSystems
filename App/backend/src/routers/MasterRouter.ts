import { Router } from 'express';
import ManagerRouter from './manager/ManagerRouter';
import VaccineRouter from './vaccine/VaccineRouter';

class MasterRouter {
  private _router = Router();
  private _managerR = ManagerRouter;
  private _vaccineR = VaccineRouter;

  get router() {
    return this._router;
  }

  constructor() {
    this._configure();
  }

  /**
   * Connect routes to their matching routers.
   */
  private _configure() {
    this._router.use('/gestor', this._managerR);
    this._router.use('/vacina', this._vaccineR);
  }
}

export = new MasterRouter().router;