import { NextFunction, Request, Response, Router } from 'express';
import VaccineController from '../../controllers/VaccineController';

class VaccineRouter {
  private _router = Router();
  private _controller = VaccineController;

  get router() {
    return this._router;
  }

  constructor() {
    this._configure();
  }

  /**
   * Connect routes to their matching controller endpoints.
   */
  private _configure() {
    this._router.get('/status/:id', (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json(this._controller.getVaccineStatus(Number(req.params.id)));
    });

    this._router.get('/status', (req: Request, res: Response, next: NextFunction) => {
      res.status(200).json(this._controller.getAllVaccinesStatus());
    });

    this._router.get('/:id', (req: Request, res: Response, next: NextFunction) => {
      res.status(200).json(this._controller.getVaccine(Number(req.params.id)));
    });

    this._router.get('/', (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json(this._controller.getVaccines());
    });
  }
}

export = new VaccineRouter().router;