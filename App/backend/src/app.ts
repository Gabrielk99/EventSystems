import dotenv from 'dotenv';
import express from 'express';
import { Request, Response, NextFunction } from 'express';
import MasterRouter from './routers/MasterRouter';
import bodyParser from 'body-parser';
import ErrorHandler from './models/ErrorHandler';
import { webSocketConnection } from './models/webSocket';


const cors = require('cors');

// load the environment variables from the .env file
dotenv.config({
  path: '.env'
});

/**
 * Express server application class.
 * @description Will later contain the routing system.
 */
class Server {
  public app = express();
  public router = MasterRouter;
}

// initialize server app
const server = new Server();

//ativando o reconhecimento do proxy
server.app.use(bodyParser.json());

//ativando o reconhecimento do proxy
server.app.use(cors());

server.app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

// handle any router started with /api
server.app.use('/api', server.router);

// error handling
server.app.use((err: ErrorHandler, req: Request, res: Response, next: NextFunction) => {
  res.status(err.statusCode || 500).json({
    status: 'error',
    statusCode: err.statusCode,
    message: err.message
  });
});

let application:any = null;
// make server listen on some port
((port = process.env.APP_PORT || 5000) => {
  application= server.app.listen(port, () => console.log(`> Listening on port ${port}`));
})();

webSocketConnection(application);

