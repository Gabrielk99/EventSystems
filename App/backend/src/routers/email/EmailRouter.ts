import { NextFunction, Request, Response, Router } from 'express';
import { sendEmail, setEmailSender } from '../../helper/Email'

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
       const result = sendEmail(req.body)
       res.status(200).json(result);
    });

    this._router.post('/setKey/:key', (req: Request, res: Response, next: NextFunction) => {
        console.log("aaaa")
       setEmailSender(Number(req.params.key))
       res.status(200);
    });
  }
}

export = new EmailRouter().router;