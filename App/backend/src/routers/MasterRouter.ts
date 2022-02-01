import { Router } from 'express';
import ManagerRouter from './manager/ManagerRouter';
import VaccineRouter from './vaccine/VaccineRouter';
import EmailRouter from './email/EmailRouter';

class MasterRouter {
  private _router = Router();
  private _managerR = ManagerRouter;
  private _vaccineR = VaccineRouter;
  private _emailR = EmailRouter;

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
    this._router.use('/email', this._emailR)
  }
}

export = new MasterRouter().router;