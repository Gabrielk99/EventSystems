"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __generator = (this && this.__generator) || function (thisArg, body) {
    var _ = { label: 0, sent: function() { if (t[0] & 1) throw t[1]; return t[1]; }, trys: [], ops: [] }, f, y, t, g;
    return g = { next: verb(0), "throw": verb(1), "return": verb(2) }, typeof Symbol === "function" && (g[Symbol.iterator] = function() { return this; }), g;
    function verb(n) { return function (v) { return step([n, v]); }; }
    function step(op) {
        if (f) throw new TypeError("Generator is already executing.");
        while (_) try {
            if (f = 1, y && (t = op[0] & 2 ? y["return"] : op[0] ? y["throw"] || ((t = y["return"]) && t.call(y), 0) : y.next) && !(t = t.call(y, op[1])).done) return t;
            if (y = 0, t) op = [op[0] & 2, t.value];
            switch (op[0]) {
                case 0: case 1: t = op; break;
                case 4: _.label++; return { value: op[1], done: false };
                case 5: _.label++; y = op[1]; op = [0]; continue;
                case 7: op = _.ops.pop(); _.trys.pop(); continue;
                default:
                    if (!(t = _.trys, t = t.length > 0 && t[t.length - 1]) && (op[0] === 6 || op[0] === 2)) { _ = 0; continue; }
                    if (op[0] === 3 && (!t || (op[1] > t[0] && op[1] < t[3]))) { _.label = op[1]; break; }
                    if (op[0] === 6 && _.label < t[1]) { _.label = t[1]; t = op; break; }
                    if (t && _.label < t[2]) { _.label = t[2]; _.ops.push(op); break; }
                    if (t[2]) _.ops.pop();
                    _.trys.pop(); continue;
            }
            op = body.call(thisArg, _);
        } catch (e) { op = [6, e]; y = 0; } finally { f = t = 0; }
        if (op[0] & 5) throw op[1]; return { value: op[0] ? op[1] : void 0, done: true };
    }
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.webSocketConnection = void 0;
var ws_1 = __importDefault(require("ws"));
var VaccineController_1 = __importDefault(require("../controllers/VaccineController"));
function onError(ws, err) {
    console.error("onError: ".concat(err.message));
}
function onMessage(ws, data) {
    console.log("onMessage: ".concat(data));
    ws.send("recebido!");
}
function onConnection(ws, req) {
    var url = req.url;
    var method = req.method;
    console.log(url, method);
    if (url === '/api/vacina/status' && method === 'GET') {
        whileSend(ws, 1);
    }
    ws.on('message', function (data) { return onMessage(ws, data); });
    ws.on('error', function (error) { return onError(ws, error); });
    console.log("onConnection");
}
var webSocketConnection = function (server) {
    var wss = new ws_1.default.Server({
        server: server
    });
    wss.on('connection', onConnection);
    console.log("App Web Socket Server is running!");
    return wss;
};
exports.webSocketConnection = webSocketConnection;
var whileSend = function (ws, caseRequest) { return __awaiter(void 0, void 0, void 0, function () {
    var controller, message;
    return __generator(this, function (_a) {
        switch (_a.label) {
            case 0:
                switch (caseRequest) {
                    case 1:
                        controller = VaccineController_1.default;
                }
                _a.label = 1;
            case 1:
                if (!true) return [3 /*break*/, 3];
                message = controller === null || controller === void 0 ? void 0 : controller.getAllVaccinesStatus();
                if (message.status !== 'error') {
                    ws.send(JSON.stringify(message));
                }
                else {
                    return [3 /*break*/, 1];
                }
                return [4 /*yield*/, delay(700)];
            case 2:
                _a.sent();
                return [3 /*break*/, 1];
            case 3: return [2 /*return*/];
        }
    });
}); };
var delay = function (time) {
    return new Promise(function (resolve) { return setTimeout(resolve, time); });
};
