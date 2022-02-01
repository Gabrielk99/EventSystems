import { NextFunction, Request, Response, Router } from 'express';
import { sendEmail } from '../../helper/Email'

class EmailRouter {
  private _router = Router();

  get router() {
    return this._router;
  }

  constructor() {
    this._configure();
  }

  private _configure() {
    this._router.post('/send', (req: Request, res: Response, next: NextFunction) => {
       sendEmail(req.body)
       res.status(200);
    });
  }
}

export = new EmailRouter().router;