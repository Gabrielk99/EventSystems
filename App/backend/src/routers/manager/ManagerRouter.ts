import { NextFunction, Request, Response, Router } from 'express';
import ManagerController from '../../controllers/ManagerController';

class ManagerRouter {
  private _router = Router();
  private _controller = ManagerController;

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
    this._router.get('/localizacao', (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json(this._controller.getManagersLocation());
    });

    this._router.get('/localizacao/:id', (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json(this._controller.getManagerLocation(Number(req.params.id)));
    });

    this._router.get('/', (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json(this._controller.getManagers());
    });

    this._router.get('/:id', (req: Request, res: Response, next: NextFunction) => {
        res.status(200).json(this._controller.getManager(Number(req.params.id)));
    });
  }
}

export = new ManagerRouter().router;